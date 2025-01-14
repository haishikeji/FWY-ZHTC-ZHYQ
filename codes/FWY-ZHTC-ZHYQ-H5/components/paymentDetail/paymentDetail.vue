<template>
<!-- 	<view class="container">
		<view class="title">收费记录</view>
		<view class="list" v-if="values.length > 0">
			<view class="receipt" slot="body">
				<scroll-view scroll-y="true" @scrolltolower="handleScroll" class="scroll">
					<view class="list">
						<view class="item" v-for="item in values" :key="item.orderId" @click="toPaymentDetail(item)">
							<view>车牌号：{{ item.licensePlateNumber }}</view>
							<view>入场时间：{{ item.entryTime }}</view>
							<view>离开时间：{{ item.departureTime }}</view>
							<view>停车时长：{{ item.duration }}</view>
							<view>缴费时间：{{ item.paymentTime }}</view>
							<view>缴费金额：{{ item.orderAmount }}</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</view>
		<view class="nodata" v-else>
			<u-empty mode="data" icon="http://cdn.uviewui.com/uview/empty/data.png">
			</u-empty>
		</view>
	</view>
	 -->
	<view class="container">
		<view class="title">收费记录</view>
		<view v-if="values.length > 0">
			<scroll-view scroll-y="true" @scrolltolower="handleScroll" class="scroll">
				<view class="list">
					<view class="item" v-for="item in values" :key="item.orderId" @click="toPaymentDetail(item)">
						<view>车牌号：{{ item.licensePlateNumber }}</view>
						<view>入场时间：{{ item.entryTime }}</view>
						<view>离开时间：{{ item.departureTime }}</view>
						<view>停车时长：{{ item.duration }}</view>
						<view>缴费时间：{{ item.paymentTime }}</view>
						<view>缴费金额：{{ item.orderAmount }}</view>
					</view>
					<view class="load">
						<uni-load-more :status="status"></uni-load-more>
					</view>
				</view>
			</scroll-view>
		</view>
		<u-empty v-else mode="data" icon="http://cdn.uviewui.com/uview/empty/data.png">
		</u-empty>
	</view>
</template>

<script>
	export default {
		name: "paymentDetail",
		props: {
			values: {
				type: Array,
				default: () => []
			},
			status:{
				type: String,
				default: ''
			}
		},
		data() {
			return {
				pageNum: 1, // 当前页码
				pageSize: 3, // 数据最少条数
			};
		},
		computed: {
		},
		methods: {
			toPaymentDetail(item) {
				console.log(item)
				console.log('跳转到订单详情');
				let jsonString = JSON.stringify(item);
				// 对 JSON 字符串进行编码，以确保 URL 是有效的
				let encodedJsonString = encodeURIComponent(jsonString);
				//跳转到收费详情
				// uni.navigateTo({
				// 	// url: `/pages/ticket`,
				// 	url: `/pages/content/parkingVoucher?data=`+ encodedJsonString
				// });
			},
			handleScroll() {
				console.log("加载下一页")
				this.$emit('child-event', '一些数据');
				// if (Math.ceil(this.berthTotal / 12) > this.pageNum) {
				// 	this.status = 'loading'
				// 	// 触底时 pageNum+1
				// 	this.pageNum++;
				// 	this.getParkAreaBerth(this.selectValue)
				// } else {
				// 	this.status = 'no-more'
				// }
			}
		}
	}
</script>

<style scoped lang="scss">
	.container{
		width: 95%;
		margin: auto;
		text-align: left;
		padding-top: 8rpx;
				
		.title{
			margin-bottom: 8rpx;
			padding-left: 6rpx;
		}
		
		.scroll {
			max-height: 60vh;
			
			.list{
				font-size: 28rpx;
				
				.item {
					margin-bottom: 30rpx;
					border: 1px dotted darkgray;
					padding: 10px;
				}
			}
		}
	}
</style>