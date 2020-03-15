package me.jinuo.imf.websocket.handler

import ProxyInvokerSupport
import com.sghd.common.socket.filter.session.PushProxyFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class PushProxyInvoker<T>(factory: PushProxyFactory): ProxyInvokerSupport<T>(), InvocationHandler {


	override fun  invoke(proxy:Any, method: Method, vararg args:Any) :Any?{
		// 推送接口不接受返回值
		return null
	}
}