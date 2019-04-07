# sb_elasticsearch_jestclient
使用jestClient操作elasticsearch


全文搜索引擎 Elasticsearch 入门教程
http://www.ruanyifeng.com/blog/2017/08/elasticsearch.html


springboot+jest 操作Elasticsearch 参考：https://www.cnblogs.com/youxiu326/p/elasticSearch-jestClient.html


创建mapper 参考：https://blog.csdn.net/tanga842428/article/details/76229235

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
  
