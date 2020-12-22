package net.ysq.easymall.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.ysq.easymall.component.ParamInvalidError;
import net.ysq.easymall.po.Orders;
import net.ysq.easymall.po.User;
import net.ysq.easymall.service.OrderService;
import net.ysq.easymall.vo.CartRspVo;

/**
 * @author	passerbyYSQ
 * @date	2020-12-14 20:38:20
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/pay")
	public String pay(@RequestParam("id") String orderId) {
		orderService.pay(orderId);
		return "redirect:/order/myorder";
	}
	
	@GetMapping("/del")
	public String del(@RequestParam("id") String orderId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Orders order = orderService.getMyOneOrder(orderId, user.getId());
		if (ObjectUtils.isEmpty(order)) { // 订单号错误
			throw new ParamInvalidError("订单号错误");
		}
		
		orderService.delMyOneOrder(orderId);
		return "redirect:/order/myorder";
	}
	
	/**
	 * 我的订单页面
	 */
	@GetMapping("/myorder")
	public String myOrder(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		List<Orders> myAllOrders = orderService.getMyAllOrders(user.getId());
		model.addAttribute("myAllOrders", myAllOrders);
		return "order_list";
	}
	
	/**
	 * 订单提交
	 * @param address	收货地址
	 * @param prodIds	商品id的数组
	 */
	@PostMapping("/submit")
	public String submit(String receiverinfo, String[] prodIds, HttpSession session) {
		// 参数判断，暂略
		
		User user = (User) session.getAttribute("user");
		
		Orders order = new Orders();
		String orderId = UUID.randomUUID().toString(); // UUID充当订单id
		order.setId(orderId);
		order.setReceiverinfo(receiverinfo); // 收获人姓名、手机、地址等信息
		order.setPaystate(0); // 未支付
		order.setOrdertime(LocalDateTime.now());
		order.setUserId(user.getId());
		
		// 提交订单
		orderService.addOrder(order, prodIds);
		
		return "redirect:/order/myorder";
	}
	
	/**
	 * 订单页面
	 * @param prodIds
	 */
	@PostMapping("/index")
	public String index(String[] prodIds, HttpSession session, Model model) {
		
		// 参数判断，暂略
		
		User user = (User) session.getAttribute("user");
		List<CartRspVo> itemList = orderService.showOrderProds(user.getId(), prodIds);
		
		model.addAttribute("itemList", itemList);
		return "order_add";
	}
	
	

}
