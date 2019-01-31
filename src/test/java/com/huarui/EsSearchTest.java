package com.huarui;

import com.huarui.entity.Book;
import com.huarui.utils.JsonUtils;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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
    int indexFrom = 0;
    int indexSize = 10;

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

    /***
     *  match和term的区别是,match查询的时候,elasticsearch会根据你给定的字段提供合适的分析器,而term查询不会有分析器分析的过程
     *  match查询相当于模糊匹配,只包含其中一部分关键词就行
     *
          GET /library/books/_search
         {
         "fields":["preview","title"]
             "query":{
                "match":{
                    "preview":"elasticsearch"
                }
            }
         }
     *
     *
     */


    /**
     * 使用QueryBuilder
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符            TODO 如:matchQuery("name", "张三")
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段 TODO 如:multiMatchQuery("张三 001", "name", "code"..);
     * matchAllQuery();         匹配所有文件
     */
    @Test
    public void termQuery() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders
                //.matchQuery("name", "哈哈哈");//单个匹配
                 .termQuery("name", "哈哈哈");//单值完全匹配查询
                // .multiMatchQuery("嘻嘻 SGS","name","code"); //匹配多个字段
                //.matchAllQuery();//匹配所有文件

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(indexSize);
        searchSourceBuilder.from(indexFrom);
        String queryStr = searchSourceBuilder.toString();

        System.out.println();
        System.out.println(queryStr);

        Search search = new Search.Builder(queryStr)
                .addIndex(indexName)
                .addType(indexType)
                .build();
        JestResult jestResult = jestClient.execute(search);
        if(jestResult.isSucceeded()) {
            System.out.println("成功了");
            List<Book> books = jestResult.getSourceAsObjectList(Book.class);
            if(books!=null && books.size()>0){
                books.stream().forEach(it-> System.out.println(it));
            }else {
                System.out.println("未查询到匹配的结果");
            }
        }else {
            System.out.println("失败了");
        }

    }


    /**
     * 组合查询
     * must(QueryBuilders)      : AND
     * mustNot(QueryBuilders)   : NOT
     * should:                  : OR
     */
    @Test
    public void boolQuery() throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("code","SGS"))
                .mustNot(QueryBuilders.termQuery("name", "嘻嘻"))
                .should(QueryBuilders.termQuery("name", "哈哈哈"));

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(indexSize);
        searchSourceBuilder.from(indexFrom);
        String queryStr = searchSourceBuilder.toString();

        System.out.println();
        System.out.println(queryStr);

        Search search = new Search.Builder(queryStr)
                .addIndex(indexName)
                .addType(indexType)
                .build();
        JestResult jestResult = jestClient.execute(search);
        if(jestResult.isSucceeded()) {
            System.out.println("成功了");
            List<Book> books = jestResult.getSourceAsObjectList(Book.class);
            if(books!=null && books.size()>0){
                books.stream().forEach(it-> System.out.println(it));
            }else {
                System.out.println("未查询到匹配的结果");
            }
        }else {
            System.out.println("失败了");
        }

    }

    /**
     * 通配符查询, 支持 *
     * 匹配任何字符序列, 包括空
     * 避免* 开始, 会检索大量内容造成效率缓慢
     */
    @Test
    public void testWildCardQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name", "哈*");

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(indexSize);
        searchSourceBuilder.from(indexFrom);
        String queryStr = searchSourceBuilder.toString();

        System.out.println();
        System.out.println(queryStr);

        Search search = new Search.Builder(queryStr)
                .addIndex(indexName)
                .addType(indexType)
                .build();
        JestResult jestResult = jestClient.execute(search);
        if(jestResult.isSucceeded()) {
            System.out.println("成功了");
            List<Book> books = jestResult.getSourceAsObjectList(Book.class);
            if(books!=null && books.size()>0){
                books.stream().forEach(it-> System.out.println(it));
            }else {
                System.out.println("未查询到匹配的结果");
            }
        }else {
            System.out.println("失败了");
        }

    }

    /**
     * 范围内查询
     */
    @Test
    public void testRangeQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("price")
                .from("0.7")
                .to("1.7")
                .includeLower(true)     // 包含上界
                .includeUpper(true);    // 包含下界

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(indexSize);
        searchSourceBuilder.from(indexFrom);
        String queryStr = searchSourceBuilder.toString();

        Search search = new Search.Builder(queryStr)
                .addIndex(indexName)
                .addType(indexType)
                .build();

        JestResult jestResult = jestClient.execute(search);
        if(jestResult.isSucceeded()) {
            System.out.println("成功了");
            List<Book> books = jestResult.getSourceAsObjectList(Book.class);
            if(books!=null && books.size()>0){
                books.stream().forEach(it-> System.out.println(it));
            }else {
                System.out.println("未查询到匹配的结果");
            }
        }else {
            System.out.println("失败了");
        }


    }


}
