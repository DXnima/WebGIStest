package com.example.webgistest.geoserver;

import com.example.webgistest.exception.LayerNotFoundException;
import com.example.webgistest.exception.WorkSpaceNotFoundException;
import com.example.webgistest.pojo.MapInfo;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.*;
import it.geosolutions.geoserver.rest.decoder.utils.NameLinkElem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoServerReader {
    private final GeoServerRESTReader geoServerRESTReader;

    public GeoServerReader(String restUrl) throws MalformedURLException {
        this(restUrl, "admin", "geoserver");
    }

    public GeoServerReader(Map<Object, Object> config) throws MalformedURLException {
        this(config.get("restURL").toString(), config.get("username").toString(), config.get("password").toString());
    }

    public GeoServerReader(String restUrl, String userName, String password) throws MalformedURLException {
        GeoServerRESTManager geoServerRESTManager = new GeoServerRESTManager(new URL(restUrl), userName, password);
        geoServerRESTReader = geoServerRESTManager.getReader();
    }

    /**
     * 判断工作空间是否存在
     *
     * @param workspaceName 工作空间名称
     * @return 判断工作空间是否存在
     */
    public Boolean existsWorkspace(String workspaceName) {
        return geoServerRESTReader.existsWorkspace(workspaceName);
    }

    /**
     * 获取工作空间列表
     *
     * @return 工作空间列表
     */
    public ArrayList<String> getWorkspaces() {
        RESTWorkspaceList workspaces = geoServerRESTReader.getWorkspaces();

        ArrayList<String> workspacesList = new ArrayList<>();

        workspaces.forEach(restShortWorkspace -> {
            String name = restShortWorkspace.getName();

            workspacesList.add(name);
        });

        return workspacesList;
    }

    /**
     * 判断某个工作空间下的矢量数据源是否存在
     *
     * @param workspaceName 工作空间名称
     * @param datastoreName 数据源名称
     * @return 数据源是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsDataStore(String workspaceName, String datastoreName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        return geoServerRESTReader.existsDatastore(workspaceName, datastoreName);
    }

    /**
     * 获取数据源列表
     *
     * @param workspaceName 工作空间民称
     * @return 数据集名称列表
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public ArrayList<String> getDataStores(String workspaceName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        RESTDataStoreList dataStores = geoServerRESTReader.getDatastores(workspaceName);

        ArrayList<String> dataStoresList = new ArrayList<>();

        dataStores.forEach(restShortWorkspace -> {
            String name = restShortWorkspace.getName();

            dataStoresList.add(name);
        });

        return dataStoresList;
    }

    /**
     * 判断栅格数据源是否存在
     *
     * @param workspaceName     工作空间名称
     * @param coverageStoreName 栅格数据源名称
     * @return 栅格数据源是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsCoverageStore(String workspaceName, String coverageStoreName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        return geoServerRESTReader.existsCoveragestore(workspaceName, coverageStoreName);
    }

    /**
     * 获取栅格数据源列表
     *
     * @param workspaceName 工作空间名称
     * @return 栅格数据源列表数组
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public ArrayList<String> getCoverageStores(String workspaceName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        RESTCoverageStoreList coverageStores = geoServerRESTReader.getCoverageStores(workspaceName);

        ArrayList<String> coverageStoresList = new ArrayList<>();

        coverageStores.forEach(restShortWorkspace -> {
            String name = restShortWorkspace.getName();

            coverageStoresList.add(name);
        });

        return coverageStoresList;
    }

    /**
     * 判断某个工作空间下的图层是否存在
     *
     * @param workspaceName 工作空间名称
     * @param layerName     图层名称
     * @return 图层是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsLayer(String workspaceName, String layerName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        RESTLayer layer = geoServerRESTReader.getLayer(workspaceName, layerName);
        return layer != null;
    }

    /**
     * 获取所有图层名称列表
     *
     * @return 图层名称列表
     */
    public ArrayList<Map<String, String>> getLayersList() throws Exception {
        RESTLayerList layers = geoServerRESTReader.getLayers();

        ArrayList<Map<String, String>> layerList = new ArrayList<>();
        for (NameLinkElem layer : layers) {
            String layerName = layer.getName();
            String work = layerName.split(":")[0];
            if (work.equals("nurc") || work.equals("tiger") || work.equals("topp") || work.equals("sf")) {
                continue;
            }
            RESTLayer ly = geoServerRESTReader.getLayer(layerName);
            RESTFeatureType feature = geoServerRESTReader.getFeatureType(ly);
            String title = feature.getTitle();
            Map<String, String> map = new HashMap<>();
            map.put("layerName", layerName);
            map.put("title", title);
            layerList.add(map);
        }

        return layerList;
    }

    /**
     * 获取图层信息
     *
     * @param workspaceName 工作空间名称
     * @param layerName     图层名称
     * @return 图层信息
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public MapInfo getLayer(String workspaceName, String layerName, String restURL) throws WorkSpaceNotFoundException, LayerNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        if (!existsLayer(workspaceName, layerName)) {
            throw new LayerNotFoundException(workspaceName + ":" + layerName);
        }
        RESTLayer layer = geoServerRESTReader.getLayer(workspaceName, layerName);
        RESTFeatureType feature = geoServerRESTReader.getFeatureType(layer);
        List<String> attributes = new ArrayList<>();
        feature.getAttributes().forEach(attribute -> {
            attributes.add(attribute.getName());
        });
        RESTNamespace namespace = geoServerRESTReader.getNamespace(workspaceName);
        MapInfo mapInfo = new MapInfo();
        mapInfo.setName(feature.getName());
        mapInfo.setTitle(feature.getTitle());
        mapInfo.setAbstracts(feature.getAbstract());
        mapInfo.setCrs(feature.getCRS());
        mapInfo.setType(layer.getTypeString());
        mapInfo.setWorkspace(workspaceName);
        mapInfo.setStyle(layer.getDefaultStyle());
        mapInfo.setAttributes(attributes);
        mapInfo.setUri(namespace.getURI().toString());
        mapInfo.setRestURI(restURL);
        return mapInfo;
    }

    /**
     * 判断图层组是否存在
     *
     * @param workspaceName  工作空间民称
     * @param layerGroupName 图层组名称
     * @return 图层组是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsLayerGroup(String workspaceName, String layerGroupName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        return geoServerRESTReader.existsLayerGroup(workspaceName, layerGroupName);
    }

    /**
     * 获取所有图层组名称列表
     *
     * @param workspaceName 工作空间名称
     * @return 图层组名称列表
     */
    public ArrayList<String> getLayerGroups(String workspaceName) {
        RESTLayerGroupList layerGroups = geoServerRESTReader.getLayerGroups(workspaceName);

        ArrayList<String> layerGroupList = new ArrayList<>();

        for (NameLinkElem layerGroup : layerGroups) {
            String layerGroupName = layerGroup.getName();

            layerGroupList.add(layerGroupName);
        }

        return layerGroupList;
    }

    /**
     * 判断某个样式是否存在
     *
     * @param styleName 样式名称
     * @return 样式是否存在
     */
    public Boolean existsStyle(String styleName) {
        return geoServerRESTReader.existsStyle(styleName);
    }

    /**
     * 判断某工作空间下是否包含 style 服务
     *
     * @param workspaceName 工作空间民称
     * @param styleName     style 服务名称
     * @return 是否包含style服务
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsStyleFromWorkspace(String workspaceName, String styleName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        return geoServerRESTReader.existsStyle(workspaceName, styleName);
    }

    /**
     * 获取所有样式服务
     *
     * @return 样式服务名称列表
     */
    public ArrayList<String> getStyles() {
        RESTStyleList styles = geoServerRESTReader.getStyles();

        ArrayList<String> stylesList = new ArrayList<>();

        for (NameLinkElem style : styles) {
            String styleName = style.getName();

            stylesList.add(styleName);
        }

        return stylesList;
    }

    /**
     * 获取某工作空间下所有样式服务
     *
     * @param workspaceName 工作空间名称
     * @return 样式服务名称列表
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public ArrayList<String> getStyles(String workspaceName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }

        RESTStyleList styles = geoServerRESTReader.getStyles(workspaceName);

        ArrayList<String> stylesList = new ArrayList<>();

        for (NameLinkElem style : styles) {
            String styleName = style.getName();

            stylesList.add(workspaceName + ":" + styleName);
        }

        return stylesList;
    }

}
