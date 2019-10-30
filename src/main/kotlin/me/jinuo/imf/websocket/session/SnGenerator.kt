package me.jinuo.imf.websocket.session

import java.util.concurrent.atomic.AtomicInteger

/**
 * 序列号生成器，用于生成以1开始的正整数，当到达最大值将重新从1开始自增
 * 负数
 */
class SnGenerator {

    private val sequence = AtomicInteger(START)

    /**
     * 获取下一个序列号
     * @return
     */
    operator fun next(): Int {
        val result = sequence.decrementAndGet()
        if (result <= Integer.MIN_VALUE) {
            sequence.set(START)
        }
        return result
    }

    companion object {
        /** 开始值  */
        private val START = 0
    }
}