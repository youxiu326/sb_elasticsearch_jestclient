
###### 简单查询语句备忘

```
GET ur_mall_product_color/product/_search
{
  "from": 0,
	"size": 5,
  "_source" : ["code","style"],
  "query": {
    "match": {"style":"假日"}
  }
}
```