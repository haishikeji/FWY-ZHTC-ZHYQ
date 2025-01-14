<template>
	<view>
		<view class="header" >
			<view class="avatarAndName" @click="tapSkipLogin">
				<u-avatar :src="rawData.avatar" size=55></u-avatar>
				<text class="userName">{{rawData.nickName}}</text>
			</view>
		</view>
		<view class="list" v-for="(item,index) in myList" :key="item.name" @click="select(item,index)">
			<view class="left">
				<image :src="item.src" mode="widthFix" class="img-left"></image>
				<text>{{item.name}}</text>
			</view>
			<image src="../../static/jianto.svg" mode="widthFix" class="img-right"></image>
		</view>
		<!-- <view class="messageNotification" @click="authSubMessage">消息通知</view> -->
		<u-modal :show="show" :content='content' @confirm='confirm'></u-modal>
	</view>
</template>

<script>
	import {
		login
	} from '@/libs/api.js'
	export default {
		data() {
			return {
				userInfo: null, // 用于存储已授权的用户信息
				content: '请先登录，再进行操作',
				show: false,
				messageInfoShow: true,
				myList: [
					{
						src: '../../static/svg/mylist1.svg',
						name: '我的消息',
						route: '/pages/myList/message/myNews'
					},
					{
						src: '../../static/svg/activity.svg',
						name: '活动中心',
						route: '/pages/myList/activity/activityCenter'
					},
					{
						src: '../../static/svg/mylist2.svg',
						name: '我的卡劵',
						route: '/pages/myList/myCoupon'
					},
					{
						src: '../../static/svg/mylist3.svg',
						name: '包月管理',
						route: '/pages/myList/monthManage'
					},
					{
						src: '../../static/svg/mylist4.svg',
						name: '车辆管理',
						route: '/pages/myList/carManage'
					},
					{
						src: '../../static/svg/mylist5.svg',
						name: '我的订单',
						route: '/pages/myList/myOrder/orderForm'
					},
					{
						src: '../../static/svg/mylist6.svg',
						name: '我的发票',
						route: '/pages/myList/myInvoice/myInvoice'
					},
					{
						src: '../../static/svg/mylist7.svg',
						name: '常见问题',
						route: '/pages/myList/commonProblem'
					},
					{
						src: '../../static/svg/mylist8.svg',
						name: '用户反馈',
						route: '/pages/myList/userFeedback'
					},
					{
						src: '../../static/svg/newsNotify.svg',
						name: '消息通知'
					}
				],
				selectedIndex: null, // 用于标识当前选中的项的索引
				selectedItem: null,
				rawData: {
					avatar: '',
					nickName: ''
				},
			};
		},
		onLoad() {
			this.rawData.avatar = uni.getStorageSync('avatarUrl')
			this.rawData.nickName = uni.getStorageSync('nickName') ? uni.getStorageSync('nickName') : '请点击登录'
		},
		methods: {
			select(item, index) {
				if (uni.getStorageSync('userId')) {
					if (this.selectedItem !== null) {
						this.selectedItem = null;
					}
					// 设置当前选中项的状态
					this.selectedItem = item;
					// 设置当前选中项的索引
					this.selectedIndex = index;
					// 根据选中的选项跳转到相应的页面
					uni.navigateTo({
						url: `${this.myList[index].route}?name=${this.myList[index].name}`
					});
				} else {
					this.show = true
				}
				
				if(item.name === "消息通知") {
					this.authSubMessage()
				}
			},
			confirm() {
				this.show = false
			},
			tapSkipLogin() {
				if (!uni.getStorageSync('userId')) {
					uni.navigateTo({
						url: '/pages/login/login'
					});
				}else{
					uni.navigateTo({
						url: '/pages/myList/editAvatar'
					});
				}

			},
			
			// 授权订阅消息
			authSubMessage() {
				this.messageInfoShow = true
				let tmplIds="LJE3fOx48f9ziTCqwnwgOjlanSTLfcwctwP8mn89Opw" ; //消息模板id
				uni.getSetting({
				    withSubscriptions: true,   //  这里设置为true,下面才会返回mainSwitch
				    success: function(res){   
						// 调起授权界面弹窗
						if (res.subscriptionsSetting.mainSwitch) {  // 用户打开了订阅消息总开关
							if (res.subscriptionsSetting.itemSettings != null) {   // 用户同意总是保持是否推送消息的选择, 这里表示以后不会再拉起推送消息的授权
								let moIdState = res.subscriptionsSetting.itemSettings[tmplIds];  // 用户同意的消息模板id
								if(moIdState === 'accept'){   
								  console.log('接受了消息推送');
								}else if(moIdState === 'reject'){
									console.log("拒绝消息推送");
								}else if(moIdState === 'ban'){
									console.log("已被后台封禁");
								}
							} else {
								// 当用户没有点击 ’总是保持以上选择，不再询问‘  按钮。那每次执到这都会拉起授权弹窗
								uni.showModal({
									title: '提示',
									content:'请授权开通服务通知',
									showCancel: true,
									success: function (ress) {
										if (ress.confirm) {  
											uni.requestSubscribeMessage({   // 调起消息订阅界面
												tmplIds: [tmplIds],
												success (res) { 
													console.log('订阅消息 成功 ', res);
												},
												fail (er){
													console.log("订阅消息 失败 ", er);
												}
											})     
										}
									}
								})
							}
						}else {
							console.log('订阅消息未开启')
						}
					}, 
				}) 
			},
		}
	};
</script>

<style lang="scss">
	.header {
		height: 150rpx;
		background-color: #1677FF;
		display: flex;
		flex-direction: row;
		align-items: center;
		padding: 0 26rpx;
		
		.avatarAndName {
			display: flex;
			flex-direction: row;
			align-items: center;
		}

		.userUrl {
			width: 100rpx;
			border-radius: 100px;
			margin-right: 30rpx;
		}

		.userName {
			margin-left: 20rpx;
			font-size: 25px;
			font-weight: bold;
		}
	}

	.list {
		display: flex;
		justify-content: space-between;
		align-items: center;
		height: 100rpx;

		.left {
			display: flex;
			align-items: center;

			.img-left {
				width: 50rpx;
				margin-right: 50rpx;
				margin-left: 10rpx;
			}
		}

		.img-right {
			width: 30rpx;
			margin-right: 40rpx;
		}
	}
	
	.messageNotification {
		display: flex;
		flex-direction: row;
		width: 100%;
	}
</style>