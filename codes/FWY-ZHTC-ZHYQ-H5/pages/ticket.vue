<template>
	<view class="box">
		<view ref="receiptContent" class="receipt">
			<!-- 小票内容 -->
			<text>道路停车凭证</text>
			<text>运营方：杭州万物互联智慧产业有限公司</text>
			<text>服务咨询电话：400-926-0127</text>
			<text>工号：2022101703610</text>
			<text>停车路段：万都豪庭</text>
			<text>泊位号：6010613</text>
			<text>车牌号：京A11111【蓝牌】</text>
			<text>驶入时间：2024-12-23 12:11:08</text>
			<text>打印时间：2024-12-23 17:11:08</text>
			<text>总历史欠费：0.00元</text>
			<text>缴费平台：长沙城市通APP</text>
			<text>长沙城市通线下缴款二维码</text>
			<text>本泊位系利用城市道路等公共资源划定，按管理成本 收取停车泊位费。(不含保管费)</text>
			<text>收费标准：按嵘发改费[2014]115号文件执行。</text>
			<text>温馨提示</text>
			<text>您好!请在离场前妥善保曾好您的车辆及贵重物品，如有损毁被盗概不负责。本泊位是利用城市道路公 共资源划定、按管理成本收取公共停车费(不含保管费)。 服务监督电话0575-83795119</text>
			<text>嵘州城市通</text>
			<text>线下缴款二维码</text>
		</view>
		<button @click="printReceipt">打印小票</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// 初始化打印机插件实例
				sunmiPrint: null,
				parkingSpace:{},
			};
		},
		onReady() {
			// 在页面加载完成后初始化打印机插件实例
			this.initPrinter();
		},

		onLoad(e) {
			console.log(JSON.parse(decodeURIComponent(e.parkingSpace)),'车位传过来的数据================')
			this.parkingSpace = JSON.parse(decodeURIComponent(e.parkingSpace))
		},
		methods: {
			initPrinter() {
				// 根据您的打印机类型选择对应的插件引入方式
				this.sunmiPrint = uni.requireNativePlugin('Sunmi-Print-Inner');
				// 连接打印机
				this.sunmiPrint.connect(res => {
					if (res.connect === 'hello') {
						console.log('打印机成功连接');
					}
				});

			},
			printReceipt() {
				// 准备多行文本内容
				const receiptLines = [{
						text: "道路停车凭证",
						align: 0,
						size: 30,
						bold: true
					},
					{
						text: "运营方：杭州万物互联智慧产业有限公司",
						align: 0
					},
					{
						text: "服务咨询电话：400-926-0127",
						align: 0
					},
					{
						text: "工号：2022101703610",
						align: 0
					},
					{
						text: "停车路段：万都豪庭",
						align: 0
					},
					{
						text: "泊位号：6010613",
						align: 0
					},
					{
						text: "车牌号：京A11111【蓝牌】",
						align: 0
					},
					{
						text: "驶入时间：2024-12-23 12:11:08",
						align: 0
					},
					{
						text: "打印时间：2024-12-23 17:11:08",
						align: 0
					},
					{
						text: "总历史欠费：0.00元",
						align: 0
					},
					{
						text: "缴费平台：长沙城市通APP",
						align: 0
					},
					{
						text: "长沙城市通线下缴款二维码",
						align: 1
					},
					{
						text: "本泊位系利用城市道路等公共资源划定，按管理成本 收取停车泊位费。(不含保管费)",
						align: 0
					},
					{
						text: "收费标准：按嵘发改费[2014]115号文件执行。",
						align: 0
					},
					{
						text: "温馨提示",
						align: 1
					},
					{
						text: "您好!请在离场前妥善保曾好您的车辆及贵重物品，如有损毁被盗概不负责。本泊位是利用城市道路公 共资源划定、按管理成本收取公共停车费(不含保管费)。 服务监督电话0575-83795119",
						align: 0
					},
					{
						text: "嵘州城市通",
						align: 1
					},
					{
						text: "线下缴款二维码",
						align: 1
					},
				];
				const partingLine = [{
						style: "4",
						height: "30"
					},
					{
						style: "2",
						height: "3"
					},
					{
						style: "4",
						height: "30"
					},
				]

				this.printBySunmiPlugin(receiptLines); // 打印文本内容
				this.qrCode(); //打印二维码
				this.segmentation(partingLine); //打印分割线
			},

			printBySunmiPlugin(lines) {
				const lineSpacing = 1; // 行间距值

				// 遍历多行文本内容，并调用打印方法
				lines.forEach((line, index) => {
					this.sunmiPrint.printText(line); // 打印当前行文本

					// 如果不是最后一行，则添加行间距
					if (index < lines.length - 1) {
						this.addSpacing(lineSpacing);
					}
				});
			},
			//打印空行
			addSpacing(spacing) {
				for (let i = 0; i < spacing; i++) {
					this.sunmiPrint.printText({
						text: '\n',
						align: 0
					});
				}
			},
			//打印二维码
			qrCode() {
				this.sunmiPrint.printQrCode({
					text: "1234567890",
					size: 10,
					errorlevel: 0,
					align: 1
				})
			},
			//打印分隔线
			segmentation(item) {
				const lineSpacing = 4;
				item.forEach((line, index) => {
					// 打印当前行文本
					this.sunmiPrint.printDividingline(line)
					if (index < item.length - 1) {
						this.addSpacing(lineSpacing);
					}
				})
			}
		},
		beforeDestroy() {
			// 在页面销毁前断开打印机连接
			if (this.sunmiPrint) {
				this.sunmiPrint.disconnect();
			}
		}
	};
</script>

<style lang="scss">
	/* 小票样式 */
	.box {
		padding: 20rpx 60rpx;
	}

	.receipt {
		display: flex;
		flex-direction: column;
		justify-content: center;

		text {
			margin: 10rpx;
			/* 这里仅用于样式展示，实际打印时不生效 */
		}
	}
</style>