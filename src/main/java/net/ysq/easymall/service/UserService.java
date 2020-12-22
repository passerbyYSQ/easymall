package net.ysq.easymall.service;

import net.ysq.easymall.po.User;

/**
 * @author	passerbyYSQ
 * @date	2020-11-11 19:53:12
 */
public interface UserService {
	
	User register(String username, String password, String email);
	
	/**
	 * 调用阿里云API发送邮箱验证码 
	 * @return	验证码
	 */
	String sendEmailCaptcha(String email);
	
	// 查询用户名是否被注册
	User selectByUsername(String username);
	
	// 查询邮箱是否存在
	User selectPhotoByEmail(String email);
	
	// 根据邮箱和密码查询
	User login(String email, String password);
	
}
