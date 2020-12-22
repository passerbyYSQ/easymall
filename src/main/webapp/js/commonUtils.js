//import '../lib/spark-md5.min.js';

// 计算文件的MD5值，分块计算，支持大文件
function calculteFileMd5(file, callback) {
	var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
	var chunkSize = 2097152,    // Read in chunks of 2MB 。每一块的大小
        chunks = Math.ceil(file.size / chunkSize), // 整个文件可分为多少块，向下取整
        currentChunk = 0,	// 当前加载的块。初始化为0
        spark = new SparkMD5.ArrayBuffer(),
        fileReader = new FileReader();
	
	// fileReader加载文件数据到内存之后会执行此回调函数
	fileReader.onload = function (e) {
		console.log('read chunk nr ' + (currentChunk + 1) + ' of ' + chunks);
		
		spark.append(e.target.result);                   // Append array buffer
		currentChunk++;

		if (currentChunk < chunks) {
			loadNext();
		} else {
			console.log('finished loading');
			// 计算出文件的md5值
			fileMd5 = spark.end();
			 
			console.log('computed hash: ' + fileMd5); // Compute hash
			
			callback(fileMd5);
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


// 获取指定的路径参数，获取不到返回空串
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
// 判断字符串是否为空
function isEmpty(str) {
	return str == null || str == '' || str == undefined;
}
