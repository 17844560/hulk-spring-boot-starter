[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
# hulk-spring-boot-starter

### 介绍
基于SpringBoot auto configuration实现的websocket,支持注解annotion方式监听tcp server

#### Dependency
```groovy
 implementation "me.jinuo.imf:hulk-spring-boot-starter:1.0.0-SNAPSHOT"
```
####  使用@EnableWebSocket
```kotlin
class EnableWebSocket(val value: String = "/ws", val proxy: Boolean = false)
```
###申明控制器
```kotlin
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class WSController(val value: Short = 0)

/** mq事件*/
@WSController(0)
class EchoController{
    @WSMapping(1)
    fun echo(msg:String){
        pring(msg)
    }
}
```
### 推送使用SessingManager
```
@Component
class EchoService{
    private lateinit var sessionManager:SessionManager

    fun echo(msg:String){
        var session =  sessionManager.getSession(id);  
        session.write(msg)
    }
}
```
