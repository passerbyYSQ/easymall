package net.ysq.easymall.dao;

import java.util.List;

import net.ysq.easymall.common.BaseMapper;
import net.ysq.easymall.po.Orderitem;
import net.ysq.easymall.vo.OrderItemInfoRspVo;

public interface OrderitemMapper extends BaseMapper<Orderitem> {
	
	// 查询某个订单中的商品信息
	List<OrderItemInfoRspVo> selectOrderItems(String orderId);
	
}