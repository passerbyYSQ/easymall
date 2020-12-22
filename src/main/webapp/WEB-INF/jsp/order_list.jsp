<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
	<link href="${ pageContext.request.contextPath }/css/orderList.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file = "_head.jsp" %>
	<c:if test="${fn:length(myAllOrders) == 0}">
		<div id="no_order_info">
			您还没有添加任何订单！
		</div>
	</c:if>
	
	<!-- 模版数据 -start -->
	<c:forEach var="order" items="${myAllOrders}">
	
	<div style="margin: 0 auto;width:999px;">
		<dl class="Order_information">
			<dt>
				<h3>订单信息</h3>
			</dt>
			<dd style="line-height: 26px;">
				订单编号：${order.id}
				<br />
				下单时间：${order.ordertime}
				<br /> 
				订单金额：${order.money}
				<br /> 
				支付状态：
						<c:choose>
							<c:when test="${order.paystate == 0}">
								<font color="red">未支付</font>
							</c:when>
							<c:when test="${order.paystate == 1}">
								<font color="blue">已支付</font>
							</c:when>
						</c:choose>
						&nbsp;&nbsp;|&nbsp;&nbsp;
						
						<a href="${ pageContext.request.contextPath }/order/del?id=${order.id}" onClick="return confirm('是否确认删除该订单');">
							<img src="${ pageContext.request.contextPath }/img/orderList/sc.jpg" width="69" height="19"/>
						</a>
						&nbsp;
				 		<a href="${ pageContext.request.contextPath }/order/pay?id=${order.id}" onClick="return confirm('是否确认支付该订单');"> 
					 		<img src="${ pageContext.request.contextPath }/img/orderList/zx.jpg" width="69" height="19">
						</a>
						<br /> 
				所属用户：xxx
				<br/> 
				收货地址：${order.receiverinfo}
				<br/> 
				支付方式：在线支付
			</dd>
		</dl>
	
		<table width="999" border="0" cellpadding="0"
			cellspacing="1" style="background:#d8d8d8;color:#333333">
			<tr>
				<th width="276" height="30" align="center" valign="middle" bgcolor="#f3f3f3">商品图片</th>
				<th width="247" align="center" valign="middle" bgcolor="#f3f3f3">商品名称</th>
				<th width="231" align="center" valign="middle" bgcolor="#f3f3f3">商品单价</th>
				<th width="214" align="center" valign="middle" bgcolor="#f3f3f3">购买数量</th>
				<th width="232" align="center" valign="middle" bgcolor="#f3f3f3">总价</th>
			</tr>
			<c:forEach var="item" items="${order.itemList}">
			<tr>
				<td align="center" valign="middle" bgcolor="#FFFFFF">
					<img src="${pageContext.request.contextPath}${item.imgUrl}" width="90" height="105">
				</td>
				<td align="center" valign="middle" bgcolor="#FFFFFF">${item.prodName}</td>
				<td align="center" valign="middle" bgcolor="#FFFFFF">${item.price}元</td>
				<td align="center" valign="middle" bgcolor="#FFFFFF">${item.num}件</td>
				<td align="center" valign="middle" bgcolor="#FFFFFF">${item.price * item.num}元</td>
			</tr>
			</c:forEach>
		</table>
		<div class="Order_price">${order.money}元</div>
	</div>
	
	</c:forEach>
	
	<!-- 模版数据 -end -->
	<%@ include file = "_foot.jsp" %>
</body>
</html>
