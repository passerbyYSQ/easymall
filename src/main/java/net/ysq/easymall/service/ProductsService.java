package net.ysq.easymall.service;

import java.util.List;

import net.ysq.easymall.po.Products;
import net.ysq.easymall.vo.ProdListReqParamsVo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-30 19:28:32
 */
public interface ProductsService {
	
	// 查询所有的商品分类
	List<String> getAllCategories();
	
	// 根据多条件检索商品
	List<Products> getProdByConds(ProdListReqParamsVo params);
	
	// 根据主键查询商品详情信息
	Products getOneProdInfo(String prodId);
	
}
