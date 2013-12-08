<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="root" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>义乌市琦诺饰品有限公司</title>
	
	<style type="text/css">
		#errorBody{
			color:red ;
		}
		
	</style>
	<link href="${root }/resources/plugin/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
	<link href="${root }/resources/plugin/select2/3.4.5/select2.css" rel="stylesheet">
	<link href="${root }/resources/plugin/select2/3.4.5/select2-bootstrap.css" rel="stylesheet">
	
	<script src="${root }/resources/plugin/jquery/jquery-1.9.1.js" type="text/javascript" ></script>
	<script src="${root }/resources/plugin/ckeditor/ckeditor.js" type="text/javascript" ></script>
	<script src="${root }/resources/plugin/juicer/juicer.js" type="text/javascript" ></script>
	<script src="${root }/resources/plugin/select2/3.4.5/select2.js" type="text/javascript" ></script>
	
	<script src="${root }/resources/plugin/bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript" ></script>
	<script src="${root }/resources/plugin/bootbox/4.1.0/bootbox.js" type="text/javascript"></script>
	<!--[if lt IE 8]>
    	<link href="${root }/resources/plugins/bootstrap-ie/bootstrap-ie7.css" rel="stylesheet" type="text/css" />
	<![endif]-->
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
		<script src="${root }/resources/plugins/bootstrap-ie/html5shiv.js" type="text/javascript" ></script>
		<script src="${root }/resources/plugins/bootstrap-ie/respond.min.js" type="text/javascript" ></script>
    <![endif]-->
	<script type="text/javascript">
		$(function(){
			$("#brandId,#categorys").select2();
			$(".error").hide();
			//抽取产品
			$("#save").click(function(){
				$.ajax({
					url:"${root }/product/nimibus/save"
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
<p>公司：<a href="http://www.nimibus.cn/" target="_blank">义乌市琦诺饰品有限公司</a></p>
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
<form:form method="post" modelAttribute="param" action=""  class="form-horizontal" role="form" style="width:1000px;margin:auto;">
	<form:hidden path="storeId"/><!-- 商品id -->
	<form:hidden path="languageEn"/><!-- 英文语言id -->
	<form:hidden path="languageCn"/><!-- 中文语言id -->
	
	<div class="form-group">
	    <label for="urlPath" class="col-sm-4 control-label"></label>
	    <div class="col-sm-8">
			<button type="button" name="save" id="save" class="btn btn-primary">开始抽取产品</button>
			<img id="loading" style="display:none;" src="${root }/resources/images/loading.gif">
	    </div>
		
	</div>
	<div class="form-group">
	    <label for="urlPath" class="col-sm-4 control-label">抽取产品的网址</label>
	    <div class="col-sm-8">
	      <input type="text" name="urlPath" id="urlPath" value="" class="form-control"/>
	      <p class="help-block">
	      		<label for="isDownloadImage"><input type="checkbox" value="true" id="isDownloadImage" name="isDownloadImage" checked/>抽取时下载产品图片</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     		<label for="onlyGetCurrentPage"><input type="checkbox" value="true" id="onlyGetCurrentPage" name="onlyGetCurrentPage" />只抽取当前页面的产品</label>
	      </p>
	    </div>
	</div>
	<div class="form-group">
	    <label for="urlPath" class="col-sm-4 control-label">商品所属的品牌<p class="help-block">参考[商品->链接->品牌]</p></label>
	    <div class="col-sm-8">
	      <select id="brandId" name="brandId"  class="form-control">
	      	<c:forEach items="${manuList }" var="m">
	      		<option value="${m.mid }">${m.mname }</option>
	      	</c:forEach>
	      </select>
	    </div>
	</div>
	<div class="form-group">
	    <label for="urlPath" class="col-sm-4 control-label">商品所属的目录<p class="help-block">参考[商品->链接->所属目录]</p></label>
	    <div class="col-sm-8">
	      <select id="categorys" name="categorys" class="form-control" multiple >
	      	<c:forEach items="${categoryList }" var="c">
	      		<option value="${c.cid }">${c.cname }</option>
	      	</c:forEach>
	      </select>
	    </div>
	</div>
	<div class="form-group">
	    <label for="urlPath" class="col-sm-4 control-label">opencart的image的全路径<p class="help-block">路径截止到data文件夹</p></label>
	    <div class="col-sm-8">
	      <input type="text" name="imageDataPath" value="E:\workLesson\setup\xampp\htdocs\opencart\image\data" class="form-control"/>
	    </div>
	</div>
	<div class="form-group">
	    <label for="minNumDiff" class="col-sm-4 control-label">商品起订量比实际起订量的差值</label>
	    <div class="col-sm-8">
	      <input type="text" id="minNumDiff" name="minNumDiff" value="3" class="form-control"/>
	    </div>
	</div>
	<div class="form-group">
	    <label for="productPriceTime" class="col-sm-4 control-label">商品价格与实际价格的倍数</label>
	    <div class="col-sm-8">
	      <input type="text" id="productPriceTime" name="productPriceTime" value="1.5" class="form-control"/>
	    </div>
	</div>
	<div class="form-group">
	    <label for="descTags" class="col-sm-4 control-label">商品描述的默认tag标签<p class="help-block">多个标签请以逗号分隔</p></label>
	    <div class="col-sm-8">
	      <input type="text" id="descTags" name="descTags" value="" class="form-control"/>
	    </div>
	</div>
	<div class="form-group">
	    <label for="desItemType" class="col-sm-4 control-label">商品描述的Item Type</label>
	    <div class="col-sm-8">
	      <input type="text" id="desItemType" name="desItemType" value="Rivet Bracelet Series" class="form-control"/>
	    </div>
	</div>
	<div class="row">
	     	其他产品描述
	</div>
	<div class="row">
	      <textarea name="otherHtml" id="otherHtml" class="form-control"><div class="clearfix" id="custom-description" style="color: rgb(0, 0, 0); font-size: 11.8px; word-wrap: break-word; padding: 15px 0px; width: 710px; overflow: hidden; line-height: 1.5; font-family: verdana; background-position: 0px 0px; background-repeat: repeat no-repeat;"> 
<h2 class="description" style="font-weight: 700; color: rgb(51, 51, 51); font-size: 16px; margin: 0px; padding: 0px 10px 10px; font-family: Arial;">&nbsp;</h2> 
<p style="margin: 0px; padding: 0px; word-wrap: break-word; font-family: arial;"><span style="line-height: 18px; color: rgb(112, 48, 160);"><span style="line-height: 30px; font-size: 20px;">Welcome to our great store! You &nbsp;will love here once you come as our goods and service are great!</span></span></p> 
<div style="word-wrap: break-word; font-family: arial;">&nbsp;</div>
<div style="word-wrap: break-word; font-family: arial;"><span style="line-height: 30px; font-size: 20px;">Special offer:&nbsp;</span>Kindly please give us your good Feedback &amp; Reward If you are satisfied with the item and our service. Please do not hesitate to give us a positive feedback with 5 stars. 5% discount will be offered for your new order then. Please help remind us when you place new order, thank you</div> 
<div style="word-wrap: break-word; font-family: arial;"><span style="line-height: 30px; font-size: 20px;">Return Policy:</span>&nbsp;If you feel not satisfied with the items you received from us, and the item is different from description. Please let us know. Full refund, exchange or replacement is offered. Just if in need, please return the item to us within 3 days after you receive it. Please contact us for the detailed return &amp; exchange instruction. We are not responsible for the loss due to the lack of communication.</div>
<div style="word-wrap: break-word; font-family: arial;">&nbsp;</div>
</div></textarea>
	</div>
	
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