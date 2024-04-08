# 0321 一面
## 八股文
### stl的内存分配器
- [参考文章](https://github.com/rongweihe/CPPNotes/blob/master/STL-source-code-notes/5%20%E5%8D%83%E5%AD%97%E9%95%BF%E6%96%87+%2030%20%E5%BC%A0%E5%9B%BE%E8%A7%A3-%E9%99%AA%E4%BD%A0%E6%89%8B%E6%92%95%20STL%20%E7%A9%BA%E9%97%B4%E9%85%8D%E7%BD%AE%E5%99%A8%E6%BA%90%E7%A0%81.md)  
    2层内存分配器，大于128K直接new，小于则在内存池中获取
    
### std::move原理，forward是干什么的
- 左值与右值的区别：[C++中左值和右值的理解](https://nettee.github.io/posts/2018/Understanding-lvalues-and-rvalues-in-C-and-C/)
    - 左值：等于号左边的值，是一个变量，可以被赋值；
    - 右值：是一个常量，不能被赋值；临时变量也算作右值。
    - 左值引用：&，就是我们常用的引用定义，也叫别名；
    - 右值引用：&& 这里对应左值引用，就是等于号右边的地址的引用。这里最常用的场景是这样
        ```cpp
        // case A
        Intvec& operator=(Intvec other)
        {
            log("copy assignment operator");
            std::copy(m_size, tmp.m_size);
            std::copy(m_data, tmp.m_data);
            return *this;
        }
        // case B
        Intvec& operator=(Intvec&& other)
        {
            std::swap(m_size, other.m_size);
            std::swap(m_data, other.m_data);
            return *this;
        }
        Intvec b = Intvec(123); // 这里Intvec(123)产生了一个变量，b的赋值构造可以调用右值引用的函数。从而减少对象的拷贝。
        ```
- [std::move和std::forward的本质和区别](https://www.jianshu.com/p/b90d1091a4ff)
    - std::move执行到右值的无条件转换，本质还是通过static_cast来实现的
    - std::forward只有需要转换的时候才会转换

### std::unique_ptr指针赋值是怎么操作的
    std::move进行指针的转移
- [C++智能指针 shared_ptr,unique_ptr和weak_ptr](https://zhuanlan.zhihu.com/p/29628938) [C++11智能指针之weak_ptr](https://blog.csdn.net/Xiejingfa/article/details/50772571)
    - 智能指针主要是利用对象的生命周期来管理指针。主要利用构造和析构来进行管理
    - shared_ptr 共享指针，内置有引用计数器
    - unique_ptr 独占指针，屏蔽拷贝赋值函数等
    - weak_ptr 弱引用指针，来解决shared_ptr相互引用的问题。
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

# 0329 一面
## 项目 30分钟结束
- 问了很多检索的问题
    四层检索体系，索引怎么同步到线上。
- 流批一体的话，如果旧的广告定向修改了，如何处理？
    
- 系统有哪些问题，监控是怎么做的？系统问题怎么发现？