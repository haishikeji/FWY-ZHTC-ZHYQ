<template>
	<view>
		<view class="NotData" v-if="cardList.length === 0">
			<image class="notData-icon" src="../../../../static/svg/article.svg"></image>
			<view class="notData-title">暂无数据</view>
		</view>
		<view class="u-page" v-else>
				<view style="display:flex;flex-direction:column;align-items:center;justify-content: space-between;">
					<view class="openManage" v-for="(item, index) in cardList" @click="updateInvoiceInfo(item, index)">
						<view class="TopRight" v-if="item.isDefault == '1'">
							<view class="tag">默认</view>
							<image class="imgRight" src="../../../../static/rectangleright.svg">
							</image>
						</view>
						<view class="content" :class="{ 'default': item.isDefault == '0' }">
							<view class="name">{{item.name}}</view>
							<view class="code" v-if="item.invoiceType === '1'">税号：{{item.taxSn}}</view>
							<view class="code" v-if="item.invoiceType === '2'">手机号：{{item.phonenumber}}</view>
						</view>
					</view>
					<!-- v-if="selectedInvoice !== '选择开票'" -->
					<view style="padding: 20rpx 0; margin-bottom: 120rpx;" @click="onReachList" >
						<!-- 底部加载状态 -->
						<u-loadmore :status="loadStatus" line />
					</view>
				</view>
		</view>
		<!-- v-if="selectedInvoice !== '选择开票'" -->
		<view class="btn" @click="toOpenManage" >
			<view class="newAddInvoice">新增发票抬头</view>
		</view>
	</view>
</template>

<script>
	import { userInvoiceInfo, userDefaultInvoice } from '../../../../libs/api';
	export default {
		data() {
			return {
				cardList:[],
				loadStatus: 'loading', // 加载状态
				page: 1,
				size: 10,
				// selectedInvoice: "",
				invoiceData: {},
				// confirmInvoiceInfo: [],
				// index: 0
			}
		},
		onLoad(e) {
			// this.selectedInvoice = e.selectInvoice
			// this.confirmInvoiceInfo = e.confirmInvoiceInfo
			// console.log("this.confirmInvoiceInfo ---> ", this.confirmInvoiceInfo);
			// this.index = e.index
		},
		
		// 下拉刷新
		onPullDownRefresh() {
			this.refresh()
		},
		
		// 上拉加载数据
		onReachBottom() {
			this.onReachList()
		},

		mounted() {
			if(this.selectedInvoice == '选择开票') {
				this.getUserDefaultInvoice()
			} else {
				this.getUserInvoiceList()
			}
		},
		methods: {
			toOpenManage() {
				uni.redirectTo({
					url: "/pages/myList/addOpenManage"
				})
			},
			
			// 跳转到发票信息页面-->更改用户发票信息
			updateInvoiceInfo(item, index) {
				// if(this.selectedInvoice === '选择开票') {
				// 	uni.navigateTo({
				// 		url: `/pages/myList/myInvoice/electronicInvoice/confirmInvoicingInfo/confirmInvoiceInfo?confirmInvoiceInfo=${this.confirmInvoiceInfo}&index=${this.index}`
				// 	})
				// } else 
				// {
					let updateCardList = JSON.stringify(this.cardList[index])
					uni.redirectTo({
						url: `/pages/myList/addOpenManage?updateCardList=${updateCardList}`
					})
				// }
			},
			// invoiceClose() {
			// 	this.invoiceShow = false
			// },
			// invoiceOpen() {
			// 	this.invoiceShow = true
			// },
			
			// 刷新
			refresh() {
				this.page = 1
				this.cardList = []
				// if(this.selectedInvoice == '选择开票') {
				// 	this.getUserDefaultInvoice()
				// } else {
					this.getUserInvoiceList()
				// }
			},
			
			// 加载更多
			onReachList(current) {
				if(this.loadStatus == 'loading' || this.loadStatus == 'nomore') {
					// 当加载状态现在处于 加载中 loading 或者 没有更多 nomore 的时候return
					// 判断 加载中 loading 是为了防止请求重复触发  判断 nomore 是没有数据的时候就不需要再请求了
					return
				}
				this.size += 5
				this.getUserInvoiceList()
				this.loadStatus = 'loading'
			},
			
			// 获取用户发票信息
			async getUserInvoiceList() {
				let invoiceInfo = {
					pageNum: this.page, 
					pageSize: this.size, 
					userId: uni.getStorageSync('userId')
				}
				try{
					const res = await userInvoiceInfo(invoiceInfo)
					this.cardList = res.data.rows
					console.log("this.cardList ---> ", this.cardList);
					// 排序，将isDefault为"1"的项排列在最前面
					this.cardList.sort((a, b) => {
					  if (a.isDefault === "1") return -1;  // a排在b前面
					  if (b.isDefault === "1") return 1;   // b排在a前面
					  return 0;  // 保持原顺序
					});
					
					this.loadStatus = 'loadmore' // 请求结束后把加载状态默认换成  loadmore 加载更多
					if(res.data.total <= this.page * this.size) {
						// res.data.total 是总页数 如果你的(当前页数 page * 你的一页条数 size) >= 总页数 那就说明没有更多的数据了
						this.loadStatus = 'nomore'
					}
					setTimeout(()=>{
						// 关闭下拉刷新状态 延迟500是为了不那么快
						uni.stopPullDownRefresh()
					}, 500)
				}catch(e){
					console.log("getUserInvoiceList error:", e);
					setTimeout(()=>{
						uni.stopPullDownRefresh()
					}, 500)
				}
			},
			
			// 获取用户默认发票抬头
			// async getUserDefaultInvoice() {
			// 	let data = {
			// 		userId: uni.getStorageSync('userId')
			// 	}
			// 	try{
			// 		const res = await userDefaultInvoice(data)
			// 		this.cardList = [res.data]
			// 	}catch(e){
			// 		console.log("getUserDefaultInvoice ERROR ", e);
			// 	}
			// }
		}
	}
