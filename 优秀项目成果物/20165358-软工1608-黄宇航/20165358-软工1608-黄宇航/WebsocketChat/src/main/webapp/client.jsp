<%--
  Created by IntelliJ IDEA.
  User: 黄宇航
  Date: 2018/1/18
  Time: 13:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>联系客服</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>

<div class="col-md-6 col-md-offset-3">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">客服中心</h3>
        </div>
        <div class="panel-body">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8 ">
                        <div class="panel panel-default">
                            <div class="tabbable" id="tabs-359364">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a id="servertab" href="#panel-private" data-toggle="tab"
                                    >客服</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="panel-public">
                                        <div class="tab-pane" id="publictext"
                                             style="height: 300px; overflow-y: scroll;">
                                        </div>
                                        <br> <input class="btn btn-sm" id="clearpublic" type="button" value="清除记录">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-default">
                            <div class="tabbable" id="tabs-501853">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a href="#panel-connect" data-toggle="tab" id="firsttab">个人</a>
                                    </li>
                                    <li><a href="#panel-userList" data-toggle="tab" id="msglista">消息列表</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="panel-connect">
                                        <div>
                                            <p></p>
                                            <button id="reconnect" class="btn btn-primary">
                                                重连客服
                                            </button>

                                            <p></p>

                                            <button id="endservice" class="btn btn-primary">
                                                结束服务
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <hr>
            <div id="senddiv">
                <div id="targetdiv" style="display: none;"> 目标:<input class="input-small" id="target" type="text"><br>
                    <br></div>
                <textarea class="form-control" id="msgtext" placeholder="输入你想发送的信息" rows="5" maxlength="200"></textarea>
                <h6 class="pull-right" id="count_message"></h6>
                <input class="btn btn-info" type="button" id="sendmsg" value="发送"/>
            </div>
        </div>
    </div>
</div>
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
    var websocket = null;
    var readytosend = false;
    var reconnect = false;

    if (!('WebSocket' in window)) {
        alert('当前浏览器 Not support websocket');
    } else {
        connect();
    }

    function connect() {
        websocket = new WebSocket("ws://localhost:8080/client");
        websocket.onopen = onOpen;
        websocket.onmessage = onMsg;
        websocket.onclose = onClose;
        websocket.onerror = onError;
        reconnect = false;
        readytosend = true;
    }


    $("#clearpublic").click(function () {
        $("#publictext").empty();
    });

    $("#sendmsg").click(function () {
        if (readytosend) {
            websocket.send($("#msgtext").val());
            $("#msgtext").val("");
        }
    });

    $("#reconnect").click(function () {
        if (websocket != null) {
            websocket.close();
        } else {
            connect();
        }

        reconnect = true;
    });

    $("#endservice").click(function () {
        if (websocket != null) {
            websocket.close();
        }
    });


    function onMsg(event) {
        if (event.data.indexOf("已接入") != -1) {
            readytosend = true;
        } else if (event.data == "end") {
            websocket.close();
            return;
        }

        $("#publictext").append("<a>" + event.data + "</a><br/>");
    }

    function onClose() {
        $("#publictext").append("<a>断开连接</a><br/>");
        readytosend = false;
        websocket = null;

        if (reconnect) {
            connect();
        }
    }

    function onError() {
        alert("发生错误");
    }

    function onOpen() {
    }

    window.onbeforeunload = function () {
        websocket.onclose = function () {
        };
        websocket.close()
    };
</script>
</body>
</html>