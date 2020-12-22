// 点击刷新图标，刷新验证码
function refreshCaptcha() {
	var url = '/account/captcha/generate?rand=' + Math.random();
	$('#captcha').css({
		'background-image': 'url(' + url + ')'
	});
	$('#captcha').val('');
	var parentDiv = $('#captcha').parents('div.has-feedback');
	var iconSpan = parentDiv.children('span.glyphicon');
	var msgSpan = parentDiv.children('span.error-msg');
	// 父亲div移除样式
	parentDiv.removeClass('has-success has-error');
	// 图标设置移除样式
	iconSpan.removeClass('glyphicon-ok glyphicon-remove');
	msgSpan.html('');
}

function getUrlParam(key) {
	// ? 后面的
	var searchStr = window.location.search.substring(1);
	console.log(searchStr);
	
	var paramMap = new Array(); 
	
	var paramEntrys = searchStr.split('&');
	for(var i=0; i<paramEntrys.length; i++) {
		var entry = paramEntrys[i].split('=');
		paramMap[ entry[0] ] = entry[1];
	}
	
	console.log(paramMap);
	
	return paramMap[key];  
}

$(function() {
	// 登录表单验证
	$("#loginForm").validate({
		// 表单验证成功通过后的回调
		submitHandler: function(form) {
			//console.log(form);
			//form.submit();
			// 异步提交表单
			// Ajax提交数据
			var email = $('#email').val();
			var password = $('#password').val();
			var captcha = $('#captcha').val();
			var redirectUrl = getUrlParam("redirect_url");
			var url = '/account/login';
			if (redirectUrl != null && redirectUrl != "" ) {
				url += ('?redirect_url=' + redirectUrl);
			}
			
			console.log(redirectUrl);
			$.ajax({
	            url: url,    // 提交到controller的url路径
	            type: "POST",    // 提交方式
	            data: {
					email: email,
					password: password,
					captcha: captcha
				},  
	            dataType: "json",    // 服务器端返回的数据类型
	            success: function (res) {    
					console.log(res);
					if (res.code === 2000) {
						window.location.href = res.data;
					} else {
						alert('密码错误');
						location.reload();
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
			email: { // input的name属性
				required: true,
				email: true,
				//isMobile: true
				remote: {
					cache: false,
					async: true,
					type: 'GET',
					url: '/account/photo',
					data: { // 请求所需的参数列表
						email: function() {
							return $('#email').val();
						}
					},
					// 由于remote需要的返回值是布尔值（false表示不通过），而实际返回值是一个封装对象
					// 所以需要dataFilter对返回的封装对象进行预处理，并给remote返回所需的布尔值
					dataFilter: function(jsonStr, type) {
						var res = JSON.parse(jsonStr);
						
						console.log(typeof(res));
						console.log(res);
						console.log(res.code);
						
						var isOk = (res.code === 2000);
						console.log(isOk);
						// 如果email存在且data部分不为null（有头像），显示用户的头像
						if (isOk && res.data != null) {
							$('img.photo').attr('src', res.data);
						}
						return isOk;
					}
				}
			},
			password: {
				required: true
			},
			captcha: {
				required: true,
				remote: {
					cache: false,
					async: true,
					type: 'GET',
					url: '/account/captcha/validate',
					data: { // 提交给服务端的数据（键值对）
						captcha: function() {
							return $('#captcha').val();
						}
					},
					dataFilter: function(jsonStr, type) {
						var res = JSON.parse(jsonStr);
						var isCorrect = (res.code === 2000);
						if (!isCorrect) { // 验证码错误
							refreshCaptcha();
						}
						return isCorrect;
					}
				}
			}
		},
		messages: { // 与验证规则一一对应的消息提示
			email: {
				required: '邮箱不能为空',
				email: '邮箱格式错误',
				remote: '该邮箱尚未注册'
			},
			password: {
				required: "密码不能为空"
			},
			captcha: {
				required: "验证码不能为空",
				remote: "验证码错误，已经刷新"
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

