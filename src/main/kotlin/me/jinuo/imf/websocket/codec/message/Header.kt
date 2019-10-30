package me.jinuo.imf.websocket.codec.message

import me.jinuo.imf.websocket.definition.Command
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

/**
 * @date 2019-10-26 20:30
 * @author frank
 * @desc 消息头部一共10个字节（sn=8）+(state=2)+(module=2)+(cmd=2)
 * 0                                        4        6        8        10(BYTE)
 * +----------------------------------------+--------+--------+--------+
 * |                  sn                    | state |   module  |  cmd |
 * +----------------------------------------+--------+--------+--------+
 */
class Header {
    companion object {
        fun read(buffer: ByteBuffer): Header {
            val header = Header()
            header.sn = buffer.getInt()
            header.state = buffer.getShort()
            header.module = buffer.getShort()
            header.cmd = buffer.getShort()
            return header
        }

        fun valueOf(sn: Int, state: Short, module: Short, cmd: Short):Header {
            val header = Header()
            header.sn = sn
            header.state = state
            header.module = module
            header.cmd = cmd
            return header
        }

        //头长度
        var HEAD_LENGTH: Int = 10
    }

    // 8 byte
    var sn: Int = 0
    // 状态
    var state: Short = 0
    // 2 byte
    var module: Short = 0
    // 2 byte
    var cmd: Short = 0

    fun getCommand(): Command {
        return Command(module, cmd)
    }

    /**
     * 检查状态值中是否有指定状态
     * @param state 状态值
     * @param check 被检查的状态
     * @return
     */
    fun hasState(check: Short): Boolean {
        return state and check == check
    }

    /**
     * 添加状态
     * @param added 被添加的状态
     */
    fun addState(added: Short) {
        state = state or added
    }

    /**
     * 移除状态
     * @param removed 被移除的状态
     */
    fun removeState(removed: Short) {
        state = (state xor removed)
    }

}