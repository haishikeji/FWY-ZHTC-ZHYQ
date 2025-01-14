<template>
	<view class="box">
		<licensePlate page='index' />
		<!-- 欠费提醒 -->
		<view class="warn">
			<image :src="getSvg('warn')" mode="widthFix" />
			<view class="word">
				<h4>欠费提醒</h4>
				<text>{{arrearage.length === 0 ? '暂无欠费信息':arrearage[0]}}</text>
			</view>
		</view>
		<!-- 停车包月 -->
		<view class="bag-month">
			<image :src="getSvg('bag_month')" mode="widthFix" />
			<view class="word">
				<h4>停车包月</h4>
				<text>线上包期续费，享更多停车优惠</text>
			</view>
			<button @click="goMonthSubscript">去包月</button>
		</view>
	</view>
</template>

<script>
	import licensePlate from '../../components/licensePlate/licensePlate.vue'
	export default {
		components: {
			licensePlate
		},
		data() {
			return {
				list: ['蓝牌', '绿牌', '黄牌', '其他'],
				current: 0,
				isChange: false, //是否改变为英文
				isClick: 0, //点击的索引
				value: ['', '', '', '', '', '', '', ''], // 车牌值
				carKeyboard: false, //是否显示车牌号键盘
				arrearage: [] //欠费信息
			}
		},
		onLoad(e) {
			console.log("获取到的", e);
		},
		methods: {
			sectionChange(index) {
				this.current = index;
			},
			getPlateLength() {
				// 根据当前选择的车牌类型返回相应的车牌号码长度
				switch (this.current) {
					case 0:
					case 2:
						return 7;
					case 1:
						return 8;
					default:
						return 8; // 默认情况下不限制长度
				}
			},
			//选择输入第几位
			handleCar(e) {
				console.log("这是输入00000", e);
				this.isClick = e
				if (this.isClick == 0) {
					//如果是第0位的则输入完后自动切换为英文
					this.isChange = true;
				} else {
					this.isChange = false;
				}
				//显示键盘
				this.carKeyboard = true
				console.log("这是输入00000", this.isClick);
			},
			//关闭键盘
			closeKey() {
				this.carKeyboard = false;
			},
			// 按键被点击(点击退格键不会触发此事件)
			valChange(val) {
				// this.value[this.isClick] = val;==this.$set(this.value, this.isClick, val);
				//将键盘选择的输入到车牌号内，因为this.value[this.isClick] = val无法同步更新所以用this.$set(this.value, this.isClick, val)
				this.$set(this.value, this.isClick, val)
				//如果未输入完车牌号
				if (this.isClick < this.getPlateLength() - 1) {
					//输入完跳转到下一位
					this.isClick = this.isClick + 1
				} else {
					//输入完了关闭键盘
					this.carKeyboard = false;
				}
			},
			// 退格键被点击
			backspace() {
				// 清空选择的号码
				this.value[this.isClick] = ''
				//如果大于0位的删除会跳到上一个位数
				if (this.isClick > 0) {
					this.isClick = this.isClick - 1
				} else {
					//第0位的时候直接清空,并关闭键盘
					this.$set(this.value, 0, '')
					this.carKeyboard = false;
				}
			},
			
			// 去包月
			goMonthSubscript() {
				uni.navigateTo({
					url: '../myList/monthManage'
				})
			}
		}
	}
</script>

<style lang="scss">
	.box {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		background-color: rgba(255, 255, 255, 0.6);
	}

	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin: 40rpx 0;
	}

	.warn {
		display: flex;
		// justify-content: center;
		align-items: center;
		flex-wrap: wrap;
		width: 650rpx;
		height: 100rpx;
		padding: 16rpx;
		margin: 16rpx;
		background-color: #fff;

		image {
			width: 50rpx;
			margin-right: 20rpx;
		}

		.word {
			h4 {
				letter-spacing: 5rpx;
			}

			text {
				font-size: 10px;
				color: #677191;
				letter-spacing: 2rpx;
			}
		}
	}

	.bag-month {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		width: 650rpx;
		height: 100rpx;
		padding: 16rpx;
		margin: 16rpx;
		background-color: #fff;

		image {
			width: 70rpx;
			margin-right: 10rpx;
		}

		.word {
			h4 {
				letter-spacing: 5rpx;
			}

			text {
				font-size: 10px;
				color: #677191;
				letter-spacing: 2rpx;
			}
		}

		button {
			background-color: #FF6B00;
			color: #fff;
			border-radius: 999px;
			width: 190rpx;
			height: 70rpx;
			line-height: 65rpx;
			font-size: 16px;
			border: 1rpx solid #FF6B00;
		}
	}
</style>