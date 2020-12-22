<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<!-- 不设置的话，手机端不会进行响应式布局 -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>注册</title>
		
		<!-- 引入Bootstrap核心样式文件（必须） -->
		<link rel="stylesheet" href="/lib/bootstrap/css/bootstrap.min.css">
		
		<!-- 你自己的样式或其他文件 -->
		<link rel="stylesheet" href="/css/register.css">
		
		<!--站点图标-->
		<!-- ... -->
	</head>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-sm-4 col-sm-offset-4 panel panel-default register-box">
					<div class="panel-body">
						<h3 class="title">欢迎注册EasyMall</h3>
						<img class="img-thumbnail logo" src="/img/logo.jpg">
						<form id="registerForm" onSubmit="return false;">
							<!-- 
							关于bootstrap输入框提示
							父容器div的has-feedback不能少，has-success和has-error选其一
							-->
							<div class="form-group has-feedback">
								<label for="username">用户名</label>
								<span class="error-msg"></span>
								<input type="text" maxlength="32" class="form-control" id="username" name="username" placeholder="用户名">
								<span class="glyphicon form-control-feedback"></span>				
							</div>
							<div class="form-group has-feedback">
								<label for="password">密码</label>
								<span class="error-msg"></span>
								<input type="password" class="form-control" id="password" name="password" placeholder="密码">
								<span class="glyphicon form-control-feedback"></span>	
							</div>
							<div class="form-group has-feedback">
								<label for="confirmPassword">确认密码</label>
								<span class="error-msg"></span>
								<input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="重新输入密码">
								<span class="glyphicon form-control-feedback"></span>	
							</div>
							<div class="form-group has-feedback">
								<label for="email">邮箱</label>
								<span class="error-msg"></span>
								<input type="email" class="form-control" id="email" name="email" placeholder="邮箱">
								<span class="glyphicon form-control-feedback"></span>				
							</div>
							<div class="form-group has-feedback">
								<label for="captcha">验证码</label>
								<span class="error-msg" id="captcha-msg"></span>
								<div class="input-group">
									<input type="text" maxlength="6" class="form-control" id="captcha" name="captcha" placeholder="验证码">
									<div class="input-group-btn">
										<input id="send-btn" type="button" value="发送验证码" onClick="sendEmailCaptcha()" class="btn btn-default" >
									</div>
								</div>
								<span class="glyphicon form-control-feedback captcha_icon"></span>	
							</div>
							<button type="submit" class="btn btn-success register-btn">注&nbsp;&nbsp;册</button>
							<a class="to-login" href="/account/login">已有账号？去登录 >></a>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<!-- jQuery -->
		<script src="/lib/jquery/jquery.min.js"></script>
		<script src="/lib/jquery/jquery.validation.min.js"></script>
		<!-- 引入所有的Bootstrap的JS插件 -->
		<script src="/lib/bootstrap/js/bootstrap.min.js"></script>
		
		<script src="/js/register.js"></script>
	</body>
</html>
