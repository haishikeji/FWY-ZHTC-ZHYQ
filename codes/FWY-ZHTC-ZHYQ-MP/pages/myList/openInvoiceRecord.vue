<template>
	<view class="openRecord">
		<view class="uni-padding-wrap">
			<uni-segmented-control :current="current" :values="items" style-type="text" :active-color="activeColor"
				@clickItem="onClickItem" />
		</view>
		<view class="content" style="margin-top: 132rpx;">
			<view v-if="current === 0" style="display: flex; flex-direction: column;">
				<view class="monthlyOrder">
					<view class="NotData" v-if="invoiceRecord.length === 0">
						<image class="notData-icon" src="../../static/svg/article.svg"></image>
						<view class="notData-title">暂无订单</view>
					</view>
					<view class="content" v-else>
						<view class="center" v-for="(item, index) in invoiceRecord" :key="index">
							<!-- :class="{ active: isActive(item) }" -->
							<view class="waitOrderCard" @click="toggleSelection(item)">
								<view class="TopRight">
									<view class="tag">已开票</view>
									<image class="imgRight" src="../../static/svg/Rectanglegray.svg">
									</image>
								</view>
								<view class="top">
									<view class="left">发票信息</view>
								</view>
								<view class="center">
									<view>开票金额：{{item.amount}}</view>
									<view>申请开票时间：{{item.createTime}}</view>
									<view>发票备注：{{item.remark}}</view>
								</view>
							</view>
						</view>
						<!-- <view style="padding: 20rpx 0;" @click="onReachList">
							底部加载状态
							<u-loadmore :status="loadStatus" line />
						</view> -->
					</view>
				</view>
			</view>
			<view v-if="current === 1" style="display: flex; flex-direction: column;">
				<view class="monthlyOrder" style="height: 450rpx;"  >
					<view class="NotData" v-if="invoiceRecord.length === 0">
						<image class="notData-icon" src="../../static/svg/article.svg"></image>
						<view class="notData-title">暂无订单</view>
					</view>
					<view class="content" v-else>
						<view class="center" v-for="(item, index) in invoiceRecord" :key="index">
							<!-- :class="{ active: isActive(item) }" -->
							<view class="waitOrderCard" @click="toggleSelection(item)">
								<view class="TopRight">
									<view class="tag">已开票</view>
									<image class="imgRight" src="../../static/svg/Rectanglegray.svg">
									</image>
								</view>
								<view class="top">
									<view class="left">发票信息</view>
								</view>
								<view class="center">
									<view>开票金额：{{item.amount}}</view>
									<view>申请开票时间：{{item.createTime}}</view>
									<view>发票备注：{{item.remark}}</view>
								</view>
							</view>
						</view>
						<!-- <view style="padding: 20rpx 0;" @click="onReachList">
							底部加载状态
							<u-loadmore :status="loadStatus" line />
						</view> -->
					</view>
				</view>
			</view>
			<!-- <view class="btn" @click="toSeleDetailInfoClick">
				<view class="selectDetailInfo">查看明细信息</view>
			</view>	 -->
		</view>
	</view>
</template>

