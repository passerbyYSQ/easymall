//import '../js/commonUtils'

// 真正上传文件的ajax请求
var uploadAjax = null;
var fileMd5; // 文件md5值

function downloadQRCode() {
	if (fileMd5 != null) {
		var url = '/file/qrcode/generate?fileMd5=' + fileMd5 + '&seconds=900';
		$('#downloadQRcode').attr('src', url);
	}
}

function upload() {
	
	// 文件限制检查
	var file = $('#i-file')[0].files[0];
	if (file == null) {
		return;
	}
	
	
	var suffix = file.name.substr(file.name.lastIndexOf('.'));
	var type = file.type;
	var totalBytes = file.size;
	var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
	console.log(suffix, type, totalBytes);
	
	// 开始。通过回调函数，进行链式调用
	calculteFileMd5();
	
	// 计算文件的MD5值，分块计算，支持大文件
	function calculteFileMd5() {
		var chunkSize = 2097152,    // Read in chunks of 2MB 。每一块的大小
	        chunks = Math.ceil(file.size / chunkSize), // 整个文件可分为多少块，向下取整
	        currentChunk = 0,	// 当前加载的块。初始化为0
	        spark = new SparkMD5.ArrayBuffer(),
	        fileReader = new FileReader();
		
		// fileReader加载文件数据到内存之后会执行此回调函数
		fileReader.onload = function (e) {
			refreshMsg('read chunk nr ' + (currentChunk + 1) + ' of ' + chunks);
			spark.append(e.target.result);                   // Append array buffer
			currentChunk++;
	
			if (currentChunk < chunks) {
				loadNext();
			} else {
				refreshMsg('finished loading');
				// 计算出文件的md5值
				fileMd5 = spark.end();
				refreshMsg('computed hash: ' + fileMd5);  // Compute hash
				
				requestCheckFile(); 
			}
		};
		
		// 开始计算
		loadNext();
		
		function loadNext() {
			var start = currentChunk * chunkSize,
				end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;
		
			fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
		}
	}
	
	// 请求服务器验证文件
	function requestCheckFile() {
		$.ajax({
			url: '/file/check',    // 提交到controller的url路径
			type: "POST",    // 提交方式
			dataType: "json",    
			data: {
				fileMd5: fileMd5,
				totalBytes: totalBytes,
				suffix: suffix
			}, 
			success: function (res) {    
				console.log(res);
				if (res.code === 2000) {
					var percentage = parseFloat(res.data.uploadedBytes) / totalBytes * 100; 
					refreshStatus(percentage);
					if (res.data.uploadedBytes < totalBytes) {
						requestRealUpload(res.data);
					}
				} 
			}
		});
	}
	
	// 分块上传
	function requestRealUpload(params) {
		var chunkSize = 2097152;    // 每一块的大小。2 M
	    //var chunks = Math.ceil((totalBytes - params.uploadedBytes) / chunkSize); // 尚未上传的部分可分为几块，取下整
	    //var currentChunk = 0;	// 当前加载的块。初始化为0

		uploadChunk(params.uploadedBytes);
		
		// 请求服务端，上传一块
		function uploadChunk(uploadedBytes) {
			var formData = new FormData();
			var start = uploadedBytes;
			var end = Math.min(start + chunkSize, totalBytes);
			console.log(start, end);
			formData.append('file', blobSlice.call(file, start, end)); // [start, end)
			formData.append('uploadToken', params.uploadToken); // 携带token
			var preLoaded = 0; // 当前块的上一次加载的字节数，用于计算速度
			var preTime = new Date().getTime(); // 上一次回调进度的时间
			uploadAjax = $.ajax({
			    url:  '/file/upload',
			    type: "POST",
			    data: formData,
				cache: false,
			    contentType: false,  // 必须 不设置内容类型
			    processData: false,  // 必须 不处理数据
			    xhr: function() {
			        //获取原生的xhr对象
			        var xhr = $.ajaxSettings.xhr();
			        if (xhr.upload) {
			            //添加 progress 事件监听
						//console.log(xhr.upload);
						xhr.upload.onprogress = function(e) {
							// e.loaded 应该是指当前块，已经加载到内存的字节数
							// 这里的上传进度是整个文件的上传进度，并不是指当前这一块
							var percentage = (start + e.loaded) / totalBytes * 100; 
							refreshStatus(percentage); // 更新百分比
							
							// 计算速度
							var now = new Date().getTime();
							var duration = now - preTime; // 毫秒
							var speed = ((e.loaded - preLoaded) / duration).toFixed(2); // KB/s
							preLoaded = e.loaded;
							preTime = now;
							//if (duration > 1000) {
								// 隔1秒才更新速度
								refreshMsg('正在上传：' + speed + ' KB/s');
							//}
						};
						xhr.upload.onabort = function() {
							refreshMsg('已取消上传，服务端已保存上传完成的分块，下次重传可续传');
						};
			        }
			        return xhr;
			    },
			    success: function(res) {
			        //成功回调 
					console.log(res);   
					if (res.code === 2000) {
						if (res.data < totalBytes) {
							uploadChunk(res.data); // 上传下一块
						} else {
							refreshMsg('上传完成！'); //所有块上传完成
						}
					} else {
						refreshMsg(res.msg); // 当前块上传失败，提示错误，后续块停止上传
					}
			    }
			});		
		}
	}
	
	// 刷新进度条
	function refreshStatus(percentage) {
		var per = (percentage).toFixed(2);
		console.log(per);
		$('#progressbar').text(per + '%');
		$('#progressbar').css({
			width: per + '%'
		});
	}
	// 更新提示信息
	function refreshMsg(msg) {
		$('#proccess-msg').text(msg);
	}
}

// 直接终端上传的ajax请求，后端会抛出异常
function cancel() {
	if (uploadAjax != null) {
		console.log(uploadAjax);
		uploadAjax.abort();
	}
}