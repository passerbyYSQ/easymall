<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link href="${ pageContext.request.contextPath }/css/cart.css" rel="stylesheet" type="text/css">
	</head>
	<body>
	
	<%@ include file="_head.jsp" %>
		<div id="wrap">
			<!-- 标题信息 -->
			<ul id="title">
				<li>
					<input class="allC" name="allC" type="checkbox"/>
					<span id="title_checkall_text">全选</span>
				</li>
				<li class="li_prod">商品</li>
				<li>单价（元）</li>
				<li>数量</li>
				<li>小计（元）</li>
				<li>操作</li>
			</ul>
			
			<form id="cartForm" action="${pageContext.request.contextPath}/order/index" method="post">
			
			<!-- 购物信息 -->
			<c:forEach var="cartItem" items="${cartList}">
				<ul class="prods">
					<li>
						<input type="checkbox" name="prodIds" value="${cartItem.prodId}" class="prodC" id="checkbox-${cartItem.cartID}"/> 
					</li>
					<li class="li_prod">
						<img src="${ pageContext.request.contextPath }${cartItem.imgUrl}" width="90" height="90" class="prodimg" />
						<span class="prodname">${cartItem.prodName}</span>
					</li>
					<li class="li_price">${cartItem.price}</li>
					<li>
						<a href="javascript:void(0)" class="delNum" >-</a>
						<input class="buyNumInp" id="${cartItem.prodId}" type="text"
							 readonly="readonly" value="${cartItem.num}" />
						<a href="javascript:void(0)" class="addNum" >+</a>
					</li>
					<li class="sum_price">${ cartItem.price * cartItem.num}</li>
					<li><a id="${cartItem.prodId}" class="delProd" href="javascript:void(0)">删除</a></li>
				</ul>
			</c:forEach>
			
			</form>
			
			<!-- 总计条 -->
			<div id="total">
				<div id="total_1">
					<!--  
					<input type="checkbox" class="allC" name="allC"/> 
					<span>全选</span>
					-->
					<a id="del_a" class="del_all" href="javascript:void(0)">删除选中的商品</a>
					<div id="div_sum">
						<span id="span_1">总价：</span>
						<span>￥</span>
						<span id="span_2" class="total_sum_price">199</span>&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div id="total_2">	
					<a id="goto_order" href="javascript:void(0)" onClick="document:cartForm.submit();">去结算</a>
				</div>
			</div>
			
		</div>
	<%@ include file="_foot.jsp" %>
	
