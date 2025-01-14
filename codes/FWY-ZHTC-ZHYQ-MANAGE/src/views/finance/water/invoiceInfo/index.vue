<template>
  <div class="app-container">
    <div class="stat">
      <el-row :gutter="20">
        <el-col :span="4">
          今日开票金额：{{todayInvoiceMoney}} 元
        </el-col>
        <el-col :span="4">
          本月开票金额：{{monthInvoiceMoney}} 元
        </el-col>
      </el-row>
    </div>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="98px">
      <el-form-item label="开票类型" prop="invoiceType">
        <el-select v-model="queryParams.invoiceType" placeholder="请选择开票类型" clearable>
          <el-option
            v-for="dict in dict.type.bus_invoice_issuance_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开票状态" prop="invoiceStatus">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.bus_invoice_issuance_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开票订单类型" prop="orderType">
        <el-select v-model="queryParams.orderType" placeholder="请选择开票订单类型" clearable>
          <el-option
            v-for="dict in dict.type.bus_invoice_order_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phoneNumber">
        <el-input
          v-model="queryParams.phoneNumber"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="invoiceInfoList">
      <el-table-column label="申请人手机号" align="center" prop="phoneNumber" />
      <el-table-column label="开票订单类型" align="center" prop="orderType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_invoice_order_type" :value="scope.row.orderType"/>
        </template>
      </el-table-column>
      <el-table-column label="开票类型" align="center" prop="invoiceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_invoice_issuance_type" :value="scope.row.invoiceType"/>
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="amount" />
      <el-table-column label="开票状态" align="center" prop="invoiceStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_invoice_issuance_status" :value="scope.row.invoiceStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="发票抬头" align="center" prop="invoiceTitle" />
      <el-table-column label="开票时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="handleDetail(scope.row)"
            v-hasPermi="['operate:invoiceInfo:list']"
          >查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看明细 -->
    <el-dialog title="明细" :visible.sync="open" width="800px" append-to-body>
      <el-table v-loading="detailLoading" :data="invoiceDetailList">
        <el-table-column label="订单号" align="center" prop="orderSn" width="200" />
        <el-table-column label="车牌号" align="center" prop="licensePlateNumber" />
<!--        <el-table-column label="会员名称" align="center" prop="userName" />-->
<!--        <el-table-column label="手机号码" align="center" prop="phoneNumber" />-->
        <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center" prop="endTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单金额(元)" align="center" prop="orderAmount" />
      </el-table>
      <pagination
        v-show="totalDetail>0"
        :total="totalDetail"
        :page.sync="queryParamsDetail.pageNum"
        :limit.sync="queryParamsDetail.pageSize"
        @pagination="getInvoiceDetailList"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listInvoiceInfo, getStatData } from "@/api/finance/invoiceInfo";
import { listInvoiceDetailInfo } from "@/api/finance/invoiceDetailInfo";

export default {
  name: "InvoiceInfo",
  dicts: ['bus_invoice_issuance_type', 'bus_invoice_order_type', 'bus_invoice_issuance_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 发票记录表格数据
      invoiceInfoList: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        userInvoiceId: null,
        orderType: null,
        invoiceType: null,
        amount: null,
        invoiceStatus: null,
        invoiceTitle: null,
        invoiceTime: null,
      },
      todayInvoiceMoney: 0,
      monthInvoiceMoney: 0,
      detailLoading: false,
      invoiceDetailList: [],
      totalDetail: 0,
      queryParamsDetail: {
        pageNum: 1,
        pageSize: 10,
      }
    };
  },
  created() {
    getStatData().then(response => {
      this.todayInvoiceMoney = response.data.todayInvoiceMoney
      this.monthInvoiceMoney = response.data.monthInvoiceMoney
    })
    this.getList();
  },
  methods: {
    /** 查询发票记录列表 */
    getList() {
      this.loading = true;
      listInvoiceInfo(this.queryParams).then(response => {
        this.invoiceInfoList = response.rows;
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
    handleDetail(row){
      console.log("获取开票订单明细")
      this.open = true;
      const invoiceId = row.invoiceId
      this.queryParamsDetail.invoiceId = invoiceId;
      this.getInvoiceDetailList();
      // getMonthlyRentalCar(monthlyRentalCarId).then(response => {
      //   this.detailForm = response.data;
      //
      //   this.queryParamsOrder.monthlyRentalCarId = monthlyRentalCarId
      //   //查询该车辆订单记录
      //   this.getMonthlyOrderList()
      //
      //   this.detail.open = true;
      // });
    },
    getInvoiceDetailList(){
      this.detailLoading = true;
      listInvoiceDetailInfo(this.queryParamsDetail).then(response => {
        this.invoiceDetailList = response.rows;
        this.totalDetail = response.total;
        this.detailLoading = false;
      });
    }
  }
};
</script>
<style lang="scss" scoped>
  .stat{
    margin-bottom: 28px;
  }
</style>
