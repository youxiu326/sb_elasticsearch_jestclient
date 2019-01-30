package com.huarui.service;

import com.huarui.entity.SearchEntity;
import io.searchbox.client.JestResult;

/**
 * Created by lihui on 2019/1/30.
 */
public interface JestClientService {

    <T extends SearchEntity> JestResult index(T t, String indexKey);

    JestResult delete(String id);

    <T extends SearchEntity> T get(String id, Class<T> clazz);

    JestResult deleteIndex();

    //<T extends SearchEntity> List<T> getPageData(PageModel page, Class<T> clazz) throws JestExcetion;

}
