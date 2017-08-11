/*var query = location.search;

var str = query.split("=");

var stra=str[1].split("&");

var code=str[2];
var state=stra[0];*/
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}
var code = GetQueryString("code");
var state = GetQueryString("state");
$(function() {
	/*$.ajax({
		type: "get", // 用POST方式传输
		dataType: "json", // 数据格式:JSON
		url: '/queueGame/getWxUser.do',
		data: "code=" + code + "&state=" + state,
		async: true,
		success: function(msg) {}
	});*/

})
var url = window.location.href;
var articleId = "";
var shareTitle = "盛大开业 特大酬宾--壹万店o2o长沙体验厅";
var shareImgUrl = url+'/images/share_icon.jpg';
var userinfo = localStorage.getItem("_userinfo");
var timestamp;
var noncestr;
var signature;
//获取签名
var targetUrl = location.href.split('#')[0];
//targetUrl = targetUrl.replace('&', '%26');
$.ajax({
	type: "GET",
	url: "/queueGame/getSignature/csjdw?targetUrl="+targetUrl,
	//data:{timestamp:timestamp,noncestr:noncestr,url:url},
	data: {
		url: url
	},
	success: function(data) {
		var objData = data; //JSON.parse(data);
		timestamp = objData.timestamp;
		noncestr = objData.noncestr;
		signature = objData.signature;
		console.log(objData);
		wxShare();
	}
});

function wxShare() {
	wx.config({
		debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		appId: 'wxd4ce3f7bb3088e37', // 和获取Ticke的必须一样------必填，公众号的唯一标识
		timestamp: timestamp, // 必填，生成签名的时间戳
		nonceStr: noncestr, // 必填，生成签名的随机串
		signature: signature, // 必填，签名，见附录1
		jsApiList: [
				'onMenuShareTimeline',//分享朋友圈接口
                'onMenuShareAppMessage'//分享给朋友接口
			] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
}
wx.ready(function() {
	//config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
	//config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关
	//接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。

	//----------“分享给朋友”
	wx.onMenuShareAppMessage({
		title: "分享给朋友  贺长沙家电网体验馆开业", // 分享标题
		desc: shareTitle, // 分享描述
		link: url, // 分享链接
		imgUrl: 'http://172.25.2.101:8086/images/share_ico.jpg', // 分享图标
		type: '', // 分享类型,music、video或link，不填默认为link
		dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
		success: function() {
			// 用户确认分享后执行的回调函数、
		},
		cancel: function() {
			// 用户取消分享后执行的回调函数
		}
	});
	//------------"分享到朋友圈"
	wx.onMenuShareTimeline({
		title: '朋友圈贺长沙家电网体验馆开业', // 分享标题
		desc: shareTitle, // 分享描述
		link: url, // 分享链接
		imgUrl: 'http://172.25.2.101:8086/images/share_ico.jpg', // 分享图标
		success: function() {
			// 用户确认分享后执行的回调函数
		},
		cancel: function() {
			// 用户取消分享后执行的回调函数
		}
	});
});