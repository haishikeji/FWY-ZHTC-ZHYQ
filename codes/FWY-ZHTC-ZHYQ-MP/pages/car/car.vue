<template>
	<view class="map_wrap">
		<view v-show="isMap">
			<!-- 地图组件 -->
			<view>
				<map class="map" id="map" :longitude="longitude" :latitude="latitude" scale="14" show-location="true"
					:markers="markers" @markertap="markertap"></map>
			</view>
			<!-- 搜索框 -->
			<view class="search_wrap" @click="isMap = !isMap">
				<!-- <input type="text" v-model="keyword" placeholder="输入关键字搜索"/> -->
				<!-- <button @click="searchLocation">搜索</button> -->
				<uni-search-bar placeholder="输入关键字搜索" bgColor="#fff" readonly class="search" />
			</view>
		</view>
		<searchPullUp v-show="!isMap" @pullUpData='pullUpData' @onisMap='onisMap'/>
	</view>
</template>

<script>
	import bmap from '@/libs/bmap-wx.js';
	import searchPullUp from '@/components/searchPullUp/searchPullUp.vue'
	export default {
		components: {
			searchPullUp
		},
		data() {
			return {
				longitude: 0,
				latitude: 0,
				markers: [],
				keyword: '',
				isMap: true
			};
		},
		onShow() {
			this.getLocationPermission();
		},
		methods: {
			getLocationPermission() {
				// 请求用户权限
				uni.authorize({
					scope: 'scope.userLocation',
					success: () => {
						this.getLocation();
					},
					fail: (err) => {
						console.log('用户拒绝授权', err);
						// 引导用户到设置页开启权限
						uni.showModal({
							title: '位置信息授权',
							content: '我们需要访问您的位置信息，请授权。',
							success: (res) => {
								if (res.confirm) {
									// 打开小程序设置界面
									uni.openSetting({
										success: (settingRes) => {
											if (settingRes.authSetting[
													'scope.userLocation']) {
												this.getLocation();
											}
										}
									});
								}
							}
						});
					}
				});
			},
			getLocation() {
				uni.getLocation({
					type: 'gcj02', // 返回可用于 map 组件的坐标
					success: (res) => {
						console.log('定位成功', res);
						this.longitude = res.longitude;
						this.latitude = res.latitude;
					},
					fail: (err) => {
						console.log('定位失败', err);
					}
				});
			},
			pullUpData(e) {
				console.log(e);
				this.longitude = e.lng
				this.latitude = e.lat
				this.isMap = true
				this.$nextTick(() => {
					this.updateMapCenter()
				})
			},
			updateMapCenter() {
				this.markers = [{
					id: 1,
					longitude: this.longitude,
					latitude: this.latitude,
					title: '当前位置',
					iconPath: '../../static/images/carfield.png',
					width: 50,
					height: 50
				}];
			},
			markertap(e) {
				console.log(e.markerId);
			},
			onisMap(e){
				console.log(e);
				
				this.isMap = true
			}
		}
	};
</script>

<style lang="scss">
	.map_wrap {
		position: relative;
	}

	.map {
		width: 100%;
		height: 100vh;
	}

	.search_wrap {
		position: absolute;
		top: 10rpx;
		left: 10rpx;
		width: calc(100% - 20rpx);
		display: flex;
		align-items: center;
		justify-content: space-between;
		z-index: 999;
	}

	.search {
		width: 700rpx;
	}
</style>