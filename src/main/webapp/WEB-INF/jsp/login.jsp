<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<!-- 不设置的话，手机端不会进行响应式布局 -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>登录</title>

		<!-- 引入Bootstrap核心样式文件（必须），其中第一个"/"表示webapp -->
		<link rel="stylesheet" href="/lib/bootstrap/css/bootstrap.min.css">

		<!-- 你自己的样式或其他文件 -->
		<link rel="stylesheet" href="/css/login.css">

		<!--站点图标-->
		<!-- ... -->
		
	</head>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-sm-4 col-sm-offset-4 panel panel-default login-box">
					<div class="panel-body">
						<img class="img-circle photo" src="https://www.ysqorz.top/uploaded/2020/08/01/84975f251f4fad5c28.45011031/1596268367.png"
						 alt="头像">
						<form id="loginForm" onSubmit="return false;">
							<!-- 
							关于bootstrap输入框提示
							父容器div的has-feedback不能少，has-success和has-error选其一
							-->
							<div class="form-group has-feedback">
								<label for="email">邮箱</label>
								<!-- 错误提示信息 -->
								<span class="error-msg"></span>
								<input type="email" class="form-control" id="email" name="email" placeholder="邮箱">
								<!-- 图标，打勾或者打叉。glyphicon-ok和glyphicon-remove -->
								<span class="glyphicon form-control-feedback"></span>				
							</div>
							<div class="form-group clear-float has-feedback">
								<label for="password">密码</label>
								<span class="error-msg"></span>
								<input type="password" class="form-control" id="password" name="password" placeholder="密码">
								<span class="glyphicon form-control-feedback"></span>	
							</div>
							<div class="form-group has-feedback">
								<label for="captcha">验证码</label>
								<span class="glyphicon glyphicon-refresh refresh" onClick="refreshCaptcha()"></span>
								<span class="error-msg"></span>
								<input type="text" maxlength="4" class="form-control captcha" id="captcha" name="captcha" 
								placeholder="验证码" >
								<span class="glyphicon form-control-feedback captcha_icon"></span>	
							</div>
							<!-- 注意表单中必须有type="submit"的按钮，否则表单验证通过后，无法进入回调函数submitHandler() -->
							<button type="submit" class="btn btn-success login-btn">登&nbsp;&nbsp;录</button>
							<a class="to-register" href="/account/register" >还没有账号？去注册 >></a>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		
		<script src="/lib/jquery/jquery.min.js"></script>
		<script src="/lib/jquery/jquery.validation.min.js"></script>
		<!-- 引入所有的Bootstrap的JS插件 -->
		<script src="/lib/bootstrap/js/bootstrap.min.js"></script>
		
		<script src="/js/login.js"></script>
	</body>
</html>
