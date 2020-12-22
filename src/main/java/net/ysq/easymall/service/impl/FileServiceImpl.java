package net.ysq.easymall.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.ysq.easymall.common.CloseUtils;
import net.ysq.easymall.common.JwtUtils;
import net.ysq.easymall.common.QRCodeUtils;
import net.ysq.easymall.dao.DbFileMapper;
import net.ysq.easymall.po.DbFile;
import net.ysq.easymall.po.User;
import net.ysq.easymall.service.FileService;

/**
 * @author	passerbyYSQ
 * @date	2020-11-13 17:55:09
 */
@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	private DbFileMapper dbFileMapper;
	
	@Override
	public DbFile findById(Integer fileId) {
		DbFile record = new DbFile();
		record.setId(fileId);
		return dbFileMapper.selectOne(record);
	}

	@Override
	public DbFile checkFileExist(String fileMd5) {
		DbFile record = new DbFile();
		// 设置查询条件
		record.setFileMd5(fileMd5);
		// 找不到返回null
		DbFile dbFile = dbFileMapper.selectOne(record);
		//System.out.println(dbFile);
		return dbFile;
	}

	@Override
	public DbFile createFile(String fileMd5, long totalBytes, String suffix, User user) {
		try {
			// 创建目标目录
			File classpath = ResourceUtils.getFile("classpath:");
	        File destDir = new File(classpath, "upload/" + user.getEmail());
	        if (!destDir.exists()) {
	            destDir.mkdirs(); // 递归创建创建多级
	            System.out.println("创建目录成功：" + destDir.getAbsolutePath());
	        }
	        // 利用UUID生成随机文件名
	     	String randName = UUID.randomUUID().toString().replace("-", "") + suffix;
			File destFile = new File(destDir, randName);
			// 创建目标
			destFile.createNewFile();
			
			String path = user.getEmail() + "/" + randName;
			DbFile dbFile = new DbFile();
			dbFile.setFileMd5(fileMd5);
			dbFile.setRandName(randName);
			dbFile.setPath(path);
			dbFile.setTotalBytes(totalBytes);
			dbFile.setUploadedBytes(0L);
			dbFile.setCreatorId(user.getId());
			int count = dbFileMapper.insertSelective(dbFile);
			
			return count == 1 ? dbFile : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String generateUploadToken(String email, DbFile dbFile) {
		Map<String, String> claims = new HashMap<>();
		claims.put("fileMd5", dbFile.getFileMd5());
		// 5分钟后过期
		String jwt = JwtUtils.generateJwt(claims, 60 * 1000 * 5);
		return jwt;
	}
	
	@Override
	public void generateDownloadQRCode(long seconds, DbFile dbFile, OutputStream outStream) throws Exception {
		Map<String, String> claims = new HashMap<>();
		claims.put("fileMd5", dbFile.getFileMd5());
		long millis = Duration.ofSeconds(seconds).toMillis();
		String downloadToken = JwtUtils.generateJwt(claims, millis);
		String downloadUrl = ServletUriComponentsBuilder
	        .fromCurrentContextPath()
	        .path("/file/qrcode/download")
	        .queryParam("downloadToken", downloadToken)
	        .toUriString();
		
		QRCodeUtils.encode(downloadUrl, outStream);
		//QRCodeUtil.generateWithStr(downloadUrl, outStream);
	}

	@Override
	public long transfer(MultipartFile file, DbFile dbFile) {
		
		InputStream inStream = null;
		ReadableByteChannel inChannel = null;
		FileOutputStream outStream = null;
		FileChannel outChannel = null;
		try {
			inStream = file.getInputStream();
			inChannel = Channels.newChannel(inStream);
			
			File classpath = ResourceUtils.getFile("classpath:");
			File destFile = new File(classpath, "upload/" + dbFile.getPath());
			outStream = new FileOutputStream(destFile, true); // 注意，第二个参数为true，否则无法追加
			outChannel = outStream.getChannel();
			
			long count = outChannel.transferFrom(inChannel, outChannel.size(), file.getSize());
			//long count = inChannel.transferTo(dbFile.getUploadedBytes(), inChannel.size(), outChannel);
			
			DbFile record = new DbFile();
			record.setId(dbFile.getId());
			record.setUploadedBytes(dbFile.getUploadedBytes() + count);
			// 更新已上传的字节数到数据库
			dbFileMapper.updateByPrimaryKeySelective(record);
			
			return record.getUploadedBytes();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CloseUtils.close(inChannel, inStream, outChannel, outStream);
		}
		return dbFile.getUploadedBytes();
	}

	@Override
	public void download(DbFile dbFile, HttpServletResponse response) {
		FileInputStream inStream = null;
		FileChannel inChannel = null;
		OutputStream outStream = null;
		WritableByteChannel outChannel = null;
		try {
			File classpath = ResourceUtils.getFile("classpath:");
			File destFile = new File(classpath, "upload/" + dbFile.getPath());
			inStream = new FileInputStream(destFile);
	        inChannel = inStream.getChannel();
	        outStream = response.getOutputStream();
	        outChannel = Channels.newChannel(outStream);
	        inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CloseUtils.close(outChannel, outStream, inChannel, inStream);
		}
	}

}
