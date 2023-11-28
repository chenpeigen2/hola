### 一些限制
> template engine if detect "# or #" will replace as -> "\0"

> template engine only support ${字段} or $字段 replacement

> template engine do not support (important)
```text
$and	数组	如果数组中的所有选择器匹配，则匹配。
$or	数组	如果数组中的任何选择器匹配，则匹配。所有选择器必须使用相同的索引。
$not	选择器	如果给定的选择器不匹配，则匹配。
$nor	数组	如果数组中没有一个选择器匹配，则匹配。
$all	数组	匹配数组值（如果它包含参数数组的所有元素）。
$elemMatch	选择器	匹配并返回包含数组字段且至少有一个元素与所有指定查询条件匹配的所有文档。
$allMatch	选择器	匹配并返回包含数组字段及其所有元素与所有指定查询条件匹配的所有文档。
$keyMapMatch	选择器	匹配并返回包含至少包含一个匹配所有指定查询条件的键的映射的所有文档。

（In）平等	$lt	任何JSON	字段小于参数
 	$lte	任何JSON	字段小于或等于参数。
 	$eq	任何JSON	字段等于参数
 	$ne	任何JSON	字段不等于参数。
 	$gte	任何JSON	字段大于或等于参数。
 	$gt	任何JSON	字段大于参数的to。
对象	$exists	布尔	检查字段是否存在，无论其值如何。
 	$type	弦	检查文档字段的类型。有效值为 "null" ， "boolean" ， "number" ， "string" ， "array" 和 "object" .
数组	$in	JSON值数组	文档字段必须存在于提供的列表中。
 	$nin	JSON值数组	文档字段不必存在于提供的列表中。
 	$size	整数	匹配文档中数组字段长度的特殊条件。非数组字段不能与此条件匹配。
其他	$mod	[除数，余数]	除数和余数都是正整数或负整数。非整数值导致404。匹配文档的位置 field % Divisor == Remainder 为true，并且仅当文档字段为整数时。
 	$regex	弦	与文档字段匹配的正则表达式模式。仅当字段为字符串值且与提供的正则表达式匹配时才匹配。匹配算法基于Perl兼容正则表达式（PCRE）库。有关实现内容的更多信息，请参见 Erlang Regular Expression
```

```json
{
    "selector": {
      "$or": [
        {
          "social_id": "羽毛球"
        },
        {
          "class_id": "$classInfo"
        }
      ],
      "gender": "#${gender}#"
    }
  }
  
  
```


### Sample
if you pass the ?classInfo=17一班&gender=0 
will inflate as bellows:
```json
{
    "selector": {
      "$or": [
        {
          "social_id": "羽毛球"
        },
        {
          "class_id": "17一班"
        }
      ],
      "gender": 0
    }
  }
```