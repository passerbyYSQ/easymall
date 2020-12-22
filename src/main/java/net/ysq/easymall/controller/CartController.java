package net.ysq.easymall.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.ysq.easymall.common.ResultModel;
import net.ysq.easymall.common.StatusCode;
import net.ysq.easymall.po.Products;
import net.ysq.easymall.po.User;
import net.ysq.easymall.service.CartService;
import net.ysq.easymall.service.ProductsService;
import net.ysq.easymall.vo.CartRspVo;

/**
 * 购物车
 * 
 * @author	passerbyYSQ
 * @date	2020-12-10 19:58:01
 */
@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductsService productsService;
	
	/**
	 * 显示购物车
	 * @return
	 */
	@GetMapping("/index")
	public String index(HttpSession session, Model model) {
		// 获取用户信息
		User user = (User) session.getAttribute("user");
		
		List<CartRspVo> cartList = cartService.showCart(user.getId());
		
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 将某件商品添加到购物车ajax请求，返回json字符串
	 * @param prodId	商品的id
	 * @param buyNum	购买数量
	 * @return ResultModel<Integer>		购物车中商品的数量	
	 */
	@PostMapping("/add") 
	@ResponseBody // 不要忘了
	public ResultModel<Integer> addToCart(String prodId, Integer buyNum, HttpSession session) {
		
		if (StringUtils.isEmpty(prodId) || buyNum <= 0) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		// 不能信任前端的prodId，需要确保prodId对应的商品必须存在
		Products prod = productsService.getOneProdInfo(prodId);
		if (ObjectUtils.isEmpty(prod)) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		User user = (User) session.getAttribute("user");
		Integer count = cartService.addToCart(user.getId(), prod.getId(), buyNum);
		
		return ResultModel.success(count);
	}
	
	@PostMapping("/updateBuyNum")
	@ResponseBody // 不要忘了
	public ResultModel<Void> updateBuyNum(String prodId, Integer buyNum, HttpSession session) {
		
		// 危险：如果prodId获取不到，修改数据时会修改购物车中的所有数据
		if (StringUtils.isEmpty(prodId)) {
			return ResultModel.error(StatusCode.PARAM_IS_INVALID);
		}
		
		User user = (User) session.getAttribute("user");
		Integer count = cartService.updateBuyNum(user.getId(), prodId, buyNum);
		
		if (count == 1) {
			return ResultModel.success();
		} else {
			return ResultModel.error(StatusCode.CART_NUM_UPDATE_FAILED);
		}
	}
	
	@PostMapping("/delOne")
	@ResponseBody // 不要忘了
	public ResultModel<Void> delCartOneItem(String prodId, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		Integer count = cartService.delOneItem(user.getId(), prodId);
		if (count == 1) {
			return ResultModel.success();
		} else {
			return ResultModel.error(StatusCode.CART_ONE_ITEM_DELETE_FAILED);
		}
	}
	
	@PostMapping("/delMore")
	@ResponseBody // 不要忘了
	public ResultModel<Integer> delCartMoreItems(@RequestParam("prodIds") String[] prodIds, 
			HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		Integer count = cartService.delMoreItems(user.getId(), prodIds);
		if (count == prodIds.length) {
			return ResultModel.success(count);
		} else {
			ResultModel<Integer> rspModel = ResultModel.error(StatusCode.CART_MORE_ITEM_DELETE_FAILED);
			rspModel.setData(count);
			return rspModel;
		}
	}

}
