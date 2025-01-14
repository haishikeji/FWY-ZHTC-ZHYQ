<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-row>
        <el-form-item label="停车场" prop="parkAreaId">
          <el-select v-model="queryParams.parkAreaId" clearable placeholder="请选择停车场">
            <el-option
              v-for="item in areaQueryOptions"
              :key="item.parkAreaId"
              :label="item.name"
              :value="item.parkAreaId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收费员" prop="userId">
          <el-select v-model="queryParams.userId" clearable placeholder="请选择巡检员">
            <el-option
              v-for="item in sysUserOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="PDA设备" prop="devicePdaId">
          <el-select v-model="queryParams.devicePdaId" clearable placeholder="请选择PDA设备">
            <el-option
              v-for="item in devicePdaOptions"
              :key="item.devicePdaId"
              :label="item.code"
              :value="item.devicePdaId"
            />
          </el-select>
        </el-form-item>
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
      </el-row>
      <el-row>
        <el-form-item label=" " prop="type">
          <el-radio-group v-model="queryParams.type">
            <el-radio :label="0">按日</el-radio>
            <el-radio :label="1">按月</el-radio>
            <el-radio :label="2">按年</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['finance:artificialChargeReport:export']">导出</el-button>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

        </el-form-item>
      </el-row>
    </el-form>

    <el-table v-loading="loading" :data="artificialChargeReportList">
      <el-table-column label="日期" align="center" prop="date" width="180" />
      <el-table-column label="收费员" align="center" prop="userName" width="120" />
      <el-table-column label="PDA设备" align="center" prop="pdaDeviceCode" width="80" />
      <el-table-column label="缴费率" align="center" prop="paymentRate" width="80" />
      <el-table-column label="应收金额" align="center" prop="receivableAmount" width="180" />
      <el-table-column label="应收订单数" align="center" prop="receivableNumber" width="120" />
      <el-table-column label="收费金额(元)" align="center" prop="realityAmount" width="180" />
      <el-table-column label="收费订单数(含追缴)" align="center" prop="realityNumber" width="210" />
      <el-table-column label="追缴订单金额" align="center" prop="recoveredAmount" width="180" />
      <el-table-column label="追缴订单数" align="center" prop="recoveredNumber" width="180" />
      <el-table-column label="支付宝收费金额(元)" align="center" prop="alipayAmount" width="210" />
      <el-table-column label="微信收费金额(元)" align="center" prop="wxpayAmount" width="210" />
      <el-table-column label="现金收费金额(元)" align="center" prop="cashAmount" width="210" />
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
import { listArtificialChargeReport } from "@/api/finance/artificialChargeReport";
import { listParkArea } from "@/api/business/ParkArea";
import { allUser } from "@/api/system/user";
import { listDevicePda } from "@/api/device/devicePda";

export default {
  name: "ArtificialChargeReport",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 收费员收费报表格数据
      artificialChargeReportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //系统用户列表
      sysUserOptions:[],
      //停车场数据-查询
      areaQueryOptions:[],
      //pda设备数据
      devicePdaOptions:[],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: 0
      },
    };
  },
  created() {
    this.getAreaList();
    this.getSysUserList();
    this.getDevicePdaList()

    this.getList();
  },
  methods: {
    /** 查询系统用户列表 */
    getSysUserList() {
      allUser().then(response => {
        this.sysUserOptions = response.data;
      });
    },
    /** 查询pda设备列表 */
    getDevicePdaList() {
      listDevicePda().then(response => {
        this.devicePdaOptions = response.data;
      });
    },
    /** 查询停车场列表 */
    getAreaList() {
      listParkArea().then(response => {
        this.areaQueryOptions = response.data;
      });
    },
    /** 查询收费员收费报列表 */
    getList() {
      this.loading = true;
      listArtificialChargeReport(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.artificialChargeReportList = response.rows;
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
      this.daterangeDate = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/artificialChargeReport/export', {
        ...this.queryParams
      }, `artificialChargeReport_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
