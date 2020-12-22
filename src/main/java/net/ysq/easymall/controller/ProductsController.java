package net.ysq.easymall.controller;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.ysq.easymall.po.Products;
import net.ysq.easymall.service.ProductsService;
import net.ysq.easymall.vo.ProdListReqParamsVo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-30 16:45:47
 */
@Controller
@RequestMapping("/product")
@Validated
public class ProductsController {
	
	@Autowired
	private ProductsService productsService;
	
	/**
	 * 多条件检索商品
	 * @param params	条件参数
	 * @param model	
	 * @return
	 */
	// 暂不考虑分页
	// 通过URL访问：get，没有参数
	// 通过提交表单：post，有参数，部分参数可能为空
	@RequestMapping("/list")
	public String list(@ModelAttribute("params") ProdListReqParamsVo params, 
			Model model) {
		// 参数检查略
		//System.out.println(params);
		if (!ObjectUtils.isEmpty(params.getMinPrice()) &&
				!ObjectUtils.isEmpty(params.getMaxPrice()) && 
				params.getMinPrice() > params.getMaxPrice()) {
			double min = params.getMinPrice();
			params.setMinPrice(params.getMaxPrice());
			params.setMaxPrice(min);
		}
		
		// 查询分类
		List<String> cates = productsService.getAllCategories();
		model.addAttribute("cates", cates);
		//cates.stream().forEach(System.out::println);
		
		// 查询所有商品，暂不考虑分页
		List<Products> prods = productsService.getProdByConds(params);
		//prods.stream().forEach(System.out::println);
		
		model.addAttribute("prods", prods);
		
		return "prod_list";
	}
	
	// 加上@RequestParam注解，如果获取不到pid，则会返回400错误，不会继续往下执行
	// 如果不加这个注解，获取不到也会往下执行
	@GetMapping("/info")
	public String info(@NotBlank @RequestParam("pid") String prodId, Model model) {
		
		Products prod = productsService.getOneProdInfo(prodId);
		// 可能为空！！！
		model.addAttribute("prod", prod);
		return "prod_info";
	}
	
	
}
