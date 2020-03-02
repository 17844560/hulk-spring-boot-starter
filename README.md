# hulk-spring-boot-starter

### 介绍
基于RocketMq实现的事件发布订阅，主要用于微服务之前的事件传播，底层是使用spring的事件机制，支持注解@EventListener方式观察事件@EventListener

#### Dependency
```groovy
 implementation "me.jinuo.imf:loki-spring-boot-starter:1.0.0-SNAPSHOT"
```
####  Event / RemoteEvent


```kotlin
open class Event()//主要用于应用内件传播,支持同步事件和异步事件(底层由线程池调试)
open class RemoteEvent() : Event<T>()//微服务之前的事件传播，只支持集群消费
```

#### 使用
场景：用户注册
1.  引入依赖默认启用

2.  配置RocketMq(注册中心)NameServer
```yaml
event:
  name-server: rocketmq.veigar.jinuo.fun:21133
```
3. 申明Event事件体
```kotlin
/** 单应用内事件*/
class RegisterEvent(
    val id: Long?=null,
    val nickname: String?=null,
    val password: String?=null,
    val email: String?=null,
    val recaptchaToken: String?=null,
    val referralCode: String?=null
):Event()

/** mq事件*/
class RemoteRegisterEvent(
    val id: Long?=null,
    val nickname: String?=null,
    val password: String?=null,
    val email: String?=null,
    val recaptchaToken: String?=null,
    val referralCode: String?=null
):RemoteEvent()
```
3.  在你的Spring 组件中注入``` EventBus```
```kotlin
    @Resource
    private lateinit var eventBus: EventBus
```
4.  使用eventBus发布事件（用户服务）
```text
    //构造一个单应用用户注册事件体
    val event = RegisterEvent(*,*,*,*)
    //发布应用内事件
    eventBus.post(event)
    
    //同步事件，不支持RemoteEvent
    eventBus.syncPost(event)

    //构造基于rocketMq的远程事件
    val event = RemoteRegisterEvent(*,*,*,*)
    eventBus.post(event)

```
5.  观察事件(Spring 支持多观察者)，每个微服务都可以监听
#####账务服务
```kotlin
@Component
class RegisterEventReceiver{
    
    @EventListener
    fun methodName(event:RemoteRegisterEvent){
        //TODO 初始化钱包
    }   

}
```
#####背包服务
```kotlin
@Component
class RegisterEventReceiver: ApplicationListener<RemoteRegisterEvent>{
    
    fun onApplicationEvent(event:RemoteRegisterEvent){
        //TODO 初始化背包
    }   
}
```
