# 0321 一面
## 八股文
### stl的内存分配器
### std::unique_ptr指针赋值是怎么操作的
    std::move进行指针的转移

### std::move原理，forward是干什么的
### 给定几百万行的url记录，机器内存只有2GB，如果统计出重复的url数据
    将原始数据按照key进行打散，分桶统计（map-reduce的原理）
- 如果给定的url是streaming输入的，如果判断是否是已经存在的url  
    布隆过滤器
- 布隆过滤器可以删除数据吗?  
    不能，布隆过滤器的原理就是将输入进行hash后，将结果放置到对应的bit中。如果一个输入的所对应的bit有一个为0，说明这个数据肯定不存在，反之则不一定。
### 大数据的相关，hbase的存储原理
- 写入一条数据，HBase怎么存储的
    参考文档：https://km.woa.com/articles/show/431210?kmref=search&from_page=1&no=6
- map-reduce，如果数据倾斜怎么处理？  
    给key加上随机数进行打散
    - key上加随机数会有什么问题？  
        value相同的记录会被随机分到不同的桶中，在进行去重等操作的时候可能出现问题  
## 写代码
### 二叉树求宽度
          1
       /     \
      2       3
     / \     / \
    4   *   *   6 ——? width = 4  
- 思路：二叉树的行遍历，但是需要注意中间为nullptr的要保留
    ```cpp
    #include <iostream>
    #include <deque>
    using namespace std;
    class Node {
    public:
        Node* left;
        Node* right;
        int value;
        Node(){};
        Node(Node* l, Node* r, int val){
            left = l;
            right = r;
            value = val;
        }
        Node(int val) {
            value = val;
        }
    };
    int getMaxWidth(Node* root) {
        std::deque<Node*> queue;
        queue.push_back(root);
        int maxWidth = 0;
        while(!queue.empty()) {
            int size = (int)queue.size();
            maxWidth = size > maxWidth ? size : maxWidth;
            while (size-- > 0) {
                Node* node = queue.front();
                queue.pop_front();
                if (node != nullptr) {
                    queue.push_back(node->left);
                    queue.push_back(node->right);
                } else {
                    queue.push_back(nullptr);
                    queue.push_back(nullptr);
                }
            }
            // 删除头和尾部的nullptr
            while (!queue.empty() && queue.front() == nullptr) {
                queue.pop_front();
            }
            while (!queue.empty() && queue.back() == nullptr) {
                queue.pop_back();
            }
        }
        return maxWidth;
    }
    int main() {
        Node* node4 = new Node(4);
        Node* node6 = new Node(6);
        Node* node2 = new Node(node4, nullptr, 2);
        Node* node3 = new Node(nullptr, node6, 3);
        Node* node1 = new Node(node2, node3, 3);
        std::cout << getMaxWidth(node1) << std::endl;
        return 0;
    }
    ```
- case1

             1
          /     \
         2       3
        /  \    /  \
       *    4  *    6 ——? width = 3

- case2

             1
          /     \
         2       3
        /  \    /  \
       4    *  6    * ——? width = 3