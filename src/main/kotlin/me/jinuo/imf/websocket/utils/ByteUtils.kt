package me.jinuo.imf.websocket.utils

/**
 * Byte 工具类
 * @author frank
 */
object ByteUtils {

    /**
     * 将 byte[] 中指定位置开始的4个 byte 转换为 int
     * @param array
     * @param offset 开始位置
     * @return
     */
    @JvmOverloads
    fun intFromByte(array: ByteArray, offset: Int = 0): Int {
        return array[offset].toInt() shl 24 or (
                array[offset + 1].toInt() and 0xFF shl 16) or (
                array[offset + 2].toInt() and 0xFF shl 8) or
                (array[offset + 3].toInt() and 0xFF)
    }

    /**
     * 将 int 值转换到 byte[] 中的指定位置开始的4个 byte 元素中
     * @param number
     * @param array
     * @param offset 开始位置
     * @return
     */
    @JvmOverloads
    fun intToByte(number: Int, array: ByteArray = ByteArray(4), offset: Int = 0): ByteArray {
        array[offset + 3] = number.toByte()
        array[offset + 2] = (number shr 8).toByte()
        array[offset + 1] = (number shr 16).toByte()
        array[offset] = (number shr 24).toByte()
        return array
    }

    /**
     * 将 byte[8] 转换为 long
     * @param array
     * @return
     */
    fun longFromByte(b: ByteArray): Long {
        return longFromByte(b, 0)
    }

    /**
     * 将 byte[] 中指定位置开始的8个 byte 转换为 long
     * @param array
     * @param offset 开始位置
     * @return
     */
    fun longFromByte(array: ByteArray, offset: Int): Long {
        val s0 = (array[offset].toInt() and 0xFF).toLong()
        val s1 = (array[offset + 1].toInt() and 0xFF).toLong()
        val s2 = (array[offset + 2].toInt() and 0xFF).toLong()
        val s3 = (array[offset + 3].toInt() and 0xFF).toLong()
        val s4 = (array[offset + 4].toInt() and 0xFF).toLong()
        val s5 = (array[offset + 5].toInt() and 0xFF).toLong()
        val s6 = (array[offset + 6].toInt() and 0xFF).toLong()
        val s7 = (array[offset + 7].toInt() and 0xFF).toLong()
        return s0 shl 56 or (s1 shl 48) or (s2 shl 40) or (s3 shl 32) or (s4 shl 24) or (s5 shl 16) or (s6 shl 8) or s7
    }

    /**
     * 将 long 值转换到 byte[] 中的指定位置开始的4个 byte 元素中
     * @param number
     * @param array
     * @param offset 开始位置
     * @return
     */
    @JvmOverloads
    fun longToByte(number: Long, array: ByteArray = ByteArray(8), offset: Int = 0): ByteArray {
        array[offset + 7] = number.toByte()
        array[offset + 6] = (number shr 8).toByte()
        array[offset + 5] = (number shr 16).toByte()
        array[offset + 4] = (number shr 24).toByte()
        array[offset + 3] = (number shr 32).toByte()
        array[offset + 2] = (number shr 40).toByte()
        array[offset + 1] = (number shr 48).toByte()
        array[offset] = (number shr 56).toByte()
        return array
    }

    /**
     * 将 byte[] 中指定位置开始的2个 byte 转换为 short
     * @param array
     * @param offset 开始位置
     * @return
     */
    @JvmOverloads
    fun shortFromByte(array: ByteArray, offset: Int = 0): Short {
        return (array[offset].toInt() and 0xFF shl 8 or (array[offset + 1].toInt() and 0xFF)).toShort()
    }

    /**
     * 将 short 值转换到 byte[] 中的指定位置开始的4个 byte 元素中
     * @param number
     * @param array
     * @param offset 开始位置
     * @return
     */
    @JvmOverloads
    fun shortToByte(number: Short, array: ByteArray = ByteArray(2), offset: Int = 0): ByteArray {
        array[offset + 1] = number.toByte()
        array[offset] = (number.toInt() shr 8).toByte()
        return array
    }

}