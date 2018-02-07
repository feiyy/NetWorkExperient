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
    <title>客服系统</title>
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
            <h3 class="panel-title">客服中心</h3>
        </div>
        <div class="panel-body">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-8 ">
                        <div class="panel panel-default">
                            <div class="tabbable" id="tabs-359364">
                                <ul class="nav nav-tabs" id="clientul">
                                    <li class="active"><a class="clienta" data-toggle="tab" href="#panel1" id="usera1">用户</a>
                                    </li>
                                    <li><a data-toggle="tab" href="#panel2" id="usera2">用户</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="panel1">
                                        <div class="tab-pane" id="text1"
                                             style="height: 300px; overflow-y: scroll;">
                                        </div>
                                        <br> <input class="btn btn-sm" id="clearp1" type="button" value="清除记录">
                                    </div>
                                    <div class="tab-pane" id="panel2">
                                        <div class="tab-pane" id="text2"
                                             style="height: 300px; overflow-y: scroll">
                                        </div>
                                        <br> <input class="btn btn-sm" id="clearp2" type="button" value="清除记录">
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
                                    <li><a href="#panel-userList" data-toggle="tab" id="msglista">在线客服</a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="panel-connect">
                                        <div id="userdiv">
                                            <p></p>
                                            <button id="endservice" class="btn btn-primary">
                                                结束该会话
                                            </button>

                                            <p></p>
                                            <label class="control-label" id="numlabel">客服号</label>

                                            <input type="text" class="form-control" id="serviceid"
                                                   autocomplete="off">
                                            <p></p>
                                            <button id="transfer" class="btn btn-primary">
                                                转交
                                            </button>
                                        </div>

                                        <div id="logindiv">
                                            <button id="login" class="btn btn-primary" data-toggle="modal"
                                                    data-target="#myModal">
                                                登录
                                            </button>
                                        </div>
                                        <br/>
                                    </div>
                                    <div class="tab-pane" id="panel-userList">
                                        <ul id="servicelist" class="list-group"
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
    var islogin = false;
    var websocket = null;
    var client1id = -1;
    var client2id = -1;
    var clientid = -1;

    $("#userdiv").hide();
    $("#senddiv").hide();

    if (!('WebSocket' in window)) {
        alert('当前浏览器 Not support websocket');
    }

    $("#submit").click(function () {
        username = $("#username").val();
        var password = $("#password").val();

        websocket = new WebSocket("ws://localhost:8080/service?username=" + username + "&password=" + password);
        websocket.onopen = onOpen;
        websocket.onmessage = onMsg;
        websocket.onclose = onClose;
        websocket.onerror = onError;
    });

    $("#clearp1").click(function () {
        $("#text1").empty();
    });

    $("#clearp2").click(function () {
        $("#text2").empty();
    });


    $("#sendmsg").click(function () {
        if (clientid != -1) {
            websocket.send("nor--:" + clientid + "--:" + $("#msgtext").val());
            $("#msgtext").val("");
        }
    });


    $("#usera1").click(function () {
        clientid = client1id;
        $("#usera1").text("用户");
    });

    $("#usera2").click(function () {
        clientid = client2id;
        $("#usera2").text("用户");
    });

    $("#endservice").click(function () {
        if (clientid != -1) {
            websocket.send("end--:" + clientid);
        }
    });

    $("#transfer").click(function () {
        var serviceid = $("#serviceid").val();

        if (clientid != -1 && new RegExp("^[1-9]\\d*$", "").test(serviceid)) {
            websocket.send("trans--:" + clientid + "--:" + serviceid);
        }
    });

    function onMsg(event) {
        var data = event.data;
        var id;
        var list;
        console.info(data + " " + clientid + " " + client1id + " " + client2id);

        if (data == "notice:登录成功") {
            $("#logindiv").hide();
            $("#userdiv").show();
            $("#senddiv").show();
            alert(data);

            islogin = true;
        } else if (data.indexOf("断开连接") != -1) {
            id = data.split(":")[2];

            if (client1id == id) {
                client1id = -1;
                $("#text1").append("<a>" + data + "</a><br/>");
                $("#usera1").text("新消息");
            } else if (client2id == id) {
                client2id = -1;
                $("#text2").append("<a>" + data + "</a><br/>");
                $("#usera2").text("新消息");
            }

            if (clientid == id) {
                clientid = -1;
            }
        } else if (data.indexOf("已接入") != -1) {
            msg = data.split("--:");
            id = msg[0];
            console.info(data + " id:" + id);

            if (client1id == -1) {
                client1id = id;

                if (clientid == -1) {
                    clientid = client1id;
                }

                $("#text1").append("<a>" + msg[1] + "</a><br/>");
                $("#usera1").text("新消息");
            } else {
                client2id = id;

                if (clientid == -1) {
                    clientid = client2id;
                }

                $("#text2").append("<a>" + msg[1] + "</a><br/>");
                $("#usera2").text("新消息");
            }
        } else if (data.substring(0, 4) == "list") {
            list = data.split("--:")[1].split("||");
            var i = list.length;
            $("#servicelist").empty();

            while (i--) {
                $("#servicelist").append("<li>" + list[i] + "</li>");
            }
        } else if (data.substring(0, 4) == "msgs") {
            list = data.split("--:")[2].split("||");
            id = data.split("--:")[1];
            var len = list.length;
            var textlabel;

            if (id == client1id) {
                textlabel = $("#text1");
                $("#usera1").text("新消息");
            } else if (id == client2id) {
                textlabel = $("#text2");
                $("#usera2").text("新消息");
            }

            for (i = 0; i < len; i++) {
                textlabel.append("<a>" + list[i] + "</a><br/>");
            }
        } else if (data.indexOf("notice") == 0) {
            alert(data);
        } else if (data.indexOf("转接成功") == 0) {
            console.info(data);
            id = data.split("--:")[1];

            if (client1id == id) {
                client1id = -1;
                $("#text1").append("<a>转接成功</a><br/>");
                $("#usera1").text("新消息");
            } else if (client2id == id) {
                client2id = -1;
                $("#text2").append("<a>转接成功</a><br/>");
                $("#usera2").text("新消息");
            }

            console.info(clientid + " " + client1id + " " + client2id)

            if (clientid == id){
                clientid = -1;
            }

            console.info(clientid + " " + client1id + " " + client2id)
        } else {
            msg = data.split("--:");

            if (msg[0] == client1id) {
                $("#text1").append("<a>" + msg[1] + "</a><br/>");
                $("#usera1").text("新消息");
            } else if (msg[0] == client2id) {
                $("#text2").append("<a>" + msg[1] + "</a><br/>");
                $("#usera2").text("新消息");
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
</script>
</body>
</html>