<!-- 引入jQuery函数库 -->
<script src="/lib/jquery/jquery.min.js"></script>
<script type="text/javascript">
	function toOrder() {
		var prodIds = $("input[name='prodIds']:checked")
				.map(function() {
					return $(this).val()
				}).get();
		console.log(prodIds);
		if (prodIds.length < 0) {
			return alert('请选择商品');
		}
		$.ajax({
			url: "${ pageContext.request.contextPath }/order/index",
			type: 'POST',
			data: {
				prodIds: prodIds
			},
			success: function(res) {
				
			}
		});
	}

	/* 文档就绪函数 */
	$(function() {
		totalMoney();
		
		/* 为"减号"绑定点击事件, 点击"减号"将购买数量输入框中的值减1
		 * 同时利用ajax将session中cartmap中的对应商品的数量减1
		 * 重新计算总金额(包括当前商品的总价和所有商品总价的和)
		 */
		$(".delNum").click(function() {
			//1.点击"减号"将购买数量输入框中的值减1
			//>>获取输入框
			$buyNumInp = $(this).siblings("input")//input输入框
			var buyNum = $buyNumInp.val();
			var newBuyNum = buyNum;
			if (buyNum > 1) {//购买数量必须要大于等于1
				newBuyNum = buyNum - 1;
			}
			
			//2.利用ajax将session中cartmap中的对应商品的购买数量减1
			//>>获取商品的id和购买数量(newBuyNum)
			var pid = $buyNumInp.attr("id");
			//>>发送ajax请求
			$.post("${ pageContext.request.contextPath }/cart/updateBuyNum", {
				"prodId" : pid,
				"buyNum" : newBuyNum
			}, function(res) {
				if (res.code === 2000) {
					updateItemSum($buyNumInp, newBuyNum);
					totalMoney();
				} else {
					alert(res.msg);
				}
			});
		});

		/* 为"加号"绑定点击事件, 点击"加号"将购买数量输入框中的值加1
		 * 同时利用ajax将session中cartmap中的对应商品的数量加1
		 * 重新计算总金额(包括当前商品的总价和所有商品总价的和)
		 */
		$(".addNum").click(function() {
			//1.点击"加号"将购买数量输入框中的值加1
			//>>获取输入框
			$buyNumInp = $(this).siblings("input")//input输入框
			var buyNum = $buyNumInp.val();
			var newBuyNum = parseInt(buyNum) + 1;
			
			//2.利用ajax将session中cartmap中的对应商品的购买数量减1
			//>>获取商品的id和购买数量(newBuyNum)
			var pid = $buyNumInp.attr("id");
			
			//>>发送ajax请求
			$.post("${ pageContext.request.contextPath }/cart/updateBuyNum", {
				"prodId" : pid,
				"buyNum" : newBuyNum
			}, function(res) {
				if (res.code === 2000) {
					updateItemSum($buyNumInp, newBuyNum);
					totalMoney();
				} else {
					alert(res.msg);
				}
			});
		});
		
		// 更新数量和小计
		function updateItemSum($buyNumInput, newBuyNumVal) {
			console.log($buyNumInput);
			// 更新显示的数量
			$buyNumInput.val(newBuyNumVal);
			// 更新小计
			var price = $buyNumInput.parents("ul").find(".li_price").text();
			var prod_sum = parseFloat(price) * newBuyNumVal;
			$buyNumInput.parents("ul").find(".sum_price").text(prod_sum);
		}
		
		/* 重新计算总金额 */
		function totalMoney() {
			//计算购物车所有商品的总价
			var totalMoney = 0;
			$(".sum_price").each(function() {
				totalMoney += parseFloat($(this).text());
			});
			//设置所有商品的总价
			$("#span_2").text(totalMoney);
		}

		/* 设置全选和全不选 */
		$(".allC").click(function() {
			//$(".prodC").attr("checked", $(this).attr("checked"));
			$(".prodC").prop("checked", $(this).prop("checked"));
		});
		// checkbox的联动
		$(".prodC").click(function() {
			var isAllChecked = true;
			$(".prodC").each(function() {
				if (!$(this).prop("checked")) {
					isAllChecked = false;
				}
			});
			$(".allC").prop("checked", isAllChecked);
		});
		
		/* 删除当前商品 
			* 给每一个商品后面的删除按钮添加点击事件, 
			* 点击"删除"删除当前商品(通过也要删除购物车中的商品)
		*/
		$(".delProd").click(function(){
			if (!confirm("是否确认将该商品从购物车中移除？")) {
				return;
			}
			
			//1.获取商品的id
			var pid = $(this).attr("id");
			
			var _this = this;
						
			//2.利用ajax请求删除购物车中指定id的商品(-1后台会删除该商品)
			$.post("${ pageContext.request.contextPath }/cart/delOne",{
				"prodId":pid
			}, function(res) {
				if (res.code === 2000) {
					//3.删除当前页面中的商品
					$(_this).parents("ul").remove();
							
					totalMoney();
				} else {
					alert(res.msg);
				}
			});
		});	
		
		/*
		删除所有选中的商品
		*/
		$(".del_all").click(function(){
			if (!confirm('是否确认将所选商品从购物车中移除？')) {
				return;
			}
			
			var prodIds = $("input[name='prodIds']:checked")
				.map(function() {
					return $(this).val()
				}).get();
			console.log(prodIds);
			$.ajax({
				url: "${ pageContext.request.contextPath }/cart/delMore",
				type: 'POST',
				traditional: true,
				data: {
					"prodIds": prodIds
				},
				success: function(res) {
					if (res.code === 2000) {
						$("input[name='prodIds']:checked").parents("ul").remove();
						totalMoney();
					} else {
						alert(res.msg);
						window.location.reload();
					}
				}
			});
		});
	});
</script>	
	</body>
</html>