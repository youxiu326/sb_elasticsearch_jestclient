# sb_elasticsearch_jestclient
使用jestClient操作elasticsearch


全文搜索引擎 Elasticsearch 入门教程
http://www.ruanyifeng.com/blog/2017/08/elasticsearch.html


springboot+jest 操作Elasticsearch 参考：https://www.cnblogs.com/youxiu326/p/elasticSearch-jestClient.html


创建mapper 参考：https://blog.csdn.net/tanga842428/article/details/76229235

中文doc: https://www.elastic.co/guide/cn/elasticsearch/guide/cn/query-time-boosting.html

```

1.创建一个索引

发送post请求:
http://192.168.79.129:9200/productindex

返回:
{
     "acknowledged": true
}

2.查看索引mapping内容

发送get请求：
http://192.168.79.129:9200/productindex/_mapping?pretty
返回:
{
  "productindex": {
    "mappings": {}
  }
}

3.给productindex索引加一个type，type name为product，并设置mapping

发送post请求:
http://192.168.79.129:9200/productindex/product/_mapping?pretty
参数:
{
    "product": {
            "properties": {
                "title": {
                    "type": "string",
                    "store": "yes"
                },
                "description": {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "price": {
                    "type": "double"
                },
                "onSale": {
                    "type": "boolean"
                },
                "type": {
                    "type": "integer"
                },
                "createDate": {
                    "type": "date"
                }
            }
        }
  }
  
  返回:
  {
    "acknowledged": true
  }
  
  4.查看mapper是否创建成功
  
  发送get请求:
  http://192.168.79.129:9200/productindex/_mapping?pretty
  
  返回:
  {
    "productindex": {
      "mappings": {
        "product": {
          "properties": {
            "createDate": {
              "type": "date",
              "format": "strict_date_optional_time||epoch_millis"
            },
            "description": {
              "type": "string",
              "index": "not_analyzed"
            },
            "onSale": {
              "type": "boolean"
            },
            "price": {
              "type": "double"
            },
            "title": {
              "type": "string",
              "store": true
            },
            "type": {
              "type": "integer"
            }
          }
        }
      }
    }
  }
  
  5.修改mapping (product新增一个字段，那么需要修改mapping)
  
  发送post请求:
  http://192.168.79.129:9200/productindex/product/_mapping?pretty
  
  参数:
  {
   "product": {
              "properties": {
                   "amount":{
                      "type":"integer"
                 }
              }
          }
  }
  
  返回:
  {
    "acknowledged": true
  }
  
  
  
  
 

  6.创建索引
  
  
  {
    "product": {
        "properties": {
            "title": {
                "type": "keyword"
            },
            "description": {
                "type": "keyword"
            },
            "price": {
                "type": "double"
            },
            "onSale": {
                "type": "boolean"
            },
            "type": {
                "type": "integer"
            },
            "createDate": {
                "type": "date"
            },
            "colorDetails": {
                "properties":{
                    "image": {
                        "type": "keyword"
                    },
                    "code": {
                        "type": "keyword"
                    }
                }
            }
        }
    }
}

```


 ---------------------------------------------------