</script>

<style lang="scss">
	body {
		background: #f5f6fa;
	}
	
	.NotData {
			display: flex;
			align-items: center;
			flex-direction: column;
			margin: 200rpx auto;
			.notData-icon {
				width: 120rpx;
				height: 120rpx;	
			}
			.notData-title {
				color: rgba(0, 0, 0, 0.38);
				
				/* Body 1 */
				font-family: "PingFang SC";
				font-size: 14px;
				font-style: normal;
				font-weight: 400;
				line-height: 20px; /* 142.857% */
				margin-top: 15rpx;
			}
	}
	
	.openManage {
		width: 670rpx;
		height: 20vh;
		flex-shrink: 0;
		border-radius: 8px;
		background: #FFF;
		margin: 15rpx auto;

		
		.TopRight {
			margin-left: 580rpx;
			width: 90rpx;
			height: 50rpx;
			.imgRight {
				position: absolute;
				width: 90rpx;
				height: 50rpx;
				margin-top: -53rpx;
			}
			.tag {
				color: #1677FF;
				font-family: "PingFang SC";
				font-size: 20rpx;
				font-style: normal;
				font-weight: 400;
				line-height: 48rpx; /* 240% */
				letter-spacing: 0.4rpx;
				margin-left: 25rpx;
			}
		}
		
		.content {
			display: flex;
			flex-direction: column;
			align-items: start;
			margin-left: 30rpx;
			.name {
				color: rgba(0, 0, 0, 0.80);
				font-family: "PingFang SC";
				font-size: 40rpx;
				font-style: normal;
				font-weight: 500;
				line-height: 48rpx; /* 120% */
				letter-spacing: 0.8rpx;
			}
			.code {
				color: rgba(0, 0, 0, 0.80);
				font-family: "PingFang SC";
				font-size: 24rpx;
				font-style: normal;
				font-weight: 400;
				line-height: 99.698rpx; /* 415.405% */
				letter-spacing: 0.48rpx;
			}
		}
	}
	
	.default {
	  margin-top: 50rpx;
	}
	
	.btn {
		width: 100%;
		height: 11%;
		position: fixed;
		z-index: 100;
		bottom: 0;
		left: 0;
		right: 0;
		background: #f5f5f5;
		.newAddInvoice {
			position: fixed;
			left: 0;
			right: 0;
			width: 90%;
			background-color: #1677FF;
			color: #fff;
			height: 8%;
			margin-bottom:5%;
			margin: 0 auto;
			text-align: center;
			line-height: 90rpx;
			margin-top: 20rpx;
		}
	}
</style>