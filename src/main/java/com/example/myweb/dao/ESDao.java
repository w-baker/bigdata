package com.example.myweb.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myweb.entity.HDFSObject;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ESDao {
    @Value("${es.host}")
    private String esHost;
    @Value("${es.port}")
    private int esPort;
    @Value("${es.scheme}")
    private String esScheme;
    private RestClient restClient = RestClient.builder(new HttpHost(esHost, esPort, esScheme)).build();

    public List<HDFSObject> query(String keywords) {
        Request request = new Request("GET", "/human_resource/_doc/_search");
        String indexscript = "{\r\n" +
                "  \"query\": {\r\n" +
                "    \"match\": {\r\n" +
                "      \"fileinfo\": \"" + keywords + "\"\r\n" +
                "    }\r\n" +
                "  }\r\n" +
                "}";
        request.setEntity(new NStringEntity(indexscript, ContentType.APPLICATION_JSON));
        List<HDFSObject> filelist = new ArrayList<HDFSObject>();
        HDFSObject fileobject = null;
        try {
            Response response = restClient.performRequest(request);
            RequestLine requestLine = response.getRequestLine();
            HttpHost host = response.getHost();
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders();
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            String str = jsonObject.getJSONObject("hits").get("hits").toString();
            JSONArray arr = JSONArray.parseArray(str);
            for (int i = 0; i < arr.size(); i++) {
                String tmp = arr.getJSONObject(i).get("_source").toString();
                JSONObject o = JSONObject.parseObject(tmp);
                fileobject = new HDFSObject();
                fileobject.setName(o.get("filename").toString());
                fileobject.setPath(o.get("hdfsurl").toString() + "/" + o.get("filename").toString());
                fileobject.setAccessTime(o.get("createdate").toString());
                fileobject.setUsername(o.get("username").toString());
                fileobject.setIsdirectory(false);
                long filelen = Long.valueOf(o.get("filelength").toString());
                fileobject.setLen(filelen);
                filelist.add(fileobject);

            }
            //System.out.println(requestLine.toString()+","+host.getHostName()+","+statusCode+","+headers.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filelist;
    }

    private void delete() {
        Request request = new Request("POST", "student2/_delete_by_query");
        String indexscript = "{\r\n" +
                "  \"query\": {\r\n" +
                "    \"term\": {\r\n" +
                "      \"name\": \"aaa\"\r\n" +
                "    }\r\n" +
                "  }\r\n" +
                "}";
        request.setEntity(new NStringEntity(indexscript, ContentType.APPLICATION_JSON));
        try {
            Response response = restClient.performRequest(request);
            RequestLine requestLine = response.getRequestLine();
            HttpHost host = response.getHost();
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(requestLine.toString() + "," + host.getHostName() + "," + statusCode + "," + headers.toString());
            System.out.println(responseBody);
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addnew(String indexscript) {
        Request request = new Request("POST", "/human_resource/_doc/");
        request.setEntity(new NStringEntity(indexscript, ContentType.APPLICATION_JSON));
        try {
            Response response = restClient.performRequest(request);
            RequestLine requestLine = response.getRequestLine();
            HttpHost host = response.getHost();
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(requestLine.toString() + "," + host.getHostName() + "," + statusCode + "," + headers.toString());
            System.out.println(responseBody);
            //restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createIndex() {
        Request request = new Request("PUT", "/student2");
        String indexscript = "{" +
                "    \"settings\": {" +
                "        \"number_of_shards\": 1," +
                "        \"number_of_replicas\": 1" +
                "    }," +
                "    \"mappings\": {" +
                "      \"properties\": {" +
                "            \"name\": {\"type\": \"text\",\"analyzer\": \"ik_max_word\",\"search_analyzer\": \"ik_smart\"}," +
                "            \"address\": {\"type\": \"text\",\"analyzer\": \"ik_max_word\",\"search_analyzer\": \"ik_smart\"}," +
                "            \"age\": {\"type\": \"integer\"},\r\n" +
                "            \"interests\": {\"type\": \"text\",\"analyzer\": \"ik_max_word\",\"search_analyzer\": \"ik_smart\",\"fielddata\": true}," +
                "            \"birthday\": {\"type\": \"date\"}\r\n" +
                "      }" +
                "    }" +
                "}";
        request.setEntity(new NStringEntity(indexscript, ContentType.APPLICATION_JSON));
        try {
            Response response = restClient.performRequest(request);
            RequestLine requestLine = response.getRequestLine();
            HttpHost host = response.getHost();
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(requestLine.toString() + "," + host.getHostName() + "," + statusCode + "," + headers.toString());
            System.out.println(responseBody);
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowNodes() {
        Request request = new Request("GET", "/_cat/nodes");
        try {
            Response response = restClient.performRequest(request);
            RequestLine requestLine = response.getRequestLine();
            HttpHost host = response.getHost();
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(requestLine.toString() + "," + host.getHostName() + "," + statusCode + "," + headers.toString());
            System.out.println(responseBody);
            restClient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}