<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="98px">
      <el-form-item label="车牌颜色" prop="licensePlateType">
        <el-select v-model="queryParams.licensePlateType" placeholder="请选择车牌颜色" clearable>
          <el-option
            v-for="dict in dict.type.bus_order_license_plate_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="欠费金额小于" prop="minAmount">
        <el-input-number
          v-model="numberRange.minAmount"
          placeholder="欠费金额小于"
          clearable
          :min="0"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="欠费金额大于" prop="maxAmount">
        <el-input-number
          v-model="numberRange.maxAmount"
          placeholder="欠费金额大于"
          clearable
          :min="0"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="欠费时间" prop="dateRange">
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
      <el-form-item label="车牌号码" prop="licensePlateNumber">
        <el-input
          v-model="queryParams.licensePlateNumber"
          placeholder="请输入车牌号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['finance:notMemberArrearsReport:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="notMemberArrearsReportList" >
      <el-table-column label="车牌号码" align="center" prop="licensePlateNumber" />
      <el-table-column label="车牌颜色" align="center" prop="licensePlateType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_order_license_plate_type" :value="scope.row.licensePlateType"/>
        </template>
      </el-table-column>
      <el-table-column label="欠费订单数" align="center" prop="arrearsOrderNumber" />
      <el-table-column label="欠费总金额(元)" align="center" prop="arrearsOrderRealityAmount" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { listNotMemberArrearsReport } from "@/api/finance/notMemberArrearsReport";
import InputNumberRange from "@/components/inputNumberRange.vue"

export default {
  name: "NotMemberArrearsReport",
  dicts: ['bus_order_license_plate_type'],
  components: {
    InputNumberRange,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 非会员欠费报表表格数据
      notMemberArrearsReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        licencePlateSn: null,
        licensePlateType: null,
        arrearsOrderNumber: null,
        arrearsOrderAmount: null
      },
      // 日期范围
      dateRange: [],
      numberRange: {
        minAmount:null,
        maxAmount:null
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询非会员欠费报表列表 */
    getList() {
      this.loading = true;
      let param = this.addDateRange(this.queryParams, this.dateRange);
      param.params['minAmount'] = this.numberRange.minAmount;
      param.params['maxAmount'] = this.numberRange.maxAmount;
      listNotMemberArrearsReport(param).then(response => {
        this.notMemberArrearsReportList = response.rows;
        this.total = response.total;
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
      this.dateRange = []
      this.numberRange= {
        minAmount: null,
        maxAmount: null
      }
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/notMemberArrearsReport/export', {
        ...this.queryParams
      }, `notMemberArrearsReport_${new Date().getTime()}.xlsx`)
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
