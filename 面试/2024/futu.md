
# 0319 一轮面试 
## 项目
    做了啥，遇到的最难点，怎么优化的？
## 八股文
### 输入www.baidu.com网络处理的过程，如果是https呢
    - 域名解析：（浏览器缓存-操作系统缓存-DNS服务器解析）
    - 建立连接：
        - http（三次握手）
        - https（与服务器进行密钥的校验）
    - 服务器处理
        - 接入层
        - 逻辑业务层
        - 数据存储层
    - 浏览器渲染结果
### 服务端502代表可能是什么问题，怎么定位问题？
    表示对用户访问请求的响应超时造成的，主要是服务端造成的。解决的办法就是定位后台的耗时点在哪里；
    - 排查后台模块是否异常
    - 数据库操作耗时
    - 网络带宽等

### mysql user表(user表-50w)，thread表（用户发帖表-500w）
    user表：uid, username, something……
    thread表：tid, uid, content

- sql找出top10发帖数量的用户？
    ```sql
    select username,
           count(tid) as ss
    from user
    left join thread on user.uid = thread.uid
    group by uid
    order by ss desc limit 10;
    ```
- 如何构建表的索引
    - user表：uid主键索引
    - thread表：tid主键索引，uid索引

- A join B or B join A 是否有差异？

    在这种场景下，A表和B表肯定能关联上。左右表join的差异不是很大

- sql是否最优？嵌套子查询
    ```sql
    SELECT u.username, tt.ct
    FROM
        (SELECT uid, COUNT(*) AS ct
        FROM thread
        GROUP BY uid
        ORDER BY ct DESC
        LIMIT 10) AS tt
    LEFT JOIN user u ON tt.uid = u.uid;
    ```

## 写代码
### 代码优雅实现
    用户订单的每笔交易都需要收费，收费是分阶段进行收费的。
    大于1笔，小于5笔的部分：每笔收20;
    大于等于5笔，小于10笔的部分：每笔收30；
    大于等于10笔小于20笔的部分：每笔收50
    大于等于20笔的部分：每笔收60
    请写出一个优雅的实现，通俗易懂
- 代码实现
```cpp
    #include <climits>
    #include <iostream>
    #include <vector>
    using namespace std;

    class ChargeRule {
    public:
        int begin;
        int end;
        float charge_per_trans;
        ChargeRule(int bn, int ed, float money) {
            begin = bn;
            end = ed;
            charge_per_trans = money;
        }
    };

    float calculateCharge(int orders) {
        static const vector<ChargeRule> cRules = {
            ChargeRule(1, 5, 20),
            ChargeRule(5, 10, 30),
            ChargeRule(10, 20, 50),
            ChargeRule(20, INT_MAX, 60)};
        float totalCharge = 0;
        for (const auto& rule : cRules) {
            if (orders >= rule.end) {
                totalCharge += (rule.end - rule.begin) * rule.charge_per_trans;
            } else if (orders >= rule.begin) {
                totalCharge += (orders - rule.begin + 1) * rule.charge_per_trans;
            }
        }

        return totalCharge;
    }

    int main() {
        cout << calculateCharge(6);
        return 0;
    }
```
### 递归序列化
    给定一个map的数据，将数据转换为string，同时按照小写key的顺序排列
- 输入:
    ```json
    "data": {
        "UserName":"name",
        "Value": "value",
        "List": {
            "k1" : "k1",
            "k2" : "k2",
        }
    }
    ```
- 输出:
    ```
    data.list.k1=k1&data.list.k2=k2&data.username=name&data.value=value
    ```
- 代码实现
    ```cpp
    #include <climits>
    #include <iostream>
    #include <vector>
    #include "thirdparty/jsoncpp/json.h" // 当时写代码使用的是本地库，如果运行的库这里可能需要兼容一下
    using namespace std;

    void traverseJson(std::string key, Json::Value& data, std::map<std::string, std::string>& strMap) {
        if (data.isObject()) {
            for (auto iter = data.begin(); iter != data.end(); ++iter) {
                traverseJson(key + "." + iter.memberName(), data[iter.memberName()], strMap);
            }
        } else {
            // 转换为小写
            std::transform(key.begin(), key.end(), key.begin(), [](unsigned char c) {
                return std::tolower(c);
            });
            strMap[key] = data.asString();
        }
    }

    std::string getString(std::string jsonStr) {
        std::map<std::string, std::string> strMap;
        Json::Reader reader;
        Json::Value jsonData;
        if (!reader.parse(jsonStr, jsonData)) {
            return "parse error";
        }
        for (auto iter = jsonData.begin(); iter != jsonData.end(); ++iter) {
            traverseJson(iter.memberName(), jsonData[iter.memberName()], strMap);
        }
        
        std::ostringstream oss;
        // 打印拼接
        for (auto iter = strMap.begin(); iter != strMap.end(); ++iter) {
            oss << iter->first << "=" << iter->second + "&";
        }
        std::string result = oss.str();
        if (result.size() > 0) {
            result = result.substr(0, result.size() - 1);
        }
        return result;
    }

    int main() {
        // JSON字符串
        std::string jsonString = R"(
            {
                "data": {
                    "UserName":"name",
                    "Value": "value",
                    "List": {
                        "k1" : "k1",
                        "k2" : "k2"
                    }
                }
            }
        )";
        return getString(jsonString);
    }
    ```
- 引入的问题: kv序列排序的问题
    - 直接使用std::map结构，底层是红黑树，默认是升序排列的
    ```cpp
        #include <iostream>
        #include <map>
        #include <functional>

        int main() {
            std::map<std::string, int, std::greater<std::string>> myMap;
            myMap.insert(std::make_pair("Three", 3));
            myMap.insert(std::make_pair("One", 1));
            myMap.insert(std::make_pair("Two", 2));
            for (const auto& pair : myMap) {
                std::cout << pair.first << ": " << pair.second << std::endl;
            }
            return 0;
        }
    ```
    - vector 写排序函数
    ```cpp
        #include <iostream>
        #include <vector>
        #include <algorithm>

        bool compareFirstColumn(
                const std::pair<std::string, std::string>& pair1,
                const std::pair<std::string, std::string>& pair2) {
            return pair1.first < pair2.first;
        }

        int main() {
            std::vector<std::pair<std::string, std::string>> myVector;

            myVector.push_back(std::make_pair("C", "Charlie"));
            myVector.push_back(std::make_pair("A", "Alpha"));
            myVector.push_back(std::make_pair("B", "Bravo"));

            std::sort(myVector.begin(), myVector.end(), compareFirstColumn);

            for (const auto& pair : myVector) {
                std::cout << pair.first << ": " << pair.second << std::endl;
            }
            return 0;
        }
    ```

 
# 0327 二轮面试
## 八股文
- mysql关联的问题
- 动态规划的代码，很简单。
## 项目
- 项目有什么难点？系统有什么瓶颈，目前有什么问题？
    感觉是项目不匹配，说了很多，他都不怎么感冒。自我感觉还挺好的，哈哈哈

```
    启发：
    中性问题要有自己的总结思考；
    尽量表达出可以无缝迁移，工作时间长主要看的就是项目经历
```