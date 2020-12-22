package net.ysq.easymall.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.interfaces.DecodedJWT;

import net.ysq.easymall.common.JwtUtils;
import net.ysq.easymall.common.ResultModel;
import net.ysq.easymall.common.StatusCode;
import net.ysq.easymall.po.DbFile;
import net.ysq.easymall.po.User;
import net.ysq.easymall.service.FileService;
import net.ysq.easymall.vo.FileCheckRspVo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-13 17:42:40
 */
@Controller
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("/upload")
	public String uploadPage() {
		return "upload";
	}
	
	@PostMapping("/check")
	@ResponseBody
	public ResultModel<FileCheckRspVo> checkFileExist(String fileMd5, long totalBytes, 
			String suffix, HttpSession session) {
		// 简单的参数检查，之后再全局处理优化
		if (StringUtils.isEmpty(fileMd5) || totalBytes <= 0
				|| StringUtils.isEmpty(suffix)) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		/*
		// 检查大小
        DataSize size = DataSize.of(totalBytes, DataUnit.BYTES);
        // 限制100 M
        DataSize limit = DataSize.of(100, DataUnit.MEGABYTES);
        if (size.compareTo(limit) > 0) {
            String msg = String.format("当前文件大小为 %d MB，最大允许大小为 %d MB",
                    size.toMegabytes(), limit.toMegabytes());
            return ResultModel.error(StatusCode.FILE_SIZE_EXCEEDED.getCode(), msg);
        }
        */
		User user = (User) session.getAttribute("user");
		
		// 根据md5去数据库查询是否已存在文件
		DbFile dbFile = fileService.checkFileExist(fileMd5);
		// 如果不存在，则创建文件，并插入记录。如果已存在，返回结果
		if (ObjectUtils.isEmpty(dbFile)) {
			dbFile = fileService.createFile(fileMd5, totalBytes, suffix, user);
		}
		
		FileCheckRspVo fileCheckRspVo = new FileCheckRspVo();
		fileCheckRspVo.setUploadedBytes(dbFile.getUploadedBytes());
		if (dbFile.getUploadedBytes() < dbFile.getTotalBytes()) { // 未上传完，返回token
			String uploadToken = fileService.generateUploadToken(user.getEmail(), dbFile);
			fileCheckRspVo.setUploadToken(uploadToken);
		}
		
		return ResultModel.success(fileCheckRspVo);
	}
	
	@PostMapping("/upload")
	@ResponseBody
	public ResultModel<Long> uploadFile(MultipartFile file, String uploadToken) {
		
		// 解析过程可能会抛出异常，全局进行捕获
		DecodedJWT decodedJWT = JwtUtils.verifyJwt(uploadToken);
		String fileMd5 = decodedJWT.getClaim("fileMd5").asString();
		// 如果token验证通过（没有异常抛出），则肯定能找得到
		DbFile dbFile = fileService.checkFileExist(fileMd5);
		
		// 上传文件
		long uploadedBytes = fileService.transfer(file, dbFile);
		System.out.println("已上传：" + uploadedBytes);
		//System.out.println("总大小：" + dbFile.getTotalBytes());
		
		return ResultModel.success(uploadedBytes);
	}
	
	@GetMapping("/qrcode/generate")
	public void downloadByQrcode(String fileMd5, long seconds, 
			HttpServletResponse response) throws IOException, Exception {
		if (ObjectUtils.isEmpty(fileMd5)) {
			throw new Exception("fileMd5为空");
		}
		if (ObjectUtils.isEmpty(seconds) || seconds <= 0) {
			seconds = 60 * 15; // 15分钟
		}
		DbFile dbFile = fileService.checkFileExist(fileMd5);
		if (ObjectUtils.isEmpty(dbFile)) {
			throw new Exception("fileMd5错误");
		}
		
		fileService.generateDownloadQRCode(seconds, dbFile, response.getOutputStream());
	}
	
	@GetMapping("/qrcode/download")
	public void downloadByQrcode(String downloadToken, HttpSession session, 
			HttpServletResponse response) {
		//System.out.println("download!!");
		
		DecodedJWT decodedJWT = JwtUtils.verifyJwt(downloadToken);
		String fileMd5 = decodedJWT.getClaim("fileMd5").asString();
		DbFile dbFile = fileService.checkFileExist(fileMd5);
		
		// 设置响应头
		response.setHeader("Content-Type", "application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment; filename=" + dbFile.getRandName());
		
		fileService.download(dbFile, response);
	}
}
