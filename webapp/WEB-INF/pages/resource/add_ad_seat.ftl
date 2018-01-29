<#assign webTitle="新增广告位" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
	<link rel="stylesheet" type="text/css" href="http://ottstatic2.taiyiplus.com/css/new_main.css">
	<link rel="stylesheet" type="text/css" href="http://ottstatic2.taiyiplus.com/css/icon_fonts.css">
	
    <!-- 头部 -->
    <@model.webMenu current="资源管理" child="资源管理" />
    
    <div class="main-container">
    	<div class="clearfix">
	        <div class="main-box">
	            <div class="crumb-nav">
	                <a href="/jiucuo/list">资源管理</a>　>新增广告位
	            </div>
	            <div class="bd new-active">
	                <div class="hd mt-10"><h3>新增投放广告位</h3></div>
	                
	                <div>
						<form action="">
							媒体名称:<input type="text">
							<div id="demo3" class="citys">
				                 <p> 投放地区:
				                    <select name="province"></select>
				                    <select name="city"></select>
				                    <select name="area"></select>
				                    <select name="town"></select>
				                </p>
				            </div>
						</form>
					</div>
	            </div>
	        </div>
	    </div>
	</div>
	
	<script type="text/javascript" src="http://ottstatic2.taiyiplus.com/js/jquery-2.1.4.min.js"></script>
	<script type="text/javascript">
        		var $town = $('#demo3 select[name="town"]');
        		var townFormat = function(info){
        			$town.hide().empty();
        			if(info['code']%1e4&&info['code']<7e5){	//是否为“区”且不是港澳台地区
        				$.ajax({
        					url:'http://passer-by.com/data_location/town/'+info['code']+'.json',
        					dataType:'json',
        					success:function(town){
        						$town.show();
        						for(i in town){
        								$town.append('<option value="'+i+'">'+town[i]+'</option>');
        						}
        					}
        				});
        			}
        		};
                $('#demo3').citys({
        			province:'福建',
        			city:'厦门',
        			area:'思明',
        			onChange:function(info){
        				townFormat(info);
        			}
        		},function(api){
        			var info = api.getInfo();
        			townFormat(info);
        		});
            </script>
<@model.webend />