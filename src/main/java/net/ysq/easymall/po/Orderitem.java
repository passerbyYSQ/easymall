package net.ysq.easymall.po;

import javax.persistence.*;

@Table(name = "orderitem")
public class Orderitem {
    @Id
    @Column(name = "order_id")
    private String orderId;

    @Id
    @Column(name = "product_id")
    private String productId;

    private Integer buynum;

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return product_id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return buynum
     */
    public Integer getBuynum() {
        return buynum;
    }

    /**
     * @param buynum
     */
    public void setBuynum(Integer buynum) {
        this.buynum = buynum;
    }
}