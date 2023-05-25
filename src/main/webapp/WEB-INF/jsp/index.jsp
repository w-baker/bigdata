<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.hadoop.fs.FileStatus,com.example.myweb.entity.UserInfo" %>
<%@ page import="com.example.myweb.entity.HDFSObject,java.util.*" %>
<%
    List<HDFSObject> hdfsobjectlist = (List<HDFSObject>) request.getAttribute("hdfsobjectlist");
    String currenturl = "";
    String fullurl = "";
    String username = "";
    Object o = request.getSession().getAttribute("username");
    if (o != null) username = o.toString();
    if (request.getAttribute("currenturl") != null) {
        if (request.getAttribute("currenturl") != null) currenturl = request.getAttribute("currenturl").toString();
        if (request.getAttribute("fullurl") != null) fullurl = request.getAttribute("fullurl").toString();
        UserInfo u = (UserInfo) request.getAttribute("user");
        if (u != null) username = u.getUsername();
    }
%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta name="description" content="A fully featured admin theme which can be used to build CRM, CMS, etc.">
    <meta name="author" content="Coderthemes">
    <link rel="shortcut icon" href="assets/images/cloud.ico">
    <title>软通人力资源大数据检索系统</title>
    <link href="assets/plugins/modal-effect/css/component.css" rel="stylesheet">
    <link href="assets/plugins/sweetalert/dist/sweetalert.css" rel="stylesheet" type="text/css">
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="assets/css/core.css" rel="stylesheet" type="text/css">
    <link href="assets/css/icons.css" rel="stylesheet" type="text/css">
    <link href="assets/css/components.css" rel="stylesheet" type="text/css">
    <link href="assets/css/pages.css" rel="stylesheet" type="text/css">
    <link href="assets/css/menu.css" rel="stylesheet" type="text/css">
    <link href="assets/css/responsive.css" rel="stylesheet" type="text/css">
    <link href="file/css/fileinput.min.css" rel="stylesheet" type="text/css">
    <link href="assets/css/bootstrap-table.min.css" rel="stylesheet" type="text/css"/>
    <script src="assets/js/modernizr.min.js"></script>
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
</head>


<body class="fixed-left">

<!-- Begin page -->
<div id="wrapper">

    <!-- Top Bar Start -->
    <div class="topbar">
        <!-- LOGO -->
        <div class="topbar-left">
            <div class="text-center">
                <a href="index" class="logo"><i class="md md-cloud-queue"></i> <span>软通人力资源检索系统</span></a>
            </div>
        </div>
        <div class="navbar navbar-default" role="navigation">
            <div class="container">
                <div class="pull-left">
                    <button class="button-menu-mobile open-left">
                        <i class="fa fa-bars"></i>
                    </button>
                </div>
                <form id="query" action="queryinfo" method="post">
                    <div class="input-group" style="width: 30%; line-height: 70px;">
                        <input type="text" id="querykeywords" name="querykeywords" class="form-control"
                               placeholder="输入关键字即可全文检索..."> <span class="input-group-btn">
							<button id="search" type="submit" class="btn waves-effect waves-light btn-primary">
								<i class="fa fa-search"></i>
							</button>
                    </span>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- Top Bar End -->


<!-- ========== Left Sidebar Start ========== -->

<div class="left side-menu">
    <div class="sidebar-inner slimscrollleft">
        <div class="user-details">
            <div class="pull-left">
                <img src="assets/images/users/avatar-1.jpg" alt="" class="thumb-md img-circle">
            </div>
            <div class="user-info">
                <div class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span
                            id="userName"><%=username %></span><span
                            class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0)"><i class="md md-settings"></i> 个人设置</a></li>
                        <!--<li><a href="javascript:void(0)"><i class="md md-lock"></i> 锁&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;屏</a></li>-->
                        <li><a href="javascript:void(0)" onclick="logout()"><i class="md md-settings-power"></i>
                            注销登录</a></li>
                    </ul>
                </div>

                <p class="text-muted m-0">普通用户</p>
            </div>
        </div>
        <!--- Divider -->
        <div id="sidebar-menu">
            <ul>
                <li class="has_sub"><a href="index" class="waves-effect waves-light"><i
                        class="fa fa-file-text-o m-r-5"></i><span>全部简历</span> </a>
                <li><a href="recycleBin" class="waves-effect waves-light"><i
                        class="fa fa-trash-o m-r-5"></i><span>全部图片</span></a></li>
                <li><a href="#" class="waves-effect waves-light"><i
                        class="fa fa-paper-plane-o m-r-5"></i><span>全部音视频</span></a></li>
                <li class="has_sub"><a href="#" class="waves-effect waves-light"><i
                        class="fa fa-star-o m-r-5"></i><span>我的收藏</span></a></li>
                <li><a href="myShare" class="waves-effect waves-light"><i class="md md-share"></i><span>资源分享</span></a>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
    </div>
