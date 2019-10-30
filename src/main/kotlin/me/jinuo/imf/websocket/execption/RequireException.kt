package me.jinuo.imf.websocket.execption

import java.lang.RuntimeException

/**
 * @author frank
 * @date 2019-10-30 11:22
 * @desc
 **/
class RequireException(message: String, exception: Throwable? = null) : RuntimeException(message, exception)