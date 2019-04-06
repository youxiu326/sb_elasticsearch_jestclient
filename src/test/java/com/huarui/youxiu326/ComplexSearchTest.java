package com.huarui.youxiu326;

import com.huarui.entity.Book;
import com.huarui.exection.JestExcetion;
import com.huarui.service.JestClientService;
import com.huarui.service.impl.JestClientServiceImpl;
import io.searchbox.client.JestResult;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplexSearchTest {

    private static Logger LOGGER = LoggerFactory.getLogger(ComplexSearchTest.class);

    @Autowired
    private JestClientService jestService;

    /**
     * 批量创建索引
     * @throws JestExcetion
     */
    @Test
    public void testIndexBatch() throws JestExcetion {

        List<Book> books = new ArrayList<>();
        for (int i=327;i<=337;i++){
            Book book = new Book(i+"",new Date(),"youxiu"+i,"创建一个索引"+i,3.26F);
            books.add(book);
        }
        JestResult jestResult = jestService.indexBatch(books);
        if (jestResult.isSucceeded()){
            LOGGER.info("创建成功");
        }else{
            LOGGER.info("创建失败");
        }

    }


    /**
     * Term 精准查询 并分页
     * @throws JestExcetion
     */
    @Test
    public void testPageSearchUseTerm() throws JestExcetion {
        int from = 0;
        int size = 2;
        //精确搜索
        //TermQueryBuilder age = QueryBuilders.termQuery("code", "youxiu326");
        //一次匹配多个值
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("code", Arrays.asList("youxiu326", "youxiu327", "youxiu328", "youxiu329", "youxiu330", "youxiu331"));
        //匹配多个字段
        //QueryBuilders.multiMatchQuery("匹配值","name","code");



        List<Book> books = jestService.pageSearch(from, size, termsQueryBuilder, Book.class);
        if (books!=null && books.size()>0){
            books.stream().forEach(it-> System.out.println(it));
        }else{
            LOGGER.info("未查询到匹配的数据");
        }

    }

    /**
     * Wildcard 通配符查询 并分页   (支持 *,避免* 开始避免检索大量内容造成效率缓慢)
     * @throws JestExcetion
     */
    @Test
    public void testPageSearchUseWildcard() throws JestExcetion {
        int from = 0;
        int size = 2;

        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("code", "youxiu*");

        List<Book> books = jestService.pageSearch(from, size, wildcardQueryBuilder, Book.class);
        if (books!=null && books.size()>0){
            books.stream().forEach(it-> System.out.println(it));
        }else{
            LOGGER.info("未查询到匹配的数据");
        }
    }


    /**
     * 区间搜索
     * @throws JestExcetion
     */
    @Test
    public void testPageSearchUseRange() throws JestExcetion {

        RangeQueryBuilder RangeQueryBuilder = QueryBuilders.rangeQuery("price")
                .from(0.9F)
                .to(3.26F)
                .includeLower(true)     // 包含上界
                .includeUpper(true);    // 包含下界

        List<Book> books = jestService.search(RangeQueryBuilder, Book.class);
        if (books!=null && books.size()>0){
            books.stream().forEach(it-> System.out.println(it));
        }else{
            LOGGER.info("未查询到匹配的数据");
        }
    }

    /**
     * 组合查询
     *  must(QueryBuilders)      : AND
     *  mustNot(QueryBuilders)   : NOT
     *  should:                  : OR
     * @throws JestExcetion
     */
    @Test
    public void testPageSearchUseBool() throws JestExcetion {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("code", "youxiu336"))
                .mustNot(QueryBuilders.termQuery("name", "创建一个索引332"))
                .should(QueryBuilders.termQuery("name", "创建一个索引337"));

        List<Book> books = jestService.search(boolQueryBuilder,Book.class);
        if (books!=null && books.size()>0){
            books.stream().forEach(it-> System.out.println(it));
        }else{
            LOGGER.info("未查询到匹配的数据");
        }

    }

    /**
     * count查询
     * @throws JestExcetion
     */
    @Test
    public void testCount() throws JestExcetion {

        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("code", "youxiu*");
        Double count = jestService.count(wildcardQueryBuilder);
        System.out.println("查询到"+count+"个");

    }

}
