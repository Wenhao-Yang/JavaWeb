//核心文件
var stock={
	color:{
		border:"#ccc",//边框颜色
		withinBorder:"#dedede",//内边框颜色
		solid:"#333",//虚线颜色
		trend:"#0000ff",//K线颜色
		fill:"rgba(154,196,248,.3)",//填充颜色
		arc:"#0453b4",	//圆心
		volume:"#999",	//成交量颜色
		red:"#dd2200",
		green:"#32a90f",
		minuteAvg:"#ffff00",//分时均线
		avg:{//日K
			_5:"#dd2200",
			_10:"#822e32",
			_20:"#f6c750",
			_30:"#155a9c"
			},
		MACD:{
			DIF:"#dd2200",
			DEA:"#155a9c"
		},
		KDJ:{
			K:"#f0f888",
			D:"#54fcfc",
			J:"#ff80ff"
		}
	},
	touch:{left:5,top:100},
	stockID:"",//当前股票ID,
	stockType:"",//股票分类
	data:null,//当前行情数据,
	isPC:false,//判断电脑还是手机
	jspath:null,//当前JS路径
	day:null,//今天日期
	current:null,//当前菜单选择
	addNum:function(num1, num2) {
		var sq1,sq2,m;
		try {sq1 = num1.toString().split(".")[1].length;}
		catch (e) {sq1 = 0;}
		try {sq2 = num2.toString().split(".")[1].length;}
		catch (e) {sq2 = 0;}
		m = Math.pow(10,Math.max(sq1, sq2));
		console.log("addNum");
		return (num1 * m + num2 * m) / m;
	},
	header:function(){
		console.log("hearder");
		var data=this.data;
		$("#name").html(data[0]);
		//(当前价-昨收价)/昨收价
		var range=(data[3]-data[2])/data[2];
		//console.log(data);
		if(data[3]==0){
			range=0;
			data[3]=data[2];
		}
		
		range=(range*100).toFixed(2);
		$("#current-price").html(data[3]+"("+range+"%)");
		if(range>0){
			$("#current-price").attr("class","red");
		}else if(range<0){
			$("#current-price").attr("class","green");
		}else{
			$("#current-price").attr("class","grey");
		}
		if(data[8]>100000000){
			$("#current-bargain").html((data[8]/100000000).toFixed(2)+"亿股");
		}else{
			$("#current-bargain").html((data[8]/10000).toFixed(2)+"万股");	
		}
		
		$("#current-time").html("时间："+data[31]);
		data=null;
	},
	ajax:function(data,c,o){
		console.log("ajax");
		var timeout=15000;
		if(data.Action=="current-hq"){timeout=3000;}
		console.log(data);
		var ajaxTimeoutTest=$.ajax({
								  method: "POST",
								  timeout : timeout, 
								  url: "usert/sinfo.action",
								  //http://jieone.com/demo/stock/data/stock.php?callback=jQuery17206472256606980518_1495438225734&Action=minute&stockID=002024&stockType=sz
								  data: {sid:stock.stockID,stype:stock.stockType},
								  dataType: "json",
								  
								}).done(function( data ) {
									//console.log(12);
									//console.log(data);
									c.call(o,true,data);
									delete data;
								}).fail(function() {
									console.log(data);
									//c.call(o,true,data);
									c.call(o,false)
								});
	},
	//行情
	hq:{
		
		time:0,
		//是否交易时间内
		isRun:function(){
			console.log("isRun");
			console.log(stock.day);
			console.log(stock.data[30]);
			if(stock.day!=stock.data[30]){
				return false;	
			}
			var date=new Date(stock.hq.time*1000);
			var t=date.getHours()+date.getMinutes()/100;
			//交易时间内 ,延迟1分钟
			if((t>=9.15&&t<11.31)||(t>=13&&t<15.01)){
				return true;	
			}
			return false;
		},
		//首次加载行情
		load:function(data){
			console.log("hq:load");
			if(data.hq!=null){
				$("#name").html(data.hq.split(",")[0]);
			}
			if(data.isHqReload){
				this.reload(function(result){
					if(result){
						stock.menu.load('minute');
						stock.menu.click();//添加点击事件
						stock.hq.run();//启动线程
					}else{
						stock.error("<div onclick='window.location=window.location'>加载失败，点击重试</div>");
					}
					
				});	
			}else{
				stock.data=data.hq.split(",");	
				//更新顶部
				stock.header();	
				
				stock.menu.load('minute');
				stock.menu.click();//添加点击事件
				stock.hq.run();//启动线程
						
			}
			
			
		},

		//更新行情
		reload:function(c,o){
			console.log("hq:reload");
			$.getScript("http://hq.sinajs.cn/list="+stock.stockType+stock.stockID).done(function(){ 
				stock.data=eval("hq_str_"+stock.stockType+stock.stockID).split(",");
				//console.log(stock.data);
				//更新顶部
				stock.header();
				
				c.call(o,true);
			}).fail(function() {
				c.call(o,false);
			});
		},
		run:function(){
			console.log("hq:run");
			var index=0;
			
			//启动线程
			window.setInterval(function(){
				console.log(stock.hq.isRun());
				if(stock.hq.isRun()){
					index++;
					stock.hq.reload(function(result){
						console.log("897243578923478583427852345");
						//console.log("result:"result);
						if(1){
							console.log($("#minute"));
							if($("#minute").length==1){
								console.log("jkdsjcjsadcjanscnkascsadcsadc");
								stock_minute1.update();
							}
						}
					});
					//一分钟
					if(index==20){
						index=0;
						if($("#minute").length==1){
							stock_minute1.reload();
						}
					}
				}else{
					$("#dot").css({left:-1000});	
				}
				stock.hq.time+=3;
			},3000);
			
		}
	},
	//错误提示
	error:function(msg,top){
		console.log("error");
		if($("#error").length==0){
			$("body").append('<div id="error"><div></div></div>');
		}
		$("#loading").hide();
		$("#error").show();
		if(top==undefined){top=0;}
		$("#error").css({top:top})
		$("#error div").html(msg);
	},
	configData:null,
	//只加载一次
	load:function(data){
		console.log("load");
		
		$.ajaxSetup({
				cache: true
		});
		$("body").append('<div id="loading"></div>');
		
		this.stockID=data.stockID;
		this.stockType=data.stockType;
		this.day=data.day;
		this.hq.time=data.time;

		this.isPC=this.script.isPC();
		
		if(this.isPC){
			$("body").css({width:800,height:500})
		}else{
			$(window).bind('resize load', function() {
				$("body").css("zoom", $(window).width() / 640);
			});
		}
		this.jspath=this.script.jspath();
		//stock.tpl.header();//创建顶部模板
		
		//stock.tpl.menu(data);//创建菜单
		stock.tpl.minute('minute');
		console.log(data);
		
		this.hq.load(data);
		delete data;
	},
	//处理菜单
	menu:{
		
		load:function(name){
			console.log("menu:load");
			$("#nav li").removeClass("current");
			$("#nav #nav-"+name).addClass("current");
			stock.current=name;
			$("#error").hide();
			$("#header ul").hide();
			if($("#"+name).length==0){
				$("#loading").show();
				stock.script.load(name);//引入JS	
			}else{
				$("#loading").hide();
				$(".item").hide();
				$("#"+name).show();
			}
			
			
		},
		//点击
		click:function(){
			console.log("menu:click");
			$("#nav li").click(function(){
				stock.menu.load($(this).attr("name"));
			})
		  }
	},
	//模板(只创建一次)
	tpl:{
		
		//顶部
		header:function(){
			console.log("tpl:header");
			var str="";
			str+='<div id="net-layout"></div><div id="net-error">网络已断开</div><div id="header"><span id="name"></span><span id="current-price"></span><span id="current-bargain"></span><span id="current-time"></span><ul></ul><span id="close"><img src="images/close.png"></span></div>';
			$("body").append(str);
			//释放内存
			delete str;
		},
		//菜单
		menu:function(data){
			console.log("tpl:menu");
			var str="";
			str+='<div id="nav"><ul>';
			for(var i=0;i<1;i++){
				str+='<li name="'+data.menu[i].name+'" id="nav-'+data.menu[i].name+'">'+data.menu[i].title+'</li>';	
			}
			str+='</ul></div>';
			$("body").append(str);
			//释放内存
			delete str;
			delete data;
		},
		//曲线模板
		min:function(name){
			console.log("tpl:min");
			if($("#"+name).length==0){
				str="";
				str+='<div class="item item-min" id="'+name+'"><div class="touch" id="touch-'+name+'">';
				
				str+='<div class="K-line"><ul class="price"> <li class="red"></li> <li class="red"></li><li class="green"></li></ul><ul class="range"><li class="red"></li><li class="red"></li><li class="green"></li></ul><canvas id="'+name+'-k" class="k"></canvas></div>';
				//str+='<div class="logo"></div>';
				
				str+='<ul class="KL-time"><li></li><li></li><li></li><li></li></ul>';
				
				str+='<div class="L-line"><ul class="volume"><li class="max-volume"></li> </ul><canvas id="'+name+'-l" class="l"></canvas></div>';
				
				str+='</div></div>';
				$("body").append(str);
				str=null;
			}
			
			name=null;
		},
		//K线模板
		k:function(name){
			console.log("tpl:k");
			if($("#"+name).length==0){
				str="";
				str+='<div class="item item-k" id="'+name+'"><div class="touch" id="touch-'+name+'">';
				
				str+='<div class="K-line"><ul class="price"> <li></li> <li></li><li></li><li></li><li></li></ul> <canvas id="'+name+'-k" class="k"></canvas></div>';
				//str+='<div class="logo"></div>';
				
				str+='<ul class="KL-time"><li></li> <li></li><li></li><li></li><li></li></ul>';
				
				str+='<div class="L-line"><ul class="volume"><li class="max-volume"></li><li></li><li></li></ul><canvas id="'+name+'-l" class="l"></canvas></div>';
				
				
				str+='<ul class="avg"></ul>';
				str+='<ul class="MACD"><li style=" color:'+stock.color.MACD.DIF+'">DIF:<span></span></li><li style="color:'+stock.color.MACD.DEA+'">DEA:<span></span></li><li style=" color:#155a9c">MACD:<span></span></li></ul>';
				
				str+='<ul class="KDJ"><li style=" color:'+stock.color.KDJ.K+'">K:<span></span></li><li style="color:'+stock.color.KDJ.D+'">D:<span></span></li><li style=" color:'+stock.color.KDJ.J+'">J:<span></span></li></ul>';
				
				str+='<div class="x"><span class="current-price"></span></div><div class="y"></div>';
		
				str+='</div>';
				
				str+='<ul id="ktool"><li class="fq" name="qfq">前复权</li><li class="current fq" name="normal">不复权</li><li class="fq" name="hfq">后复权</li><li class="current volume" name="normal">成交量</li> <li class="volume" name="MACD">MACD</li><li class="volume" name="KDJ">KDJ</li></ul>';
				str+='<div class="zoom"><span class="up"></span><span class="down"></span></div>';
				
				str+='</div>';
				$("body").append(str);
				str=null;
			}
			name=null;
		},
		minute:function(name){
			console.log("tpl:minute");
			if($("#"+name).length==0){
				var str="";
				str+='<div class="item" id="'+name+'"><div class="touch" id="touch-'+name+'">';
				
				str+='<div class="K-line"><div id="dot"></div><ul class="price"> <li></li><li></li><li></li> <li></li> <li></li></ul><ul class="range"><li></li> <li></li> <li></li> <li></li> <li></li></ul><canvas id="minute-k" class="k"></canvas></div>';
				
				//str+='<div class="logo"></div>';
				
				str+='<ul class="KL-time"><li class="one">9:30</li> <li class="two">11:30/13:00</li> <li class="three">15:00</li> </ul>';
				
				str+='<div class="L-line"><ul class="volume"><li class="max-volume"></li> </ul><canvas id="'+name+'-l" class="l"></canvas></div>';
				
				str+='</div><ul id="sell-buy"></ul></div>';
				$("body").append(str);
				str=null;
			}
			name=null;
		}
	},
	//加载JS
	script:{
		jspath:function(){
			console.log("script:jspath");
			var js="stock.js";
			var scripts = document.getElementsByTagName("script");
			var path = "";
			for (var i = 0, l = scripts.length; i < l; i++) {
				var src = scripts[i].src;
				if (src.indexOf(js) != -1) {
					var ss = src.split(js);
					path = ss[0];
					break;
				}
			}
			var href = location.href;
			href = href.split("#")[0];
			href = href.split("?")[0];
			var ss = href.split("/");
			ss.length = ss.length - 1;
			href = ss.join("/");
			if (path.indexOf("https:") == -1 && path.indexOf("http:") == -1 && path.indexOf("file:") == -1 && path.indexOf("\/") != 0) {
				path = href + "/" + path;
			}
			//console.log(path);
			return path;
		},
		isPC:function() {
			console.log("script:isPC");
			var userAgentInfo = navigator.userAgent;
			var Agents = ["Android", "iPhone",
						"SymbianOS", "Windows Phone",
						"iPad", "iPod"];
			var flag = true;
			for (var v = 0; v < Agents.length; v++) {
				if (userAgentInfo.indexOf(Agents[v]) > 0) {
					flag = false;
					break;
				}
			}
			return flag;
		},
		reload:function(name){
			console.log("script:reload");
			$("#loading").show();
			this.load(name);
		},
		//JS入口
		load:function(name){
	        console.log("script:load");
			this.include.load(name,function(){
				
				eval("stock_"+name).load(name,function(result){
					console.log("result:"+result);
					result = true;
					if(!result){
						stock.error('<span onclick="stock.script.reload(\''+name+'\')" >点1击1重新加载</span>',90);	
						return false;	
					}
					$("#error").hide();
					//创建模板
					if(name=="minute"){
						stock.tpl.minute(name);
					}else if(name=="dayK"||name=="weekK"||name=="monthK"){
						stock.tpl.k(name);
					}else{
						stock.tpl.min(name);
					}
					//画图
					console.log("name1:"+name);
					// /..canvas();
					console.log("name1:"+name);
					//eval("stock_"+name).canvas(function(){
						//$(".item").hide();
						//$("#"+name).show();
						//$("#loading").hide();//隐藏加载框
					//});
					console.log("name2:"+name);
					delete name;
				});
			});	
		
			
		},
		//加载js(只加载一次)
		include:{
			jslist:{},
			load:function(name,c,o){
				console.log("include:load");
				if(eval("this.jslist._"+name)==undefined){
					eval("this.jslist._"+name+"='"+name+"'");
					if(name=="minute"){
						console.log(stock.jspath);
					
						
					}else{
						//console.log('http://jieone.com/demo/stock/data/stock.php?Action=loadjs&name='+name);
						$.getScript('data/stock.php?Action=loadjs&name='+name).done(function() {  
							
							c.call(o);
						}).fail(function() { 
							eval("stock.script.include.jslist._"+name+"=undefined");
							stock.error('<span onclick="stock.script.reload(\''+name+'\')" >点击重新加载</span>',90);	
						});
					}
				}else{
					c.call(o);
				}
			}
		}
	}
}





