<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>${prod.name}</title>
	<link href="/css/prodInfo.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="warp">
		<div id="left">
			<div id="left_top">
				<img src="${prod.imgurl}"/>
			</div>
			<div id="left_bottom">
				<img id="lf_img" src="/img/prodInfo/lf.jpg"/>
				<img id="mid_img" src="${prod.imgurl}" width="60px" height="60px"/>
				<img id="rt_img" src="/img/prodInfo/rt.jpg"/>
			</div>
		</div>
	<form action="#"  method="post">
		<div id="right">
			<div id="right_top">
				<span id="prod_name">${prod.name}<br/></span>
				<br>
				<span id="prod_desc">${prod.description}<br/></span>
			</div>
			<div id="right_middle">
				<span id="right_middle_span">
					EasyMall 价：
				<span class="price_red">￥${prod.price}
				<br/>
			    运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：满 100 免运费<br />
			    服&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务：由EasyMall负责发货，并提供售后服务<br />
			    购买数量：
	            <a href="javascript:void(0)" id="delNum" onclick="delCount()">-</a>
	            <input type="text" id="buyNumInp" name="buyNum" value="1">
		        <a href="javascript:void(0)" id="addNum" onclick="addCount()">+</a>
			</div>
			<div id="right_bottom">
				<input type="hidden" name="pid" value=""/>
				<input class="add_cart_but" type="submit" value=""/>	
			</div>
		</div>
	</form>
	</div>
	
	<script src="/lib/jquery/jquery.min.js"></script>
	<script type="text/javascript">
		$(function() {
			/* 
			监听数量的输入框，绑定输入框的keyup和afterpaste事件。
			将非数字的字符替换为空  
			onkeyup="value=value.replace(/[^\d]/g,'')" 
	               onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"	
			*/
			$("input[name='buyNum']").on("keyup afterpaste", function() {
				// 如果输入框输入非数字的字符，立马将该字符替换为空
				var corrected = $(this).val().replace(/[^\d]/g, '');
				if (corrected == '' || corrected == 0) {
					corrected = 1;
				}
				$(this).val(corrected);
			});
			
			
		});
		
		// 需要定义在文档就绪事件外部
		// 数量+1，暂不考虑后端库存
		function addCount() {
			var curCount = parseInt($('#buyNumInp').val());
			$('#buyNumInp').val(curCount + 1);
		}
		
		// 数量-1
		function delCount() {
			var curCount = parseInt($('#buyNumInp').val());
			console.log(typeof($('#buyNumInp').val()));
			if (curCount > 1) {
				$('#buyNumInp').val(curCount - 1);
			}
		}
		
	</script>
	
</body>
</html>