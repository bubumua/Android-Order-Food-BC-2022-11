## 题目

### 题目要求

“点餐”系统，是信息时代的应用平台。用一部智能机，就能完成以前PC才能完成的各种点餐任务，方便快捷，应用广泛。本课题要求实现简单的“点餐”系统功能（**单机版**）

1.  显示欢迎界面后，进入主界面（必选）

2. 主界面支持对菜的**分类**（可网上查询资料自定义，菜建议使用图片显示），支持特色菜**推荐滚动显示**，支持**套餐功能**（必选）

3. 顾客可以选择**具体菜、份量打勾确定**，待顾客所有菜选择确认后，提交**生成订单**（必选）

4. 支持对具体菜的类似麻辣、微辣等酸辣苦甜**定制需求**，自定义不限（必选）

5. 需**输出顾客订单总价和菜单详细清单**（必选）

6. 支持**优惠券**打折功能（可选）

7. 功能扩展（可选）

8. 上述功能需要在Android系统上实现演示（必选）

### 评分细则

根据最终APK实现程度以及设计文档，进行考核（基准分60分，总分100分），具体细则如下

1. 搭建环境：可正常进行Android开发和调试（+5分）

2. 课题实现分析与设计： 
   1. 文档描述清晰，功能设计合理（+10分），无文档（-10分）
3. 功能实现与演示：
   1. 必选功能都实现（+15分），少1个（-5分）
   2. 代码整齐、可读性高（+5分），无注释（-10分）
   3. 可选功能（+5分）
   4. 功能异常或出现死机（每个 -5分）
   5. UI设计优美、用户体验好（+5分）

## 参考与规范

### 参考

https://gitee.com/FranzLiszt1847/OrderOnline

https://github.com/Rahmouni-Seif-BI/Android-APP-Seif-Delivery-Food

### 代码规范

#### JavaDoc

在`build.gradle`的`dependencies`中添加

```
implementation files('{path}\\platforms\\android-{version}\\android.jar')
```

+ `{path}`改为你的路径
+ `{version}`改为项目用的安卓SDK版本号

在设置-编辑器-实时模板-Java-添加动态模板-模板文本中添加以下字段

```
 *
 * $simple_description$
 *
 $param1$$param1_description$ 
 * @Return $return$
 * @Author 你的用户名
 * @date $date$ $time$
 * @commit $commit_message$
 */
```

