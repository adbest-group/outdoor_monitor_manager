<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<meta charset="utf-8">
<style type="text/css">
    html, body{ min-width: 100%; }
    .activity{margin: 20px}

    .activity-aggregate{ border: 1px solid #ddd; border-radius: 5px;font-size: 14px;}
    .activity-aggregate .choose-area-line{ width: 80%;height: 1px;margin-left: 10%;border-bottom: 1px solid #ddd}
    .basic-info li{width: auto;margin-right: 20px}
    .activity .basic-info,.activity .basic-info .bd{padding: 0}
    .basic-info .bd .a-title{ width: 130px; }
    .basic-info .bd td{padding: 5px; font-size: 14px;}
    .role-nav-authority { padding: 5px 0;}
    .role-nav-authority li{ float: left; overflow: hidden; height: 25px; color: #333;width: 80px}
    .role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 3px 3px 0 0; vertical-align: text-top;}
    .activity-but {text-align: center}
    .activity-but button{padding:5px  30px;}

    #citypicker .hd{ display: none; }
    #citypicker li.city-item label{ font-size: 14px; }
    .formZone{ margin-left: 0; display: block; border:0;}
    #citypicker > div > ul{ width: 80%; }
    .formZone li.city-item input{ margin: 0 3px 0 0; }
    .formZone li.city-item.on > div{ padding-top: 0; }
    .formZone li.city-item .child{ width: 210px; }
    .formZone li.city-item li{ width: auto; padding: 0 0 6px 2px;}
	.formZone li.city-item span{padding: 0px 2px}
    .fix:before,
    .fix:after {  display: table;  content: '';  }
    .fix:after {  clear: both;  }
    .fix {  *zoom: 1;  }

    .choose-area{}
    /*.choose-area{padding: 10px 0px 10px 100px}*/
    .city {  margin: 0;  padding: 0;  list-style: none;  font-size: 14px;  line-height: 20px;   }
    .city-item {  position: relative;  float: left;  margin: 2px 5px; width:140px;   height: 20px;  border: 1px solid transparent;  }
    .city-item input[type="checkbox"] {  vertical-align: middle;  }
    .city-item .stat {  position: absolute;  top: 2px;  right: 8px;  padding: 0 4px;  color: #fff;  font-size: 12px;  line-height: 18px;  background-color: #67CFFF;  border-radius: 4px;  display:none;  }
    .city-item.hover {  border-color: #ddd;  }
    .child {  display: none;  position: absolute;  top: -1px;  left: 100px;  z-index: 999;  width: 180px;  border: 1px solid #ddd;  background-color: #fff;  }
    .child-item {  float: left;height: 20px;  white-space: nowrap;  text-overflow: ellipsis;  overflow:hidden;  }
    .city-item.hover .child {  display: block;  }
    .city-item.on .child{ display: block;}
</style>

<div class="activity-aggregate activity">
    <div class="clearfix">
        <a class="ct-area">热门城市</a>
        <ul class="role-nav-authority" id="checkBoxOTT">
             <li><label><input type="checkbox" name="role" value="11">北京</label></li>
            <li><label><input type="checkbox" name="role" value="31">上海</label></li>
            <li><label><input type="checkbox" name="role" value="3301">杭州</label></li>
            <li><label><input type="checkbox" name="role" value="4403">深圳</label></li>
            <li><label><input type="checkbox" name="role" value="3201">南京</label></li>
            <li><label><input type="checkbox" name="role" value="4401">广州</label></li>
            <li><label><input type="checkbox" name="role" value="3502">厦门</label></li>
        </ul>
    </div>
    <div class="clearfix choose-area formZone">
        <ul class="M-cBody" id="citypicker"></ul>
        <input type="hidden" id="city" value='{}'>
    </div>
</div>

<div class="activity-but">
    <button type="button" class="btn btn-red" id="activitySubmit">确&nbsp;定</button>
</div>

<div class="hold-bottom" style="height:130px"></div>


<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>

<script type="text/javascript" src="${model.static_domain}/js/jquery.unitDate.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/jquery.unitDire.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/jquery.citypicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/jquery.changecity.js?v=1.0"></script>

<script>

    $(function(){
    
    
     var codeArr =parent.$('#area_code').val().split(',');
      var codeProvArr =parent.$('#dmp_area_prov').val().split(',');
      var allCode = codeArr.concat(codeProvArr);
        $('input:checkbox').each(function(){
            if(allCode.indexOf( $(this).val())!=-1){
                $(this).attr('checked','checked');
            }
        });
        
      //点击
        $('#checkBoxOTT li input').click(function(e){
            e.stopPropagation();
            var code = $(this).val();
            var flag = $(this).prop('checked');
            var citypicker = $('#citypicker');
            var city = citypicker.find('input[type="checkbox"][name="childId"][value="'+ code +'"]');
            city.prop('checked', flag);
            var provAll = city.closest('.city-item');
            var falgProv =provAll.find('input[type="checkbox"][name="childId"]:checked').length!=0
            provAll.find('input[type="checkbox"][name="cityId"]').prop('checked', falgProv);
        });

        //点击 市
        $('input[type="checkbox"][name="childId"]').click(function(e){
            var hotCode=['11','31','3301','4403','3201','4401','3502'];
            var code = $(this).val();
            var flag = $(this).prop('checked');
            if(hotCode.indexOf(code)!=-1){
                var city = $('input[type="checkbox"][name="role"][value="'+ code +'"]');
                city.prop('checked', flag);
            }
        });

        //点击 省
        $('input[type="checkbox"][name="cityId"]').click(function(e){
            var hotCode=['11','31','3301','4403','3201','4401','3502'];
            var flagProv = $(this).prop('checked');
            $(this).closest('.city-item').find('input[type="checkbox"][name="childId"]').each(function(){
                var code = $(this).val();
                if(hotCode.indexOf(code)!=-1){
                    var city = $('input[type="checkbox"][name="role"][value="'+ code +'"]');
                    city.prop('checked', flagProv);
                }
            });
        });
        
        
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引

        $('#activitySubmit').on('click', function(){
            var val=[],checkedCode=[],checkedCodeProv=[];

            //筛选出 全国地图的选中 选项
            $('input[type="checkbox"][name="childId"]:checked').each(function(){
                checkedCode.push($(this).val());
                val.push($(this).parent().text());
            });
            
             //筛选出 全国地图的选中 省份
            $('input[type="checkbox"][name="cityId"]:checked').each(function(){
                checkedCodeProv.push($(this).val());
            })
            

            //添加 热门城市中未重复的选项
            $('input[type="checkbox"][name="role"]:checked').each(function(){
                if(checkedCode.indexOf($(this).val())==-1){
                    checkedCode.push($(this).val())
                    val.push($(this).parent().text());
                }
            })

            var valStr='';
            if(val.join(',').length<=15)valStr=val.join(',');
            else valStr=val.join(',').slice(0,15)+"..."
            parent.$('#area_val').val(valStr);
            parent.$('#area_code').val(checkedCode.join(','));
             parent.$('#dmp_area_prov').val(checkedCodeProv.join(','));
            parent.layer.close(index);
        });
    });

</script>
