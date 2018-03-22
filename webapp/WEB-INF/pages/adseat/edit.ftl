<#assign webTitle="资源管理-编辑广告位" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="广告位管理" />
<link rel="stylesheet" type="text/css"
      href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css"
      href="${model.static_domain}/css/icon_fonts.css">

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
                    <h3>编辑广告位</h3>
                </div>
                <form id="form" action="#">
                    <input type="hidden" id="id" name="id"
                           value="<#if (adSeatInfo?exists)&&(adSeatInfo.id?exists)>${adSeatInfo.id}</#if>"/>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位名称：</td>
                            <td>
                                <input type="text" disabled value="<#if (adSeatInfo?exists)>${adSeatInfo.name!""}</#if>"
                                       style="width: 130px;" id="name" name="name" autocomplete="off"
                                       class="form-control">
                                <span id="nameTip"></span>
                            </td>
                        </tr>

                        <#--<tr>-->
                        <#--<td class="a-title"><font class="s-red">*</font>广告位编号：</td>-->
                        <#--<td>-->
                        <#--<input type="text" style="width: 130px;" id="adCode" value="<#if (adSeatInfo?exists)>${adSeatInfo.adCode!""}</#if>" name="adCode" autocomplete="off" class="form-control">-->
                        <#--<span id="adCodeTip"></span>-->
                        <#--</td>-->
                        <#--</tr>-->

                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位置：</td>
                            <td><input type="text" disabled style="width: 130px;" id="location" name="location"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.location!""}</#if>"
                                       autocomplete="off" class="form-control">
                                <span id="locationTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位类型：</td>
                            <td>
                                <div class="select-box select-box-100 un-inp-select ll">
                                    <select disabled class="searchable-select-holder" name="adSeatType">
                                    <@model.showAllAdSeatTypeOps value="" />
                                    </select>
                                </div>
                                <span id="adSeatTypeTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>所在地区：</td>
                            <td>
                                <div id="demo3" class="citys">
                                    <p>
                                        <select disabled class="searchable-select-holder" id="adSeatInfo-province"
                                                name="province" style="width: 147px"></select>
                                        <select disabled class="searchable-select-holder" id="adSeatInfo-city"
                                                name="city"></select>
                                        <select disabled class="searchable-select-holder" id="adSeatInfo-region"
                                                name="region"></select>
                                        <select disabled class="searchable-select-holder" id="adSeatInfo-street"
                                                name="street"></select>
                                    </p>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位尺寸：</td>
                            <td>
                                <input type="text" disabled style="width: 60px;"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.width!""}</#if>" id="width"
                                       name="width" autocomplete="off" class="form-control">
                                *
                                <input type="text" disabled style="width: 60px;"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.height!""}</#if>" id="height"
                                       name="height" autocomplete="off" class="form-control">

                                <span id="widthTip"></span>
                                <span id="heightTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位经度：</td>
                            <td>
                                <input type="text" disabled style="width: 130px;" id="lon"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.lon!""}</#if>" name="lon"
                                       autocomplete="off" class="form-control">

                                <span id="lonTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位纬度：</td>
                            <td>
                                <input type="text" disabled style="width: 130px;" id="lat"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.lat!""}</#if>" name="lat"
                                       autocomplete="off" class="form-control">

                                <span id="latTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>日均PV：</td>
                            <td>
                                <input type="text" style="width: 130px;"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.pv!""}</#if>" id="pv" name="pv"
                                       autocomplete="off" class="form-control">
                                <span id="pvTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>私家车日均PV：</td>
                            <td>
                                <input type="text" style="width: 130px;" id="privateCarPv"
                                       <#if (adSeatInfo?exists)>value="${adSeatInfo.privateCarPv!""}</#if>"
                                       name="privateCarPv" autocomplete="off" class="form-control">
                                <span id="privateCarPvTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>公交车日均PV：</td>
                            <td>
                                <input type="text" style="width: 130px;" id="busPv"
                                       value="<#if (adSeatInfo?exists)>${adSeatInfo.busPv!""}</#if>" name="busPv"
                                       autocomplete="off" class="form-control">
                                <span id="busPvTip"></span>
                            </td>
                        </tr>

                        <tr>
                            <td class="a-title"><font class="s-red">*</font>人群选择：</td>
                            <td>男</td>
                        </tr>
                        <#list agePartValues as part>
                            <#assign id = crowdTable["2"][part.id?string].get(0)!0 />
                            <#assign v = crowdTable["1"][part.id?string].get(1)!"0" />
                            <tr>
                                <td class="a-title"><font class="s-red">*</font>${part.text}：</td>
                                <td>
                                    <#if (id?number>0) >
                                        <input name="crowd_1_${part.id}_name" type="hidden" value="${id}"/>
                                    </#if>
                                    <input type="range" name="crowd_1_${part.id}_value" value="${v}" max="1000" style="width:300px;" /> <span>${(v?number/1000)?string("#0.#%")}</span>
                                </td>
                            </tr>
                        </#list>

                        <tr>
                            <td class="a-title">&nbsp;</td>
                            <td>女</td>
                        </tr>
                        <#list agePartValues as part>
                        <#assign id = crowdTable["2"][part.id?string].get(0)!0 />
                        <#assign v = crowdTable["2"][part.id?string].get(1)!0 />
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>${part.text}：</td>
                            <td>
                                <#if (id?number>0) >
                                    <input name="crowd_2_${part.id}_name" type="hidden" value="${id}"/>
                                </#if>
                                <input type="range" name="crowd_2_${part.id}_value" value="${v}" max="1000" style="width:300px;" /> <span>${(v?number/1000)?string("#0.#%")}</span>
                            </td>
                        </tr>
                        </#list>

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
<!-- 图片缩放 -->
<script type="text/javascript"
        src="${model.static_domain}/js/jquery.resize.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>

