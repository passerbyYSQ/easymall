package net.ysq.easymall.vo;

import org.springframework.util.StringUtils;

import net.ysq.easymall.common.TextUtils;

/**
 * @author	passerbyYSQ
 * @date	2020-11-17 9:11:58
 */
public class RegisterReqVo {
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String captcha;
	
	public boolean validate() {
		return !StringUtils.isEmpty(username) &&
				!StringUtils.isEmpty(password) &&
				TextUtils.isEmail(email) &&
				!StringUtils.isEmpty(captcha);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
