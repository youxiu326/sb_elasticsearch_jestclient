package com.huarui;

import com.huarui.entity.Book;
import com.huarui.exection.JestExcetion;
import com.huarui.service.JestClientService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.indices.mapping.PutMapping;
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


    /**
     * 新增or修改
     */
    @Test
    public void testUpdate(){

        String indexName = "newIndex";
        String indexType = "newType";

        String mappingString = "{UserMapper={properties={createtm={analyzer=ik_max_word, type=text, fields={pinyin={analyzer=pinyin_analyzer, type=text}, raw={ignore_above=256, type=keyword}}}, name={analyzer=ik_max_word, type=text, fields={pinyin={analyzer=pinyin_analyzer, type=text}, raw={ignore_above=256, type=keyword}}}, description={analyzer=ik_max_word, type=text, fields={pinyin={analyzer=pinyin_analyzer, type=text}, raw={ignore_above=256, type=keyword}}}, id={type=long}, age={type=integer}}}}";
        PutMapping.Builder builder = new PutMapping.Builder(indexName, indexType, mappingString);

        try {
            JestResult jestResult = jestClient.execute(builder.build());
            System.out.println("createIndexMapping result:{}" + jestResult.isSucceeded());
            if (jestResult.isSucceeded()){
                System.out.println("成功啦");
            }else {
                System.out.println("失败啦");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
