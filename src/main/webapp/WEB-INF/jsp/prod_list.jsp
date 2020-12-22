<%@ page language="java" import="java.util.*" pageEncoding="utf-8"  %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
	<title>商品列表</title>
	<!-- 不要少了一开始的横杠（webapp下），否则表示的就是相对路径 -->
	<link href="/css/prodList.css" rel="stylesheet" type="text/css">
	<style type="text/css">	
		div.prod_div a {
			color: #666;
			text-decoration: none;
		}
		div.prod_div a:hover {
			color: #CA141D;
		}
	</style>
</head>
<body>

	<%@ include file="_head.jsp" %>
	<div id="content">
		<div id="search_div">
			<form method="post" action="/product/list">
				<span class="input_span">商品名：
					<input type="text" name="goodsName" value="${params.goodsName}" />
				</span>
				<span class="input_span">商品种类：
					<select name="cate">
						<option value="">不限</option>
						<c:forEach var="cate" items="${cates}">
							<option value="${cate}" 
								<c:if test="${params.cate==cate}">selected</c:if> >
								${cate}
							</option>
						</c:forEach>						
					</select>
				</span>
				<span class="input_span">商品价格区间：
					<input type="text" name="minPrice" value="${params.minPrice}" 
					  onkeyup="value=value.replace(/[^\d]/g,'')" 
                      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" /> 
					 - 
					<input type="text" name="maxPrice" value="${params.maxPrice}"
					onkeyup="value=value.replace(/[^\d]/g,'')" 
                      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" />
				</span>
				<input type="submit" value="查 询">
			</form>
		</div>
		<div id="prod_content">
			<c:forEach var="prod" items="${prods}">
				<div class="prod_div">
					<a href="/product/info?pid=${prod.id}" target="_blank">
						<img src="${prod.imgurl}" />
					</a>
					<div id="prod_name_div">
						<h4>	
							<a href="/product/info?pid=${prod.id}" target="_blank">${prod.name}</a>
						</h4> 
					</div>
					<div id="prod_price_div">
						￥ ${prod.price} 元
					</div>
					<div>
						<div id="gotocart_div">
							<a href="javascript:void(0);" onClick="addToCart('${prod.id}', 1)" >加入购物车</a>
						</div>					
						<div id="say_div">
							133人评价
						</div>					
					</div>
				</div>
			</c:forEach>
			<div style="clear: both"></div>
		</div>
	</div>
	<%@ include file="_foot.jsp" %>
	
	<script src="/lib/jquery/jquery.min.js"></script>
	<script type="text/javascript">
		
		function addToCart(prodId, buyNum) {
			$.ajax({
				url: '/cart/add',
				type: 'POST',
				data: {
					prodId: prodId,
					buyNum: buyNum
				},
				dataType: "json",
				success: function(res) {
					console.log(res);
					if (res.code === 2000) {
						alert('加入购物车成功，当前购物车商品数：' + res.data);
					} else {
						alert(res.msg);
					}
				}
			});
		}
	
	</script>
	
</body>
</html>
