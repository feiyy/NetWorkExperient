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
    <title>MyChat</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" style="display:none">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    登录
                </h4>
            </div>
            <div id="msgerror" class="alert alert-warning alert-dismissable" style="display:none">
                <h4><i class="icon fa fa-warning" id="errormsg"></i>Wrong</h4>

                <p id="msg-error-p"></p>
            </div>
            <div id="msgsuccess" class="alert alert-success alert-dismissable" style="display:none">
                <h4><i class="icon fa fa-info"></i>Success</h4>
                <p id="ss-msg-success-p"></p>
            </div>
            <form id="myform" enctype="multipart/form-data">
                <input type="hidden" name="token" id="token" value="${sessionScope.csrftoken}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">用户名</label>
                        <input type="text" class="form-control" id="username">
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">密码</label>
                        <input type="password" class="form-control" id="password">
                    </div>
                </div>
            </form>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary  ladda-button" id="submit" data-style="zoom-in">
                    <span class="ladda-label">登录</span>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="col-md-6 col-md-offset-3">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">聊天室</h3>
        </div>
        <div class="panel-body">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8 ">
                        <div class="panel panel-default">
                            <div class="tabbable" id="tabs-359364">
                                <ul class="nav nav-tabs">
                                    <li class="active"><a id="chatrommtab" href="#panel-public"
                                                          data-toggle="tab">房间</a></li>
                                    <li><a id="privatetab" href="#panel-private" data-toggle="tab"
                                    >私聊</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="panel-public">
                                        <div class="tab-pane" id="publictext"
                                             style="height: 300px; overflow-y: scroll;">
                                        </div>
                                        <br> <input class="btn btn-sm" id="clearpublic" type="button" value="清除记录">
                                    </div>
                                    <div class="tab-pane" id="panel-private">
                                        <br>
                                        <div class="tab-pane" id="privatetext"
                                             style="height: 300px; overflow-y: scroll"></div>
                                        <br> <input class="btn btn-sm" id="clr_pri" type="button" value="清除记录">
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
                                        <div id="userdiv">
                                            <label class="control-label" id="numlabel">房号</label>

                                            <input type="text" class="form-control" id="roomid"
                                                   autocomplete="off">
                                            <p></p>
                                            <button id="enterroom" class="btn btn-primary">
                                                加入对话
                                            </button>

                                            <p></p>
                                            <button id="service" class="btn btn-primary">
                                                联系客服
                                            </button>
                                        </div>

                                        <div id="logindiv">
                                            <button id="login" class="btn btn-primary" data-toggle="modal"
                                                    data-target="#myModal">
                                                登录
                                            </button>
                                        </div>
                                        <br/>

                                        <div id="rommuserlistdiv">

                                        </div>
                                    </div>
                                    <div class="tab-pane" id="panel-userList">
                                        <ul id="msglist" class="list-group"
                                            style="max-height: 330px; overflow-y: scroll">
                                        </ul>
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
    var username;
    var websocket = null;
    var roomid;
    var publicready = false;
    var privateready = false;
    var readytosend = false;
    var nowprivate = "";
    var touser;
    var type = "public";

    $("#userdiv").hide();
    $("#senddiv").hide();

    if (!('WebSocket' in window)) {
        alert('当前浏览器 Not support websocket');
    }

    $("#submit").click(function () {
        username = $("#username").val();
        var password = $("#password").val();

        websocket = new WebSocket("ws://localhost:8080/websocket?username=" + username + "&password=" + password);
        websocket.onopen = onOpen;
        websocket.onmessage = onMsg;
        websocket.onclose = onClose;
        websocket.onerror = onError;
    });

    $("#clearpublic").click(function () {
        $("#publictext").empty();
    });

    $("#sendmsg").click(function () {
        if (readytosend) {
            if (type == "private"){
                websocket.send(type + "--:" + $("#msgtext").val() + "--:" + touser);
            } else {
                websocket.send(type + "--:" + $("#msgtext").val());
            }

            $("#msgtext").val("");
        }
    });

    $("#enterroom").click(function () {
        if (type == "public"){
            roomid = $("#roomid").val();
            if (new RegExp("^[1-9]\\d*$", "").test(roomid)) {
                websocket.send("enterroom--:" + roomid);
                $("#chatrommtab").text("房间(" + roomid + ")");
                readytosend = true;
                publicready = true;
            }
        } else {
            touser = $("#roomid").val();
            if (touser.length > 0) {
                websocket.send("chatprivately--:" + touser);
                $("#privatetab").text("私聊(" + touser + ")");
            }
        }
    });

    $("#chatrommtab").click(function () {
        $("#numlabel").text("房号");
        type = "public";
        readytosend = publicready;
    });

    $("#privatetab").click(function () {
        $("#numlabel").text("用户");
        type = "private";
        readytosend = privateready;
    });

    $("#msglista").click(function () {
        $("#msglista").text("消息列表");
    });

    $("#service").click(function () {
        window.open("client.jsp");
    });

    function onMsg(event) {
        var data = event.data;

        if (new RegExp("^notice:.*?", "").test(data)) {
            if (data == "notice:登录成功") {
                $("#logindiv").hide();
                $("#userdiv").show();
                $("#senddiv").show();
                $("#logout").show();
                alert(data);
            } else if(data == "notice:该用户已下线"){
                privateready = false;
                readytosend = false;
                alert(data);
            } else if (data.indexOf("不在线") != -1){
                $("#privatetext").append("<a>" + data.split(":")[1] + "</a><br/>");
                readytosend = false;
                privateready = false;
            } else if (data.indexOf("已连线") != -1){
                $("#privatetext").append("<a>" + data.split(":")[1] + "</a><br/>");
                readytosend = true;
                privateready = true;
                nowprivate = touser;
            } else {
                alert(data);
            }
        } else {
            dealCommand(data);
        }
    }

    function dealCommand(data) {
        var msg = data.split("--:");
        var type = msg[0];
        var fromusername = msg[1];
        var text = msg[2];

        if (type == "private" ) {
            if (fromusername == nowprivate || fromusername == username){
                $("#privatetext").append("<a>" + fromusername + "：" + text + "</a><br/>");
            } else {
                $("#msglista").text("新消息");
                $("#msglist").append("<li>" + fromusername + "：" + text + "</li>");
            }
        } else if (type == "public") {
            $("#publictext").append("<a>" + fromusername + "：" + text + "</a><br/>");
        } else if (type == "userlist") {
            var rommuser = text.split("||");
            var i = rommuser.length;

            $("#rommuserlistdiv").empty();
            $("#rommuserlistdiv").append("<ul id=\"userlist\" class=\"list-group\"\n" +
                "                                              style=\"max-height: 330px; overflow-y: scroll\"></ul> ");
            $("#userlist").append("<li>用户列表：</li>");

            while (i--) {
                $("#userlist").append("<li>" + rommuser[i] + "</li>");
            }
        }
    }

    function onClose() {
        chatroomid = -1;
        readytosend = false;

        websocket.close();
    }

    function onrror() {
        alert("发生错误");
    }

    function onOpen() {
    }

    window.onbeforeunload = function () {
        websocket.close();
    }

    function showerror(msg) {
        $("#msg-error-p").text(msg);
        $("#msg-error-p").text(msg);
        $("#msgerror").show();
        $("#msgsuccess").hide();
        $('html,body').animate({scrollTop: 0}, 'slow');
    }

    function showsuccess(msg) {
        $("#ss-msg-success-p").text(msg);
        $("#msgerror").hide();
        $("#msgsuccess").show();
        $('html,body').animate({scrollTop: 0}, 'slow');
    }
</script>
</body>
</html>