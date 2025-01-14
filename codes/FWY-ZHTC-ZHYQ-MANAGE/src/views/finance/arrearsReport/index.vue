<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="88px">
      <el-form-item label="停车场类型" prop="parkAreaTypeId">
        <el-select v-model="queryParams.parkAreaTypeId" clearable  placeholder="请选择停车场类型" @change="handleAreaTypeChangeForQuery">
          <el-option
            v-for="item in areaTypeQueryOptions"
            :key="item.parkAreaTypeId"
            :label="item.name"
            :value="item.parkAreaTypeId"
          ></el-option>
        </el-select>
      </el-form-item>
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
          v-hasPermi="['finance:arrearsReport:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="arrearsReportList">
      <el-table-column label="欠费时长" align="center" prop="duration" />
      <el-table-column label="欠费订单数" align="center" prop="orderNumber" />
      <el-table-column label="欠费总金额(元)" align="center" prop="totalAmount" />
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
import { listArrearsReport } from "@/api/finance/arrearsReport";
import { listParkAreaType } from "@/api/business/ParkAreaType";
import { listParkArea } from "@/api/business/ParkArea";

export default {
  name: "ArrearsReport",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 欠费坏账报表表格数据
      arrearsReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkAreaTypeId: null,
        parkAreaId: null,
      },
      //停车场类型-查询
      areaTypeQueryOptions:[],
      //停车场数据-查询
      areaQueryOptions:[],
    };
  },
  created() {
    this.getList();
    this.getAreaTypeList();
    this.getAreaList();
  },
  methods: {
    /** 查询停车场类型列表 */
    getAreaTypeList() {
      listParkAreaType().then(response => {
        this.areaTypeQueryOptions = response.data;
      });
    },
    /** 查询停车场列表 */
    getAreaList() {
      listParkArea().then(response => {
        this.areaQueryOptions = response.data;
      });
    },
    /** 查询欠费坏账报表列表 */
    getList() {
      this.loading = true;
      listArrearsReport(this.queryParams).then(response => {
        this.arrearsReportList = response.rows;
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
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/arrearsReport/export', {
        ...this.queryParams
      }, `arrearsReport_${new Date().getTime()}.xlsx`)
    },
    //获取某类型下停车场数据
    handleAreaTypeChangeForQuery(value){
      this.areaQueryOptions = [];
      this.queryParams.parkAreaId = null;
      let param = {
        parkAreaTypeId: value
      }
      listParkArea(param).then(response => {
        this.areaQueryOptions = response.data;
      });
    },
  }
};
</script>
