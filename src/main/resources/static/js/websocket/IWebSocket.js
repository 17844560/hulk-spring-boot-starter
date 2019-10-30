function IWebSocket() {
    ws = null;
    //注册的指令对应的回调函数
    receivers = new Dictionary();
    //回调
    callbacks = new Dictionary();
    url = "";
    //消息序号
    sn = 0;
    //连接是否有效（未断开）
    active = false;

    /**
     * 连接建立
     * @param evt
     */
    this.onOpen = function (evt) {
        active = true;
        console.log("连接打开" + JSON.stringify(evt));
    };

    /**
     * 收到消息
     * @param evt
     */
    this.onMessage = function (evt) {
        var arrayBuffer = evt.data;
        var message = decode(arrayBuffer);
        //是否是回复消息(二进制的第一位表示，是否是回应状态)
        if (message.header.hasState(1 << 0)) {
            var callback = callbacks.get(sn);
            if (callback) {
                //移除回调
                callbacks.remove(sn);
                callback(message)
            }
        } else {
            //服务器主动推送
            var command = getCommand(message.header.module, message.header.cmd);
            var processor = receivers.get(command);
            if (processor) {
                processor(message);
            } else {
                console.error("指令[" + command + "]对就的回调函数不存在！")
            }
        }
    };

    /**
     * 连接关闭
     * @param evt
     */
    this.onClose = function (evt) {
        console.log("连接关闭");
        active = false;
    };

    this.onError = function (evt) {
        console.log("发生错误")
    };

    this.conn = function (url) {
        ws = new WebSocket(url);
        ws.binaryType = "arraybuffer";
        ws.onopen = this.onOpen;
        ws.onmessage = this.onMessage;
        ws.onclose = this.onClose;
        ws.onerror = this.onError;
    };

    /**
     * 注册一个回调函数
     * @param module
     * @param cmd
     * @param fun 收到消息会回调的方法
     */
    this.register = function (module, cmd, fun) {
        if (!fun) {
            console.error("注册的回调方法不能为空！");
            return
        }
        receivers.put(getCommand(module, cmd), fun);
    };

    this.send = function (module, cmd, message, callback) {
        var sn = nextSn();
        var arrayBuffer = encode(sn, module, cmd, message);
        //注册回调
        if (callback) {
            callbacks.put(sn, callback)
        }
        ws.send(arrayBuffer)
    };

    function nextSn() {
        if (sn >= Number.MAX_VALUE) {
            sn = 1;
        }
        sn++;
        return sn;
    }
}

function getCommand(module, cmd) {
    return module + "-" + cmd;
}

/**
 * 将字符串转为utf8 编码的byte[]
 * @param msg
 * @returns {Array} int8Array
 */
function encodeUtf8(msg) {
    encoder = new TextEncoder('utf8');
    var array = encoder.encode(msg);
    var buffer = array.buffer;
    return new Int8Array(buffer);
}

/**
 * utf8编码方式转为String
 * @param byteArray
 * @returns {string}
 */
function decodeUtf8(byteArray) {
    decoder = new TextDecoder('utf8');
    return decoder.decode(byteArray)
}

/**
 * 反消息转换成arrayBuffer
 * @param sn
 * @param module
 * @param cmd
 * @param message
 * @returns {ArrayBuffer}
 */
function encode(sn, module, cmd, message) {
    var header = Header.valueOf(sn, 0, module, cmd);
    var body = encodeUtf8(message);
    var length = HEADER_LENGTH + body.length;
    var arrayBuffer = new ArrayBuffer(length);
    var dataView = new DataView(arrayBuffer);
    //sn
    dataView.setInt32(0, header.sn);
    //state
    dataView.setInt16(4, header.state);
    //module
    dataView.setInt16(6, header.module);
    //cmd
    dataView.setInt16(8, header.cmd);
    var index = HEADER_LENGTH;
    //当前指针位置
    for (var i = 0; i < body.length; i++) {
        dataView.setInt8(index, body[i]);
        index += 1
    }
    return arrayBuffer
}

/**
 * 解码
 * @param arrayBuffer
 * @returns {{valueOf: (function(*, *)), header: null, body: null}}
 */
function decode(arrayBuffer) {
    var dataView = new DataView(arrayBuffer);
    var sn = dataView.getInt32(0);
    var state = dataView.getInt16(4);
    var module = dataView.getInt16(6);
    var cmd = dataView.getInt16(8);
    var bodyArray = arrayBuffer.slice(HEADER_LENGTH, dataView.byteLength);
    var body = decodeUtf8(bodyArray);
    var header = Header.valueOf(sn, state, module, cmd);
    return Message.valueOf(header, body);
}

/**
 * 字典
 * @constructor
 */
function Dictionary() {
    this.put = put;
    this.datastore = [];
    this.get = get;
    this.remove = remove;
    this.showAll = showAll;
    this.count = count;
    this.clear = clear;
}

function put(key, value) {
    this.datastore[key] = value;
}

function get(key) {
    return this.datastore[key];
}

function remove(key) {
    delete this.datastore[key];
}

function showAll() {
    var str = "";
    for (var key in this.datastore) {
        str += key + " -> " + this.datastore[key] + "; "
    }
    console.log(str);
}

function count() {
    var n = 0;
    for (var key in Object.keys(this.datastore)) {
        ++n;
    }
    console.log(n);
    return n;
}

function clear() {
    for (var key in this.datastore) {
        delete this.datastore[key];
    }
}

/**
 * @date 2019-10-28 20:55
 * @author frank
 * @desc webSocket 消息头部一共10个字节（sn=8）+(state=2)+(module=2)+(cmd=2)
 * 0                                        4        6        8        10(BYTE)
 * +----------------------------------------+--------+--------+--------+
 * |                  sn                    | state |   module  |  cmd |
 * +----------------------------------------+--------+--------+--------+
 */
var Header = {
    sn: -1,
    state: -1,
    module: -1,
    cmd: -1
};
//头长度
var HEADER_LENGTH = 10;
//构造一个Header
Header.valueOf = function (sn, state, module, cmd) {
    return {
        sn: sn,
        state: state,
        module: module,
        cmd: cmd,
        hasState: function (check) {
            var re = state & check;
            return re === check
        }
    }
};

/**
 * websocket 传输对象（最终会被解析为 byte[] ）
 * @type {{valueOf: (function(*, *)), header: null, body: null}}
 */
function Message() {
    header = null;
    body = null;
}

Message.valueOf = function (header, body) {
    var message = new Message();
    message.header = header;
    message.body = body;
    return message;
};