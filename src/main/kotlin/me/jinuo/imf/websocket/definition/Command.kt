package me.jinuo.imf.websocket.definition

/**
 * @author frank
 * @date 2019-10-25 14:10
 * @desc 消息指令
 **/
data class Command(var module: Short,
                   var command: Short)