```

计算集群中文档的数量，我们可以用这个
curl -XGET 'http://localhost:9200/_count?pretty' -d '
{
    "query": {
        "match_all": {}
    }
}


创建一个索引:

curl -X PUT "localhost:9200/megacorp/employee/1" -H 'Content-Type: application/json' -d'
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
'
curl -X PUT "localhost:9200/megacorp/employee/2" -H 'Content-Type: application/json' -d'
{
    "first_name" :  "Jane",
    "last_name" :   "Smith",
    "age" :         32,
    "about" :       "I like to collect rock albums",
    "interests":  [ "music" ]
}
'
curl -X PUT "localhost:9200/megacorp/employee/3" -H 'Content-Type: application/json' -d'
{
    "first_name" :  "Douglas",
    "last_name" :   "Fir",
    "age" :         35,
    "about":        "I like to build cabinets",
    "interests":  [ "forestry" ]
}
'



查询索引
http://youxiu326.xin:9200/megacorp/employee/

{
  "query" : {}
}

curl -X GET "localhost:9200/megacorp/employee/1"

查询megacorp索引 employee类型 所有的文档
curl -X GET "localhost:9200/megacorp/employee/_search"



GET /megacorp/employee/_search
{
    "query" : {
        "match" : {
            "last_name" : "Smith"
        }
    }
}

 同样搜索姓氏为 Smith 的雇员，但这次我们只需要年龄大于 30 的。查询需要稍作调整，使用过滤器 filter ，它支持高效地执行一个结构化查询
curl -X GET "localhost:9200/megacorp/employee/_search" -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool": {
            "must": {
                "match" : {
                    "last_name" : "smith"
                }
            },
            "filter": {
                "range" : {
                    "age" : { "gt" : 30 }
                }
            }
        }
    }
}


找出一个属性中的独立单词是没有问题的，但有时候想要精确匹配一系列单词或者短语 。 比如， 我们想执行这样一个查询，仅匹配同时包含 “rock” 和 “climbing” ，并且 二者以短语 “rock climbing” 的形式紧挨着的雇员记录。
curl -X GET "localhost:9200/megacorp/employee/_search" -H 'Content-Type: application/json' -d'
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    }
}
'


高亮查询
许多应用都倾向于在每个搜索结果中 高亮 部分文本片段，以便让用户知道为何该文档符合查询条件。在 Elasticsearch 中检索出高亮片段也很容易。
再次执行前面的查询，并增加一个新的 highlight 参数
curl -X GET "localhost:9200/megacorp/employee/_search" -H 'Content-Type: application/json' -d'
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    },
    "highlight": {
        "fields" : {
            "about" : {}
        }
    }
}
'

分析
终于到了最后一个业务需求：支持管理者对雇员目录做分析。 Elasticsearch 有一个功能叫聚合（aggregations），允许我们基于数据生成一些精细的分析结果。聚合与 SQL 中的 GROUP BY 类似但更强大。
举个例子，挖掘出雇员中最受欢迎的兴趣爱好：
get http://youxiu326.xin:9200/megacorp/employee/_search?pretty
{

	"aggs": {
	    "all_interests": {
	      "terms": { "field": "interests" }
	    }
	  }

}

{
	"query": {
	    "match": {
	      "last_name": "smith"
	    }
	  },
	"aggs": {
	    "all_interests": {
	      "terms": { "field": "interests" }
	    }
	  }

}


```

----------------------------------

创建文档 顺便创建索引 类型 指定id

```
put http://youxiu326.xin:9200/website/blog/123

{
  "title": "My first blog entry",
  "text":  "Just trying this out...",
  "date":  "2014/01/01"
}

创建文档 不指定id
post http://youxiu326.xin:9200/website/blog
{
  "title": "My second blog entry",
  "text":  "Still trying this out...",
  "date":  "2014/01/01"
}

取回一个文档
curl -X GET "localhost:9200/website/blog/123?pretty"

返回文档的一部分
curl -X GET "localhost:9200/website/blog/123?_source=title,text"

如果你只想得到 _source 字段，不需要任何元数据，你能使用 _source 端点
curl -X GET "localhost:9200/website/blog/123/_source"

检查文档是否存在
如果只想检查一个文档是否存在 --根本不想关心内容--那么用 HEAD 方法来代替 GET 方法。 HEAD 请求没有返回体，只返回一个 HTTP 请求报头：
curl -i -XHEAD http://localhost:9200/website/blog/123


``
在内部，Elasticsearch 已将旧文档标记为已删除，并增加一个全新的文档。 尽管你不能再对旧版本的文档进行访问，但它并不会立即消失。当继续索引更多的数据，Elasticsearch 会在后台清理这些已删除文档
``

然而，如果已经有自己的 _id ，那么我们必须告诉 Elasticsearch ，只有在相同的 _index 、 _type 和 _id 不存在时才接受我们的索引请求。这里有两种方式，他们做的实际是相同的事情。
使用哪种，取决于哪种使用起来更方便
第一种方法使用 op_type 查询 -字符串参数：

PUT /website/blog/123?op_type=create
{ ... }

第二种方法是在 URL 末端使用 /_create :

PUT /website/blog/123/_create
{ ... }



乐观锁并发控制
curl -X PUT "localhost:9200/website/blog/1?version=1" -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "Starting to get the hang of this..."
}
'

``
所有文档的更新或删除 API，都可以接受 version 参数，这允许你在代码中使用乐观的并发控制，这是一种明智的做法
``



文档部分更新
curl -X POST "localhost:9200/website/blog/1/_update?pretty" -H 'Content-Type: application/json' -d'
{
   "doc" : {
      "tags" : [ "testing" ],
      "views": 0
   }
}
'

更新失败后重试
唯一需要做的就是尝试再次更新。

这可以通过 设置参数 retry_on_conflict 来自动完成， 这个参数规定了失败之前 update 应该重试的次数，它的默认值为 0

curl -X POST "localhost:9200/website/pageviews/1/_update?retry_on_conflict=5" -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1",
   "upsert": {
       "views": 0
   }
}
'

查看索引 mapping
http://14565v57k2.iok.la:9200/ur_mall_product_color/product/_mapping


分页
curl -X GET "localhost:9200/_search?size=5"
curl -X GET "localhost:9200/_search?size=5&from=5"
curl -X GET "localhost:9200/_search?size=5&from=10"


简单搜索
curl -X GET "localhost:9200/_search?q=mary"

Elasticsearch 是如何在三个不同的字段中查找到结果的呢？
当索引一个文档的时候，Elasticsearch 取出所有字段的值拼接成一个大的字符串，作为 _all 字段进行索引。例如，当索引这个文档时：

{
    "tweet":    "However did I manage before Elasticsearch?",
    "date":     "2014-09-14",
    "name":     "Mary Jones",
    "user_id":  1
}

这就好似增加了一个名叫 _all 的额外字段：
"However did I manage before Elasticsearch? 2014-09-14 Mary Jones 1"
除非设置特定字段，否则查询字符串就使用 _all 字段进行搜索。


curl -X GET "localhost:9200/website/_mapping/blog?pretty"

```

