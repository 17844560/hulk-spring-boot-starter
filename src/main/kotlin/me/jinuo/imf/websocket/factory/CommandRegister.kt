package me.jinuo.imf.websocket.factory

import me.jinuo.imf.websocket.definition.Command
import me.jinuo.imf.websocket.definition.Processor
import me.jinuo.imf.websocket.execption.CommandException

class CommandRegister {

    private val method2Processor = HashMap<Command, Processor>()

    fun register(command: Command, processor: Processor) {
        if (method2Processor.putIfAbsent(command, processor) != null) {
            throw CommandException("指令[$command]重复!")
        }
    }


    fun getProcessor(command: Command): Processor? {
        return method2Processor[command]
    }

}