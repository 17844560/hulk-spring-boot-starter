package me.jinuo.imf.websocket.parameter

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class InBody(val value: String, val require: Boolean = false)
