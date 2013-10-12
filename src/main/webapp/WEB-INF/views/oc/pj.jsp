<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="root" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>义乌市琦诺饰品有限公司</title>
	<script type="text/javascript" src="${root }/resources/plugin/jquery/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${root }/resources/plugin/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="${root }/resources/plugin/juicer/juicer.js"></script>
	<style type="text/css">
		*{
			
		}
		td input{
			width:350px;
		}
		.tdKey{
			border:1px solid ;
			color:red ;
			background-color:#F0F0F0 ;
			font-size:13px;
		}
		#errorBody{
			color:red ;
		}
		
	</style>
	<script type="text/javascript">
		$(function(){
			$(".error").hide();
			$("form tr td:even").addClass("tdKey");
			var otherHtml = "<div class=\"clearfix\" id=\"custom-description\" style=\"color: rgb(0, 0, 0); font-size: 11.8px; word-wrap: break-word; padding: 15px 0px; width: 710px; overflow: hidden; line-height: 1.5; font-family: verdana; background-position: 0px 0px; background-repeat: repeat no-repeat;\"> " 
				  + "	<h2 class=\"description\" style=\"font-weight: 700; color: rgb(51, 51, 51); font-size: 16px; margin: 0px; padding: 0px 10px 10px; font-family: Arial;\">&nbsp;</h2> " 
				  + "	<p style=\"margin: 0px; padding: 0px; word-wrap: break-word; font-family: arial;\"><span style=\"line-height: 18px; color: rgb(112, 48, 160);\"><span style=\"line-height: 30px; font-size: 20px;\">Welcome to our great store! You &nbsp;will love here once you come as our goods and service are great!</span></span></p> " 
				  + "	<div style=\"word-wrap: break-word; font-family: arial;\">&nbsp;</div> " 
				  + "	<div style=\"word-wrap: break-word; font-family: arial;\"><span style=\"line-height: 30px; font-size: 20px;\">Special offer:&nbsp;</span>Kindly please give us your good Feedback &amp; Reward If you are satisfied with the item and our service. Please do not hesitate to give us a positive feedback with 5 stars. 5% discount will be offered for your new order then. Please help remind us when you place new order, thank you</div> " 
				  + "	<div style=\"word-wrap: break-word; font-family: arial;\"><span style=\"line-height: 30px; font-size: 20px;\">Return Policy:</span>&nbsp;If you feel not satisfied with the items you received from us, and the item is different from description. Please let us know. Full refund, exchange or replacement is offered. Just if in need, please return the item to us within 3 days after you receive it. Please contact us for the detailed return &amp; exchange instruction. We are not responsible for the loss due to the lack of communication.</div> " 
				  + "	<div style=\"word-wrap: break-word; font-family: arial;\">&nbsp;</div> " 
				  + "	</div> " ;
			//$("#otherHtml").text(otherHtml);
			
			//抽取产品
			$("#save").click(function(){
				$.ajax({
					url:"${root }/product/pj/save"
					,type:"post"
					,dataType:"json"
					,data:$("form:eq(0)").serialize()
					,beforeSend:function(){
						$("#loading").show();
					}
				}).success(function(data){
					if(data.success){
						var msg = "抽取产品成功！\r\n本次共抽取产品【"+data.count+"】个！花费时间【"+data.time+"】秒！\r\n" ;
						if(data.errorCount > 0){
							var errorMsg = juicer($("#tmp").html(), data) ;
							$("#errorBody").html(errorMsg) ;
							$(".error").show();
						}else{
							$(".error").hide();
						}
						alert(msg);
					}else{
						alert("抽取产品失败！");
					}
				}).error(function(){
					alert("链接服务器失败！");
				}).complete(function(){
					$("#loading").hide();
				});
			});
			
			
			CKEDITOR.replace('otherHtml', {});
			
		});
	</script>
</head>
<body>

<h3><a href="<c:url value="/" />">返回首页</a></h3>
<p>公司：<a href="http://www.pjspw.com/" target="_blank">平价饰品网</a></p>
<hr>
<div class="error">
<table>
	<thead>
		<tr>
			<td>序号</td>
			<td>商品型号</td>
			<td>商品原始链接</td>
			<td>错误消息</td>
		</tr>
	</thead>
	<tbody id="errorBody">
		
	</tbody>
</table>
</div>
<form:form method="post" modelAttribute="param" action="">
<div>
	<span style="color:red;">抽取产品的网址：</span><input type="text" name="urlPath" value="" style="width:800px;"/>
	<input type="button" name="save" id="save" value="开始抽取产品"/>
	<img id="loading" style="display:none;" src="${root }/resources/images/loading.gif">
	<br/>
	
	<span><label><input type="checkbox" value="true" name="isDownloadImage" checked/>下载产品图片</label></span>
	<br/>
	<span><label><input type="checkbox" value="true" name="onlyGetCurrentPage" />只抽取当前页面的产品</label></span>
	<span style="font-size:12px;">（默认抽取当前网页后的所有分页页面中的所有产品）</span>
