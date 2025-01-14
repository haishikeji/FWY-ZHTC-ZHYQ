<template>
	<view class="stopOrder">
		<uni-section>
			<view class="uni-padding-wrap uni-common-mt">
				<uni-segmented-control 
					:current="current" 
					:values="items" 
					style-type="text"
					:inActiveColor="inActiveColor"
					:active-color="activeColor" 
					@clickItem="onClickItem" />
			</view>
			<view class="content">
				<!-- 待支付 -->
				<view v-if="current === 0">
					<view class="NotData" v-if="true">
						<image class="notData-icon" src="../../static/svg/article.svg"></image>
						<view class="notData-title">暂无订单</view>
					</view>
					<view class="waitOrderCard" v-else>
						<view class="top">
							<view class="left">待支付</view>
							<view class="right">
								<view class="right-money">¥200</view>
								<text class="xs">.00</text>
							</view>
						</view>
						<view class="center" v-for="(item, index) in card" :key="index">
							<view>{{item.name}}：{{item.value}}</view>
						</view>
						<view class="warp">
							<button class="button" type="primary">去支付</button>
						</view>
					</view>
				</view>
				<!-- 已完成 -->
				<view v-if="current === 1">
					<view class="NotData" v-if="false">
						<image class="notData-icon" src="../../static/svg/article.svg"></image>
						<view class="notData-title">暂无订单</view>
					</view>
					<view class="waitOrderCard" v-else>
						<view class="top">
							<view class="left">完成</view>
							<view class="right">
								<view class="right-money">¥200</view>
								<text class="xs">.00</text>
							</view>
						</view>
						<view class="center" v-for="(item, index) in card" :key="index">
							<view>{{item.name}}：{{item.value}}</view>
						</view>
					</view>
				</view>
				<!-- 已申诉 -->
				<view v-if="current === 2">
					<view class="NotData" v-if="false">
						<image class="notData-icon" src="../../static/svg/article.svg"></image>
						<view class="notData-title">暂无订单</view>
					</view>
					<view class="waitOrderCard" v-else>
						<view class="top">
							<view class="left">申诉中</view>
							<view class="right">
								<view class="right-money">¥200</view>
								<text class="xs">.00</text>
							</view>	
						</view>
						<view class="center" v-for="(item, index) in card" :key="index">
							<view>{{item.name}}：{{item.value}}</view>
						</view>
						<view class="warp">
							<u-button type="primary" :plain="true" text="撤回申诉"></u-button>
						</view>
					</view>
				</view>
			</view>
		</uni-section>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				items: ['待支付', '已完成', '已申诉'],
				current: 0,
				colorIndex: 0,
				inActiveColor: '#b2b2b2',
				activeColor: 'rgba(0, 0, 0, 0.9)',
				styleType: 'button',
				
				card: [
					{name:'入场时间', value: "2024-04-12 12:30:00"},
					{name:'离开时间', value: "2024-04-12 12:30:00"},
					{name:'车牌号', value: "浙D896329"},
					{name:'经手人', value: "周一"},
				]
			}
		},
		methods: {
			onClickItem(e) {
				if (this.current !== e.currentIndex) {
					this.current = e.currentIndex
				}
			},
			styleChange(e) {
				if (this.styleType !== e.detail.value) {
					this.styleType = e.detail.value
				}
			},
			colorChange(e) {
				if (this.styleType !== e.detail.value) {
					console.log(e.detail.value);
					this.activeColor = e.detail.value
				}
			}
		}
	}
</script>

<style lang="scss">
	.stopOrder {
		width: 100%;
		height: 100vh;
		background: #F5F6FA !important;
		z-index: 5;
		
		.uni-common-mt {
				margin-top: -30rpx;
			}
		
			.uni-padding-wrap {
				// width: 750rpx;
				padding: 0rpx 60rpx;
			}
		
			.content {
				/* #ifndef APP-NVUE */
				display: flex;
				/* #endif */
				justify-content: center;
				align-items: center;
				width: 100%;
				height: 100%;
				background: #f5f6fa;
				text-align: center;
				
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
					height: 514rpx;
					border-radius: 8rpx;
					background: #FFF;
					margin-top: -358rpx;
					
					.top {
						display: flex;
						align-items: center;
						flex-direction: row;
						justify-content: space-around;
						padding: 20rpx;
						border-bottom: 2rpx solid #efefef;
						.left {
							color:#161616;
							font-family: "PingFang SC";
							font-size: 32rpx;
							font-style: normal;
							font-weight: 400;
							line-height: 48rpx; /* 150% */
							margin-left: -100rpx;
						}
						.right {
							display: flex;
							align-items: center;
							flex-direction: row;
							margin-right: -100rpx;
							.right-money {
								color: #EF2619;
								font-family: "PingFang SC";
								font-size: 40rpx;
								font-style: normal;
								font-weight: 500;
								line-height: 48rpx;
							}
							.xs {
								color: #EF2619;
								font-family: "PingFang SC";
								font-size: 30rpx;
								font-style: normal;
								font-weight: 500;
								line-height: 48rpx;
							}
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
						padding: 5rpx 67rpx;
					}
					
					.warp {
							padding: 10px;
							.button {
								margin-bottom: 10px;
							}
							.button[type=primary] {
								background-color: #1677ff;
							}
					}
					
				}
			}
		
			.content-text {
				font-size: 28rpx;
				color: #666;
			}
			
			.segmented-control__text .segmented-control__item--text {
				margin-bottom: 20rpx !important;
			}

			
	}
	
</style>