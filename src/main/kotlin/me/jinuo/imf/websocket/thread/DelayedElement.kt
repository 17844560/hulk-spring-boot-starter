package me.jinuo.imf.websocket.thread

import java.util.*
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

/**
 * @author frank
 * @date 2019-10-29 20:48
 * @desc 延时队列元素
 **/
class DelayedElement<T>(
        /** 元素内容  */
        var content: T,
        /** 过期时间  */
        var end: Date) : Delayed {

    companion object {
    }
    override fun getDelay(timeUnit: TimeUnit): Long {
        val now = System.currentTimeMillis()
        val delay = end.time - now
        when (timeUnit) {
            TimeUnit.MILLISECONDS -> return delay
            TimeUnit.SECONDS -> return TimeUnit.MILLISECONDS.toSeconds(delay)
            TimeUnit.MINUTES -> return TimeUnit.MILLISECONDS.toMinutes(delay)
            TimeUnit.HOURS -> return TimeUnit.MILLISECONDS.toHours(delay)
            TimeUnit.DAYS -> return TimeUnit.MILLISECONDS.toDays(delay)
            TimeUnit.MICROSECONDS -> return TimeUnit.MILLISECONDS.toMicros(delay)
            TimeUnit.NANOSECONDS -> return TimeUnit.MILLISECONDS.toNanos(delay)
            else -> {
            }
        }
        return delay
    }

    override fun compareTo(o: Delayed): Int {
        val delay1 = this.getDelay(TimeUnit.MILLISECONDS)
        val delay2 = o.getDelay(TimeUnit.MILLISECONDS)
        val result = java.lang.Long.valueOf(delay1).compareTo(java.lang.Long.valueOf(delay2))
        if (result != 0) {
            return result
        }
        // 时间判断无法区分时，执行如下判断(用于维持 compareTo 的使用约束)
        return if (this == o) {
            0
        } else {
            this.hashCode() - o.hashCode()
        }
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (content == null) 0 else content!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as DelayedElement<*>?
        if (content == null) {
            if (other!!.content != null)
                return false
        } else if (content != other!!.content)
            return false
        return true
    }

}

fun <T> valueOf(content: T, end: Date): DelayedElement<T> {
    return DelayedElement(content, end)
}
