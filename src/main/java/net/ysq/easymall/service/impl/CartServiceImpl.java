package net.ysq.easymall.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import net.ysq.easymall.dao.CartMapper;
import net.ysq.easymall.po.Cart;
import net.ysq.easymall.service.CartService;
import net.ysq.easymall.vo.CartRspVo;
import tk.mybatis.mapper.entity.Example;

/**
 * @author	passerbyYSQ
 * @date	2020-12-10 20:12:20
 */
@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartMapper cartMapper;
	
	@Override
	public List<CartRspVo> showCart(Integer userId) {
		return cartMapper.selectAllCart(userId);
	}

	@Override
	public Integer addToCart(Integer userId, String prodId, Integer buyNum) {
		Cart record = new Cart(); 
		// 设置条件
		record.setUserId(userId);
		record.setPid(prodId);
		
		// 购物车中是否已有该商品
		Cart item = cartMapper.selectOne(record);
		
		if (ObjectUtils.isEmpty(item)) {
			// 不存在，新插入一条cart记录
			record.setNum(buyNum);
			cartMapper.insert(record);
		} else {
			// 已经存在，num=num+buyNum
			Cart record1 = new Cart(); 
			record1.setCartid(item.getCartid());
			record1.setNum(item.getNum() + buyNum);
			// 根据主键更新num字段
			cartMapper.updateByPrimaryKeySelective(record1);
		}
		
		// 查询购物车中的商品数量
		Example cartExample = new Example(Cart.class);
		cartExample.createCriteria()
			.andEqualTo("userId", userId); // po属性名还是数据库表字段名？？
		int count = cartMapper.selectCountByExample(cartExample);
		
		return count;
	}

	@Override
	public Integer updateBuyNum(Integer userId, String prodId, Integer buyNum) {
		Cart record = new Cart(); 
		record.setNum(buyNum);
		
		Example cartExample = new Example(Cart.class);
		cartExample.createCriteria()
			.andEqualTo("userId", userId)
			.andEqualTo("pid", prodId);
		int count = cartMapper.updateByExampleSelective(record, cartExample);
		return count;
	}

	@Override
	public Integer delOneItem(Integer userId, String prodId) {
		Cart record = new Cart();
		record.setUserId(userId);
		record.setPid(prodId);
		
		int count = cartMapper.delete(record);
		return count;
	}

	@Override
	public Integer delMoreItems(Integer userId, String[] prodIds) {
		// 将参数封装成map，传给mapper
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("prodIds", prodIds);
		
		Integer count = cartMapper.delMoreItems(params);
		return count;
	}

}
