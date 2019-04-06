package com.huarui;

import com.huarui.entity.Book;
import com.huarui.exection.JestExcetion;
import com.huarui.service.JestClientService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

/**
 * Created by lihui on 2019/1/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsBaseTest {

    @Autowired
    private JestClient jestClient;

    @Autowired
    private JestClientService jestService;

    /**
     * 新增or修改
     */
    @Test
    public void testUpdate(){

        String indexName = "test";
        String indexType = "books";
        String indexKey = "3";

        Book book = new Book("3",new Date(),"SGS","补哈补",0.8F);

        Index index = new Index
                .Builder(book)//文档
                .index(indexName)//索引
                .type(indexType)//文档类型
                .id(indexKey)//key
                .build();

        try {
            JestResult execute = jestClient.execute(index);
            if (execute.isSucceeded()){
                System.out.println("成功啦");
            }else {
                System.out.println("失败啦");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单个查询
     */
    @Test
    public void testGet() throws JestExcetion {
        Book book = jestService.get("1", Book.class);
        System.out.println(book.getName());
        System.out.println(book.getCode());
        System.out.println(book.getId());
        /*if (delete.isSucceeded()){
            System.out.println("成功");
        }else {
            System.out.println("失败");
        }*/
    }

    /**
     * 删除
     */
    @Test
    public void testDelete() throws JestExcetion {
        JestResult delete = jestService.delete("1");
        if (delete.isSucceeded()){
            System.out.println("成功");
        }else {
            System.out.println("失败");
        }
    }

}
