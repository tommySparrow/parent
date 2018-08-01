<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-edit</title>
<script>
	function uploadPic() {
		//完成用户选择文件的上传

		var options = {
			url : "/uploadFile.do",
			type : "post",
			dataType : "json",
			success : function(data) {
				//alert(data.path);
				$("#allUrl").attr("src",data.path);
				$("#imgUrl").val(data.path);
			}
		};

		//异步提交表单
		$("#jvForm").ajaxSubmit(options);

	}
</script>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 品牌管理 - 修改</div>
		<form class="ropt">
			<input type="submit" onclick="this.form.action='v_list.shtml';"
				value="返回列表" class="return-button" />
		</form>
		<div class="clear"></div>
	</div>
	<div class="body-box" style="float: right">
		<form id="jvForm" action="doEdit.do" method="post">
			<input type="hidden" value="${brand.id}" name="id" />
			<table cellspacing="1" cellpadding="2" width="100%" border="0"
				class="pn-ftable">
				<tbody>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h"><span
							class="pn-frequired">*</span> 品牌名称:</td>
						<td width="80%" class="pn-fcontent"><input type="text"
							value="${brand.name}" class="required" name="name"
							maxlength="100" /></td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h"><span
							class="pn-frequired">*</span> 上传商品图片(90x150尺寸):</td>
						<td width="80%" class="pn-fcontent">注:该尺寸图片必须为90x150。</td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h"></td>
						<td width="80%" class="pn-fcontent"><img width="100"
							height="100" id="allUrl" src="${brand.imgUrl}" /> <input
							type="hidden" value="${brand.imgUrl}" name="imgUrl" id="imgUrl" /> <input type="file"
							name="mpf" onchange="uploadPic()" /></td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h">品牌描述:</td>
						<td width="80%" class="pn-fcontent"><input type="text"
							value="${brand.description}" class="required" name="description"
							maxlength="80" size="60" /></td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h">排序:</td>
						<td width="80%" class="pn-fcontent"><input type="text"
							value="${brand.sort}" class="required" name="sort" maxlength="80" />
						</td>
					</tr>
					<tr>
						<td width="20%" class="pn-flabel pn-flabel-h">是否可用:</td>
						<td width="80%" class="pn-fcontent"><input type="radio"
							name="isDisplay" value="1" />可用 <input type="radio"
							name="isDisplay" value="0" />不可用</td>
						<script>
							$("input[name='isDisplay']").val(
									[ '${brand.isDisplay}' ]);
						</script>
					</tr>
				</tbody>
				<tbody>
					<tr>
						<td class="pn-fbutton" colspan="2"><input type="submit"
							class="submit" value="提交" /> &nbsp; <input type="reset"
							class="reset" value="重置" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>