<template>
  <div ref="chart" style="width: 100%; height: 100%"></div>
</template>

<script>
  import * as echarts from 'echarts';
  import { getRealTimeTrafficVolume } from "@/api/bigdata/largeScreen";

  export default {
    data() {
      return {
        chartInstance: null,
        option: {}, // 图表配置项
      };
    },
    mounted() {
      // 获取图表容器
      const chartContainer = this.$refs.chart;
      // 初始化图表实例
      this.chartInstance = echarts.init(chartContainer);

      // 监听浏览器窗口大小变化事件
      window.addEventListener('resize', () => {
        this.chartInstance.resize();
      });
    },
    created() {
      // 在组件挂载后初始化图表
      this.initChart();
    },
    methods: {
      initChart() {
        let dateList = []
        let roadsideList = []
        let warehouseList = []

        getRealTimeTrafficVolume().then(response => {
          response.data.forEach(
            item => {
              dateList.push(item.date);
              roadsideList.push(item.roadside)
              warehouseList.push(item.warehouse)
            }
          )

          // 设置图表配置项
          this.option = {
            animationDuration: 6000,
            tooltip: {
              trigger: 'axis', // 设置触发类型为坐标轴触发
            },
            title: {
              text: `车流量实时监测`,
              top: 10,
              left: 10,
              textStyle: {
                color: '#a6c6f3'
              }
            },
            legend: {
              data: ['路侧流量', '场库流量'],
              right: 10,
              top: 10,
              textStyle: {
                color: '#fff'
              }
            },
            xAxis: {
              type: 'category',
              data: dateList,
              axisLabel: {
                interval: 1,
                textStyle: {
                  color: "#fff",
                },
              },
            },
            yAxis: {
              type: 'value',
              axisLabel: {
                textStyle: {
                  color: "#fff",
                },
              },
              splitLine: {
                show: true,
                lineStyle: {
                  color: "#ffffff", // 设置为白色
                  type: "dashed", // 设置为虚线
                },
              },
            },
            grid: {
              top: "20%",
              bottom: "15%",
              left: "10%",
              right: "10%",
            },
            series: [
              {
                name: '路侧流量',
                data: roadsideList,
                type: 'line',
                smooth: true,
                itemStyle: {
                  color: '#1FC6FF', // 设置 Line 1 的颜色
                }
              },
              {
                name: '场库流量',
                data: warehouseList,
                type: 'line',
                smooth: true,
                itemStyle:{
                  color: '#FF911F' // 设置 Line 2 的颜色
                }
              }
            ]
          }

          // 使用配置项渲染图表
          this.chartInstance.setOption(this.option);
        })
      }
    }
  }
</script>

<style scoped>

</style>
