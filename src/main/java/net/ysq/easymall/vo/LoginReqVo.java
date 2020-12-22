package net.ysq.easymall.vo;

import org.springframework.util.StringUtils;

import net.ysq.easymall.common.TextUtils;

/**
 * @author	passerbyYSQ
 * @date	2020-11-11 20:47:23
 */
public class LoginReqVo {
	
	private String email;
	
	private String password;
	
	private String captcha;
	
	public boolean validate() {
		return TextUtils.isEmail(email) &&
				!StringUtils.isEmpty(password) &&
				!StringUtils.isEmpty(captcha);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