<script>
	import { userInvoiceRecord } from '../../libs/api';
	export default {
		data() {
			return {
				items: ['临停电子发票', '包月电子发票'],
				current: 0,
				activeColor: '#007aff',
				active: false,
				invoiceRecord: [],
				activeItem: null,
				// page: 1,
				// size: 10,
				// loadStatus: 'loading', // 加载状态
			};
		},
		created() {
			this.getUserInvoiceRecord()
		},
		// // 下拉刷新
		// onPullDownRefresh() {
		// 	this.refresh()
		// },
		
		// 上拉加载数据
		// onReachBottom() {
		// 	this.onReachList()
		// },
		methods: {
			onClickItem(e) {
				if (this.current !== e.currentIndex) {
					this.current = e.currentIndex
				}
				this.getUserInvoiceRecord()
			},
			
			// 刷新
			// refresh() {
			// 	this.page = 1
			// 	this.invoiceRecord = []
			// 	this.getUserInvoiceRecord()
			// },
			// 加载更多
			// onReachList() {
			// 	if(this.loadStatus == 'loading' || this.loadStatus == 'nomore') {
			// 		// 当加载状态现在处于 加载中 loading 或者 没有更多 nomore 的时候return
			// 		// 判断 加载中 loading 是为了防止请求重复触发  判断 nomore 是没有数据的时候就不需要再请求了
			// 		return
			// 	}
			// 	this.size += 5
			// 	this.getUserInvoiceRecord()
			// 	this.loadStatus = 'loading'
			// },
			
			// 获取用户开票记录
			async getUserInvoiceRecord() {
				let data = {
					orderType: Number(this.current) + 1,
					// pageNum: this.page,
					// pageSize: this.size,
					userId: uni.getStorageSync('userId')
				}
				try{
					const res = await userInvoiceRecord(data)
					this.invoiceRecord = res.data
					
					// this.loadStatus = 'loadmore' // 请求结束后把加载状态默认换成  loadmore 加载更多
					// if(res.data.total <= this.page * this.size) {
					// 	// res.data.total 是总页数 如果你的(当前页数 page * 你的一页条数 size) >= 总页数 那就说明没有更多的数据了
					// 	this.loadStatus = 'nomore'
					// }
					// setTimeout(()=>{
					// 	// 关闭下拉刷新状态 延迟500是为了不那么快
					// 	uni.stopPullDownRefresh()
					// }, 500)
				}catch(e){
					console.log("getUserInvoiceRecord error ", e);
					// setTimeout(()=>{
					// 	uni.stopPullDownRefresh()
					// }, 500)
				}
			},
			
			// 跳转到明细
			toggleSelection(item) {
				console.log("item ----> ", item);
				if(item) {
					uni.navigateTo({
					  url: `/pages/myList/myInvoice/selectDetailInfo/selectDetailInfo?activeItem=${JSON.stringify(item)}`
					});
				}
			},
		}
	}
</script>

<style lang="scss">
	body {
		background: #f5f6fa;
	}
	.openRecord {
		height: 100vh;
		overflow: auto;
		// background-color: #000;
	}
	
	.uni-padding-wrap {
		position: fixed;
		left: 0;
		right: 0;
		top: 0;
		z-index: 10;
		// width: 750rpx;
		padding: 30rpx;
		background-color: #fff;
	}
	
	.nomessage {
		width: 100%;
		height: 700rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	
		.message-img {
			width: 150rpx;
		}
	
		text {
			color: rgba(0, 0, 0, 0.38);
		}
	}
	
	.monthlyOrder {
		width: 100%;
		// height: 90vh;
		background: #f5f6fa;
		
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
		
		.waitOrderCard {
			width: 682rpx;
			border-radius: 8rpx;
			background: #FFF;
			// margin: -361rpx 30rpx;
			margin: 21rpx 30rpx;
			
			.TopRight {
				position: relative;
				margin-left: 591rpx;
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
			
			.top {
				display: flex;
				// align-items: center;
				// flex-direction: row;
				// justify-content: space-around;
				padding: 20rpx;
				border-bottom: 2rpx solid #efefef;
				
				.left {
					color:#161616;
					font-family: "PingFang SC";
					font-size: 40rpx;
					font-style: normal;
					font-weight: 400;
					line-height: 48rpx; /* 150% */
					margin-left: 48rpx;
				}
				.right {
					display: flex;
					align-items: center;
					flex-direction: row;
					margin-right: 210rpx;
				}
				
				
			}
			.rightNo {
				width: 100rpx;
				height: 50rpx;
				// background-image: url('../../static/rectangleright.svg');
				margin-left: 575rpx;
				margin-top: -80rpx;
				border: 2px solid rgba(22, 119, 255, 0.50);
				text-align: center;
				
				.no {
					color: #1677FF;
					font-family: "PingFang SC";
					font-size: 20rpx;
					font-style: normal;
					font-weight: 400;
					line-height: 52.698rpx;
					text-align: center;
					letter-spacing: 0.4rpx;
				}
			}
			
			.center {
				display: flex;
				flex-direction: column;
				text-align: left;
				color: #454545;
				font-family: "PingFang SC";
				font-size: 14px;
				font-style: normal;
				font-weight: 400;
				line-height: 28px; /* 200% */
				padding: 22rpx 67rpx;
			}
		}
		
		// .waitOrderCard.active {
		//   border: 4rpx solid rgba(22, 119, 255, 0.50);
		// }
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
		.selectDetailInfo {
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