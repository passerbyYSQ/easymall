<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<!-- 不设置的话，手机端不会进行响应式布局 -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>大文件断点续传</title>
		
		<!-- 引入Bootstrap核心样式文件（必须） -->
		<link rel="stylesheet" href="/lib/bootstrap/css/bootstrap.min.css">
		
		<!-- 你自己的样式或其他文件 -->
		<link rel="stylesheet" href="/css/upload.css">
		
		<!--站点图标-->
		<!-- ... -->
	</head>
	<body>
		<div class="container">
			<div class="progress progress-top">
			  <div id="progressbar" class="progress-bar progress-bar-success" role="progressbar" 
			  aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em;">
			    0%
			  </div>
			</div>
			<div class="form-group">
			   <div class="input-group">
					<input id='location' class="form-control" onclick="$('#i-file').click();">
					<div class="input-group-btn">
						<input type="button" id="i-check" value="选择" class="btn btn-default" onclick="$('#i-file').click();">
						<input type="button" value="上传"  onClick="upload()" class="btn btn-default" >
						<input type="button" value="取消"  onClick="cancel()" class="btn btn-default" >
						<input type="button" value="下载二维码"  onClick="downloadQRCode()" class="btn btn-default" >
					</div>
			   </div>
			   <input type="file" name="file" id='i-file' onchange="$('#location').val($('#i-file').val());" style="display: none">
				<p class="help-block proccess-msg" id="proccess-msg"></p>
			</div>
			<img id="downloadQRcode" src="" />
		</div>
		
		<script src="/lib/jquery/jquery.min.js"></script>
		<!-- 引入所有的Bootstrap的JS插件 -->
		<script src="/lib/bootstrap/js/bootstrap.min.js"></script>
		<script src="/lib/spark-md5.min.js"></script>
		<script src="/js/upload.js"></script>
	</body>
</html>
