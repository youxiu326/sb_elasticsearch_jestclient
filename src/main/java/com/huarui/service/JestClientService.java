package com.huarui.service;

import com.huarui.entity.SearchEntity;
import com.huarui.exection.JestExcetion;
import io.searchbox.client.JestResult;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

/**
 * Created by lihui on 2019/1/30.
 */
public interface JestClientService {

    <T extends SearchEntity> JestResult index(T t, String indexKey) throws JestExcetion;

    JestResult delete(String id)throws JestExcetion;

    <T extends SearchEntity> T get(String id, Class<T> clazz) throws JestExcetion;

    JestResult deleteIndex() throws JestExcetion;

    /*********************我是分割线************************/

    <T extends SearchEntity> JestResult indexBatch(List<T> t) throws JestExcetion;

    <T extends SearchEntity> List<T> pageSearch(int from, int size, QueryBuilder queryBuilder,Class<T> clazz)
            throws JestExcetion;


    <T extends SearchEntity> List<T> search(QueryBuilder queryBuilder,Class<T> clazz) throws JestExcetion;

    Double count(QueryBuilder queryBuilder) throws JestExcetion;

}
