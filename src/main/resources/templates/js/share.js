
//微信分享
$(function(){
		$(".share-btn").click(function(){
			$(".weixin-share").show();
			});
	})

//取消分享
function cancle_share_weixin(obj){
	$(obj).css('display','none');
}

//取消关注弹出层
function cancle_add_focus(obj){
	$(obj).css('display','none');
}

/*微信分享*/
		var imgUrl = 'images/share_icon.jpg';   //显示在微信里的缩略图
		var lineLink = 'http://172.25.5.143:8020/weixin/index.html?__hbt=1499148502885';  //要分享的页面URL
		var appid = '';
		var descContent = "";   //内容简介
		var shareTitle = "盛大开业 特大酬宾--壹万店o2o长沙体验厅";   //页面标题
		
		function shareFriend() {
			WeixinJSBridge.invoke('sendAppMessage',{
				"appid": appid,
				"img_url": imgUrl,
				"img_width": "200",
				"img_height": "262",
				"link": lineLink,
				"desc": descContent,
				"title": shareTitle
			}, function(res) {
				//_report('send_msg', res.err_msg);
			})
		}
		function shareTimeline() {
			WeixinJSBridge.invoke('shareTimeline',{
				"img_url": imgUrl,
				"img_width": "200",
				"img_height": "262",
				"link": lineLink,
				"desc": descContent,
				"title": shareTitle
			}, function(res) {
				   //_report('timeline', res.err_msg);
			});
		}
		function shareWeibo() {
			WeixinJSBridge.invoke('shareWeibo',{
				"content": descContent,
				"url": lineLink,
			}, function(res) {
				//_report('weibo', res.err_msg);
			});
		}
		// 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
			// 发送给好友
			WeixinJSBridge.on('menu:share:appmessage', function(argv){
				shareFriend();
			});
			// 分享到朋友圈
			WeixinJSBridge.on('menu:share:timeline', function(argv){
				shareTimeline();
			});
			// 分享到微博
			WeixinJSBridge.on('menu:share:weibo', function(argv){
				shareWeibo();
			});
		}, false);

//window.onload=function ()
//{
//	window.setInterval(render, 1000 / 30);	
//}
//
//function render()
//{	
//	var box_h=document.getElementById("share-box").offsetHeight;
//	mf_fb_h=box_h-"134"+"px"
//	document.getElementById("img-box").style.height=mf_fb_h;			 
//	img_w=document.getElementById("my-img").width;
//	mf_fb_w=img_w/2;
//	document.getElementById("img-box").width=img_w;
//	bj_left="-"+mf_fb_w+"px";
//	document.getElementById("img-box").style.marginLeft=bj_left;	
//	
//}