<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="日期">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item prop="type">
        <el-radio-group v-model="queryParams.type">
          <el-radio :label="0">按日</el-radio>
          <el-radio :label="1">按月</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['finance:temporaryReport:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="列表" name="list">
        <el-table v-loading="loading" :data="temporaryReportList" >
          <el-table-column label="日期" align="center" prop="date" width="180" />
          <el-table-column label="路侧应收(元)" align="center" prop="roadsideReceivableAmount" />
          <el-table-column label="路侧实收(元)" align="center" prop="roadsideRealityAmount" />
          <el-table-column label="路侧优惠(元)" align="center" prop="roadsideDiscountsAmount" />
          <el-table-column label="场库应收(元)" align="center" prop="garageReceivableAmount" />
          <el-table-column label="场库实收(元)" align="center" prop="garageRealityAmount" />
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
      <el-tab-pane label="趋势图" name="chart">
        <div ref="chart" style="width: 900px; height: 600px"></div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import { listTemporaryReport } from "@/api/finance/temporaryReport";
import * as echarts from "echarts";

export default {
  name: "TemporaryReport",
  data() {
    return {
      activeName: 'list',
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 临停收费报表格数据
      temporaryReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: 0
      },
      // 日期范围
      dateRange: [],
      // 图表实例
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
    this.getList();
  },
  methods: {
    handleClick(tab, event) {
      this.getList();
    },
    // 初始化图表
    initChart() {

      //获取列表的日期集合
      let dateList = [];
      let roadsideReceivableList = [];
      let roadsideRealityList = [];
      let roadsideDiscountsList = [];
      let garageReceivableList = [];
      let garageRealityList = [];

      this.temporaryReportList.forEach(
        item => {
          dateList.push(item.date);
          roadsideReceivableList.push(item.roadsideReceivableAmount)
          roadsideRealityList.push(item.roadsideRealityAmount)
          roadsideDiscountsList.push(item.roadsideDiscountsAmount)
          garageReceivableList.push(item.garageReceivableAmount)
          garageRealityList.push(item.garageRealityAmount)
        }
      )
      // 设置图表配置项
      this.option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['路侧应收', '路侧实收', '路侧优惠', '场库应收', '场库实收']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        // toolbox: {
        //   feature: {
        //     saveAsImage: {}
        //   }
        // },
        xAxis: {
          type: 'category',
          data: dateList
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '路侧应收',
            type: 'line',
            // stack: 'Total',
            // smooth: true,
            data: roadsideReceivableList
          },
          {
            name: '路侧实收',
            type: 'line',
            // stack: 'Total',
            // smooth: true,
            data: roadsideRealityList
          },
          {
            name: '路侧优惠',
            type: 'line',
            // stack: 'Total',
            // smooth: true,
            data: roadsideDiscountsList
          },
          {
            name: '场库应收',
            type: 'line',
            // stack: 'Total',
            // smooth: true,
            data: garageReceivableList
          },
          {
            name: '场库实收',
            type: 'line',
            // stack: 'Total',
            // smooth: true,
            data: garageRealityList
          },
        ]
      };

      // 使用配置项渲染图表
      this.chartInstance.setOption(this.option, true);
    },
    /** 查询临停收费报列表 */
    getList() {
      this.loading = true;
      listTemporaryReport(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.temporaryReportList = response.rows;
        this.total = response.total;
        this.initChart();
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/temporaryReport/export', {
        ...this.queryParams
      }, `temporaryReport_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style lang="scss" scoped>
  ::v-deep .el-select{
    width: 100% !important;
  }
  ::v-deep .el-input-number{
    width: 100% !important;
  }
  ::v-deep .el-date-editor{
    width: 100% !important;
  }
</style>
