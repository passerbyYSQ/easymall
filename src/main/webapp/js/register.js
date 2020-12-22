// 发送邮箱验证码
function sendEmailCaptcha() {
	var bool = $("#registerForm").validate().element('#email');
	if (bool) {
		$.ajax({
            url: '/account/email/send',    
            type: "POST",    
            data: {
				email: function() {
					return $('#email').val();
				}
			},  
            dataType: "json",    // 服务器端返回的数据类型
            success: function (res) { 
				console.log(res);
				if (res.code === 2000) {
					countDown();
				} else {
					$('#captcha-msg').text(res.msg);
				}
            }
        });
	}

	// 60s倒计时	
	function countDown() {
		$('#send-btn').attr('disabled', true); // 禁用发送按钮
		var seconds = 60;
		$('#send-btn').val(seconds + ' s');
		var timer = setInterval(function () { // 先等1秒，再执行回调函数
			seconds--;
			if (seconds <= 0) {
				clearInterval(timer);
				$('#send-btn').val('发送验证码');
				$('#send-btn').removeAttr("disabled");
			} else {
				$('#send-btn').val(seconds + ' s');
			}
		}, 1000);
	}
}

$(function() {
	// 表单验证
	$("#registerForm").validate({
		// 表单验证成功通过后的回调
		submitHandler: function(form) {
			//console.log(form);
			//form.submit();
			// Ajax异步提交数据
			var username = $('#username').val();
			var password = $('#password').val();
			var email = $('#email').val();
			var captcha = $('#captcha').val();
	        $.ajax({
	            url: '/account/register',    // 提交到controller的url路径
	            type: "POST",    // 提交方式
	            data: {
					username: username,
					password: password,
					email: email,
					captcha: captcha
				},  
	            dataType: "json",    // 服务器端返回的数据类型
	            success: function (res) {   
					console.log(res); 
					if (res.code === 2000) {
						window.location.href = '/account/login';
					} else {
						alert('注册失败');
					}
	            }
	        });
		},
		// 错误提示
		errorPlacement: function(error, element) { // 错误信息，input表单项
			// 找到父亲div
			var parentDiv = element.parents('div.has-feedback');
			// 找到显示msg的span
			var msgSpan = parentDiv.children('span.error-msg');
			// 找到图标的span
			var iconSpan = parentDiv.children('span.glyphicon');
			// 设置错误信息
			msgSpan.html('').append(error);
			// 父亲div添加样式has-error
			parentDiv.addClass('has-error');
			// 图标设置样式glyphicon-remove
			iconSpan.addClass('glyphicon-remove');
		},
		// 成功时，移除
		success: function(element) {
			// 找到父亲div
			var parentDiv = element.parents('div.has-feedback');
			// 找到显示msg的span
			var msgSpan = parentDiv.children('span.error-msg');
			// 找到图标的span
			var iconSpan = parentDiv.children('span.glyphicon');
			// 设置错误信息
			msgSpan.html('');
			// 父亲div移除样式has-error，并添加样式has-success
			parentDiv.removeClass('has-error');
			parentDiv.addClass('has-success');
			// 图标设置样式glyphicon-remove，并添加样式glyphicon-ok
			iconSpan.removeClass('glyphicon-remove');
			iconSpan.addClass('glyphicon-ok');
		},
		ignore: ".ignore",
		// 表单验证规则
		rules: {
			username: {
				required: true,
				remote: {
					cache: false,
					async: true,
					type: 'GET',
					url: '/account/username/',
					data: { // 请求所需的参数列表
						username: function() {
							return $('#username').val();
						}
					},
					// 由于remote需要的返回值是布尔值（false表示不通过），而实际返回值是一个封装对象
					// 所以需要dataFilter对返回的封装对象进行预处理，并给remote返回所需的布尔值
					dataFilter: function(jsonStr, type) {
						var res = JSON.parse(jsonStr);
						return res.code === 2000; // 2000 => 未被注册 => 通过 => true
					}
				},
				rangelength: [3, 32]  // 字符长度
			},
			password: {
				required: true,
				rangelength: [3, 32]  // 字符长度
			},
			confirmPassword: {
				required: true,
				equalTo: '#password'
			},
			email: { 
				required: true,
				email: true,
				//isMobile: true
				remote: {
					cache: false,
					async: true,
					type: 'GET',
					url: '/account/photo/',
					data: { // 请求所需的参数列表
						email: function() {
							return $('#email').val();
						}
					},
					// 由于remote需要的返回值是布尔值（false表示不通过），而实际返回值是一个封装对象
					// 所以需要dataFilter对返回的封装对象进行预处理，并给remote返回所需的布尔值
					dataFilter: function(jsonStr, type) {
						var res = JSON.parse(jsonStr);
						// res.code = 2000 表示邮箱存在，则应该返回false（邮箱已被注册）
						return !(res.code === 2000); // 所以需要取反
					}
				}
			},
			captcha: {
				required: true,
				remote: {
					cache: false,
					async: true,
					type: 'GET',
					url: '/account/email/validate',
					data: { // 提交给服务端的数据（键值对）
						captcha: function() {
							return $('#captcha').val();
						}
					},
					dataFilter: function(jsonStr, type) {
						var res = JSON.parse(jsonStr);
						return (res.code === 2000);
					}
				}
			}
		},
		messages: { // 与验证规则一一对应的消息提示
			username: { 
				required: '用户名不能为空',
				remote: '该用户名已被注册',
				rangelength: '用户名长度在3到32个字符'  // 字符长度
			},
			password: {
				required: '密码不能为空',
				rangelength: '密码长度在3到32个字符'   // 字符长度
			},
			confirmPassword: {
				required: '请再次输入密码',
				equalTo: '两次输入的密码不一致'
			},
			email: {
				required: '邮箱不能为空',
				email: '邮箱格式错误',
				remote: '该邮箱已被注册'
			},
			captcha: {
				required: "验证码不能为空",
				remote: "验证码错误"
			}
		},
		onkeyup: function(element, event) {
			var name = $(element).attr("name");
			if (name == "captcha") {
				//不可去除，当是验证码输入必须失去焦点才可以验证（错误刷新验证码）
				return false;
			}
		}
	});
});