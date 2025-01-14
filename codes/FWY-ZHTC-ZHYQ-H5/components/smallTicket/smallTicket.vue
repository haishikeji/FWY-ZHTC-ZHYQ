<template>
	<view class="container">
		<view class="title">{{type == 0?'欠费记录':'缴费记录'}}</view>
		<view v-if="values.length > 0">
			<scroll-view scroll-y="true" @scrolltolower="handleScroll" class="scroll">
				<view class="list">
					<view class="item" v-for="item in values" :key="item.orderId" @click="toTicketDetail(item)">
						<view>入场时间：{{ item.entryTime }}</view>
						<view>离开时间：{{ item.departureTime }}</view>
						<view>金额：{{ item.orderRealityAmount }}</view>
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
		name: "smallTicket",
		props: {
			values: {
				type: Array,
				default: () => []
			},
			type: {
				type: Number,
				default: 0
			}
		},
		data() {
			return {
			};
		},
		created() {
		},
		computed: {
		},
		methods: {
			toTicketDetail(item) {
				console.log(item)
				//欠费记录跳转到欠费详情，缴费记录跳转到补打小票
				if (this.type == 0) {
					uni.navigateTo({
						url: `/pages/content/arrearsDetail?orderSn=` + item.orderSn
					});
				} else {
					uni.navigateTo({
						// url: `/pages/ticket`,
						url: `/pages/content/chargeVoucher?orderSn=` + item.orderSn
					});
				}
			},
			handleScroll() {

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
			max-height: 50vh;
			
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