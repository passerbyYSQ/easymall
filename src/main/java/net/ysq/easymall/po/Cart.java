package net.ysq.easymall.po;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "cart")
public class Cart {
    @Id
    @Column(name = "cartID")
    private Integer cartid;

    private Integer userId;

    private String pid;

    private Integer num;

    /**
     * @return cartID
     */
    public Integer getCartid() {
        return cartid;
    }

    /**
     * @param cartid
     */
    public void setCartid(Integer cartid) {
        this.cartid = cartid;
    }

    /**
     * @return userId
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

    /**
     * @return pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return num
     */
    public Integer getNum() {
        return num;
    }

    /**
     * @param num
     */
    public void setNum(Integer num) {
        this.num = num;
    }
}