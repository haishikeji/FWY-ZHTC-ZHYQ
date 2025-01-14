<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="用户" prop="userId">-->
<!--        <el-select-->
<!--          v-model="queryParams.userId"-->
<!--          filterable-->
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

      <el-form-item label="订单类型" prop="orderType">
        <el-select v-model="queryParams.orderType" placeholder="请选择订单类型" clearable>
          <el-option
            v-for="dict in dict.type.order_appeal_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in dict.type.order_check_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="退款状态" prop="refundStatus">
        <el-select v-model="queryParams.refundStatus" placeholder="请选择退款状态" clearable>
          <el-option
            v-for="dict in dict.type.order_refund_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间">
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
      <el-form-item label="订单号" prop="orderSn">
        <el-input
          v-model="queryParams.orderSn"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号码" prop="licensePlateNumber">
        <el-input
          v-model="queryParams.licensePlateNumber"
          placeholder="请输入车牌号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['operate:refund:add']"
        >发起退款</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="refundList" >
      <el-table-column label="订单类型" align="center" prop="orderType" width="120" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_appeal_type" :value="scope.row.orderType"/>
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="center" prop="orderSn" width="180" />
      <el-table-column label="车牌号码" align="center" prop="licensePlateNumber" width="100" />
      <el-table-column label="申请时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="checkStatus" width="120" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_check_status" :value="scope.row.checkStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="checkBy" />
      <el-table-column label="审核时间" align="center" prop="checkTime" width="180">
        <template slot-scope="scope">
          <el-tooltip :content="scope.row.checkRemark" placement="bottom" effect="light">
            <span>{{ parseTime(scope.row.checkTime) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="refundStatus" width="180" >
        <template slot-scope="scope">
          <dict-tag v-if="scope.row.checkStatus != '1'" :options="dict.type.order_refund_status" :value="scope.row.refundStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="退款时间" align="center" prop="refundTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.refundTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款备注" align="center" prop="remark" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['operate:refund:edit']"
          >查看</el-button>
          <el-button
            v-if="scope.row.checkStatus == '1'"
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleCheck(scope.row)"
            v-hasPermi="['operate:refund:check']"
          >审核</el-button>
          <el-button
            v-if="scope.row.checkStatus != '1'"
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleRefund(scope.row)"
            v-hasPermi="['operate:appeal:refund']"
          >退款</el-button>
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

    <!-- 详情 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="licensePlateNumber">
          <el-input :disabled="true" v-model="form.licensePlateNumber" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderSn">
          <el-input :disabled="true" v-model="form.orderSn" />
        </el-form-item>
        <el-form-item label="订单类型" prop="orderType">
          <el-select :disabled="true" v-model="form.orderType" placeholder="请选择订单类型">
            <el-option
              v-for="dict in dict.type.order_appeal_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态" prop="checkStatus">
          <el-radio-group :disabled="true" v-model="form.checkStatus">
            <el-radio
              v-for="dict in dict.type.order_check_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核说明" prop="checkRemark">
          <el-input :disabled="true" v-model="form.checkRemark" type="textarea" :rows="5" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 发起退款 -->
    <el-dialog title="发起退款" :visible.sync="openRefund" width="500px" append-to-body>
      <el-form ref="formRefund" :model="formRefund" :rules="rulesRefund" label-width="80px">
        <el-form-item label="车牌号码" prop="licensePlateNumber">
          <el-input v-model="formRefund.licensePlateNumber" placeholder="请输入车牌号码" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderSn">
          <el-input v-model="formRefund.orderSn" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNumber">
          <el-input v-model="formRefund.phoneNumber" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormRefund">确 定</el-button>
        <el-button @click="cancelRefund">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核 -->
    <el-dialog title="审核" :visible.sync="openCheck" width="500px" append-to-body>
      <el-form ref="formCheck" :model="formCheck" :rules="rulesCheck" label-width="80px">
        <el-form-item label="审核状态" prop="checkStatus">
          <el-radio-group v-model="formCheck.checkStatus">
            <el-radio
              v-for="dict in dict.type.order_check_status"
              :key="dict.value"
              :label="dict.value"
              v-if="dict.value != '1'"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核说明" prop="checkRemark">
          <el-input v-model="formCheck.checkRemark" type="textarea" :rows="5" placeholder="请输入审核说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormCheck">确 定</el-button>
        <el-button @click="cancelCheck">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 退款处理 -->
    <el-dialog title="处理退款" :visible.sync="openHandleRefund" width="500px" append-to-body>
      <el-form ref="formHandleRefund" :model="formHandleRefund" :rules="rulesHandleRefund" label-width="80px">
        <el-form-item label="车牌号" prop="licensePlateNumber">
          <el-input :disabled="true" v-model="formHandleRefund.licensePlateNumber" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderSn">
          <el-input :disabled="true" v-model="formHandleRefund.orderSn" />
        </el-form-item>
        <el-form-item label="审核说明" prop="checkRemark">
          <el-input :disabled="true" v-model="formHandleRefund.checkRemark" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item label="退款状态" prop="refundStatus">
          <el-radio-group v-model="formHandleRefund.refundStatus">
            <el-radio
              v-for="dict in dict.type.order_refund_status"
              :key="dict.value"
              :label="dict.value"
              v-if="dict.value != '1'"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="退款备注" prop="remark">
          <el-input v-model="formHandleRefund.remark" type="textarea" :rows="5" placeholder="请输入退款备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormHandleRefund">确 定</el-button>
        <el-button @click="cancelHandleRefund">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRefund, getRefund, delRefund, addRefund, updateRefund, checkOrderRefund, handleOrderRefund } from "@/api/operate/refund";
import { listUser } from "@/api/operate/user";

export default {
  name: "Refund",
  dicts: ['order_refund_status', 'order_appeal_type', 'order_check_status'],
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
      // 订单退款表格数据
      refundList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        orderType: null,
        licencePlateSn: null,
        orderId: null,
        refundTime: null,
        refundStatus: null,
      },
      // 用户数据
      userOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 日期范围
      dateRange: [],
      openRefund:false,
      formRefund:{},
      rulesRefund:{},
      openCheck:false,
      formCheck:{},
      rulesCheck:{},
      openHandleRefund:false,
      formHandleRefund:{},
      rulesHandleRefund:{},

    };
  },
  created() {
    this.loadUserList();
    this.getList();
  },
  methods: {

    loadUserList(query) {
      listUser({
        pageNum: 1,
        pageSize: 10,
        nickName: query ? query : null,
      }).then(response => {
        this.userOptions = response.rows;
      });
    },
    /** 查询订单退款列表 */
    getList() {
      this.loading = true;
      listRefund(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.refundList = response.rows;
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
        orderRefundId: null,
        userId: null,
        phoneNumber: null,
        licensePlateNumber: null,
        orderId: null,
        orderSn: null,
        orderType: null,
        refundTime: null,
        refundStatus: null,
        remark: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.openRefund = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const orderRefundId = row.orderRefundId || this.ids
      getRefund(orderRefundId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订单退款";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderRefundId != null) {
            updateRefund(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRefund(this.form).then(response => {
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
      const orderRefundIds = row.orderRefundId || this.ids;
      this.$modal.confirm('是否确认删除订单退款编号为"' + orderRefundIds + '"的数据项？').then(function() {
        return delRefund(orderRefundIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('operate/refund/export', {
        ...this.queryParams
      }, `refund_${new Date().getTime()}.xlsx`)
    },
    /** 提交按钮 */
    submitFormRefund() {
      this.$refs["formRefund"].validate(valid => {
        if (valid) {
          addRefund(this.formRefund).then(response => {
            this.$modal.msgSuccess("成功");
            this.openRefund = false;
            this.getList();
          });
        }
      })
    },
    // 取消按钮
    cancelRefund() {
      this.openRefund = false;
      this.resetRefund();
    },
    resetRefund(){
      this.formRefund = {
        orderSn: null,
      };
      this.resetForm("formRefund");
    },
    handleCheck(row){
      this.resetCheck();
      const orderRefundId = row.orderRefundId || this.ids
      getRefund(orderRefundId).then(response => {
        this.formCheck = response.data;
        this.openCheck = true;
      });
    },
    // 表单重置
    resetCheck() {
      this.formCheck = {
        orderRefundId: null,
        checkStatus: null,
        checkRemark: null,
      };
      this.resetForm("formCheck");
    },
    /** 提交按钮 */
    submitFormCheck() {
      this.$refs["formCheck"].validate(valid => {
        if (valid) {
          if (this.formCheck.orderRefundId != null) {
            checkOrderRefund(this.formCheck).then(response => {
              this.$modal.msgSuccess("审核成功");
              this.openCheck = false;
              this.getList();
            });
          }
        }
      })
    },
    // 取消按钮
    cancelCheck() {
      this.openCheck = false;
      this.resetCheck();
    },
    handleRefund(row){
      this.resetHandleRefund();
      const orderRefundId = row.orderRefundId || this.ids
      getRefund(orderRefundId).then(response => {
        this.formHandleRefund = response.data;
        this.openHandleRefund = true;
      });
    },
    submitFormHandleRefund(){
      this.$refs["formHandleRefund"].validate(valid => {
        if (valid) {
          if (this.formHandleRefund.orderRefundId != null) {
            handleOrderRefund(this.formHandleRefund).then(response => {
              this.$modal.msgSuccess("处理成功");
              this.openHandleRefund = false;
              this.getList();
            });
          }
        }
      })
    },
    cancelHandleRefund(){
      this.openHandleRefund = false;
      this.resetHandleRefund();
    },
    // 表单重置
    resetHandleRefund() {
      this.formHandleRefund = {
        orderRefundId: null,
        remark: null,
      };
      this.resetForm("formHandleRefund");
    },
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
