package net.ysq.easymall.service;

import java.util.List;

import net.ysq.easymall.po.Orders;
import net.ysq.easymall.vo.CartRspVo;

/**
 * @author	passerbyYSQ
 * @date	2020-12-14 20:43:21
 */
public interface OrderService {
	
	int pay(String orderId);
	
	// 获取我的一个订单信息
	Orders getMyOneOrder(String orderId, Integer userId);
	
	// 删除【自己的】一个订单
	void delMyOneOrder(String orderId);
	
	// 查询我的所有订单信息
	List<Orders> getMyAllOrders(Integer userId);
	
	/**
	 * 订单结算页中显示选中的商品信息
	 * @param userId
	 * @param prodIds
	 * @return
	 */
	List<CartRspVo> showOrderProds(Integer userId, String[] prodIds);
	
	/**
	 * 提交订单
	 * @param order		订单信息
	 * @param prodIds	所选商品的id的数组
	 */
	void addOrder(Orders order, String[] prodIds);

}
