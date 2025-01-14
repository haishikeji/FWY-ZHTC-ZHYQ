<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="88px">
<!--      <el-form-item label="用户" prop="userId">-->
<!--        <el-select-->
<!--          v-model="queryParams.userId"-->
<!--          filterable-->
<!--          clearable-->
<!--          remote-->
<!--          reserve-keyword-->
<!--          placeholder="请输入关键词"-->
<!--          :remote-method="loadUserList"-->
<!--          :loading="loading">-->
<!--          <el-option-->
<!--            v-for="item in userOptions"-->
<!--            :key="item.nickName"-->
<!--            :label="item.nickName"-->
<!--            :value="item.userId">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="支付时间">
        <el-date-picker
          v-model="dateRange_Pay"
          style="width: 280px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="注册时间">
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
      <el-form-item label="关键字" prop="keyWords">
        <el-input
          v-model="queryParams.keyWords"
          placeholder="请输入姓名、车牌号、手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="monthlyRentalCarList" show-summary :summary-method="getSummaries" >
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber"/>
      <el-table-column label="会员手机号" align="center" prop="phoneNumber"/>
      <el-table-column label="巡检员姓名" align="center" prop="inspectionUserName"/>
      <el-table-column label="包月开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="包月开始时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="包月金额(元)" align="center" prop="orderAmount"/>
      <el-table-column label="包月订单支付时间" align="center" prop="paymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会员注册时间" align="center" prop="registerTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会员名称" align="center" prop="userName"/>
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
  import { listMonthlyRentalCarForUser } from "@/api/operate/monthlyRentalCar";

  export default {
    name: "MonthlyRentalCar",
    dicts: ['bus_order_payment_type', 'sys_user_sex', 'bus_charge_cycle_type', 'bus_monthly_rental_source'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 包月车表格数据
        monthlyRentalCarList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          keyWords: null
        },
        // 日期范围
        dateRange: [],
        // 日期范围
        dateRange_Pay: [],
      };
    },
    created() {
      this.getList();
    },
    methods: {
      getSummaries(param) {
        const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if(column.property === 'orderAmount'){
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
      /** 查询包月车列表 */
      getList() {
        this.loading = true;
        let param = this.addDateRange(this.queryParams, this.dateRange, 'RegisterTime')
        let params = this.addDateRange(param, this.dateRange_Pay, 'PayTime')
        listMonthlyRentalCarForUser(params).then(response => {
          this.monthlyRentalCarList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
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

<style lang="scss" scoped>
  ::v-deep .el-select {
    width: 100% !important;
  }

  ::v-deep .el-input-number {
    width: 100% !important;
  }

  ::v-deep .el-date-editor {
    width: 100% !important;
  }
</style>
