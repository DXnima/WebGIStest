package com.example.webgistest.geoserver;

import com.example.webgistest.constant.AcceptType;
import com.example.webgistest.constant.ContentType;
import com.example.webgistest.constant.HttpConstant;
import com.example.webgistest.exception.*;
import com.example.webgistest.style.stylevariable.GeoType;
import com.example.webgistest.style.utils.StyleUtil;
import com.example.webgistest.utils.ComTools;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureType;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSLayerGroupEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.geotools.styling.Style;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class GeoServerManager {
    //  加强geoserver publisher
    private final ImproveGeoServerPublisher geoServerRESTPublisher;
    //  geoserver REST 管理者
    private final GeoServerRESTManager geoServerRESTManager;
    //  geoserver REST 阅读者
    private final GeoServerReader reader;

    /**
     * 直接提供 geoserver 地址，用户名，密码为默认的： admin/geoserver
     *
     * @param restUrl geoserver 服务地址
     * @throws MalformedURLException 服务地址错误
     */
    public GeoServerManager(String restUrl) throws MalformedURLException {
        this(restUrl, "admin", "geoserver");

    }

    public GeoServerManager(Map<Object, Object> config) throws MalformedURLException {
        this(config.get("restURL").toString(), config.get("username").toString(), config.get("password").toString());
    }

    /**
     * 提供 geoserver 服务地址和用户名、密码
     *
     * @param restUrl  geoserver 服务地址
     * @param userName geoserver登录用户名
     * @param password geoserver 密码
     * @throws MalformedURLException 服务地址或登录失败错误
     */
    public GeoServerManager(String restUrl, String userName, String password) throws MalformedURLException {
        geoServerRESTPublisher = new ImproveGeoServerPublisher(restUrl, userName, password);
        geoServerRESTManager = new GeoServerRESTManager(new URL(restUrl), userName, password);
        reader = new GeoServerReader(restUrl, userName, password);
    }

    /**
     * 创建工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     * @throws ExistedException 工作空间已存在
     */
    public Boolean createWorkspace(String workspaceName) throws ExistedException {
        if (reader.existsWorkspace(workspaceName)) {
            throw new ExistedException("工作空间；" + workspaceName);
        }

        return geoServerRESTPublisher.createWorkspace(workspaceName);
    }

    /**
     * 通过名称 和 URI 创建工作空间
     *
     * @param workspaceName 工作空间名称
     * @param uri           URI名称
     * @return 是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws URISyntaxException         URI 格式或链接错误
     */
    public Boolean createWorkspace(String workspaceName, String uri) throws WorkSpaceNotFoundException, URISyntaxException {
        if (reader.existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        return geoServerRESTPublisher.createWorkspace(workspaceName, new URI(uri));
    }

    /**
     * 删除工作空间
     *
     * @param workspaceName 要删除的工作空间名称
     * @return 删除工作空间是否成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean removeWorkspace(String workspaceName) throws WorkSpaceNotFoundException {
        if (!reader.existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        return geoServerRESTPublisher.removeWorkspace(workspaceName, true);
    }

    /**
     * 首次创建图层时，创建默认样式
     * 首次发布图层时使用
     *
     * @param workspaceName 工作空间名称
     * @param styleName     样式名称
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间是否存在
     * @throws ErrorException             读取 SLD 文件错误
     */
    public void createLayerStyle(String workspaceName, String styleName, String type) throws WorkSpaceNotFoundException, ExistedException, ErrorException {
        if(!reader.existsStyleFromWorkspace(workspaceName,styleName)) {
            Style style = null;
            switch (GeoType.getType(type)) {
                case POINT:
                    style = StyleUtil.createDefaultPointStyle();
                    break;
                case LINE:
                    style = StyleUtil.createDefaultLineStyle();
                    break;
                case POLYGON:
                    style = StyleUtil.createDefaultPolygonStyle();
                    break;
            }
            String sld = StyleUtil.styleToString(style);
            createStyleToWorkspaceByBody(workspaceName, sld, styleName);
        }
    }

    /**
     * 创建 Style 服务
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldFile sld文件对象
     * @return 返回是否创建成功
     * @throws ExistedException style 样式服务不存在
     * @throws ErrorException   读取 SLD 文件错误
     */
    public Boolean createStyle(File sldFile) throws ErrorException, ExistedException {
        String sldFileName = sldFile.getName();

        String[] split = sldFileName.split(".sld");
        String styleName = split[0];
        //     获取sld文件内容
        String file = null;
        try {
            file = ComTools.readFile(sldFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException("SLD文件读取失败！");
        }
        if (reader.existsStyle(styleName)) {
            throw new ExistedException("样式；" + styleName);
        }
        return this.createStyle(file, styleName);
    }

    /**
     * 创建 Style 服务，并提供style 服务名称
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldBody   sld 文件对象
     * @param styleName style 服务名称
     * @return 返回是否创建成功
     * @throws ExistedException style 样式服务不存在
     * @throws ErrorException   读取 SLD 文件错误
     */
    public Boolean createStyle(String sldBody, String styleName) throws ExistedException, ErrorException {
        if (reader.existsStyle(styleName)) {
            throw new ExistedException("样式；" + styleName);
        }
        //根据系统环境 格式化 sld xml 文件
        String systemType = System.getProperty("os.name");
        boolean isWin= systemType.toLowerCase().contains("win");
        if(isWin) {
            sldBody = sldBody.replaceAll("\"", "\\\\\"");
        }

        //     请求路径
        String url = String.format("%s/rest/styles?name=%s&raw=true", geoServerRESTPublisher.getRestURL(), styleName);
        //     登录名、密码字符串
        String logInStr = geoServerRESTPublisher.getUsername() + ":" + geoServerRESTPublisher.getPassword();

        String[] cmds = {
                "curl", "-u", logInStr,
                "-X", HttpConstant.POST, url,
                "-H", AcceptType.JSON,
                "-H", ContentType.SLD,
                "-d", sldBody
        };

        /*
         * ================================================ 创建完需要put一下
         * */
        //     请求路径
        String urlPUT = String.format("%s/rest/styles/%s?raw=true", geoServerRESTPublisher.getRestURL(), styleName);

        String[] cmdsPUT = {
                "curl", "-u", logInStr,
                "-X", HttpConstant.PUT, urlPUT,
                "-H", AcceptType.JSON,
                "-H", ContentType.SLD,
                "-d", sldBody
        };

        String curlPostResult = null;
        try {
            curlPostResult = ComTools.curl(cmds);
            String a = ComTools.curl(cmdsPUT);
            System.err.print(a);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException("样式发布失败！" + styleName);
        }
        return Objects.equals(curlPostResult, styleName);
    }

    /**
     * 修改 Style 服务，并提供style 服务名称
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldBody   sld XML
     * @param styleName style 服务名称,例如tiger:poi
     * @return 返回是否创建成功
     * @throws StyleServiceNotFoundException style 样式服务不存在
     */
    public Boolean updateStyle(String sldBody, String styleName) throws WorkSpaceNotFoundException, ErrorException, StyleServiceNotFoundException {
        boolean status = styleName.contains(":");
        if (status) {
            String workspaceName = styleName.split(":")[0];
            String styleN = styleName.split(":")[1];
            return updateStyleToWorkspace(workspaceName, sldBody, styleN);
        } else {
            if (!reader.existsStyle(styleName)) {
                throw new StyleServiceNotFoundException(styleName);
            }
            //根据系统环境 格式化 sld xml 文件
            String systemType = System.getProperty("os.name");
            boolean isWin= systemType.toLowerCase().contains("win");
            if(isWin) {
                sldBody = sldBody.replaceAll("\"", "\\\\\"");
            }

            //     登录名、密码字符串
            String logInStr = geoServerRESTPublisher.getUsername() + ":" + geoServerRESTPublisher.getPassword();

            //     请求路径
            String urlPUT = String.format("%s/rest/styles/%s?raw=true", geoServerRESTPublisher.getRestURL(), styleName);

            String[] cmdsPUT = {
                    "curl", "-u", logInStr,
                    "-X", HttpConstant.PUT, urlPUT,
                    "-H", AcceptType.JSON,
                    "-H", ContentType.SLD,
                    "-d", sldBody
            };
            try {
                String a = ComTools.curl(cmdsPUT);
                System.err.print(a);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ErrorException("样式修改失败！" + styleName);
            }
            return true;
        }
    }

    /**
     * 获取SLD 通过指定样式名
     *
     * @param styleName style 服务名称
     * @return 是否包含style服务
     * @throws StyleServiceNotFoundException 样式不存在
     */
    public String getSLD(String styleName) throws WorkSpaceNotFoundException, StyleServiceNotFoundException, ErrorException {
        boolean status = styleName.contains(":");
        if (status) {
            String workspaceName = styleName.split(":")[0];
            String styleN = styleName.split(":")[1];
            return getSLDFromWorkspace(workspaceName, styleN);
        } else {
            if (!reader.existsStyle(styleName)) {
                throw new StyleServiceNotFoundException(styleName);
            }
            //     登录名、密码字符串
            String logInStr = geoServerRESTPublisher.getUsername() + ":" + geoServerRESTPublisher.getPassword();
            //     请求路径
            String urlGET = String.format("%s/rest/styles/%s.sld?raw=true", geoServerRESTPublisher.getRestURL(), styleName);

            String[] cmdsGET = {
                    "curl", "-u", logInStr,
                    "-X", HttpConstant.GET, urlGET,
                    "-H", AcceptType.XML
            };
            String sld = null;
            try {
                sld = ComTools.curl(cmdsGET);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ErrorException("SLD获取失败！");
            }
            return sld;
        }
    }

    /**
     * 删除style服务
     *
     * @param styleName 要删除的style 名称,例如tiger:poi
     * @return 是否删除成功
     * @throws StyleServiceNotFoundException 样式服务不存在
     */
    public Boolean removeStyle(String styleName) throws WorkSpaceNotFoundException, StyleServiceNotFoundException {
        boolean status = styleName.contains(":");
        if (status) {
            String workspaceName = styleName.split(":")[0];
            String styleN = styleName.split(":")[1];
            return removeStyleFromWorkspace(workspaceName, styleN);
        } else {
            if (!reader.existsStyle(styleName)) {
                throw new StyleServiceNotFoundException(styleName);
            }
            return geoServerRESTPublisher.removeStyle(styleName, true);
        }
    }

    /**
     * 创建 Style 服务到指定的工作空间下
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param workspaceName 工作空间名称
     * @param sldFile       sld文件对象
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           style服务已存在
     * @throws ErrorException             文件错误
     */
    public Boolean createStyleToWorkspace(String workspaceName, File sldFile) throws WorkSpaceNotFoundException, ExistedException, ErrorException {
        String sldFileName = sldFile.getName();
        String styleName = sldFileName.split(".sld")[0];
        //     获取sld文件内容
        String file = null;
        String sldInfo = null;
        try {
            file = ComTools.readFile(sldFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException("SLD文件读取失败！");
        }
        return this.createStyleToWorkspace(workspaceName, file, styleName);
    }

    /**
     * 创建 Style 服务，并提供style 服务名称
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldBody   sld XML
     * @param styleName style 服务名称
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           style样式服务已存在
     */
    public Boolean createStyleToWorkspaceByBody(String workspaceName, String sldBody, String styleName) throws WorkSpaceNotFoundException, ExistedException {
        if (reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new ExistedException("创建 style 样式服务：" + styleName);
        }
        return geoServerRESTPublisher.publishStyleInWorkspace(workspaceName, sldBody, styleName);
    }

    /**
     * 创建 Style 服务到指定的工作空间下，并提供style 服务名称
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param workspaceName 工作空间名称
     * @param sldBody       sld XML
     * @param styleName     style 服务名称
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           style样式服务已存在
     */
    public Boolean createStyleToWorkspace(String workspaceName, String sldBody, String styleName) throws WorkSpaceNotFoundException, ExistedException, ErrorException {
        if (reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new ExistedException("style 样式服务：" + styleName);
        }
        //根据系统环境 格式化 sld xml 文件
        String systemType = System.getProperty("os.name");
        boolean isWin= systemType.toLowerCase().contains("win");
        if(isWin) {
            sldBody = sldBody.replaceAll("\"", "\\\\\"");
        }

        //     请求路径
        String url = String.format("%s/rest/workspaces/%s/styles?name=%s&raw=true", geoServerRESTPublisher.getRestURL(), workspaceName, styleName);
        //     登录名、密码字符串
        String logInStr = geoServerRESTPublisher.getUsername() + ":" + geoServerRESTPublisher.getPassword();

        String[] cmds = {
                "curl", "-u", logInStr,
                "-X", HttpConstant.POST, url,
                "-H", AcceptType.JSON,
                "-H", ContentType.SLD,
                "-d", sldBody
        };

        /*
         * ================================================ 创建完需要put一下
         * */

        //     请求路径
        String urlPUT = String.format("%s/rest/workspaces/%s/styles/%s?raw=true", geoServerRESTPublisher.getRestURL(), workspaceName, styleName);

        String[] cmdsPUT = {
                "curl", "-u", logInStr,
                "-X", HttpConstant.PUT, urlPUT,
                "-H", AcceptType.JSON,
                "-H", ContentType.SLD,
                "-d",  sldBody
        };
        String curlPostResult = null;
        try {
            curlPostResult = ComTools.curl(cmds);
            String a = ComTools.curl(cmdsPUT);
            System.err.print(a);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException("样式创建失败！" + workspaceName + ":" + styleName);
        }

        return Objects.equals(curlPostResult, styleName);
    }

    /**
     * 修改 Style 服务
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldBody   sld XML
     * @param styleName style 服务名称
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException    工作空间不存在
     * @throws StyleServiceNotFoundException style样式服务不存在
     */
    public Boolean updateStyleToWorkspace(String workspaceName, String sldBody, String styleName) throws WorkSpaceNotFoundException, StyleServiceNotFoundException, ErrorException {
        if (!reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new StyleServiceNotFoundException("修改" + styleName);
        }
        //
        //根据系统环境 格式化 sld xml 文件
        String systemType = System.getProperty("os.name");
        boolean isWin= systemType.toLowerCase().contains("win");
        if(isWin) {
            sldBody = sldBody.replaceAll("\"", "\\\\\"");
        }
        //     登录名、密码字符串
        String logInStr = geoServerRESTPublisher.getUsername() + ":" + geoServerRESTPublisher.getPassword();
        //     请求路径
        String urlPUT = String.format("%s/rest/workspaces/%s/styles/%s?raw=true", geoServerRESTPublisher.getRestURL(), workspaceName, styleName);

        String[] cmdsPUT = {
                "curl", "-u", logInStr,
                "-X", HttpConstant.PUT, urlPUT,
                "-H", AcceptType.JSON,
                "-H", ContentType.SLD,
                "-d", sldBody
        };
        try {
            String a = ComTools.curl(cmdsPUT);
            System.err.print(a);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException("样式修改失败！" + workspaceName + ":" + styleName);
        }
        return true;
    }

    /**
     * 获取SLD 通过指定工作空间与样式名
     *
     * @param workspaceName 工作空间民称
     * @param styleName     style 服务名称
     * @return 是否包含style服务
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public String getSLDFromWorkspace(String workspaceName, String styleName) throws WorkSpaceNotFoundException, StyleServiceNotFoundException, ErrorException {
        if (!reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new StyleServiceNotFoundException(workspaceName + ":" + styleName);
        }
        //     请求路径
        String urlGET = String.format("%s/rest/workspaces/%s/styles/%s.sld?raw=true", geoServerRESTPublisher.getRestURL(), workspaceName, styleName);
        //     登录名、密码字符串
        String logInStr = geoServerRESTPublisher.getUsername() + ":" + geoServerRESTPublisher.getPassword();

        String[] cmdsGET = {
                "curl", "-u", logInStr,
                "-X", HttpConstant.GET, urlGET,
                "-H", AcceptType.XML
        };
        String sld = null;
        try {
            sld = ComTools.curl(cmdsGET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException("SLD获取失败！");
        }
        return sld;
    }

    /**
     * 删除工作空间下的style服务
     *
     * @param workspaceName 工作空间名称
     * @param styleName     要删除的style 名称
     * @return 是否删除成功
     * @throws WorkSpaceNotFoundException    工作空间不存在
     * @throws StyleServiceNotFoundException 样式服务不存在
     */
    public Boolean removeStyleFromWorkspace(String workspaceName, String styleName) throws WorkSpaceNotFoundException, StyleServiceNotFoundException {
        if (!reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new StyleServiceNotFoundException(styleName);
        }

        return geoServerRESTPublisher.removeStyleInWorkspace(workspaceName, styleName, true);
    }

    /**
     * 创建shp 图层，shp文件名则为数据源、图层名，并自动创建样式
     *
     * @param workspaceName 工作空间
     * @param shpFile       shp Zip 文件对象
     * @param crsCode       坐标系代码
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return shp图层是否创建成功
     * @throws FileNotFoundException       文件不存在错误
     * @throws ExistedException            shp源必须为 zip
     * @throws WorkSpaceNotFoundException  工作空间不存在
     * @throws ExistedException            图层名称已存在
     * @throws DataSourceNotFoundException 数据源不存在
     */
    public Boolean createShpLayerAndStyle(String workspaceName, File shpFile, int crsCode, String type) throws FileNotFoundException, ErrorException, DataSourceNotFoundException, WorkSpaceNotFoundException, ExistedException {
        //    获取shp 名称
        String shpFileName = shpFile.getName();
        //    获取shp 文件名切割数组
        String[] splitList = shpFileName.split("\\.");
        //    如果后缀名不是 zip 则直接报错
        if (!Objects.equals(splitList[1], "zip")) {
            throw new ErrorException("shp 源文件必须为 zip 压缩包文件");
        }
        String crsName = "EPSG:" + crsCode;
        //    定义数据源名和图层名
        String storeName, layerName;
        storeName = layerName = splitList[0];

        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层名称：" + layerName);
        }

        this.createLayerStyle(workspaceName, layerName, type);

        boolean publish = geoServerRESTPublisher.publishShp(workspaceName, storeName, layerName, shpFile, crsName, workspaceName+":"+layerName);
        if (publish) {
            updateDataStore(workspaceName, storeName, "GBK");
        }
        return publish;
    }

    /**
     * 创建shp 图层，shp文件名则为数据源、图层名，并指定样式
     *
     * @param workspaceName 工作空间
     * @param shpFile       shp Zip 文件对象
     * @param crsCode       坐标系代码
     * @param styleName     样式名称,例如tiger:poi
     * @return 是否shp 图层是否成功
     * @throws FileNotFoundException         文件不存在错误
     * @throws ExistedException              shp源必须为 zip
     * @throws WorkSpaceNotFoundException    工作空间不存在
     * @throws ExistedException              图层名称已存在
     * @throws StyleServiceNotFoundException style 样式服务不存在
     * @throws DataSourceNotFoundException   数据源不存在
     */
    public Boolean createShpLayer(
            String workspaceName,
            File shpFile,
            int crsCode,
            String styleName
    ) throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException, StyleServiceNotFoundException, ErrorException, DataSourceNotFoundException {
        //    获取shp 名称
        String shpFileName = shpFile.getName();

        //    获取shp 文件名切割数组
        String[] splitList = shpFileName.split("\\.");

        //    如果后缀名不是 zip 则直接报错
        if (!Objects.equals(splitList[1], "zip")) {
            throw new ErrorException("shp 源文件必须为 zip 压缩包文件");
        }

        String crsName = "EPSG:" + crsCode;

        //    定义数据源名和图层名
        String storeName, layerName;
        storeName = layerName = splitList[0];

        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层名称：%s" + layerName);
        }

        boolean status = styleName.contains(":");
        if (status) {
            String styleWorkspaceName = styleName.split(":")[0];
            String styleN = styleName.split(":")[1];
            if (!reader.existsStyleFromWorkspace(styleWorkspaceName, styleN)) {
                throw new StyleServiceNotFoundException(styleName);
            }
        } else {
            if (!reader.existsStyle(styleName)) {
                throw new StyleServiceNotFoundException(styleName);
            }
        }

        boolean publish = geoServerRESTPublisher.publishShp(workspaceName, storeName, layerName, shpFile, crsName, styleName);
        if (publish) {
            updateDataStore(workspaceName, storeName, "GBK");
        }
        return publish;
    }

    /**
     * 创建shp 图层，shp文件名则为数据源、图层名，并指定在某工作空间下的样式服务
     *
     * @param workspaceName      工作空间
     * @param shpFile            shp Zip 文件对象
     * @param crsCode            坐标系代码
     * @param styleWorkspaceName 样式服务所在工作空间名称
     * @param styleName          样式名称
     * @return 创建shp 图层是否成功
     * @throws FileNotFoundException         文件不存在错误
     * @throws ExistedException              shp源文件必须为zip
     * @throws WorkSpaceNotFoundException    工作空间不存在
     * @throws ExistedException              图层名称已存在
     * @throws StyleServiceNotFoundException style样式服务不存在
     * @throws DataSourceNotFoundException   数据源不存在
     */
    public Boolean createShpLayer(
            String workspaceName,
            File shpFile,
            int crsCode,
            String styleWorkspaceName,
            String styleName
    ) throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException, StyleServiceNotFoundException, ErrorException, DataSourceNotFoundException {
        //    获取shp 名称
        String shpFileName = shpFile.getName();

        //    获取shp 文件名切割数组
        String[] splitList = shpFileName.split("\\.");

        //    如果后缀名不是 zip 则直接报错
        if (!Objects.equals(splitList[1], "zip")) {
            throw new ErrorException("shp 源文件必须为 zip 压缩包文件");
        }

        String crsName = "EPSG:" + crsCode;

        //    定义数据源名和图层名
        String storeName, layerName;
        storeName = layerName = splitList[0];

        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层名称：%s" + layerName);
        }

        if (!reader.existsStyle(styleName)) {
            throw new StyleServiceNotFoundException(styleName);
        }

        String shpStyle = styleWorkspaceName + ":" + styleName;

        boolean publish = geoServerRESTPublisher.publishShp(workspaceName, storeName, layerName, shpFile, crsName, shpStyle);
        if (publish) {
            updateDataStore(workspaceName, storeName, "GBK");
        }
        return publish;
    }

    /**
     * 发布PostGIS 中存在的表图层，并自动创建样式
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException 工作空间不存
     * @throws ExistedException           数据源已存在、图层已存在
     * @throws ExistedException           数据源发布失败
     */
    public Boolean createPostGISLayerAndStyle(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            String type
    ) throws WorkSpaceNotFoundException, ExistedException, ErrorException {
        this.createLayerStyle(workspaceName, tableName, type);
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
        gsLayerEncoder.setDefaultStyle(workspaceName + ":" + tableName);
        boolean publish = createPostGISLayer(gsPostGISDatastoreEncoder, workspaceName, tableName, crsCode, gsLayerEncoder);
        return publish;
    }

    /**
     * 发布PostGIS 中存在的表图层
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param styleName                 style 样式服务名称,例如tiger:poi
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException 工作空间不存
     * @throws ExistedException           数据源已存在、图层已存在
     * @throws ExistedException           数据源发布失败
     */
    public Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            String styleName
    ) throws WorkSpaceNotFoundException, ExistedException {
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();

        gsLayerEncoder.setDefaultStyle(styleName);
        return createPostGISLayer(gsPostGISDatastoreEncoder, workspaceName, tableName, crsCode, gsLayerEncoder);
    }

    /**
     * 发布PostGIS 中存在的表图层，指定在工作空间中的样式服务
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param styleWorkspace            style 样式服务工作空间名称
     * @param styleName                 style 样式服务名称
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException    工作空间不存
     * @throws StyleServiceNotFoundException 样式服务不存在
     * @throws ExistedException              数据源已存在、图层已存在
     * @throws ExistedException              数据源发布失败
     */
    public Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            String styleWorkspace,
            String styleName
    ) throws WorkSpaceNotFoundException, StyleServiceNotFoundException, ExistedException {
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();

        if (!reader.existsStyleFromWorkspace(styleWorkspace, styleName)) {
            throw new StyleServiceNotFoundException(styleName);
        }

        gsLayerEncoder.setDefaultStyle(styleWorkspace + ":" + styleName);

        return createPostGISLayer(gsPostGISDatastoreEncoder, workspaceName, tableName, crsCode, gsLayerEncoder);
    }

    /**
     * 发布PostGIS 中存在的表，并指定style样式
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param gsLayerEncoder            图层配置对象
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           数据源已存在、图层已存在
     * @throws ExistedException           数据源创建失败
     */
    public Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            GSLayerEncoder gsLayerEncoder
    ) throws WorkSpaceNotFoundException, ExistedException {
        //    获取 datastore 名称
        String storeName = gsPostGISDatastoreEncoder.getName();

        if (!reader.existsDataStore(workspaceName, storeName)) {
            //    创建一个datastore
            geoServerRESTManager.getStoreManager().create(workspaceName, gsPostGISDatastoreEncoder);
        }

        if (reader.existsLayer(workspaceName, tableName)) {
            throw new ExistedException("图层：" + tableName);
        }

        boolean publishDBLayerResult = false;


        GSFeatureTypeEncoder gsFeatureTypeEncoder = new GSFeatureTypeEncoder();

        gsFeatureTypeEncoder.setTitle(tableName);
        gsFeatureTypeEncoder.setNativeName(tableName);
        gsFeatureTypeEncoder.setName(tableName);
        gsFeatureTypeEncoder.setSRS("EPSG:" + crsCode);

        publishDBLayerResult = geoServerRESTPublisher.publishDBLayer(workspaceName, storeName, gsFeatureTypeEncoder, gsLayerEncoder);

        return publishDBLayerResult;
    }

    /**
     * 更新图层信息标题、描述
     *
     * @param workspaceName 工作空间名称
     * @param layerName     图层名，top:port1
     * @param title         图层标题
     * @param abstracts     图层描述
     * @return 图层是否修改成功
     */
    public Boolean updateLayer(String workspaceName, String layerName, String title, String abstracts) throws WorkSpaceNotFoundException, LayerNotFoundException {
        GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
        fte.setTitle(title);
        fte.setAbstract(abstracts);
        return this.updateLayer(workspaceName, layerName, fte);
    }

    /**
     * 更新图层信息
     *
     * @param workspaceName 工作空间名称
     * @param layerName     图层名，top:port1
     * @param fte           GSFeatureTypeEncoder图层信息类
     * @return 图层是否修改成功
     */
    public Boolean updateLayer(String workspaceName, String layerName, GSFeatureTypeEncoder fte) throws WorkSpaceNotFoundException, LayerNotFoundException {
        if (!reader.existsLayer(workspaceName, layerName)) {
            throw new LayerNotFoundException("图层：" + workspaceName + ":" + layerName);
        }
        RESTLayer layer = geoServerRESTManager.getReader().getLayer(workspaceName, layerName);
        RESTFeatureType featureType = geoServerRESTManager.getReader().getFeatureType(layer);
        String store = featureType.getStoreName();
        if (store.contains(":")) {
            store = store.split(":")[1];
        }
        String ftypeXml = fte.toString();
        StringBuilder putUrl = (new StringBuilder(geoServerRESTPublisher.getRestURL())).append("/rest/workspaces/").append(workspaceName).append("/datastores/").append(store).append("/featuretypes/").append(layerName);
        String configuredResult = HTTPUtils.putXml(putUrl.toString(), ftypeXml, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword());
        return configuredResult != null;
    }

    /**
     * 在指定工作空间下创建图层组
     *
     * @param workspaceName  工作空间民称
     * @param layerGroupName 图层组名称
     * @param layersList     图层名称列表
     * @return 图层组是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws LayerNotFoundException     图层不存在
     * @throws ExistedException           图层组已存在
     */
    public Boolean createLayerGroup(String workspaceName, String layerGroupName, ArrayList<String> layersList) throws WorkSpaceNotFoundException, LayerNotFoundException, ExistedException {
        if (reader.existsLayerGroup(workspaceName, layerGroupName)) {
            throw new ExistedException("图层组：" + layerGroupName);
        }

        GSLayerGroupEncoder gsLayerGroupEncoder = new GSLayerGroupEncoder();
        gsLayerGroupEncoder.setWorkspace(workspaceName);
        gsLayerGroupEncoder.setName(layerGroupName);

        for (String layer : layersList) {
            String[] split = layer.split(":");

            String layerWorkspaceName = split[0];
            String layerName = split[1];

            if (!reader.existsLayer(layerWorkspaceName, layerName)) {
                throw new LayerNotFoundException(layerName);
            }
            gsLayerGroupEncoder.addLayer(layer);
        }

        return geoServerRESTPublisher.createLayerGroup(workspaceName, layerGroupName, gsLayerGroupEncoder);
    }

    /**
     * 修改矢量数据源编码格式
     *
     * @param workspaceName 要修改的矢量数据源所在的工作空间名称
     * @param storeName     要修改的矢量数据源名称
     * @param encoding      编码格式
     * @return 是否删除成功
     * @throws DataSourceNotFoundException 数据源不存在
     * @throws WorkSpaceNotFoundException  工作空间不存在
     */
    public Boolean updateDataStore(String workspaceName, String storeName, String encoding) throws WorkSpaceNotFoundException, DataSourceNotFoundException {
        if (!reader.existsDataStore(workspaceName, storeName)) {
            throw new DataSourceNotFoundException(workspaceName + ":" + storeName);
        }
        //创建shape文件存储
        GSShapefileDatastoreEncoder store = new GSShapefileDatastoreEncoder(geoServerRESTManager.getReader().getDatastore(workspaceName, storeName));
        store.setCharset(Charset.forName(encoding));
        return geoServerRESTManager.getStoreManager().update(workspaceName, store);
    }

    /**
     * 删除矢量数据源
     *
     * @param workspaceName 要删除的矢量数据源所在的工作空间名称
     * @param dataStoreName 要删除的矢量数据源名称
     * @return 是否删除成功
     * @throws DataSourceNotFoundException 数据源不存在
     * @throws WorkSpaceNotFoundException  工作空间不存在
     */
    public Boolean removeDataStore(String workspaceName, String dataStoreName) throws DataSourceNotFoundException, WorkSpaceNotFoundException {
        if (!reader.existsDataStore(workspaceName, dataStoreName)) {
            throw new DataSourceNotFoundException(dataStoreName);
        }

        return geoServerRESTPublisher.removeDatastore(workspaceName, dataStoreName, true);
    }

    /**
     * 删除栅格数据源
     *
     * @param workspaceName      要删除的栅格数据源所在的工作空间名称
     * @param coverageStoresName 要删除的栅格数据源名称
     * @return 栅格数据源是否删除成功
     * @throws WorkSpaceNotFoundException     工作空间不存在
     * @throws CoverageStoreNotFoundException 栅格数据源不存在
     */
    public Boolean removeCoverageStores(String workspaceName, String coverageStoresName) throws WorkSpaceNotFoundException, CoverageStoreNotFoundException {
        if (!reader.existsCoverageStore(workspaceName, coverageStoresName)) {
            throw new CoverageStoreNotFoundException(coverageStoresName);
        }

        return geoServerRESTPublisher.removeCoverageStore(workspaceName, coverageStoresName, true);
    }

    /**
     * 删除图层
     *
     * @param workspaceName 删除图层所在的工作空间名称
     * @param layerName     删除的图层的名称
     * @return 是否删除成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws LayerNotFoundException     图层不存在
     */
    public Boolean removeLayer(String workspaceName, String layerName) throws WorkSpaceNotFoundException, LayerNotFoundException {
        if (!reader.existsLayer(workspaceName, layerName)) {
            throw new LayerNotFoundException(layerName);
        }
        boolean remove = geoServerRESTPublisher.removeLayer(workspaceName, layerName);
        if (remove) {
            geoServerRESTPublisher.reload();
        }
        return remove;
    }

    /**
     * 删除图层组
     *
     * @param workspaceName  删除图层组所在的工作空间
     * @param layerGroupName 图层组名称
     * @return 是否删除成功
     * @throws WorkSpaceNotFoundException  工作空间不存在
     * @throws LayerGroupNotFoundException 图层组不存在
     */
    public Boolean removeLayerGroup(String workspaceName, String layerGroupName) throws WorkSpaceNotFoundException, LayerGroupNotFoundException {
        if (!reader.existsLayerGroup(workspaceName, layerGroupName)) {
            throw new LayerGroupNotFoundException(layerGroupName);
        }

        return geoServerRESTPublisher.removeLayerGroup(workspaceName, layerGroupName);
    }

}
