socket = new IWebSocket();
socket.conn("ws://192.168.0.45:8080/ws");
//注册指令
socket.register(0, 1, function (message) {
    console.log("收到指令[" + 0 + "]-[" + 1 + "]->" + message.body)
});

socket.register(0, 0, function (message) {
    console.log("收到指令[" + 0 + "]-[" + 0 + "]->" + message.body)
});

window.setTimeout(function () {
    socket.send(0, 0, "asdfadsasdf", function (message) {

    })
}, 1000)