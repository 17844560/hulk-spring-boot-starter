package com.sghd.common.socket.filter.session

import me.jinuo.imf.websocket.handler.Dispatcher
import me.jinuo.imf.websocket.session.Session
import me.jinuo.imf.websocket.session.SessionManager
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

/**
 * 发送代理工厂
 */
class PushProxyFactory(manager: SessionManager<Any, Session>, dispatcher: Dispatcher) {
    /** 动态代理处理器  */
    private val invoker: InvocationHandler = TODO()
    private val dispatcher: Dispatcher
    private val manager: SessionManager<Any, Session>
    /** 代理实例缓存  */
    private val caches = ConcurrentHashMap<Class<*>, Any>()
    /** 需要发送的SESSION  */
    private val sessions: ThreadLocal<Array<Session>> = ThreadLocal<Array<Session>>()

    /**
     * 设置发送SESSION
     */
    fun setSessions(sessions: Array<Session>) {
        this.sessions.set(sessions)
    }

    /**
     * 发送请求消息
     * @param request
     */
    fun send(request: Any?) {
        val sessions: Array<Session> = sessions.get()
//        manager.send(request, sessions)
    }

    /**
     * 获取代理实例
     * @param clz
     * @param sessions
     * @return
     */
    fun <T> getProxy(clz: Class<T>, vararg sessions: Session): T { // 设置发送SESSION
        setSessions(sessions as Array<Session>)
        // 先从缓存获取
        val obj = caches[clz] as T?
        if (obj != null) {
            return obj
        }
        // 注册接口
        //dispatcher.registerInterface(clz)
        // 构建新代理并放置到缓存
        val inst = Proxy.newProxyInstance(clz.classLoader, arrayOf<Class<*>>(clz), invoker) as T
       // caches[clz] = inst
        return inst
    }

    init {
        this.dispatcher = dispatcher
        this.manager = manager
       // invoker = PushProxyInvoker<Any>(this)
    }
}