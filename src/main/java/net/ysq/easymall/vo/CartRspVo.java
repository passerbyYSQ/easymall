package net.ysq.easymall.vo;

/**
 * 响应给前端的购物车数据
 * 
 * @author	passerbyYSQ
 * @date	2020-12-10 20:01:59
 */
public class CartRspVo {
	
	// 购物车一条记录的唯一id
	private Integer cartID; 
	
	// 商品id
	private String prodId;
	
	// 商品名称
	private String prodName; 
	
	// 商品封面图
	private String imgUrl;
	
	// 商品单价
	private Double price;
	
	// 购买数量
	private Integer num;

	public Integer getCartID() {
		return cartID;
	}

	public void setCartID(Integer cartID) {
		this.cartID = cartID;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
}
