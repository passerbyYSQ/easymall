package net.ysq.easymall.vo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-30 19:49:08
 */
public class ProdListReqParamsVo {
	
	// 商品名称关键词
	private String prodName;
	
	// 分类名字
	private String cate;
	
	// 最低价格 
	private Double minPrice;
	
	// 最高价格
	private Double maxPrice;

	public String getGoodsName() {
		return prodName;
	}

	public void setGoodsName(String goodsName) {
		this.prodName = goodsName;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Override
	public String toString() {
		return "ProdListReqParamsVo [goodsName=" + prodName + ", cate=" + cate + ", minPrice=" + minPrice
				+ ", maxPrice=" + maxPrice + "]";
	}
	
}