<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
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
        $('.select').searchableSelect();
        $('#form').submit(function () {
            return false;
        })

        $("#back").click(function () {
            history.back();
        });

        rangeTotal = function () {
            var total = 0;
            $("input[type='range']").each(function (i, n) {
                total += n.value * 1;
            });
            return total;
        }

        $("input[type='range']").on("input", function () {
            var value = $(this).val()*1;
            var total = rangeTotal();
            if (total > 1000) {
                this.value = 1000 - total + value;
            }
            value = this.value/10.0;
            if (value > 0) {
                $(this).next("span").html(value + "%");
            } else {
                $(this).next("span").html("0%");
            }
        });

        $.formValidator.initConfig({
            validatorGroup: "2",
            submitButtonID: "submit",
            debug: false,
            submitOnce: false,
            errorFocus: false,
            onSuccess: function () {
                if(rangeTotal()<1000){
                    layer.alert("人群比例总和不足100%", {
                        icon: 2
                    });
                    return;
                }
                $.ajax({
                    url: '/adseat/save',
                    type: 'POST',
                    data: $('#form').serializeObject(),
                    dataType: "json",
                    traditional: true,
                    success: function (datas) {
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
                                window.location = "/adseat/list";
                            });
                        }
                    },
                    error: function (e) {
                        layer.confirm("服务忙，请稍后再试", {
                            icon: 5,
                            btn: ['确定'] //按钮
                        });
                    }
                });
            },
            submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
        });

        //表单验证
        //广告位名称
        /*$("#name").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入广告位名称，30字以内",
            onCorrect: ""
        }).inputValidator({
            min: 1,
            max: 30,
            onError: "请输入广告位名称，30字以内"
        });
        //广告位编号
//        $("#adCode").formValidator({
//            validatorGroup:"2",
//            onShow: "　",
//            onFocus: "请输入广告位编号，30字以内",
//            onCorrect: ""
//        }).inputValidator({
//            min:1,
//            max:30,
//            onError:"请输入广告位名称，30字符以内"
//        }).ajaxValidator({
//            type: "post",
//            dataType: "json",
//			data:{id:$("#id").val()},
//            async: true,
//            url: "/platmedia/adseat/isExistsCode",
//            buttons: $("#submit"),
//            success: function(result) {
//                if (result.ret.code == 100) {
//                    return true;
//                }
//                return false;
//            },
//            error: function(jqXHR, textStatus, errorThrown) {
//                layer.confirm("服务忙，请稍后再试", {
//                    icon: 5,
//                    btn: ['确定'] //按钮
//                });
//            },
//            onError: "已存在该编号，请修改",
//            onWait: "正在对编号进行校验，请稍候..."
//        }).defaultPassed();
        //广告位位置
        $("#location").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入广告位位置，30字以内",
            onCorrect: ""
        }).inputValidator({
            min: 1,
            max: 30,
            onError: "请输入广告位位置，30字以内"
        });
        //广告位宽度
        $("#width").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入宽度(cm)",
            onCorrect: ""
        }).inputValidator({
            type: "number",
            min: 1,
            max: 10000,
            onError: "宽度支持1-10000(cm)"
        });
        //广告位宽度
        $("#height").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入高度(cm)",
            onCorrect: ""
        }).inputValidator({
            type: "number",
            min: 1,
            max: 10000,
            onError: "高度支持1-10000(cm)"
        });
        //广告位经度
        $("#lon").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入经度",
            onCorrect: ""
        }).functionValidator({
            fun: function (val) {
                if ($.trim(val).length < 1)
                    return false;
                return true;
            },
            onError: "请输入经度"
        }).inputValidator({
            type: "number",
            min: -180,
            max: 180,
            onError: "经度支持 -180 ~ 180"
        });
        //广告位纬度
        $("#lat").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入纬度",
            onCorrect: ""
        }).functionValidator({
            fun: function (val) {
                if ($.trim(val).length < 1)
                    return false;
                return true;
            },
            onError: "请输入经度"
        }).inputValidator({
            type: "number",
            min: -90,
            max: 90,
            onError: "纬度支持 -90 ~ 90"
        });*/
        //广告位日均pv
        $("#pv").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入日均PV",
            onCorrect: ""
        }).regexValidator({
            regExp: "^\\d{1,6}$",
            dataType: "string",
            onError: "PV支持1-999999"
        });
        //广告位私家车日均pv
        $("#privateCarPv").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入私家车日均PV",
            onCorrect: ""
        }).regexValidator({
            regExp: "^\\d{1,6}$",
            dataType: "string",
            onError: "私家车日均PV支持1-999999"
        });
        //广告位公交车日均pv
        $("#busPv").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onFocus: "请输入公交车日均PV",
            onCorrect: ""
        }).regexValidator({
            regExp: "^\\d{1,6}$",
            dataType: "string",
            onError: "公交车日均PV支持1-999999"
        });
    });

    //	$('#submit').click(function() {
    //		$.ajax({
    //			url : '/platmedia/adseat/save',
    //			type : 'POST',
    //			data : $('#form').serializeObject(),
    //			dataType : "json",
    //			traditional : true,
    //			success : function(data) {
    //				if (data.success) {
    //					alert('修改成功!');
    //				} else {
    //					alert('修改失败!' + data.errMsg);
    //				}
    //			}
    //		});
    //	});

    function myFunction(range) {
        var x = $(range).val();
        $(range).parent().next().find("p").html(x + "%");
    }

    /* 	var x = 0;
     var newX = 0;
     function myFunction(range) {
     x = Number($(range).val())+x;
     $(range).parent().next().find("p").html(x + "%");
     ($("tr input[type='range']").not(range)).each(function(index, element) {
     newX=x;
     if (Number($(this).val()) == 100 - newX) {
     $(this).attr("disable","disable");
     } else if(Number($(this).val())<100-newX){
     $(this).attr("max",newX);
     newX=100-x;
     }
     });
     } */

    /*获取城市  */
    var $town = $('#demo3 select[name="street"]');
    var townFormat = function (info) {
        $town.hide().empty();
        if (info['code'] % 1e4 && info['code'] < 7e5) { //是否为“区”且不是港澳台地区
            $.ajax({
                url: 'http://passer-by.com/data_location/town/' + info['code']
                + '.json',
                dataType: 'json',
                success: function (town) {
                    $town.show();
                    for (i in town) {
                        $town.append('<option value="' + i + '" <#if adSeatInfo?exists&&adSeatInfo.street?exists>'+ (i ==${adSeatInfo.street} ? "selected" : "")+'</#if>>' + town[i]
                                + '</option>');
                    }
                }
            });
        }
    };
    $('#demo3').citys({
        "province": '<#if adSeatInfo?exists>${adSeatInfo.province!""}</#if>',
        "city": '<#if adSeatInfo?exists>${adSeatInfo.city!""}</#if>',
        "region": '<#if adSeatInfo?exists>${adSeatInfo.region!""}</#if>',
        onChange: function (info) {
            townFormat(info);
        }
    }, function (api) {
        var info = api.getInfo();
        townFormat(info);
    });

    $(function () {
        $(window).resize(function () {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();

    });
</script>
<@model.webend />
