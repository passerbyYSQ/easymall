package net.ysq.easymall.dao;

import java.util.List;

import net.ysq.easymall.common.BaseMapper;
import net.ysq.easymall.po.Products;
import net.ysq.easymall.vo.ProdListReqParamsVo;

public interface ProductsMapper extends BaseMapper<Products> {
	
	// 查询所有的商品分类
	List<String> selectAllCategories();
	
	// 根据多条件检索商品
	List<Products> selectProdByConds(ProdListReqParamsVo params);
	
}