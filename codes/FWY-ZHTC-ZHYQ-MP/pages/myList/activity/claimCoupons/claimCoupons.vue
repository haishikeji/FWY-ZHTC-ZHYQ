<template>
	<view class="claimCoupons">
		<view class="coupons" v-for="(item, index) in ActiveInfoList" :key="index">
			<view class="top">
				<image :src="item.image" mode="widthFix"></image>
			</view>
			<view class="content">
				<view class="couponName">优惠券-{{item.couponName}}</view>
				<view class="validTime">有效期限:{{item.beginTime}}至{{item.endTime}}</view>
			</view>
			<view class="bottom">
				<view class="btn" @click="claimCoupon(item)">立即领取</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		userReceiveCoupon
	} from '../../../../libs/api';
	export default {
		data() {
			return {
				ActiveInfoList: []
			}
		},
		onLoad(e) {
			const data = JSON.parse(decodeURIComponent(e.data));
			this.ActiveInfoList.push(data)
		},
		methods: {
			// 领取优惠券
			async claimCoupon(item) {
				let data = {
					activityId: item.activityId,
					couponId: item.couponId,
					userId: uni.getStorageSync('userId')
				}
				try {
					const res = await userReceiveCoupon(data);
					if (res && res.code === 200) {
						uni.showToast({
							title: "领取成功~",
							icon: 'success'
						})
						uni.navigateBack()
					} else {
						uni.showToast({
							title: "领取失败~",
							icon: 'none'
						})
						uni.navigateBack()
					}
				} catch (e) {
					// handle the exception
					uni.showToast({
						title: "领取失败~",
						icon: 'none'
					})
					console.log("claimCoupon ERROR:", e);
				}
			}
		}
	}
</script>

<style lang="scss">
	body {
		background: #f5f6fa;
	}

	.claimCoupons {
		.coupons {
			width: 90%;
			margin: 20rpx auto;
			background-color: #fff;

			.top {
				width: 90%;
				margin: 0 auto;

				image {
					width: 100%;
					height: 100%;
					margin-top: 20rpx;
				}
			}

			.content {
				display: flex;
				flex-direction: column;
				align-items: start;
				margin: 20rpx 30rpx;

				.couponName {
					font-size: 34rpx;
					font-weight: 600;
				}

				.validTime {
					font-size: 28rpx;
					font-weight: 300;
					margin-top: 10rpx;
				}
			}

			.bottom {
				.btn {
					width: 90%;
					height: 90rpx;
					color: #fff;
					margin: 0 auto;
					text-align: center;
					line-height: 90rpx;
					background-color: #3c9cff;
				}
			}
		}
	}
</style>