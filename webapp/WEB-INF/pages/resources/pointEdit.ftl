<#assign webTitle="积分管理-编辑积分信息" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="积分管理" child="积分管理" />

<style type="text/css">
    .basic-info .bd .a-title {
        width: 120px;
    }

    img.demo {
        size: 50px;
    }


</style>
<div class="main-container" style="height: auto;">
    <div class="clearfix">
        <div class="main-box basic-info">
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>编辑积分信息</h3>
                </div>
                <form id="form" action="#">
                    <input type="hidden" id="id" name="id"
                           value="<#if (adpoint?exists)&&(adpoint.id?exists)>${adpoint.id}</#if>"/>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>积分名称：</td>
                            <td>
                                <input type="text" value="<#if (adpoint?exists)>${adpoint.name!""}</#if>"
                                       style="width: 130px;" id="name" name="name" autocomplete="off"
                                       class="form-control">
                                <span id="nameTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>积分值：</td>
                            <td>
                                <input type="text" <#-- disabled --> value="<#if (adpoint?exists)>${adpoint.point!""}</#if>"
                                       style="width: 130px;" id="point" name="point" autocomplete="off"
                                       class="form-control">
                                <span id="nameTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>

                            <td rowspan="6" colspan="6">
                                <div class="col-50">
                                    <button class="btn btn-red" id="submit">保存</button>
                                    <button class="btn btn-primary ml-20" id="back">返回</button>
                                </div>
                            </td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>

<script type="text/javascript"
        src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link
        href="${model.static_domain}/js/select/jquery.searchableSelect.css"
        rel="stylesheet">
<script
        src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css"
      rel="stylesheet">
<script type="text/javascript"
        src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript"
        src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script type="text/javascript">
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

	
    $(function () {
    	
        $('#form').submit(function () {
            return false;
        })
		$("#back").click(function () {
        	history.back();
    	});
      
        $.formValidator.initConfig({
            validatorGroup:"2",
            submitButtonID: "submit",
            debug: false,
            submitOnce: true,
            errorFocus: false,
            onSuccess: function(){
				$.ajax({
					url : '/sysResources/save',
					type : 'POST',
					data : $('#form').serializeObject(),
					dataType : "json",
					traditional : true,
					success : function (datas) {
                        var resultRet = datas.ret;
                        if (resultRet.code == 101) {
                            layer.confirm(resultRet.resultDes, {
                                icon: 2,
                                btn: ['确定'] //按钮
                            });
                        } else {
                            layer.confirm("保存成功", {
                                icon: 1,
                                btn: ['确定'] //按钮
                            }, function () {
                                window.location = "/sysResources/pointList";
                            });
                        }
                    },
                    error: function(e) {
                        layer.confirm("服务忙，请稍后再试", {
                            icon: 5,
                            btn: ['确定'] //按钮
                        });
                    }
				});
            },
            submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
        });

    });
</script>
<@model.webend />
