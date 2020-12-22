package net.ysq.easymall.service;

import java.util.List;

import net.ysq.easymall.vo.CartRspVo;

/**
 * @author	passerbyYSQ
 * @date	2020-12-10 20:10:28
 */
public interface CartService {
	
	// 显示购物车
	// cart表只有username，没有userId
	List<CartRspVo> showCart(Integer userId);
	
	/**
	 * 添加商品到购物车
	 * @param username	用户名
	 * @param prodId	商品id
	 * @param buyNum	购买数量
	 * @return			购物车中的商品数量（有多少种商品）
	 */
	Integer addToCart(Integer userId, String prodId, Integer buyNum);
	
	/**
	 * 修改购物车中某件商品的购买数量
	 * @param username	
	 * @param prodId
	 * @param buyNum
	 * @return			受影响的行数
	 */
	Integer updateBuyNum(Integer userId, String prodId, Integer buyNum);
	
	/**
	 * 删除购物车中的一件商品
	 * @param username
	 * @param prodId	
	 * @return			受影响的行数	
	 */	
	Integer delOneItem(Integer userId, String prodId);
	
	/**
	 * 删除购物车中的多件商品
	 * @param username
	 * @param prodIds	一个数组，里面是要删除的商品的id
	 * @return			受影响的行数
	 */
	Integer delMoreItems(Integer userId, String[] prodIds);
	
}
