package me.jinuo.imf.websocket.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author frank
 * @date 2019-10-30 10:03
 * @desc 配置文件
 **/
@ConfigurationProperties(prefix = "websocket")
class GlobalConfig {
    /** 是否开启心跳*/
    var heartbeat: Boolean = false
    /** 会话超时时间(毫秒)*/
    var timeout: Long = 10 * 60 * 1000
    /** 代理目标服务器ip*/
    var proxyHost: String = ""
    /** 代理目标服务器端口*/
    var proxyPort: Int = 0
}

