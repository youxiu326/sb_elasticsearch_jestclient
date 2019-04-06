package com.huarui.service.impl;

import com.huarui.entity.SearchEntity;
import com.huarui.exection.JestExcetion;
import com.huarui.service.JestClientService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by lihui on 2019/1/30.
 * JestClient 操作类
 */
@Service
public class JestClientServiceImpl implements JestClientService {

    private static Logger LOGGER = LoggerFactory.getLogger(JestClientServiceImpl.class);

    @Autowired
    private JestClient jestClient;

    private String indexName = "test";//索引名称
    private String indexType = "books";//文档类型

    /**
     * 创建索引
     * @param t
     * @param indexKey
     * @param <T>
     * @return
     * @throws JestExcetion
     */
    @Override
    public <T extends SearchEntity> JestResult index(T t, String indexKey) throws JestExcetion{
        JestResult jestResult = null;
        Index index = new Index
                .Builder(t)//文档
                .index(indexName)//索引
                .type(indexType)//文档类型
                .id(indexKey)//key
                .build();
        try {
            jestResult = jestClient.execute(index);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return jestResult;
    }

    /**
     * 删除索引
     * @param id
     * @return
     * @throws JestExcetion
     */
    @Override
    public JestResult delete(String id) throws JestExcetion{
        JestResult jestResult = null;
        Delete delete = new Delete.Builder(id)
                .index(indexName)
                .type(indexType)
                .build();
        try {
            jestResult = jestClient.execute(delete);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return jestResult;
    }

    /**
     * 查询索引
     * @param id
     * @param clazz
     * @param <T>
     * @return
     * @throws JestExcetion
     */
    @Override
    public <T extends SearchEntity> T get(String id, Class<T> clazz) throws JestExcetion{
        JestResult jestResult = null;
        Get get = new Get.Builder(indexName, id)
                .type(indexType)
                .build();
        try {
            jestResult = jestClient.execute(get);
            if (jestResult.isSucceeded()){
                return jestResult.getSourceAsObject(clazz);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return null;
    }

    @Override
    public JestResult deleteIndex() throws JestExcetion{
        JestResult jestResult = null;
        Delete delete = new Delete.Builder(indexName).build();
        try {
            jestResult = jestClient.execute(delete);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return jestResult;
    }

    /**
     * 批量新增索引
     * @param t
     * @param <T>
     * @return
     * @throws JestExcetion
     */
    @Override
    public <T extends SearchEntity> JestResult indexBatch(List<T> t) throws JestExcetion {
        JestResult jestResult = null;
        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(indexType);
        for (T obj : t) {
            Index index = new Index.Builder(obj).build();
            bulk.addAction(index);
        }
        try {
            jestResult = jestClient.execute(bulk.build());
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return jestResult;
    }

    /**
     * 分页查询索引
     * @param from 第一页
     * @param size 每页几条
     * @param queryBuilder 查询条件
     * @param clazz
     * @param <T>
     * @return
     * @throws JestExcetion
     */
    @Override
    public <T extends SearchEntity> List<T> pageSearch(int from, int size, QueryBuilder queryBuilder, Class<T> clazz) throws JestExcetion {

        List<T> books = null;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchSourceBuilder.query(queryBuilder);

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName)
                .addType(indexType)
                .build();
        try {
            JestResult jestResult = jestClient.execute(search);
            if (jestResult.isSucceeded()){
               books = jestResult.getSourceAsObjectList(clazz);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return books;
    }

    @Override
    public <T extends SearchEntity> List<T> search(QueryBuilder queryBuilder, Class<T> clazz) throws JestExcetion {

        List<T> books = null;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName)
                .addType(indexType)
                .build();

        try {
            JestResult jestResult = jestClient.execute(search);
            if (jestResult.isSucceeded()){
                books = jestResult.getSourceAsObjectList(clazz);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
        return books;
    }

    @Override
    public Double count(QueryBuilder queryBuilder) throws JestExcetion {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);

        Count count = new Count.Builder()
                .addIndex(indexName)
                .addType(indexType)
                .query(searchSourceBuilder.toString())
                .build();
        try {
            CountResult results = jestClient.execute(count);
            return results.getCount();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new JestExcetion(e.getMessage());
        }
    }

}
