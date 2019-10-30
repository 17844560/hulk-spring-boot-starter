package me.jinuo.imf.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import me.jinuo.imf.websocket.codec.Codec
import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.definition.DefaultResultCallback
import me.jinuo.imf.websocket.execption.CommandException
import me.jinuo.imf.websocket.factory.CommandRegister
import me.jinuo.imf.websocket.session.Session
import org.slf4j.LoggerFactory
import org.springframework.web.socket.BinaryMessage
import javax.annotation.Resource


/**
 * @author frank
 * @date 2019-10-25 20:55
 * @desc
 **/
class DefaultDispatcher : Dispatcher {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        val mapper = ObjectMapper()
        var typeFactory = TypeFactory.defaultInstance()

    }

    @Resource
    private lateinit var codec: Codec

    @Resource
    private lateinit var register: CommandRegister

    override fun receiver(session: Session, binaryMessage: BinaryMessage) {
        val message = decode(binaryMessage)
        message ?: return
        //更新收包时间
        session.setLastTime(System.currentTimeMillis())
        val command = message.getCommand()
        val processor = register.getProcessor(command)
        if (processor == null) {
            logger.error("指令[{}]定义信息不存在！", command)
            throw CommandException("指令[$command]定义信息不存在！")
        }
        //向客户端回复消息
        val callback = DefaultResultCallback.valueOf(session, message, this)
        processor.process(session, message, callback)
    }

    override fun encode(any: Any): ByteArray {
        return if (any is String) {
            any.toByteArray()
        } else {
            mapper.writeValueAsString(any).toByteArray()
        }
    }

    /**
     * 消息解码
     */
    override fun decode(message: BinaryMessage): Message? {
        val decode = codec.decode(message)
        decode ?: return null
        if (decode.body.size == 0) {
            return decode
        }
        decode.bodyJson = String(decode.body)
        return decode
    }

    override fun send(session: Session, message: Message) {
        if (!session.isActive()) {
            return logger.warn("发送消息，连接[{}->[{}]]已断开", session.getId(), session.getRemoteAddress())
        }
        //向客户端发送消息
        val encode = codec.encode(message)
        session.send(encode)
    }

    override fun getCodec(): Codec {
        return codec
    }
}