编辑模板变量
![aaa](https://imgbed-1304793179.cos.ap-nanjing.myqcloud.com/typora/20221008184457.jpg)

```
groovyScript("   def result='';   def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList();   for(i = 0; i < params.size(); i++) { 	if(i==0)result+= '* ';     if(i!=0)result+= ' * ';     result+='@param '+params[i] +' '+ ((i < (params.size() - 1)) ? '\\n' : '');   };   return result", methodParameters())
```

生成`DOC`参数

```shell
-encoding UTF-8 -charset UTF-8  -tag Return:a:"返回值" -tag Author:a:"作者"  -tag date:a:"最后修改日期" -tag commit:a:"最后提交信息"
```

类模板

```
package ${PACKAGE_NAME};

/**
 * @Type ${NAME}
 * @Desc 
 * @author Anduin9527
 * @date ${DATE} ${TIME}
 * @version 
 */
public class ${NAME} {
}
```



## 具体实现效果

#### 数据存储

##### 用户账户

使用`JetPack`的`Room`本地化存储

账户：

1. `username`
2. `password`
3. `gender`
4. `phoneNumber`
5. `UID`主键

##### 商品

（[Database Entity](#Dish)）

1. `name`
2. `category`分类
3. `price`
4. `detail`商品描述
5. `isCustomed`是否支持客制化，即定制口味
6. `customDetail`客制化口味详情，做的简单点就是直接微辣~重辣
7. `comment`商品评价
8. `GID`主键

**GID**:int
**name:**String 
**description**:String 
**price**:double 
**category**:String 
**CID**:int 
**ImageSrc**:String 
**customizable**:boolean

##### 订单

1. `name`
2. `price`
3. `number`
4. `owner`用户
5. `OID`订单ID
6. `remark`备注
7. `PID`主键

#### 登录页面

登陆页面参考第二个项目，第一个项目登录页面有明显BUG。实现基本的登录和注册就行

#### *开屏动画

参考（照搬）Android组件库

#### 页面设计

参考第二个项目的主页

<img src="https://imgbed-1304793179.cos.ap-nanjing.myqcloud.com/typora/20221008161911.png" alt="image-20221008161911731" style="zoom: 80%;" />

<img src="https://imgbed-1304793179.cos.ap-nanjing.myqcloud.com/typora/20221008162301.jpg" alt="a" style="zoom: 33%;" />

+ 商品滚动推荐（必做）的功能，1项目已经实现

+ 菜的分类放置在详情页面就是经典的外卖布局，2项目已经实现
+ 在详情页实现`APPBAR`直接搜索商品
+ 在滚动广告栏或者给一个悬浮式按钮给予**优惠券**
+ 套餐作为一个商品直接放在分类中
  + 考虑选中套餐时的客制化

#### 订餐部分

+ *考虑上浮式抽屉显示订单

+ *考虑添加商品至购物车时的动画效果

+ 点击商品进入菜品详情页面

+ 菜品详情页参考

  <img src="https://imgbed-1304793179.cos.ap-nanjing.myqcloud.com/typora/20221008163104.png" alt="image-20221008163104773" style="zoom:67%;" />

  + *考虑添加评论
  + *考虑加入更多推荐

#### 订单页面

<img src="https://imgbed-1304793179.cos.ap-nanjing.myqcloud.com/typora/20221008164931.png" alt="image-20221008164931870" style="zoom:67%;" />

+ 判断优惠券减免
+ *外送选项，考虑利用一个开关，然后蹦出两行输入框之类的
+ 该项目没有完成提交订单。提交订单后，订单页面的提交订单按钮文本改变，颜色改变，变为该订单目前的状态

#### *反馈页面

可以对以往的商品订单进行反馈评价，加一个star评分

#### 设置页面

使用一个普通的卡片列表式布局就行

1. 更改账户信息
   1. *设置头像
   2. 修改账户密码
   3. 修改绑定手机号
   4. 设置默认外送位置
2. 国际化（切换语言）
3. 检查更新

## 任务清单

- [ ] 页面设计
  - [x] 登录页面（注册页面）
    - [x] 登录和主页页面之间的跳转（含数据）
    - [x] 动画设计
    - [ ] fix：夜间模式下字体隐藏的BUG
  - [x] 主页
  - [x] 详情页
    - [ ] 优惠券领取
  - [x] 商品详情页
  - [x] 设置页面
  - [x] 订单页面
  - [x] *反馈页面
- [x] 数据库设计
  - [x] 账户增删改查
  - [x] 商品增删改查
  - [x] 订单增删改查
- [x] 主页和数据库交互逻辑
- [x] 登录页面和数据库交互逻辑
- [x] 详情页和数据库交互逻辑
- [x] 商品详情页和数据库交互逻辑
- [ ] 订单页面和数据库交互逻辑
- [x] 美术资源
  - [x] 矢量图标
  - [x] 商品信息

- [ ] 国际化（中英双语）
- [ ] *多尺寸UI适配
- [ ] *支持夜间模式

## 项目结构说明

### Activity

所有的`Activity`

1. `LoginActivity`登录页面
1. `SignUpActivity`注册页面

### Database

所有的数据库

1. `Person`用户数据库

2. <span id="Dish">`Dish`展示菜品数据库</span>

     


​    

### Utils

1. `KeyboardUtils`软键盘收起
2. `RegexEditText` 轮子哥的自定义输入框上级接口
3. `PasswordEditText`轮子哥的密码输入框
4. `ClearEditText`轮子哥的清空按钮输入框
5. `SubmitButton`轮子哥的带动画提交按钮
6. `SwitchButton`轮子哥的仿IOS开关