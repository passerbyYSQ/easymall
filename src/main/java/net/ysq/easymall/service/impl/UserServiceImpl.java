package net.ysq.easymall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ysq.easymall.common.EmailHelper;
import net.ysq.easymall.common.RandomUtils;
import net.ysq.easymall.dao.UserMapper;
import net.ysq.easymall.po.User;
import net.ysq.easymall.service.UserService;
import tk.mybatis.mapper.entity.Example;

/**
 * @author	passerbyYSQ
 * @date	2020-11-11 19:53:31
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User register(String username, String password, String email) {
		User record = new User();
		record.setUsername(username);
		record.setPassword(password);
		record.setEmail(email);
		//int count = userMapper.insertSelective(record);
		// 返回自增长id
		int id = userMapper.insertUseGeneratedKeys(record);
		return id > 0 ? record : null;
	}
	
	@Override
	public String sendEmailCaptcha(String email) {
		// 生成6位随机验证码
		String captcha = RandomUtils.getMixedStr(6);
		// 邮件的内容
		String htmlBody = "欢迎注册EasyMall，您正在验证邮箱，邮箱验证码为：" + captcha + "！";
		// 发送邮件
		EmailHelper.singleSend(new EmailHelper.EmailBean(
				"passerbyYSQ", email, "注册EasyMall，邮箱验证", htmlBody));
		return captcha;
	}

	@Override
	public User selectPhotoByEmail(String email) {
		User record = new User();
		record.setEmail(email);
		return userMapper.selectOne(record);
	}

	@Override
	public User login(String email, String password) {
		Example userExample = new Example(User.class);
		userExample.createCriteria()
				.andEqualTo("email", email)
				.andEqualTo("password", password);
		User user = userMapper.selectOneByExample(userExample);
		return user;
	}

	@Override
	public User selectByUsername(String username) {
		User record = new User();
		record.setUsername(username);
		return userMapper.selectOne(record);
	}

}
