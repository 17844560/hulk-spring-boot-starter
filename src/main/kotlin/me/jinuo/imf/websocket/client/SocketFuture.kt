package me.jinuo.imf.websocket.client

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class SocketFuture<T> :Future<T>{

    var done:Boolean = false

    private var result:T?=null

    override fun isDone(): Boolean {
        return done
    }

    override fun get(): T? {
      return  result
    }

    override fun get(timeout: Long, unit: TimeUnit): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isCancelled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}