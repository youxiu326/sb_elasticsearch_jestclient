package com.huarui;

import com.huarui.utils.JsonUtils;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihui on 2019/1/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsSearchTest {

    @Autowired
    private JestClient jestClient;

    String indexName = "test";
    String indexType = "books";

    @Test
    public void test01() throws IOException {

        String queryStr = "";
        Search search = new Search.Builder(queryStr)
                .addIndex(indexName)
                .addType(indexType)
                .build();

        JestResult jestResult = jestClient.execute(search);
        if(jestResult.isSucceeded()) {
            System.out.println("查询成功");
            System.out.println(jestResult);
        }else {
            System.out.println("查询失败");
        }

    }


    public String createqueryStr(String s){

        //查询配置构造
        Map<String,Object> condition = new HashMap<String,Object>();
        //查询总条件
        Map<String,Object> query = new HashMap<String,Object>();
        //bool语句
        Map<String,Object> bool = new HashMap<String,Object>();
        //must语句
        List<Map<String,Object>> must = new ArrayList<Map<String,Object>>();
        //should语句
        List<Map<String,Object>> should = new ArrayList<Map<String,Object>>();
        //sort语句
        List<Map<String,Object>> sort = new ArrayList<Map<String,Object>>();


        return JsonUtils.parseString(condition);
    }


}
