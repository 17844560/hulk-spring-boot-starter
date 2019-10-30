package me.jinuo.imf.websocket.definition

import me.jinuo.imf.websocket.codec.message.Message
import me.jinuo.imf.websocket.session.Session

interface TypeDefinition {

    fun isCallback(): Boolean

    fun resolveArg(session: Session, message: Message, callback: ResultCallback): Array<Any?>
}