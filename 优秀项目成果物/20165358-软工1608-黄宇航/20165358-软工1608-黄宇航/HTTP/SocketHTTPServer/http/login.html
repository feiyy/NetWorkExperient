<%--
  Created by IntelliJ IDEA.
  User: 黄宇航
  Date: 2017/7/20
  Time: 8:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="hyh.global.Variable" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%
        String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    %>
    <base href=" <%=basepath%>">
    <title>智慧东大</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <link href="css/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/fileinput.min.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="css/ionicons.min.css" rel="stylesheet">
    <link href="css/AdminLTE.min.css" rel="stylesheet" type="text/css"/>
    <link href="css/all-skins.min.css" rel="stylesheet" type="text/css"/>
    <link href="css/ladda-themeless.min.css" rel="stylesheet" type="text/css"/>
    <link rel="bookmark"  type="image/x-icon"  href="image/logo.ico"/>
    <link rel="shortcut icon" href="image/logo.ico">
</head>
<body class="skin-blue">
<!-- Site wrapper -->
<div class="wrapper">

    <header class="main-header">
        <a class="logo"><%=Variable.logo%>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top" role="navigation">

            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button" id="menu">
                <span class="sr-only">Toggle navigation</span>
                <%=Variable.title%>
            </a>
        </nav>
    </header>
    <aside class="main-sidebar">
        <section class="sidebar">
            <div class="user-panel">
                <div class="pull-left image">
                    <img src="image/logo.jpg" class="img-circle"
                         alt="User Image"/>
                </div>
                <div class="pull-left info">
                    <p>菜单</p>
                    <a href="#">智慧东大</a>
                </div>
            </div>
            <ul class="sidebar-menu">
                <ul class="sidebar-menu">
                    <li>
                        <a href="/assist">
                            <i class="fa fa-book"></i> <span>学霸养成：相约自习</span>
                        </a>
                    </li>

                    <li>
                        <a href="/authenticate">
                            <i class="fa fa-cloud-upload"></i> <span>学霸养成：辅学认证</span>
                        </a>
                    </li>
                </ul>
            </ul>
        </section>
    </aside>


    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                辅学认证
                <small>提交认证资料</small>
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div id="msgerror" class="alert alert-warning alert-dismissable" style="display:none">
                        <h4><i class="icon fa fa-warning" id="errormsg"></i> 出错了!</h4>

                        <p id="msg-error-p"></p>
                    </div>
                    <div id="msgsuccess" class="alert alert-success alert-dismissable" style="display:none">
                        <h4><i class="icon fa fa-info"></i>提交成功</h4>

                        <p id="ss-msg-success-p"></p>
                    </div>
                </div>
            </div>

            <form id="myform" enctype="multipart/form-data">

                <input type="hidden" name="token" id="token" value="${csrftoken}"/>

                <div class="row">
                    <!-- left column -->
                    <div class="col-md-6">
                        <!-- general form elements -->
                        <div class="box box-primary">
                            <div class="box-header">
                                <i class="fa fa-key"></i>

                                <h3 class="box-title">认证信息</h3>
                            </div>
                            <!-- /.box-header --><!-- form start -->

                            <div class="box-body">
                                <div class="form-horizontal">

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">学号</label>

                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="studentid" autocomplete="off"
                                                   name="studentid">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">照片1</label>

                                        <div class="col-sm-9">
                                            <input type="file" class="file" name="file" id="file1"
                                                   onchange="getFileSize(this)">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">照片2</label>

                                        <div class="col-sm-9">
                                            <input type="file" class="file" name="file">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">照片3</label>

                                        <div class="col-sm-9">
                                            <input type="file" class="file" name="file">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">照片4</label>

                                        <div class="col-sm-9">
                                            <input type="file" class="file" name="file">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">照片5</label>

                                        <div class="col-sm-9">
                                            <input type="file" class="file" name="file">
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <!-- /.box -->
                    </div>

                    <div class="col-md-6">
                        <!-- general form elements -->
                        <div class="box box-primary">
                            <div class="box-header">
                                <i class="fa fa-edit"></i>

                                <h3 class="box-title">辅学内容</h3>
                            </div>
                            <!-- /.box-header --><!-- form start -->

                            <div class="box-body">
                                <div class="form-horizontal">

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">辅学内容</label>

                                        <div class="col-sm-9">
                                        <textarea type="text" class="form-control" id="content" name="content"
                                                  autocomplete="off" style="height:224px;"></textarea>
                                        </div>
                                    </div>

                                </div>
                            </div>
                            <div class="box-footer">

                                <button id="submitauthentication" class="btn btn-primary ladda-button"
                                        data-style="zoom-in">
                                    <span class="ladda-label">提交认证</span>
                                </button>
                                <!--<input type="submit" value="提交认证" id="submitauthentication" class="btn btn-primary"/> -->
                            </div>
                        </div>

                        <!-- /.box -->
                    </div>
                </div>

            </form>
        </section>
    </div>
    <!-- /.content -->
    <footer class="main-footer">
        <div align="center">

        </div>
        <div class="pull-right hidden-xs">
            Code is beautiful
        </div>
        <strong>Powered by <a href="<%=Variable.myweb%>">ITcathyh</a> </strong>
    </footer>
</div>
<script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/jquery.form.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/fileinput.min.js"></script>
<script type="text/javascript" src="js/pair.js"></script>
<script type="text/javascript" src="js/app.js"></script>
<script type="text/javascript" src="js/spin.min.js"></script>
<script type="text/javascript" src="js/ladda.min.js"></script>
<script type="text/javascript">
    var lockupload = <%=session.getAttribute("lockupload") == null ? 0 : 1%>;
    var la = Ladda.create(document.querySelector("#submitauthentication"));

    $(document).ready(function () {
        if (lockupload == 1) {
            $("#submitauthentication").addClass("disabled");
        }
    });
</script>
</body>
</html>
