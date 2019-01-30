package com.huarui.service.impl;

import com.huarui.entity.SearchEntity;
import com.huarui.service.JestClientService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by lihui on 2019/1/30.
 * JestClient 操作类
 */
@Service
public class JestClientServiceImpl implements JestClientService {

    @Autowired
    private JestClient jestClient;

    private String indexName = "test";//索引名称
    private String indexType = "books";//文档类型

    @Override
    public <T extends SearchEntity> JestResult index(T t, String indexKey) {
        JestResult jestResult = null;
        Index index = new Index
                .Builder(t)//文档
                .index(indexName)//索引
                .type(indexType)//文档类型
                .id(indexKey)//key
                .build();
        try {
            jestResult = jestClient.execute(index);
            if (jestResult.isSucceeded()){
                System.out.println("成功啦");
            }else {
                System.out.println("失败啦");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jestResult;
    }

    @Override
    public JestResult delete(String id) {
        JestResult jestResult = null;
        Delete delete = new Delete.Builder(id)
                .index(indexName)
                .type(indexType)
                .build();
        try {
            jestResult = jestClient.execute(delete);
            if (jestResult.isSucceeded()){
                System.out.println("成功啦");
            }else {
                System.out.println("失败啦");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jestResult;
    }

    @Override
    public <T extends SearchEntity> T get(String id, Class<T> clazz) {
        JestResult jestResult = null;
        Get get = new Get.Builder(indexName, id)
                .type(indexType)
                .build();
        try {
            jestResult = jestClient.execute(get);
            return jestResult.getSourceAsObject(clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JestResult deleteIndex() {
        JestResult jestResult = null;
        Delete delete = new Delete.Builder(indexName).build();
        try {
            jestResult = jestClient.execute(delete);
            if (jestResult.isSucceeded()){
                System.out.println("成功啦");
            }else {
                System.out.println("失败啦");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO 查询
}
