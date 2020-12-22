package net.ysq.easymall.common;

/**
 * 2000 - 成功处理请求
 * 3*** - 重定向，需要进一步的操作已完成请求
 * 4*** - 客户端错误，请求参数错误，语法错误等等
 * 5*** - 服务器内部错误
 * ...
 *
 * @author passerbyYSQ
 * @create 2020-11-02 16:26
 */
// 不加上此注解，Jackson将对象序列化为json时，直接将枚举类转成它的名字
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusCode {
    SUCCESS(2000, "成功"),

    // 服务器内部错误
    UNKNOWN_ERROR(5000, "未知错误"),

    // 参数相关
    PARAM_NOT_COMPLETED(6001, "参数缺失"),
    PARAM_IS_INVALID(6002, "参数无效"),
    FILE_TYPE_INVALID(6003, "非法文件类型"),
    FILE_SIZE_EXCEEDED(6004, "文件大小超出限制"),
    CAPTCHA_INCORRECT(6005, "验证码错误"),
    PASSWORD_INCORRECT(6006, "密码错误"),

    // 用户相关
    USER_IS_EXIST(6101, "用户已存在"),
    USERNAME_IS_EXIST(6102, "用户名已被使用"),
    USER_NOT_EXIST(6103, "用户不存在，请检查参数"),
    EMAIL_NOT_EXIST(6104, "邮箱尚未注册"),
    EMAIL_SEND_FREQUENT(6105, "频繁发送，距离上次邮箱验证码发送不足60秒"),
    USER_NOT_LOGIN(6106, "用户未登录"),

    // 账户相关
    TOKEN_IS_MISSING(6200, "token缺失"),
    FORCED_OFFLINE(6201, "异地登录，当前账户被迫下线"),
    TOKEN_IS_EXPIRED(6202, "token已过期"),
    TOKEN_IS_INVALID(6203, "无效token"),
    LOGIN_FAILED(6204, "登录失败，用户名或密码错误"),
    REGISTER_FAILED(6205, "注册失败，未知错误"),
    
    // 购物车相关
    // 可能pid错误，或者无权修改他人购物车中的商品数量
    CART_NUM_UPDATE_FAILED(6300, "购物车商品的购买数量更新失败"),
    CART_ONE_ITEM_DELETE_FAILED(6301, "购物车商品删除失败"),
    CART_MORE_ITEM_DELETE_FAILED(6302, "部分购物车商品删除失败"),
    
    // 订单相关
    ORDER_DELETE_FAILED(6400, "订单删除失败，订单号错误")
    ;

    // 状态码数值
    private Integer code;
    // 状态码描述信息
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
