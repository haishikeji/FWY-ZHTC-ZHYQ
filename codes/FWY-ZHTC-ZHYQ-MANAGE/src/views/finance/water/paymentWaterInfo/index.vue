<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderSn">
        <el-input
          v-model="queryParams.orderSn"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付流水号" prop="paymentSerialNumber">
        <el-input
          v-model="queryParams.paymentSerialNumber"
          placeholder="请输入支付流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退款流水号" prop="refundSerialNumber">
        <el-input
          v-model="queryParams.refundSerialNumber"
          placeholder="请输入退款流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户流水号" prop="merchantSerialNumber">
        <el-input
          v-model="queryParams.merchantSerialNumber"
          placeholder="请输入商户流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入交易金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" prop="paymentTime">
        <el-date-picker clearable
          v-model="queryParams.paymentTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择支付时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="支付结果" prop="paymentResult">
        <el-input
          v-model="queryParams.paymentResult"
          placeholder="请输入支付结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退款原因" prop="refundReason">
        <el-input
          v-model="queryParams.refundReason"
          placeholder="请输入退款原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退款结果" prop="refundResult">
        <el-input
          v-model="queryParams.refundResult"
          placeholder="请输入退款结果"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:paymentWaterInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:paymentWaterInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:paymentWaterInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:paymentWaterInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentWaterInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="支付流水ID" align="center" prop="paymentWaterId" />
      <el-table-column label="订单号" align="center" prop="orderSn" />
      <el-table-column label="支付流水号" align="center" prop="paymentSerialNumber" />
      <el-table-column label="退款流水号" align="center" prop="refundSerialNumber" />
      <el-table-column label="商户流水号" align="center" prop="merchantSerialNumber" />
      <el-table-column label="交易金额" align="center" prop="amount" />
      <el-table-column label="支付时间" align="center" prop="paymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="paymentStatus" />
      <el-table-column label="支付结果" align="center" prop="paymentResult" />
      <el-table-column label="类型(0-临停；1-包期)" align="center" prop="type" />
      <el-table-column label="退款原因" align="center" prop="refundReason" />
      <el-table-column label="退款状态" align="center" prop="refundStatus" />
      <el-table-column label="退款结果" align="center" prop="refundResult" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:paymentWaterInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:paymentWaterInfo:remove']"
          >删除</el-button>
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

    <!-- 添加或修改支付流水对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderSn">
          <el-input v-model="form.orderSn" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="支付流水号" prop="paymentSerialNumber">
          <el-input v-model="form.paymentSerialNumber" placeholder="请输入支付流水号" />
        </el-form-item>
        <el-form-item label="退款流水号" prop="refundSerialNumber">
          <el-input v-model="form.refundSerialNumber" placeholder="请输入退款流水号" />
        </el-form-item>
        <el-form-item label="商户流水号" prop="merchantSerialNumber">
          <el-input v-model="form.merchantSerialNumber" placeholder="请输入商户流水号" />
        </el-form-item>
        <el-form-item label="交易金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入交易金额" />
        </el-form-item>
        <el-form-item label="支付时间" prop="paymentTime">
          <el-date-picker clearable
            v-model="form.paymentTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="支付结果" prop="paymentResult">
          <el-input v-model="form.paymentResult" placeholder="请输入支付结果" />
        </el-form-item>
        <el-form-item label="退款原因" prop="refundReason">
          <el-input v-model="form.refundReason" placeholder="请输入退款原因" />
        </el-form-item>
        <el-form-item label="退款结果" prop="refundResult">
          <el-input v-model="form.refundResult" placeholder="请输入退款结果" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPaymentWaterInfo, getPaymentWaterInfo, delPaymentWaterInfo, addPaymentWaterInfo, updatePaymentWaterInfo } from "@/api/finance/paymentWaterInfo";

export default {
  name: "PaymentWaterInfo",
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
      // 支付流水表格数据
      paymentWaterInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderSn: null,
        paymentSerialNumber: null,
        refundSerialNumber: null,
        merchantSerialNumber: null,
        amount: null,
        paymentTime: null,
        paymentStatus: null,
        paymentResult: null,
        type: null,
        refundReason: null,
        refundStatus: null,
        refundResult: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderSn: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询支付流水列表 */
    getList() {
      this.loading = true;
      listPaymentWaterInfo(this.queryParams).then(response => {
        this.paymentWaterInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        paymentWaterId: null,
        orderSn: null,
        paymentSerialNumber: null,
        refundSerialNumber: null,
        merchantSerialNumber: null,
        amount: null,
        paymentTime: null,
        paymentStatus: null,
        paymentResult: null,
        type: null,
        refundReason: null,
        refundStatus: null,
        refundResult: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.paymentWaterId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加支付流水";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const paymentWaterId = row.paymentWaterId || this.ids
      getPaymentWaterInfo(paymentWaterId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改支付流水";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.paymentWaterId != null) {
            updatePaymentWaterInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPaymentWaterInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const paymentWaterIds = row.paymentWaterId || this.ids;
      this.$modal.confirm('是否确认删除支付流水编号为"' + paymentWaterIds + '"的数据项？').then(function() {
        return delPaymentWaterInfo(paymentWaterIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/paymentWaterInfo/export', {
        ...this.queryParams
      }, `paymentWaterInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
