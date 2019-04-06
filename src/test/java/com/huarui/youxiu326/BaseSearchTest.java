package com.huarui.youxiu326;

import com.huarui.entity.Book;
import com.huarui.exection.JestExcetion;
import com.huarui.service.JestClientService;
import com.huarui.service.impl.JestClientServiceImpl;
import io.searchbox.client.JestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseSearchTest {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseSearchTest.class);

    @Autowired
    private JestClientService jestService;

    /**
     * 创建索引
     */
    @Test
    public void testIndex() throws JestExcetion {

        Book book = new Book("326",new Date(),"youxiu326","创建一个索引",3.26F);

        JestResult index = jestService.index(book, book.getId());
        if (index.isSucceeded()){
            LOGGER.info("创建成功了");
        }
    }

    /**
     * 根据id查询索引
     */
    @Test
    public void testGet() throws JestExcetion {

        Book book = jestService.get("326", Book.class);
        System.out.println(book);

    }

    /**
     * 根据id删除索引
     */
    @Test
    public void testDelete() throws JestExcetion {

        JestResult delete = jestService.delete("326");
        if (delete.isSucceeded()){
            LOGGER.info("删除成功");
        }else {
            LOGGER.info("删除失败");
        }

    }
}
