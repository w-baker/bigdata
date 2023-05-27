package com.example.myweb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myweb.dao.ESDao;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class MywebApplicationTests {
    @Autowired
    private RestClient restClient;

    @Test
    void contextLoads() {
        String filepath = "hdfs://192.168.200.101:8020/rt/testDir/人大部20.7班王昌宏200107101法制报告.docx";
        String[] split = filepath.split("/");
        // 获取文件名字
        String fileName = split[split.length-1];

        String fileUrl = filepath.replace("/" + fileName,"");

        Request request = new Request("POST", "/human_resource/_search");
        String indexScript = "{\n" +
                "  \"query\":{\n" +
                "    \"bool\":{\n" +
                "      \"must\":[\n" +
                "        {\n" +
                "          \"term\":{\n" +
                "            \"filename.keyword\":\"" + fileName+"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"term\":{\n" +
                "            \"hdfsurl.keyword\":\"" + fileUrl + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        request.setEntity(new NStringEntity(indexScript, ContentType.APPLICATION_JSON));
        try{
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            String str = jsonObject.getJSONObject("hits").get("hits").toString();
            JSONArray arr = JSONArray.parseArray(str);

            jsonObject = arr.getJSONObject(0).getJSONObject("_source");
            String fileInfo = (String) jsonObject.get("fileinfo");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
