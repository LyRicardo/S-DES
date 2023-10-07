# S-DES
重庆大学大数据与软件学院-信息安全导论课程实验，经胡海波老师指导，由林钰完成
## 基本测试
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/1.png)
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/2.png)
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/6.png)

## 拓展输入
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/3.png)
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/3.png)
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/5.png)

## 组间测试
已和朱清杨、邓怡杰组进行交叉测试，但是结果不同，原因是他们在求第二个子密钥k2时是在主密钥k的基础上进行循环左移，而我是在k1的基础上对k2进行循环左移

## 暴力破解
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/7.png)
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/8.png)

## 封闭测试
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/9.png)
![image](https://lyricardo.oss-cn-chengdu.aliyuncs.com/photo/10.png)

## 开发手册
src下为核心算法实现，test主要是测试内容，page是交互界面

## 用户指南
基于《信息安全导论》课程第五讲内容实现，用于对SDES（Simplified Data Encryption Standard）算法进行加密和解密操作。
### 加密
进入页面默认为加密模式，在第一行输入明文，第二行输入密钥，点击encode即可加密
### 解密
点击取消encryption mode的勾，进入解密模式，第一行输密文，第二行输密钥，点击decode即可解密
### 注意
密钥必须为标准的10位二进制数；明文的标准格式为8位二进制数。
