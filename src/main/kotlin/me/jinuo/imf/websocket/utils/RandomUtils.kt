package me.jinuo.imf.websocket.utils

import java.util.*

/**
 * 随机数工具类
 * @author frank
 */
class RandomUtils private constructor() {

    init {
        throw IllegalAccessError("该类不允许实例化")
    }

    companion object {

        /** 全局随机种子  */
        private val RANDOM = Random()
        /** 精确到小数点后10位  */
        val RATE_BASE = 1000000000

        /**
         * 检查是否命中
         * @param rate 命中率
         * @param random 伪随机序列
         * @return
         */
        @JvmOverloads
        fun isHit(rate: Double, random: Random = RANDOM): Boolean {
            if (rate <= 0) {
                return false
            }
            val value = random.nextDouble()
            return if (value <= rate) {
                true
            } else false
        }

        /**
         * 获取两个整数之间的随机数
         * @param min 最小值
         * @param max 最大值
         * @param include 是否包含边界值
         * @return
         */
        fun betweenInt(min: Int, max: Int, include: Boolean): Int {
            var min = min
            var max = max
            // 参数检查
            require(min <= max) { "最小值[$min]不能大于最大值[$max]" }
            require(!(!include && min == max)) { "不包括边界值时最小值[$min]不能等于最大值[$max]" }
            // 修正边界值
            if (include) {
                max++
            } else {
                min++
            }
            return (min + Math.random() * (max - min)).toInt()
        }

        /**
         * 获取两个整数之间的随机数,并返回为小数标识形式
         * @param min 最小值
         * @param max 最大值
         * @param include 是否包含边界值
         * @param scale 标度
         * @return
         */
        fun betweenDouble(min: Int, max: Int, include: Boolean, scale: Int): Double {
            require(scale >= 0) { "标度值不能小于0" }
            val random = betweenInt(min, max, include).toDouble()
            return random / Math.pow(10.0, scale.toDouble())
        }

        // 委托 Random 对象的方法

        /**
         * @see Random.nextBoolean
         * @return
         */
        fun nextBoolean(): Boolean {
            return RANDOM.nextBoolean()
        }

        /**
         * @see Random.nextBytes
         * @return
         */
        fun nextBytes(bytes: ByteArray) {
            RANDOM.nextBytes(bytes)
        }

        /**
         * @see Random.nextDouble
         * @return
         */
        fun nextDouble(): Double {
            return RANDOM.nextDouble()
        }

        /**
         * @see Random.nextFloat
         * @return
         */
        fun nextFloat(): Float {
            return RANDOM.nextFloat()
        }

        /**
         * @see Random.nextInt
         * @return
         */
        fun nextInt(): Int {
            return RANDOM.nextInt()
        }

        /**
         * @see Random.nextInt
         * @return
         */
        fun nextInt(n: Int): Int {
            return RANDOM.nextInt(n)
        }

        /**
         * @see Random.nextLong
         * @return
         */
        fun nextLong(): Long {
            return RANDOM.nextLong()
        }
    }
}
/**
 * 检查是否命中
 * @param rate 命中率
 * @return 命中返回 true, 不命中返回 false
 */