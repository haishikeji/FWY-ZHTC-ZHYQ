<template>
	<view class="box">
		<view class="uni-padding-wrap">
			<uni-segmented-control :current="current" :values="items" style-type="text" :active-color="activeColor"
				@clickItem="onClickItem" />
		</view>
		<view class="content">
			<view v-if="current === 0">
				<view class="card" v-for="(item, index) in users" :key="index" v-if="compareDates(item) || users.length !== 0"
					:style="'background-color:' + getCardColor(item.name)">
					<image :src="getSvg('car')" mode="widthFix" class="card-car" />
					<view class="cardday">
						<text>{{item.name}}</text>
						<text>30天</text>
					</view>
					<text class="validity">有效期至{{formatDate(item.validity)}}</text>
				</view>
				<view class="nomessage" v-if="users.length === 0">
					<image :src="getSvg('coupon')" mode="widthFix" class="message-img" />
					<text>暂无数据</text>
				</view>
			</view>
			<view v-if="current === 1">
				<view class="card" v-for="(item, index) in users" :key="index" v-if="compareDates(item) || users.length !== 0"
					:style="'background-color:' + getCardColor(item.name)">
					<image :src="getSvg('car')" mode="widthFix" class="card-car" />
					<!-- <image src="../../static/images/car.jpg" mode="widthFix" class="card-car"></image> -->
					<view class="cardday">
						<text>{{item.name}}</text>
						<text>30天</text>
					</view>
					<text class="validity">有效期至{{formatDate(item.validity)}}</text>
				</view>
				<view class="nomessage" v-if="users.length === 0">
					<image :src="getSvg('coupon')" mode="widthFix" class="message-img" />
					<text>暂无数据</text>
				</view>
			</view>
			<view v-if="current === 2">
				<view class="card" v-for="(item, index) in users" :key="index" v-if="compareDates(item) || users.length !== 0"
					:style="'background-color:' + getCardColor(item.name)">
					<image :src="getSvg('car')" mode="widthFix" class="card-car" />
					<!-- <image src="../../static/images/car.jpg" mode="widthFix" class="card-car"></image> -->
					<view class="cardday">
						<text>{{item.name}}</text>
						<text>30天</text>
					</view>
					<text class="validity">有效期至{{formatDate(item.validity)}}</text>
				</view>
				<view class="nomessage" v-if="users.length === 0">
					<image :src="getSvg('coupon')" mode="widthFix" class="message-img" />
					<text>暂无数据</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { userCouponList } from '../../libs/api';
	export default {
		data() {
			return {
				items: ['待使用', '已使用', '已过期'],
				current: 0,
				activeColor: '#007aff',
				users: [
					// {
					// 	id: 1,
					// 	name: '包月卡',
					// 	day: '30',  // 天数逻辑先不动
					// 	receiveTime: '2024-05-03',
					// 	validity: '2024-08-15',
					// 	getStatus: "0"
					// }
				],
			};
		},
		created() {
			this.getUserCouponList()
		},
		methods: {
			onClickItem(e) {
				if (this.current !== e.currentIndex) {
					this.current = e.currentIndex
				}
				this.getUserCouponList()
			},
			formatDate(dateString) {
				const date = new Date(dateString);
				const year = date.getFullYear();
				const month = (date.getMonth() + 1).toString().padStart(2, '0');
				const day = date.getDate().toString().padStart(2, '0');
				return `${year}年${month}月${day}日`;
			},
			getCardColor(name) {
				if (this.current === 0) {
					return '#FF9344';
				} else if (this.current === 1) {
					return '#44C7FF';
				} else if(this.current === 2){
					return '#DCDCDC'
				}
				return 'gray';
			},
			
			compareDates(item) {
				const currentDate = new Date(); // 获取当前时间
				const validityDate = new Date(item.validity); // 将validity转换为日期对象
				// 使用getTime()方法获取日期的时间戳进行比较
				if (this.current === 0 || this.current === 1) {
					return currentDate.getTime() < validityDate.getTime();
				} else {
					return currentDate.getTime() > validityDate.getTime();
				}
			},

			expired(item) {
				const currentDate = new Date(); // 获取当前时间
				return this.users.some(item => {
					const itemValidityDate = new Date(item.validity);
					return currentDate.getTime() > itemValidityDate.getTime();
				});
			},
			
			// 获取用户优惠券列表
			async getUserCouponList() {
				this.users = []
				let couponInfo = {
					pageNum: 1,
					pageSize: 5,
					status: this.current,
					userId: uni.getStorageSync('userId')
				}
				try{
					const res = await userCouponList(couponInfo);
					this.users = res.data.rows.map(item => ({
						id: item.activityId,
						name: item.couponName,
						receiveTime: item.receiveTime,
						validity: item.endTime,
						getStatus: item.status
					}));
				}catch(e){
					console.log("getUserCouponList ERROR:", e);
				}
			}
		}
	}
</script>

<style lang="scss">
	.box {
		height: 100vh;
		background-color: rgba(255, 255, 255, 0.2);
		// background-color: #000;
	}

	.uni-padding-wrap {
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

	.card {
		position: relative;
		margin: 30rpx;
		padding: 50rpx;
		height: 200rpx;
		background-color: #000;
		border-radius: 12px;
		color: #fff;
		display: flex;
		flex-direction: column;
		justify-content: space-between;

		.cardday {
			display: flex;
			flex-direction: column;

			text {
				font-size: 20px
			}
		}

		.card-car {
			position: absolute;
			width: 350rpx;
			right: 0rpx;
			top: 0rpx;
		}

		.validity {
			font-size: 12px
		}
	}
</style>