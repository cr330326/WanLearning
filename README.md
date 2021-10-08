# WanLearning-Kotlin 实战项目
「**WanLearning App**」基于 Material Design 风格构建的 **玩 Android** 客户端，主要是为了适应Kotlin语言开发流程。



### 主要特点

- 基于Google官方宣贯的MVVM模式开发实践
- 基于Jetpack组件库构建：LiveData、ViewModel、Lifecycle、Room、DataBinding，目前项目中只是集成的Paging组件，暂未用到
- 使用Kotlin语言开发，自定义扩展函数，代码简洁
- 使用协程来构建网络请求，链式调用，方便调试
- 工程模式以单Activity + 多Fragment架构编写，容易做单元测试和好扩展维护，提升开发效率



### 架构图

![架构组件](images/final-architecture.png)



### 效果图

![Demo](images/demo_maker.gif)

### 更新记录

V1.0.0

- 第一个版本，包括首页、公众号、项目、导航、我的5个Tab项页面功能
- 支持协程方式获取网络请求数据，LiveData来更新UI
- 支持登陆和注册功能、支持自定义切换主题颜色、支持加载Web Url



### 感谢

- 数据来源：[玩 Android](https://www.wanandroid.com/blog/show/2)

- [UnPeek-LiveData](https://github.com/KunMinX/UnPeek-LiveData)事件通知
- [LoadSir](https://github.com/KingJA/LoadSir)  加载反馈
- [Retrofit](https://github.com/square/retrofit) 网络请求框架封装
- [Glide](https://github.com/bumptech/glide)  图片加载
- [OkHttp](https://github.com/square/okhttp) 网络请求
- [Gson](https://github.com/google/gson) Gson 解析
- [Permissionx](https://github.com/guolindev/PermissionX)  动态请求权限封装
- [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout) 下拉刷新框架
- [VasSonic](https://github.com/Tencent/VasSonic) 提升H5首屏加载速度



### 给予支持💪

通过这个项目希望能够帮助大家更好地学习 Jetpack 与 MVVM 架构的结合。

如果你喜欢 WanLearning App 的工程项目，本项目的源代码对你的学习有所帮助，可以点右上角 **"Star"** 支持一下，谢谢！



### 关于我

- 个人博客：http://cryallen.com/
- 简书：https://www.jianshu.com/u/6f6f18ef43e5
- Email: cr330326@126.com
- Wechat: cr330326
- 公众号：![个人信息](images/me.jpeg)
