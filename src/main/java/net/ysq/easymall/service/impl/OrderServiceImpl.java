package net.ysq.easymall.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.ysq.easymall.dao.CartMapper;
import net.ysq.easymall.dao.OrderitemMapper;
import net.ysq.easymall.dao.OrdersMapper;
import net.ysq.easymall.po.Orderitem;
import net.ysq.easymall.po.Orders;
import net.ysq.easymall.service.OrderService;
import net.ysq.easymall.vo.CartRspVo;
import net.ysq.easymall.vo.OrderItemInfoRspVo;
import tk.mybatis.mapper.entity.Example;

/**
 * @author	passerbyYSQ
 * @date	2020-12-10 20:12:20
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private OrderitemMapper orderitemMapper;
	
	@Autowired
	private OrdersMapper ordersMapper;
	
	
	@Override
	public List<CartRspVo> showOrderProds(Integer userId, String[] prodIds) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("prodIds", prodIds);
		return cartMapper.selectOrderProds(params);
		
	}

	@Override
	@Transactional // 事务处理
	public void addOrder(Orders order, String[] prodIds) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", order.getUserId());
		params.put("prodIds", prodIds);
		// 一次性全查出来
		List<CartRspVo> cartItemList = cartMapper.selectOrderProds(params);
		
		Double sum = 0d;
		for(CartRspVo cartItem : cartItemList) {
			Orderitem orderitem = new Orderitem();
			orderitem.setOrderId(order.getId());
			orderitem.setProductId(cartItem.getProdId());
			orderitem.setBuynum(cartItem.getNum());
			
			orderitemMapper.insert(orderitem);
			
			// 删除购物车的记录
			cartMapper.deleteByPrimaryKey(cartItem.getCartID());
			
			sum += (cartItem.getPrice() * cartItem.getNum());
		}
		
		order.setMoney(sum);
		ordersMapper.insert(order);
	}

	@Override
	public List<Orders> getMyAllOrders(Integer userId) {
		Example orderExample = new Example(Orders.class);
		orderExample.createCriteria()
			.andEqualTo("userId", userId);
		// 暂不考虑分页
		List<Orders> myOrderList = ordersMapper.selectByExample(orderExample);
		
		for (Orders order : myOrderList) {
			// 查询出该订单的所有商品信息
			List<OrderItemInfoRspVo> itemList = orderitemMapper.selectOrderItems(order.getId());
			order.setItemList(itemList);
		}
		
		return myOrderList;
	}

	@Transactional // 注意加上事务注解
	@Override
	public void delMyOneOrder(String orderId) {
		
		// 根据主键删除订单
		ordersMapper.deleteByPrimaryKey(orderId);
		
		// 删除item
		Orderitem record = new Orderitem();
		record.setOrderId(orderId);
		orderitemMapper.delete(record);
	}

	@Override
	public Orders getMyOneOrder(String orderId, Integer userId) {
		Example orderExample = new Example(Orders.class);
		orderExample.createCriteria()
			.andEqualTo("id", orderId)
			.andEqualTo("userId", userId); // 查找出【我自己的】订单
		Orders order = ordersMapper.selectOneByExample(orderExample);
		return order;
	}

	@Override
	public int pay(String orderId) {
		Orders record = new Orders();
		record.setId(orderId);
		record.setPaystate(1);
		int count = ordersMapper.updateByPrimaryKeySelective(record);
		return count;
	}
	
	

}
