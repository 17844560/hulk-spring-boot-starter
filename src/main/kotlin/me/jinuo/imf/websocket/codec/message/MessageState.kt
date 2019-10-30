package me.jinuo.imf.websocket.codec.message

/**
 * @author frank
 * @date 2019-10-29 16:42
 * @desc
 **/
enum class MessageState(var state: Short) {
    NORMAL(0),
    RESPONSE(1 shl 0),
    ERROR(1 shl 1)
}