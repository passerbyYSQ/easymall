package net.ysq.easymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ysq.easymall.dao.ProductsMapper;
import net.ysq.easymall.po.Products;
import net.ysq.easymall.service.ProductsService;
import net.ysq.easymall.vo.ProdListReqParamsVo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-30 19:30:11
 */
@Service
public class ProductsServiceImpl implements ProductsService {
	
	@Autowired
	private ProductsMapper productsMapper;

	@Override
	public List<String> getAllCategories() {
		return productsMapper.selectAllCategories();
	}

	@Override
	public List<Products> getProdByConds(ProdListReqParamsVo params) {
		return productsMapper.selectProdByConds(params);
	}

	@Override
	public Products getOneProdInfo(String prodId) {
		return productsMapper.selectByPrimaryKey(prodId);
	}

}
