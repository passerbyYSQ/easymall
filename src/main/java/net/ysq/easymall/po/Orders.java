package net.ysq.easymall.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import net.ysq.easymall.vo.OrderItemInfoRspVo;

@Table(name = "orders")
public class Orders {
    @Id
    private String id;

    private Double money;

    private String receiverinfo;

    private Integer paystate;

    private LocalDateTime ordertime;

    @Column(name = "user_id")
    private Integer userId;
    
    // 该订单包含的商品信息
    List<OrderItemInfoRspVo> itemList = new ArrayList<>();

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return money
     */
    public Double getMoney() {
        return money;
    }

    /**
     * @param money
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * @return receiverinfo
     */
    public String getReceiverinfo() {
        return receiverinfo;
    }

    /**
     * @param receiverinfo
     */
    public void setReceiverinfo(String receiverinfo) {
        this.receiverinfo = receiverinfo;
    }

    /**
     * @return paystate
     */
    public Integer getPaystate() {
        return paystate;
    }

    /**
     * @param paystate
     */
    public void setPaystate(Integer paystate) {
        this.paystate = paystate;
    }

    /**
     * @return ordertime
     */
    public LocalDateTime getOrdertime() {
        return ordertime;
    }

    /**
     * @param ordertime
     */
    public void setOrdertime(LocalDateTime ordertime) {
        this.ordertime = ordertime;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	public List<OrderItemInfoRspVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderItemInfoRspVo> itemList) {
		this.itemList = itemList;
	}
}