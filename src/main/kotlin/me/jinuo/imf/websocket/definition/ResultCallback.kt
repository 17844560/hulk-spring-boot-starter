package me.jinuo.imf.websocket.definition

/**
 * @author frank
 * @date 2019-10-25 14:47
 * @desc
 **/
interface ResultCallback<T> {
    fun call(body: T?)
}