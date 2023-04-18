## Think-X

Think-X是基于Vertx的响应式基础框架。对vert.x的基础组件进行了进一步的封装。以便于开发者更专注于业务。
同时也学习下响应式框架，再顺便治疗下spring依赖症~

## 技术栈
vert.x 、hibernate-reactive、Guice等

## 项目结构

| 模块             | 描述                                    |
|----------------|---------------------------------------|
| think-x-core   | 核心依赖包，基类、异常、json、id策略等                |
| think-x-orm    | orm封装，基于hibernate-reactive，实现了通用的curd |
| think-x-inject | 基于google的Guice封装的依赖注入组件               |
| think-x-web    | 封装web相关功能，route、Validation等封装         |
| think-x-app    | restful api实例                         |
## 其他
待续。。。
