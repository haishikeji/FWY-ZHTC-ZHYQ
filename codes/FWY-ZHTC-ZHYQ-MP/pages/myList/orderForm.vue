<template>
	<view class="my-order">
		<view class="u-page" @click="onStopOrderClick">
			<view class="orderList">
				<image class="iconList" src="../../static/svg/order_approve.svg"></image>
				<view class="titleList">临停订单</view>
				<image class="rightIcon" src="../../static/svg/arrow_forward_ios.svg"></image>
			</view>
		</view>
		<view class="u-page">
			<view class="orderList" @click="onMonthOrderClick">
				<image class="iconList" src="../../static/svg/order_approve_two.svg"></image>
				<view class="titleList">包月订单</view>
				<image class="rightIcon" src="../../static/svg/arrow_forward_ios.svg"></image>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			};
		},
		onLoad() {
			
		},
		methods: {
			onStopOrderClick() {
				uni.navigateTo({
					url:"/pages/myList/stopOrder"
				})
			},
			onMonthOrderClick() {
				uni.navigateTo({
					url:"/pages/myList/monthlyOrder"
				})
			}
		}
	}
</script>

<style lang="scss">
.my-order {
	width: 100%;
	height: 100%;
	background: #f5f5f5;
	
	.u-page {
		width: 90%; 
		height: 108rpx;
		background-color: #fff; 
		margin: 32rpx auto;
		border-radius: 8px;
		
		.orderList {
			display: flex;
			
			.iconList {
				width: 60rpx;
				height: 60rpx;
				flex-shrink: 0;
				margin-top: 24rpx;
				margin-left: 24rpx;
			}
			.titleList {
				width: 500rpx;
				flex-shrink: 0;
				color: #000;
				font-family: "PingFang SC";
				font-size: 28rpx;
				font-style: normal;
				font-weight: 500;
				line-height: 40rpx; /* 142.857% */
				margin-top: 33rpx;
				margin-left: 24rpx;
			}
			.rightIcon {
				width: 24rpx;
				height: 24rpx;
				flex-shrink: 0;
				margin-top: 41rpx;
			}
		}
	}
}
</style>