</div>
<!-- Left Sidebar End -->


<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                可用空间：<span id="s1" style="margin-right: 10px"></span> 已用空间：<span id="s2"
                                                                                                 style="margin-right: 10px"></span>
                                总空间：<span id="s3"></span>
                            </h4>

                        </div>
                        <div class="panel-body">
                            <div class="progress progress-lg">
                                <div id="spaceDiv" class="progress-bar progress-bar-purple" role="progressbar"
                                     aria-valuenow="96" aria-valuemin="0" aria-valuemax="100"
                                     style="width: 100%; background-color: #9f9797;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div class="col-lg-12 col-md-8">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="btn-toolbar" role="toolbar">
                            <div class="btn-group">
                                <button id="download" type="button" class="btn btn-primary waves-effect waves-light">
                                    <i class="fa fa-download m-r-5"></i>下载
                                </button>
                                <button class="btn btn-primary waves-effect waves-light" data-toggle="modal"
                                        data-target="#uploadFile">
                                    <i class="md md-file-upload"></i> 上传
                                </button>
                                <button id="refresh" type="button" class="btn btn-primary waves-effect waves-light">
                                    <i class="md md-folder-open"></i> 新建文件夹
                                </button>
                                <button id="deleteFile" type="button" class="btn btn-primary waves-effect waves-light">
                                    <i class="fa fa-trash-o m-r-5"></i>删除
                                </button>
                                <button type="button" class="btn btn-primary waves-effect waves-light">重命名</button>
                                <button id="view" type="button" class="btn btn-primary waves-effect waves-light">预览
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- End row -->
                <!-- Start content -->
                <div class="panel">
                    <div class="panel-body">
                        <div class="row">
                            <table class="table table-striped">
                                <caption>当前目录：<%=currenturl %>
                                </caption>
                                <thead>
                                <tr>
                                    <th class="tc" width="5%"><input class="allChoose" name="" type="checkbox"></th>
                                    <th>文件名</th>
                                    <th>上传时间</th>
                                    <th>大小</th>
                                    <th>备注信息</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <%
                                    if (hdfsobjectlist != null) {
                                        for (HDFSObject fs : hdfsobjectlist) {
                                %>
                                <tr>
                                    <td class="tc"><input name="id[]" value="59" type="checkbox"></td>
                                    <td>
                                        <% if (fs.isDirectory() == true) { %>
                                        <a target="_self" href="readdir?url=<%=fs.getPath() %>" title="进入目录"><img
                                                src="assets/images/icon_dir.png"/><%=fs.getName() %>
                                        </a>
                                        <% } else {%>
                                        <img src="assets/images/icon_file.png"/><%=fs.getName() %>
                                    </td>
                                    <% }%>
                                    </td>
                                    <td><%=fs.getAccessTime()  %>
                                    </td>
                                    <td title="大小"><%=(fs.isDirectory() == false) ? fs.getLen() / 1024 : " "   %>KB <a
                                            target="_blank" href="#" title=""></a></td>
                                    <td></td>
                                    <td><% if (fs.isDirectory() == false) {%> <a class="link-update"
                                                                                 href="view?fileName=<%=fs.getPath().toString() %>">预览</a>
                                        <a class="link-update"
                                           href="download?fileName=<%=fs.getPath() %>">下载</a> <% } %>
                                        <a class="link-del" href="delete?filePath=<%=fs.getPath() %>">删除</a>
                                    </td>
                                </tr>
                                <% }
                                }
                                %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- 文件上传对话框 -->
                <div id="uploadFile" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                     aria-hidden="true" style="display: none">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form method="POST" action="/upload?url=<%=fullurl %>&username=<%=username %>"
                                  enctype="multipart/form-data">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×
                                    </button>
                                    <h4 class="modal-title">文件上传</h4>
                                </div>
                                <div class="modal-body">
                                    <div class="upload-wrap">
                                        <input id="file" name="file" type="file" multiple="multiple"/>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-default waves-effect" value="Submit">开始上传
                                    </button>
                                    <button type="button" class="btn btn-default waves-effect"
                                            onclick="closeModal('uploadFile')">关闭
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!-- end Panel -->
            </div>
        </div>
    </div>
    <script>
        function closeModal(id) {
            $('#' + id).modal('hide');
            $('#fileTable').bootstrapTable('refresh');
        }

    </script>
</body>
</html>