var stock_minute1={
		data:null,
		datacount:242,
		p_price:0,
		avgdata:new Array(),
		istouch:false,
		name:"",
		xy:[],
		load:function(name,c,o){
			this.name=name;
			//Action:name,stockID:stock.stockID,stockType:stock.stockType
			stock.ajax({},function(result,data){
				console.log("syock_minute1:load");
				if(!result||data==""){
					c.call(o,false);
				}else{
					data=data.split("\n");
					if(data[0].split(",").length==7){
						stock_minute1.data=data;
						c.call(o,true);
					}else{
						c.call(o,false);	
					}
				}
				delete data;
			});
		},
		//画图
		canvas:function(){
			console.log("canvas");
			this.dispose();//先处理数据
			this.k.load();//加载K线图
			this.l.load();//加载K线图
			this.sellbuy();
			console.log("ay");
			//只加载一次
			if(!this.istouch){
				this.istouch=true;
				this.touch.load();
			}
			$("#loading").hide();//隐藏加载框
		},
		reload:function(){
			this.load(this.name,function(result){
				if(result){
					if(stock.current==stock_minute1.name){
						stock_minute1.canvas();
					}
				}
			})	
		},
		//更新行情
		update:function(){
			//追加行情
			stock_minute1.load('minute');
			console.log("stock_minute1.update");
			console.log(this.data.length);
			if(this.data.length>0){
				if(this.data[this.data.length-1].split(",")[0].split(":").length==3){
					this.data.pop();
				}
				this.data.push(stock.data[31]+","+stock.data[3]+",-1");
			}
				
			if(stock.current==this.name){
				$("#dot").animate({opacity:0.1},300,function(){
					stock_minute1.canvas();
					$("#dot").animate({opacity:1},300);	
				})
			}
		},
		k:{
			width:1020,
			height:300,
			define:{max:0,min:0,avg:0,differ:0,PRE:0},//自定义变量(最高价格)
			
			id:null,
			load:function(){
				var data=stock_minute1.data;
				var $this=null,index=0,price=0;
				if(data==null){return false;}
				this.id=stock_minute1.name+"-k";
				
				var cans=document.getElementById(this.id).getContext('2d');
				
				$("#"+this.id).attr("width",this.width);
				$("#"+this.id).attr("height",this.height);
				
				cans.beginPath();
				  
				cans.translate(0.5,0.5);
				cans.lineWidth = 1;  
	
				cans.strokeStyle=stock.color.border;
				
				//画框
				cans.strokeRect(0, 0, this.width-1,this.height-1);
				
				
				cans.strokeStyle=stock.color.withinBorder;
				//内框线(11:30-13:30 相差2个像素)
				cans.moveTo(this.width/2-2,0);
				cans.lineTo(this.width/2-2,this.height);

				index=0;
				var style="grey";
				//横线
				for(var i=1;i<=12;i++){
					if(i%4==2){
						cans.moveTo(0,this.height/12*i);
						cans.lineTo(this.width,this.height/12*i);
						
						//当前价格
						price=stock.addNum(this.define.max,-this.define.differ/12*i);
						if(price>this.define.PRE){
							style="red";
						}else if(price<this.define.PRE){
							style="green";
						}
						//价格
						$this=$("#"+stock_minute1.name).find(".price li").eq(index);
						$this.css({top:this.height/12*i/2});
						$this.attr("class","");
						$this.addClass(style);
						$this.html(price.toFixed(2));
						
						//涨幅
						$this=$("#"+stock_minute1.name).find(".range li").eq(index);
						$this.css({top:parseInt(this.height/12*i/2)});
						$this.attr("class","");
						$this.addClass(style);
						$this.html(((price-this.define.PRE)/(this.define.PRE)*100).toFixed(2)+"%");
						
						
						index++;
					}
				}

				cans.stroke();
				cans.closePath();
				
				
				//虚线
				cans.beginPath();
				cans.strokeStyle=stock.color.solid;
				var x=0;
				var y=0;
				for(var i=0;i<this.width/12;i++){
					y=this.getY(this.define.PRE);
					cans.moveTo(x,y);
					x+=6;
					cans.lineTo(x,y);
					x+=6;
				}
				cans.stroke();
				cans.closePath();
				
				stock_minute1.xy=[];
				
				
				
				//曲线
				cans.beginPath();
				cans.lineWidth = 2; 
				cans.strokeStyle=stock.color.trend;
				var _data=data[0].split(",");
				var x=0;
				var y=this.getY(_data[1]);
				cans.moveTo(x,y);
				stock_minute1.xy.push({x:x,y:y});
				
				//从1开始
				for(var i=1;i<data.length;i++){
					var _data=data[i];
					if(_data!=""){
						_data=_data.split(",");
					}	
					x+=this.width/(stock_minute1.datacount-1);
					y=this.getY(_data[1]);
					cans.lineTo(x,y);
					stock_minute1.xy.push({x:x,y:y});
				}
				if(stock.hq.isRun()){
					$("#dot").css({left:x/2,top:y/2});
				}else{
					$("#dot").css({left:-1000});	
				}
				cans.stroke();
				
				//背景
				cans.lineTo(x, this.height);
				cans.lineTo(0, this.height);
				   
				cans.fillStyle=stock.color.fill;
				cans.fill();
				
				cans.closePath();
				
				cans.globalCompositeOperation="destination-over";
				
				//平均线
				
				data=stock_minute1.avgdata;
				
				cans.beginPath();
				cans.lineWidth = 2;  
				cans.strokeStyle=stock.color.minuteAvg;
				cans.moveTo(0,this.getY(data[0]));
				
				for(var i=1;i<data.length;i++){
					x=stock_minute1.xy[i].x;
					y=this.getY(data[i]);
					cans.lineTo(x,y);
				}
				
				cans.stroke();
				cans.closePath();
				
				
				cans.save();
				
				//释放内存
				$this=null,index=null,price=null;
				delete cans;
				delete data;
			},
			//Y坐标
			getY:function(price){
				return (this.define.max-price)/this.define.differ*this.height
			}
		},
		//L线
		l:{
			width:0,
			height:0,	
			define:{max:0,allsize:0},//自定义变量(成交量)
			id:null,
			load:function(){
				var data=stock_minute1.data;
				var $this=null,index=0,price=0;
				if(data==null){return false;}
				this.id=stock_minute1.name+"-l";
				
				var cans=document.getElementById(this.id).getContext('2d');
				
				this.width=parseInt($("#"+this.id).width())*2;
				this.height=parseInt($("#"+this.id).height())*2;

				$("#"+this.id).attr("width",this.width);
				$("#"+this.id).attr("height",this.height);
				
				cans.beginPath();
				  
				cans.translate(0.5,0.5);
				cans.lineWidth = 1;  
	
				cans.strokeStyle=stock.color.border;
				
				//画框
				cans.strokeRect(0, 0, this.width-1,this.height-1);
				
				
				cans.stroke();
				cans.closePath();
				
				
				
				cans.beginPath();
				cans.fillStyle =stock.color.volume;
				
				cans.linewidth=0;
				var x=0;
				var y=0;
				var w=this.width/stock_minute1.datacount*0.6;
				var h=0;
				
				if(this.define.max*1>100000000){
					$("#"+stock_minute1.name).find(".max-volume").html((this.define.max/100000000).toFixed(2)+"亿手");
				}else{
					$("#"+stock_minute1.name).find(".max-volume").html((this.define.max/1000000).toFixed(2)+"万手");
				}
				
				
				//最大成交量
				for(var i=0;i<data.length;i++){
					var _data=data[i];
					if(_data!=""){
						_data=_data.split(",");
					}
					h=(_data[2]/this.define.max)*this.height*0.98;
					if(h!=0){
						y=this.height-h;
						cans.fillRect(x,y,w,h);
					}
					x+=this.width/(stock_minute1.datacount);
				}
				
				
				cans.closePath();
				
				cans=null;
				delete data;
			}
		},
		//处理数据
		dispose:function(){
			console.log("dispose");
			var data=this.data,_data,define;
			
			var PRE=stock.data[2]*1;//昨日收盘价
			var limit_up=(PRE*1.1).toFixed(2);//涨停
			var limit_down=(PRE*0.9).toFixed(2);//跌停
			
			var price_max=0,price_min=100000000,volume_max=0;
			var length=data.length;
			
			this.avgdata=new Array();
			var sumPrice=new Array(),_sumPrice=0;
			var sumVolume=new Array(),_sumVolume=0;
			
			for(var i=0;i<length;i++){
				_data=data[i].split(",");
				if(parseFloat(_data[1])>price_max){price_max=_data[1];}
				if(parseFloat(_data[1])<price_min){price_min=_data[1];}
				if(parseFloat(_data[2])>volume_max){volume_max=_data[2];}
				
				if(i!=0){_sumPrice=sumPrice[i-1];_sumVolume=sumVolume[i-1];}
				if(_data[2]!=-1){
					sumPrice[i]=(_data[3]*1+_sumPrice*1);
					sumVolume[i]=(_data[2]*1+_sumVolume*1);
					
					this.avgdata.push((sumPrice[i]/sumVolume[i]).toFixed(2));
				}
			}
			
			if(price_max==price_min){
				
				//涨
				if(price_max>=PRE){
					price_max=limit_up;
					price_min=PRE;
				}else{
					price_max=PRE;
					price_min=limit_down;
				}
			}else if(price_max<PRE){
				price_max=PRE;
			}else if(price_min>PRE){
				price_min=PRE;
			}

			
			//开盘价为0，停牌
			if(stock.data[1]==0){
				price_max=limit_up;
				price_min=limit_down;
			}
			
			price_max*=1;
			price_min*=1;

			price_max+=(price_max-price_min)/11;
			price_min-=(price_max-price_min)/11;
			
			define={
				max:price_max,
				min:price_min,
				//改
				avg:(stock.addNum(price_max,price_min)/2),	
				differ:(price_max-price_min),
				
				PRE:stock.data[2]
			}
			//赋值
			stock_minute1.k.define=define;
			 
			define={
				max:volume_max
			}
			stock_minute1.l.define=define;
			
			//释放内存
			delete data;
			delete sum;
			_data=null;
			length=null;
			define=null;
			
			price_max=null,price_min=null,volume_max=null;
	},
	//设置买卖数据
	sellbuy:function(){
		var data=stock.data;
		//卖
		console.log(stock_minute1.p_price);
		var p_price = stock_minute1.p_price;
		var str="";
		str+='<li><span class="name1"  style="font-size:25px"><B>'+data[0]+'</B></span></li><br /><br />';
		str+='<li><span class="time1"  style="font-size:20px"><B>'+data[30]+'  '+data[31]+'</B></span></li>';
		var roit = (data[3]-p_price)/p_price;
		if(p_price>data[3])
		{
			str+='<li><span class="nowpri1"  style="font-size:20px"><B>'+data[3]+'       '+roit.toFixed(2)+"%"+'↓</B></span></li>';//g
		}else if(p_price<data[3]){
			str+='<li><span class="nowpri"  style="font-size:20px"><B>'+data[3]+'       '+roit.toFixed(2)+"%"+'↑</B></span></li>';//r
		}else if(p_price=data[3]){
			str+='<li><span class="nowpri2"  style="font-size:20px"><B>'+data[3]+'       '+roit.toFixed(2)+"%"+'</B></span></li>';//r
		}
		str+='<li class="hr"></li>';
		for(var i=1;i<5;i++){
			var size=parseInt( data[18+i*2]/100);
			var price=data[19+2*i];
			var style="red";
			if(price==data[2]){
				style="grey";
			}else if(price<data[2]){
				style="green";	
			}
			if(price==0){
				price="&nbsp;&nbsp;---";
			}
			str+='<li><span class="name" style="font-size:15px">卖'+i+'</span><span class="price '+style+'">'+price+'</span><span class="size">'+size+'</span></li>';
		}
		str+='<li class="hr"></li>';
		for(var i=1;i<=5;i++){	
			var size=parseInt( data[8+i*2]/100);
			var style="red";
			var price=data[9+2*i];
			if(price==data[2]){
				style="grey";
			}else if(price<data[2]){
				style="green";	
			}
			if(price==0){
				price="&nbsp;&nbsp;---";
			}
			str+='<li><span class="name" style="font-size:15px">买'+i+'</span><span class="price '+style+'">'+price+'</span><span class="size">'+size+'</span></li>';
		}
		str+='<li class="hr"></li>';
		str+='<li><span class="name"  style="font-size:15px"><B>'+"交易记录"+'</B></span></li>';
		stock_minute1.p_price = data[3];
		$("#sell-buy").html(str);
					
	},
	//监听事件
	touch:{
		map:null,
		spirit:null,
		load:function(){

			var touchID=$("#"+stock_minute1.name).find(".touch").attr("id");//当前ID
			this.map=document.getElementById(touchID);
			
			if(stock.isPC){
				$("#"+touchID).mousedown(function(e){
					stock_minute1.touch.touchStart(e.clientX,e.clientY);
					
					document.onmousemove = function(e) {
						if (!stock_minute1.touch.spirit) return;
						stock_minute1.touch.touchMove(e.clientX,e.clientY);	
					}
					document.onmouseup = function () {
						if (!stock_minute1.touch.spirit) return;
						stock_minute1.touch.touchEnd();	
					}
				});
			}else{
			
				function touchStart(event) {
					//阻止网页默认动作（即网页滚动）
					event.preventDefault();	
					if (stock_minute1.touch.spirit || !event.touches.length) return;
					var touch = event.touches[0];
					stock_minute1.touch.touchStart(touch.pageX,touch.pageY);
				}
				function touchMove(event) {
					event.preventDefault();
					if (!stock_minute1.touch.spirit || !event.touches.length) return;
					var touch = event.touches[0];
					stock_minute1.touch.touchMove(touch.pageX,touch.pageY);
				}
				function touchEnd(event) {
					if (!stock_minute1.touch.spirit) return;
					stock_minute1.touch.touchEnd();	
				}
				this.map.addEventListener("touchstart", touchStart, false);
				this.map.addEventListener("touchmove", touchMove, false);
				this.map.addEventListener("touchend", touchEnd, false);
					 
			}
		},
		//cls等于1点击时
		handle:function(x,y){
			
			var index=Math.round(stock_minute1.datacount/stock_minute1.k.width*x*2);
			var data=stock_minute1.data[index];

			if(data==undefined){
				if($("#"+stock_minute1.name).find(".x").css("top")=="-1000px"){
					$("#header ul").hide();
					return false;
				}
				index=stock_minute1.xy.length-1;
				data=stock_minute1.data[index];
			}
			$("#header ul").show();
			
			data=data.split(",");
			
			y=stock_minute1.xy[index].y/2;
			
			$("#"+stock_minute1.name).find(".x").css({top:y});
			
			$("#"+stock_minute1.name).find(".x").find(".current-price").html(data[1]);
			
			$("#"+stock_minute1.name).find(".x").find(".current-range").html(((data[1]-stock_minute1.k.define.PRE)/(stock_minute1.k.define.PRE)*100).toFixed(2)+"%");
			
			$("#"+stock_minute1.name).find(".y").css({left:parseInt(stock_minute1.xy[index].x/2)});
			
			if(data[2]==-1){
				var volume="--";
			}else{
				var volume=parseInt(data[2]/100);
			}
		
			var str="";
			str+='<li>当前价：'+data[1]+'</li>';
			str+='<li>成交量：'+volume+'</li>';
			str+='<li>时间：'+data[0]+'</li>';

			$("#header ul").html(str);
			
		
			data=null;
		},
		touchStart:function(x,y){
			x-=stock.touch.left;
			y-=stock.touch.top;
			
			this.spirit = document.createElement("div");	
			this.map.appendChild(this.spirit);
			this.spirit.className = "xy";
			$("#"+stock_minute1.name).find(".xy").html('<div class="x"><span class="current-price"></span><span class="current-range"></span></div><div class="y"></div>');
			this.handle(x,y);
		},
		touchMove:function(x,y){
			x-=stock.touch.left;
			y-=stock.touch.top;
			
			if(x<0){x=0;}
			if(y<0){y=0;}
			
			this.handle(x,y);
		},
		touchEnd:function(){
			this.map.removeChild(this.spirit);
			$("#header ul").hide();
			this.spirit = null;
		}
	}
}

