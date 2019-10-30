package me.jinuo.imf.websocket.execption

import java.lang.RuntimeException

/**
 * @author frank
 * @date 2019-10-30 11:22
 * @desc
 **/
class DecodeException(message: String, exception: Throwable) : RuntimeException(message, exception)