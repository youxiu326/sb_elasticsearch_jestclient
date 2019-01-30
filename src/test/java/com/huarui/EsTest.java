package com.huarui;

import com.huarui.entity.Book;
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
public class EsTest {

    @Autowired
    private JestClient jestClient;

    /**
     * 新增or修改
     */
    @Test
    public void testUpdate(){

        String indexName = "test";
        String indexType = "books";
        String indexKey = "1";

        Book book = new Book("1",new Date(),"TBS","哈哈哈",1.8F);

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

    @Test
    public void testDelete(){



    }

}
