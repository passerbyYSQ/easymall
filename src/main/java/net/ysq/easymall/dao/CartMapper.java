package net.ysq.easymall.dao;

import java.util.List;
import java.util.Map;

import net.ysq.easymall.common.BaseMapper;
import net.ysq.easymall.po.Cart;
import net.ysq.easymall.vo.CartRspVo;

public interface CartMapper extends BaseMapper<Cart> {
	
	// 显示购物车
	List<CartRspVo> selectAllCart(Integer userId);
	
	// 订单页中显示结算的商品
	List<CartRspVo> selectOrderProds(Map<String, Object> params);
	
	// 删除多条cart记录
	Integer delMoreItems(Map<String, Object> params);
	
}