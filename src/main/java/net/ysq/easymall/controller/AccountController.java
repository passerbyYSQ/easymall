package net.ysq.easymall.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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

import net.ysq.easymall.common.CaptchaUtils;
import net.ysq.easymall.common.ResultModel;
import net.ysq.easymall.common.StatusCode;
import net.ysq.easymall.common.TextUtils;
import net.ysq.easymall.po.User;
import net.ysq.easymall.service.UserService;
import net.ysq.easymall.vo.LoginReqVo;
import net.ysq.easymall.vo.RegisterReqVo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-11 20:15:12
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 由于配置了视图解析，所以会跳转到 webapp/WEB-INF/jsp/login.jsp
	 * webapp/WEB-INF下的资源是没有办法通过浏览器url直接访问的
	 * SpringMVC不希望把jsp放在webapp下，因为用户通过浏览器直接访问jsp页面。
	 * 而应该把jsp放到webapp/WEB-INF下的【子目录】，然后统一通过controller层的接口
	 * 进行视图跳转
	 * @return
	 */
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@PostMapping("/register")
	@ResponseBody 
	public ResultModel<Void> register(RegisterReqVo vo, HttpSession session) {
		if (!vo.validate()) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		String correctCaptcha = (String) session.getAttribute("emailCaptcha");
		// 验证码是一次性的，取出后失效
		session.removeAttribute("emailCaptcha");
		session.removeAttribute("preEmailSendTime");
		if (!vo.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
			// 验证码错误
			return ResultModel.error(StatusCode.CAPTCHA_INCORRECT);
		}
		
		User user = userService.register(
				vo.getUsername(), vo.getPassword(), vo.getEmail());
		if (ObjectUtils.isEmpty(user)) {
			return ResultModel.error(StatusCode.REGISTER_FAILED);
		}
		
		return ResultModel.success();
	}
	
	/**
	 * 发送邮箱验证码
	 */
	@PostMapping("/email/send")
	@ResponseBody 
	public ResultModel<Void> sendEmailCaptcha(String email, HttpSession session) {
		if (!TextUtils.isEmail(email)) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		// 距离上一次发送验证码是否超过60秒
		LocalDateTime preEmailSendTime = (LocalDateTime) session.getAttribute("preEmailSendTime");
		if (!ObjectUtils.isEmpty(preEmailSendTime) &&
				ChronoUnit.SECONDS.between(preEmailSendTime, LocalDateTime.now()) <= 60) {
			return ResultModel.error(StatusCode.EMAIL_SEND_FREQUENT);
		}
		
		String captcha = userService.sendEmailCaptcha(email);
		// 将邮箱验证码存到session中，注意要跟图形验证码区分
		session.setAttribute("emailCaptcha", captcha);
		// 存储该用户上一次发送邮箱验证码的时间
		session.setAttribute("preEmailSendTime", LocalDateTime.now());
		
		return ResultModel.success();
	}
	
	/**
	 * 检查邮箱验证码是否正确
	 */
	@GetMapping("/email/validate")
	@ResponseBody
	public ResultModel<Void> validateEmailCaptcha(String captcha, HttpSession session) {
		String correctCaptcha = (String) session.getAttribute("emailCaptcha");
		if (StringUtils.isEmpty(captcha) ||
				!captcha.equalsIgnoreCase(correctCaptcha)) {
			// 验证码错误
			return ResultModel.error(StatusCode.CAPTCHA_INCORRECT);
		} 
		return ResultModel.success();
	}

	/**
	 * 检查username是否已被注册
	 * @return	已被注册：true
	 */
	@GetMapping("/username")
	@ResponseBody 
	public ResultModel<Void> checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		User user = userService.selectByUsername(username);
		if (!ObjectUtils.isEmpty(user)) { // 用户名已被注册
			return ResultModel.error(StatusCode.USERNAME_IS_EXIST);
		}
		return ResultModel.success(); 
	}
	
	/**
	 * 用于ajax查询emial是否存在，存在就返回用户的头像
	 * @param email
	 * @return			用户的头像的url
	 */
	@GetMapping("/photo")
	@ResponseBody 
	public ResultModel<String> queryPhoto(String email, HttpSession session) {
		if (!TextUtils.isEmail(email)) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		User user = userService.selectPhotoByEmail(email);
		if (ObjectUtils.isEmpty(user)) {
			return ResultModel.error(StatusCode.EMAIL_NOT_EXIST);
		}
		
		// 用户存在，返回用户的头像url
		return ResultModel.success(user.getPhoto());
	}
	
	@PostMapping("/login")
	@ResponseBody
	public ResultModel<String> login(LoginReqVo vo, HttpSession session, HttpServletRequest request) {
		if (!vo.validate()) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		String correctCaptcha = (String) session.getAttribute("captcha");
		// 验证码是一次性的，取出后失效
		session.removeAttribute("captcha");
		if (!vo.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
			// 验证码错误
			return ResultModel.error(StatusCode.CAPTCHA_INCORRECT);
		}
		
		User user = userService.login(vo.getEmail(), vo.getPassword());
		if (ObjectUtils.isEmpty(user)) {
			// 密码错误
			return ResultModel.error(StatusCode.PASSWORD_INCORRECT);
		}
		// 保存登录状态
		session.setAttribute("user", user);
		
		String redirectUrl = request.getParameter("redirect_url");
		if (StringUtils.isEmpty(redirectUrl)) {
			redirectUrl = "/product/list";
		}
		
		return ResultModel.success(redirectUrl);
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/product/list";
	}
	
	/**
	 * 生成验证码
	 */
	@GetMapping("/captcha/generate")
	public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// 直接交由工具类来负责生成和输出
		CaptchaUtils.generateAndOutput(request, response);
	}
	
	/**
	 * 检查验证码是否正确
	 */
	@GetMapping("/captcha/validate")
	@ResponseBody
	public ResultModel<Void> validateCaptcha(String captcha, HttpSession session) {
		String correctCaptcha = (String) session.getAttribute("captcha");
		if (StringUtils.isEmpty(captcha) ||
				!captcha.equalsIgnoreCase(correctCaptcha)) {
			// 验证码错误
			return ResultModel.error(StatusCode.CAPTCHA_INCORRECT);
		} 
		return ResultModel.success();
	}
	
	
}
