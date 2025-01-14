<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="活动统计" name="stat">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 280px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入活动、活动名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="activityStatisticsList" show-summary :summary-method="getSummaries" >
          <el-table-column label="活动名称" align="center" prop="activityName" />
          <el-table-column label="优惠券名称" align="center" prop="couponName" />
          <el-table-column label="优惠类型" align="center" prop="type">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.bus_coupon_type" :value="scope.row.type"/>
            </template>
          </el-table-column>
          <el-table-column label="已领取数量" align="center" prop="receivedQuantity" />
          <el-table-column label="已领取面额（元）" align="center" prop="receivedMoney" />
          <el-table-column label="已使用数量" align="center" prop="usedQuantity" />
          <el-table-column label="已使用面额（元）" align="center" prop="usedMoney" />
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />

      </el-tab-pane>
      <el-tab-pane label="活动数据分析" name="analysis">
        <div ref="chart_analysis" style="width: 900px; height: 600px"></div>
      </el-tab-pane>
      <el-tab-pane label="趋势图表" name="chart">
        <div ref="chart" style="width: 900px; height: 600px"></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { pageStatisticsActivity } from "@/api/operate/activity";
import * as echarts from "echarts";

export default {
  name: "Coupon",
  dicts: ['bus_coupon_type'],
  data() {
    return {
      activeName: 'stat',
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 活动表格数据
      activityStatisticsList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      // 日期范围
      dateRange: [],
      // 图表实例
      chartInstance: null,
      option: {}, // 图表配置项
      // 图表实例
      chartInstanceAnalysis: null,
      optionAnalysis: {}, // 图表配置项
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

    // 获取图表容器
    const chartContainerAnalysis = this.$refs.chart_analysis;
    // 初始化图表实例
    this.chartInstanceAnalysis = echarts.init(chartContainerAnalysis);

    // 监听浏览器窗口大小变化事件
    window.addEventListener('resize', () => {
      this.chartInstanceAnalysis.resize();
    });
  },
  created() {
    this.getList();
  },
  methods: {
    handleClick(tab, event) {
      this.getList();
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if(column.property === 'receivedQuantity'
          || column.property === 'receivedMoney'
          || column.property === 'usedQuantity'
          || column.property === 'usedMoney'
        ){
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            // sums[index] += ' 元';
          } else {
            // sums[index] = '';
          }
        }
      });

      return sums;
    },
    // 初始化图表
    initChartAnalysis() {
      //这里处理数据
      let dateList = ['2024-06-18','2024-06-19','2024-06-20','2024-06-21','2024-06-22','2024-06-23','2024-06-24'];
      let dataSourcwList = [120, 200, 150, 80, 70, 110, 130];

      // 设置图表配置项
      this.optionAnalysis = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dateList
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: dataSourcwList,
            type: 'bar'
          }
        ]
      };

      // 使用配置项渲染图表
      this.chartInstanceAnalysis.setOption(this.optionAnalysis, true);
    },
    // 初始化图表
    initChart() {
      //这里处理数据
      let dateList = ['2024-06-18','2024-06-19','2024-06-20','2024-06-21','2024-06-22','2024-06-23','2024-06-24'];
      let dataSourcwList = [1,4,5,9,7,6,8];

      // 设置图表配置项
      this.option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dateList
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: dataSourcwList,
            type: 'line',
            smooth: true
          }
        ]
      };

      // 使用配置项渲染图表
      this.chartInstance.setOption(this.option, true);
    },
    /** 查询活动列表 */
    getList() {
      this.loading = true;
      pageStatisticsActivity(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.activityStatisticsList = response.rows;
        this.total = response.total;
        if (this.activeName == 'analysis'){
          // 在组件挂载后初始化图表
          this.optionAnalysis = {}
          this.initChartAnalysis();
        }
        if (this.activeName == 'chart'){
          // 在组件挂载后初始化图表
          this.option = {}
          this.initChart();
        }
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
  }
};
</script>
