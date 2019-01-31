package com.huarui;

import com.huarui.entity.Book;
import io.searchbox.client.JestClient;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by lihui on 2019/1/31.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsHightTest {

    // 参考博客: https://www.cnblogs.com/wenbronk/p/6432990.html


    @Autowired
    private JestClient jestClient;

    String indexName = "test";
    String indexType = "books";
    int indexFrom = 0;
    int indexSize = 10;

    /**
     * count 查询
     */
    @Test
    public void testCount() throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders
                .matchQuery("name", "哈哈哈");//单个匹配
        searchSourceBuilder.query(queryBuilder);
        String queryStr = searchSourceBuilder.toString();
        Count count = new Count.Builder()
                .addIndex(indexName)
                .addType(indexType)
                .query(queryStr)
                .build();
        CountResult results = jestClient.execute(count);
        Double result = results.getCount();
        System.out.println("=============");
        System.out.println(result);
        System.out.println("=============");
    }

    /**
     * 前缀查询
     */
    @Test
    public void prefixQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders
                .prefixQuery("name", "哈");//前缀查询
        searchSourceBuilder.query(queryBuilder);
        String queryStr = searchSourceBuilder.toString();

        Search search = new Search.Builder(queryStr)
                .addIndex(indexName)
                .addType(indexType)
                .build();

        SearchResult searchResult = jestClient.execute(search);
        if (searchResult.isSucceeded()){
            List<Book> books = searchResult.getSourceAsObjectList(Book.class, true);
            books.stream().forEach(it-> System.out.println(it));
        }

    }

    @Test
    public void testIndexQuery() throws IOException {

        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("1");

        System.out.println(queryBuilder.toString());

        Search search = new Search.Builder(queryBuilder.toString())
                .addIndex(indexName)
                .addType(indexType)
                .build();
        SearchResult result = jestClient.execute(search);
        if(result.isSucceeded()){
            List<Book> books = result.getSourceAsObjectList(Book.class, true);
            books.stream().forEach(it-> System.out.println(it));
        }else {
            System.out.println("失败啦");
        }

    }

}
