package com.example.webgistest.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hsqldb.lib.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUniversity extends CommonDao {
    private String url = "https://gaokao.chsi.com.cn/sch/search.do";
    private String agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
    private Map cityMap = new HashMap();
    public GetUniversity(){
        jdbcTemplate = new JdbcTemplate();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/webgistest");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        jdbcTemplate.setDataSource(dataSource);
    }

    public List getListBySql(String sql){
        return jdbcTemplate.queryForList(sql);
    }

    public void insertDataBySql(String sql){
        jdbcTemplate.execute(sql);
    }

    /**
     * 获取城市信息
     * @return
     */
    public String getGxCity(){
        StringBuffer sql = new StringBuffer();
        sql.append("insert into gx_city");

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(agent)
                    .get();

            Element element = doc.getElementById("ssdm");
            Elements citys = element.children();
            for(int i=0;i<citys.size();i++){
                Element city = citys.get(i);
                String val = city.attr("value");
                String html = city.html();
                if(!StringUtil.isEmpty(val)){
                    cityMap.put(html, val);
                    sql.append("\r\nselect "+val+", '"+html+"'");
                    if(i!=citys.size()-1) sql.append(" union ");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return sql.toString();
    }

    /**
     * 获取高校信息
     * @param startNo
     * @return
     */
    public String getUniversityInfo(int startNo){
        StringBuffer sql = new StringBuffer();
        sql.append("insert into gx_university(name,department,schoo1_lev,types,province,first_university,first_discipline,graduate,satisfaction,lon,lat)");

        try{
            Document doc = Jsoup.connect(url+"?start="+startNo)
                    .userAgent(agent)
                    .get();

            Element element = doc.getElementsByClass("ch-table").get(0);
            Elements universities = element.getElementsByTag("tr");
            for(int i=1;i<universities.size();i++){
                Element university = universities.get(i);
                Elements elements = university.children();
                //0-院校名称，1-所属城市，4-学历层次，5-院校特性，6-是否研究生院校
                String name = elements.get(0).children().get(0).html();
                String province = elements.get(1).html();
                String department =elements.get(2).html();
                String types =elements.get(3).html();
                String schoo1_lev = elements.get(4).html();
                Elements first_un = elements.get(5).children();
                int first_university = first_un.size()>0?1:0;
                Elements first_dis = elements.get(6).children();
                int first_discipline = first_dis.size()>0?1:0;
                Elements gra = elements.get(7).children();
                int graduate = gra.size()>0?1:0;
                Elements sat = elements.get(8).children();
                String satisfaction = sat.size()>0? sat.get(0).html():"0";
                String[] lonLat = getLonLat(name);

                sql.append("\r\nselect '"+name+"', '"+department+"', '"+schoo1_lev+"', '"+
                        types+"', '"+province+"', "+first_university+", "+first_discipline+", "+
                        graduate+", "+satisfaction+", "+lonLat[0]+", "+ lonLat[1]);
                if(i!=universities.size()-1) sql.append(" union ");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return sql.toString();
    }

    public String getUrlContent(String url){
        String content = "";
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            content = sb.toString();
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String[] getLonLat(String name){
        String[] lonLat = new String[]{"0", "0"};
        String geoUrl = "http://api.tianditu.gov.cn/apiserver/ajaxproxy?proxyReqUrl=http://api.tianditu.gov.cn/search?postStr={'keyWord':'"+name+"','level':'10','mapBound':'115.38086979227658,39.39210719505213,117.49024479227658,40.4344350759115','queryType':'1','start':'0','count':'1'}&type=query&tk=92c2ba98e695eb63048b3df93a9ad732";
        String geoContent = getUrlContent(geoUrl);
        geoContent = geoContent.substring(19, geoContent.length() - 1);
        JSONObject jsonObject = JSON.parseObject(geoContent);
        JSONArray jsonArray = jsonObject.getJSONArray("pois");
        if(jsonArray!=null && jsonArray.size()>0){
            JSONObject jsonPoi = (JSONObject) jsonArray.get(0);
            lonLat = jsonPoi.get("lonlat").toString().split(" ");
        }
        return lonLat;
    }

    public static void main(String[] args){
        double start = System.currentTimeMillis();
        GetUniversity getUniversity = new GetUniversity();
        getUniversity.getGxCity();
        System.out.println(Arrays.toString(getUniversity.getLonLat("长江大学")));
        int pageSize = 20;
//        for(int i=0;i<1;i++){
//            String universitySql = getUniversity.getUniversityInfo(i*pageSize);
//            getUniversity.insertDataBySql(universitySql);
//            System.out.println("第"+(i+1)+"页的数据爬取完成。");
//        }
        double end = System.currentTimeMillis();
        System.out.println("共耗时："+(end-start)+"MS");
    }
}
