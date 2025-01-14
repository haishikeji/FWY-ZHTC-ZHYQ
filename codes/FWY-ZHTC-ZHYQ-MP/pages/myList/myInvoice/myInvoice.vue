<template>
	<view class="my-order">
		<view class="u-page" v-for="(item, index) in invoiceList" :key="index">
			<view class="orderList" @click="onOpenStopInvoiceClick(item, index)">
				<image class="iconList" :src="item.url"></image>
				<view class="titleList">{{item.title}}</view>
				<image class="rightIcon" :src="item.url2"></image>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				invoiceList: [
					{url: '../../../static/svg/receipt_long.svg', title: '开临停电子发票', url2: '../../../static/svg/arrow_forward_ios.svg'},
					{url: '../../../static/svg/receipt_long_month.svg', title: '开包月电子发票', url2: '../../../static/svg/arrow_forward_ios.svg'},
					{url: '../../../static/svg/GroupManage.svg', title: '开票管理', url2: '../../../static/svg/arrow_forward_ios.svg'},
					{url: '../../../static/svg/GroupRecord.svg', title: '开票记录', url2: '../../../static/svg/arrow_forward_ios.svg'},
				]
			};
		},
		methods: {
			// 跳转到开临停电子发票
			onOpenStopInvoiceClick(item, index) {
				if(index === 0) {
					uni.navigateTo({
						url: `/pages/myList/myInvoice/openStopInvoice/openStopInvoice?index=${index}`
					})
				} else if(index === 1) {
					uni.navigateTo({
						url: `/pages/myList/myInvoice/electronicInvoice/electronicInvoice?index=${index}`
					})
				} else if(index === 2) {
					uni.navigateTo({
						url: "/pages/myList/myInvoice/openInvoiceManage/openInvoiceManage"
					})
				} else if(index === 3) {
					uni.navigateTo({
						url:"/pages/myList/openInvoiceRecord"
					})
				}
			},
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