</div>
<table>
  <tr>
    <td>商店id</td>
    <td ><input type="text" name="storeId" value="0"/></td>
    <td>您为当前商品自定义的编码</td>
    <td><input type="text" name="productTypeCode" value="07"/></td>
  </tr>
  <tr>
    <td>英文语言id</td>
    <td><input type="text" name="languageEn" value="1"/></td>
    <td>中文语言id</td>
    <td><input type="text" name="languageCn" value="2"/></td>
  </tr>
  <tr>
    <td>opencart的image的全路径<br/>路径截止到data文件夹</td>
    <td><input type="text" name="imageDataPath" value="E:\workLesson\setup\xampp\htdocs\opencart\image\data"/></td>
    <td>opencart的image的全路径下的目录名<br/>data文件夹的目录名称</td>
    <td><input type="text" name="imageDirName" value="pj"/></td>
  </tr>
  <tr>
    <td>商品所属的品牌id<br/>参考[商品->链接->品牌]</td>
    <td><input type="text" name="brandId" value="12"/></td>
    <td>商品最低起订量</td>
    <td><input type="text" name="minNumDiff" value="3"/></td>
  </tr>
  <tr>
    <td>商品价格与实际价格的倍数</td>
    <td><input type="text" name="productPriceTime" value="1.5"/></td>
    <td>商品描述的Item Type</td>
    <td><input type="text" name="desItemType" value="Rivet Bracelet Series"/></td>
  </tr>
  <tr>
    <td>商品描述的默认tag标签<br/>多个标签请以逗号分隔</td>
    <td><input type="text" name="descTags" value="tag1,tag2,tag3"/></td>
    <td>所属目录id<br/>多个标签请以逗号分隔<br/>参考[商品->链接->所属目录]</td>
    <td><input type="text" name="categorys" value=""/></td>
  </tr>
  <tr>
    <td>商品描述style</td>
    <td><input type="text" name="descStyle" value="Trendy"/></td>
    <td>商品描述BraceletType</td>
    <td><input type="text" name="braceletType" value="Strand Bracelet"/></td>
  </tr>
</table>
其他产品描述：<br/>
<div><textarea name="otherHtml" id="otherHtml" style="width:100%;height:200px;">
<div class="clearfix" id="custom-description" style="color: rgb(0, 0, 0); font-size: 11.8px; word-wrap: break-word; padding: 15px 0px; width: 710px; overflow: hidden; line-height: 1.5; font-family: verdana; background-position: 0px 0px; background-repeat: repeat no-repeat;"> 
<h2 class="description" style="font-weight: 700; color: rgb(51, 51, 51); font-size: 16px; margin: 0px; padding: 0px 10px 10px; font-family: Arial;">&nbsp;</h2> 
<p style="margin: 0px; padding: 0px; word-wrap: break-word; font-family: arial;"><span style="line-height: 18px; color: rgb(112, 48, 160);"><span style="line-height: 30px; font-size: 20px;">Welcome to our great store! You &nbsp;will love here once you come as our goods and service are great!</span></span></p> 
<div style="word-wrap: break-word; font-family: arial;">&nbsp;</div>
<div style="word-wrap: break-word; font-family: arial;"><span style="line-height: 30px; font-size: 20px;">Special offer:&nbsp;</span>Kindly please give us your good Feedback &amp; Reward If you are satisfied with the item and our service. Please do not hesitate to give us a positive feedback with 5 stars. 5% discount will be offered for your new order then. Please help remind us when you place new order, thank you</div> 
<div style="word-wrap: break-word; font-family: arial;"><span style="line-height: 30px; font-size: 20px;">Return Policy:</span>&nbsp;If you feel not satisfied with the items you received from us, and the item is different from description. Please let us know. Full refund, exchange or replacement is offered. Just if in need, please return the item to us within 3 days after you receive it. Please contact us for the detailed return &amp; exchange instruction. We are not responsible for the loss due to the lack of communication.</div>
<div style="word-wrap: break-word; font-family: arial;">&nbsp;</div>
</div></textarea></div>

</form:form>

	<script id="tml" type="text/template">
		{@each errorList as error,index}
            <tr>
				<td>${index}</td>
				<td>${error.model}</td>
				<td><a href="${error.oriUrl}">${error.oriUrl}</a></td>
				<td>${error.msg}</td>
			</tr>
        {@/each}
	</script>
</body>
</html>
