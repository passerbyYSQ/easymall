package net.ysq.easymall.service;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import net.ysq.easymall.po.DbFile;
import net.ysq.easymall.po.User;

/**
 * @author	passerbyYSQ
 * @date	2020-11-13 17:54:06
 */
public interface FileService {
	// 下载
	void download(DbFile dbFile, HttpServletResponse response);
	
	// 生成下载的token
	void generateDownloadQRCode(long seconds, DbFile dbFile, OutputStream outStream) throws Exception;
	
	// 根据id查找
	DbFile findById(Integer fileId);
	
	// 根据fileMd5检查文件是否已存在
	DbFile checkFileExist(String fileMd5);
	
	// 在磁盘上创建文件，并将记录插入数据库
	DbFile createFile(String fileMd5, long totalBytes, String suffix, User user);
	
	// 生成上传文件的token
	String generateUploadToken(String email, DbFile dbFile); 
	
	// 复制到目标目录
	long transfer(MultipartFile file, DbFile dbFile);
}