索引文档链接: https://www.elastic.co/guide/cn/elasticsearch/guide/cn/mapping-intro.html


索引查询例子链接: https://www.elastic.co/guide/cn/elasticsearch/guide/cn/_most_important_queries.html

```
内部对象的映射

Elasticsearch 会动态 监测新的对象域并映射它们为 对象 ，在 properties 属性下列出内部域：

{
  "gb": {
    "tweet": {
      "properties": {
        "tweet":            { "type": "string" },
        "user": {
          "type":             "object",
          "properties": {
            "id":           { "type": "string" },
            "gender":       { "type": "string" },
            "age":          { "type": "long"   },
            "name":   {
              "type":         "object",
              "properties": {
                "full":     { "type": "string" },
                "first":    { "type": "string" },
                "last":     { "type": "string" }
              }
            }
          }
        }
      }
    }
  }
}



aliasName:{
	"type":"text"
	"fields":{
		"keyword":{
			"type":"keyword",
			"ignore_above":256
		}
		"pinyin"{
			"type":"text",
			"analyzer":"pinyin"
		}
	}
	"analyzer":"ik_max_word"
}


根对象
内部对象

user 和 name 域的映射结构与 tweet 类型的相同。事实上， type 映射只是一种特殊的 对象 映射，我们称之为 根对象 。除了它有一些文档元数据的特殊顶级域，例如 _source 和 _all 域，它和其他对象一样。
内部对象是如何索引的
编辑

Lucene 不理解内部对象。 Lucene 文档是由一组键值对列表组成的。为了能让 Elasticsearch 有效地索引内部类，它把我们的文档转化成这样：

{
    "tweet":            [elasticsearch, flexible, very],
    "user.id":          [@johnsmith],
    "user.gender":      [male],
    "user.age":         [26],
    "user.name.full":   [john, smith],
    "user.name.first":  [john],
    "user.name.last":   [smith]
}

内部域 可以通过名称引用（例如， first ）。为了区分同名的两个域，我们可以使用全 路径 （例如， user.name.first ） 或 type 名加路径（ tweet.user.name.first ）。


-----------------------------------------------

 ``Query DSL``
是一种非常灵活又富有表现力的 查询语言。 Elasticsearch 使用它可以以简单的 JSON 接口来展现 Lucene 功能的绝大部分。在你的应用中，你应该用它来编写你的查询语句。
它可以使你的查询语句更灵活、更精确、易读和易调试


你可以使用 match 查询语句 来查询 tweet 字段中包含 elasticsearch 的 tweet
curl -X GET "localhost:9200/_search" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "tweet": "elasticsearch"
        }
    }
}
'

multi_match 查询可以在多个字段上执行相同的 match 查询：
{
    "multi_match": {
        "query":    "full text search",
        "fields":   [ "title", "body" ]
    }
}

range 查询找出那些落在指定区间内的数字或者时间
{
    "range": {
        "age": {
            "gte":  20,
            "lt":   30
        }
    }
}

被允许的操作符如下：

gt
    大于
gte
    大于等于
lt
    小于
lte
    小于等于


term 查询被用于精确值 匹配，这些精确值可能是数字、时间、布尔或者那些 not_analyzed 的字符串
{ "term": { "age":    26           }}
{ "term": { "date":   "2014-09-01" }}
{ "term": { "public": true         }}
{ "term": { "tag":    "full_text"  }}

terms 查询和 term 查询一样，但它允许你指定多值进行匹配。如果这个字段包含了指定值中的任何一个值，那么这个文档满足条件：
{ "terms": { "tag": [ "search", "full_text", "nosql" ] }}


exists 查询和 missing 查询被用于查找那些指定字段中有值 (exists) 或无值 (missing) 的文档。
这与SQL中的 IS_NULL (missing) 和 NOT IS_NULL (exists) 在本质上具有共性

{
    "exists":   {
        "field":    "title"
    }
}



组合多查询

现实的查询需求从来都没有那么简单；它们需要在多个字段上查询多种多样的文本，并且根据一系列的标准来过滤。为了构建类似的高级查询，你需要一种能够将多查询组合成单一查询的查询方法。
你可以用 bool 查询来实现你的需求。这种查询将多查询组合在一起，成为用户自己想要的布尔查询。它接收以下参数：
must
    文档 必须 匹配这些条件才能被包含进来。
must_not
    文档 必须不 匹配这些条件才能被包含进来。
should
    如果满足这些语句中的任意语句，将增加 _score ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。
filter
    必须 匹配，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档


下面的查询用于查找 title 字段匹配 how to make millions
并且不被标识为 spam 的文档。那些被标识为 starred 或在2014之后的文档，
将比另外那些文档拥有更高的排名。如果 _两者_ 都满足，那么它排名将更高：


{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }},
            { "range": { "date": { "gte": "2014-01-01" }}}
        ]
    }
}


如果我们不想因为文档的时间而影响得分，可以用 filter 语句来重写前面的例子
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "range": { "date": { "gte": "2014-01-01" }}
        }
    }
}


通过将 range 查询移到 filter 语句中，我们将它转成不评分的查询，将不再影响文档的相关性排名。由于它现在是一个不评分的查询，可以使用各种对 filter 查询有效的优化手段来提升性能。
所有查询都可以借鉴这种方式。将查询移到 bool 查询的 filter 语句中，这样它就自动的转成一个不评分的 filter 了。
如果你需要通过多个不同的标准来过滤你的文档，bool 查询本身也可以被用做不评分的查询。简单地将它放置到 filter 语句中并在内部构建布尔逻辑：
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "bool": {
              "must": [
                  { "range": { "date": { "gte": "2014-01-01" }}},
                  { "range": { "price": { "lte": 29.99 }}}
              ],
              "must_not": [
                  { "term": { "category": "ebooks" }}
              ]
          }
        }
    }
}

``验证查询``
查询可以变得非常的复杂，尤其 和不同的分析器与不同的字段映射结合时，理解起来就有点困难了。
不过 validate-query API 可以用来验证查询是否合法
为了找出 查询不合法的原因，可以将 explain 参数 加到查询字符串中：

curl -X GET "localhost:9200/gb/tweet/_validate/query?explain" -H 'Content-Type: application/json' -d'
{
   "query": {
      "tweet" : {
         "match" : "really powerful"
      }
   }
}
'

```

##### 排序

```
为了按照相关性来排序，需要将相关性表示为一个数值。在 Elasticsearch 中， 相关性得分 由一个浮点数进行表示，并在搜索结果中通过 _score 参数返回， 默认排序是 _score 降序。
有时，相关性评分对你来说并没有意义。例如，下面的查询返回所有 user_id 字段包含 1 的结果：

GET /_search
{
    "query" : {
        "bool" : {
            "filter" : {
                "term" : {
                    "user_id" : 1
                }
            }
        }
    }
}

这里没有一个有意义的分数：因为我们使用的是 filter （过滤），这表明我们只希望获取匹配 user_id: 1 的文档，并没有试图确定这些文档的相关性。
 实际上文档将按照随机顺序返回，并且每个文档都会评为零分。

如果评分为零对你造成了困扰，你可以使用 constant_score 查询进行替代：
GET /_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "term" : {
                    "user_id" : 1
                }
            }
        }
    }
}

这将让所有文档应用一个恒定分数（默认为 1 ）。它将执行与前述查询相同的查询，并且所有的文档将像之前一样随机返回，这些文档只是有了一个分数而不是零分


按照字段的排序
在这个案例中，通过时间来对 tweets 进行排序是有意义的，最新的 tweets 排在最前。 我们可以使用 sort 参数进行实现：
curl -X GET "localhost:9200/_search" -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool" : {
            "filter" : { "term" : { "user_id" : 1 }}
        }
    },
    "sort": { "date": { "order": "desc" }}
}
'


假定我们想要结合使用 date 和 _score 进行查询，并且匹配的结果首先按照日期排序，然后按照相关性排序
curl -X GET "localhost:9200/_search" -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool" : {
            "must":   { "match": { "tweet": "manage text search" }},
            "filter" : { "term" : { "user_id" : 2 }}
        }
    },
    "sort": [
        { "date":   { "order": "desc" }},
        { "_score": { "order": "desc" }}
    ]
}
'


```


