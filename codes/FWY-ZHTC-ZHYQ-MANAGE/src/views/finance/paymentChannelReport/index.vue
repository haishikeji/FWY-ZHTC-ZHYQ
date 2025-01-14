<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日期">
        <el-date-picker
          v-model="daterangeDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['finance:paymentChannelReport:add']"
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
          v-hasPermi="['finance:paymentChannelReport:edit']"
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
          v-hasPermi="['finance:paymentChannelReport:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['finance:paymentChannelReport:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentChannelReportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="时间" align="center">
        <el-table-column label="ID" align="center" prop="financePaymentChannelId" />
        <el-table-column label="日期" align="center" prop="date" width="100">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="收费方式" align="center">
        <el-table-column label="微信支付总额(元)" align="center" prop="wxpayAmount" />
        <el-table-column label="微信笔数" align="center" prop="wxpayNumber" />
        <el-table-column label="支付宝支付总额(元)" align="center" prop="alipayAmount" />
        <el-table-column label="支付宝笔数" align="center" prop="alipayNumber" />
        <el-table-column label="优惠券全免总额(元)" align="center" prop="ticketAmount" />
        <el-table-column label="优惠券全免笔数" align="center" prop="ticketNumber" />
        <el-table-column label="现金总额(元)" align="center" prop="cashAmount" />
        <el-table-column label="现金笔数" align="center" prop="cashNumber" />
        <el-table-column label="免费笔数" align="center" prop="freeNumber" />
      </el-table-column>

      <el-table-column label="收费终端" align="center">
        <el-table-column label="小程序笔数" align="center" prop="appletNumber" />
        <el-table-column label="小程序总额(元)" align="center" prop="appletAmount" />
        <el-table-column label="PDA笔数" align="center" prop="pdaNumber" />
        <el-table-column label="PDA总额(元)" align="center" prop="pdaAmount" />
        <el-table-column label="H5笔数" align="center" prop="h5Number" />
        <el-table-column label="H5总额(元)" align="center" prop="h5Amount" />
        <el-table-column label="ETC笔数" align="center" prop="etcNumber" />
        <el-table-column label="ETC总额(元)" align="center" prop="etcAmount" />
      </el-table-column>
      <el-table-column label="总计" align="center">
        <el-table-column label="退款笔数" align="center" prop="refundNumber" />
        <el-table-column label="退款总额(元)" align="center" prop="refundAmount" />
        <el-table-column label="优惠笔数" align="center" prop="discountsNumber" />
        <el-table-column label="优惠总额(元)" align="center" prop="discountsAmount" />
        <el-table-column label="总笔数" align="center" prop="totalNumber" />
        <el-table-column label="总总额(元)" align="center" prop="totalAmount" />
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['finance:paymentChannelReport:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['finance:paymentChannelReport:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改收费渠道报对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="日期" prop="date">
          <el-date-picker clearable
            v-model="form.date"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="微信支付总额(元)" prop="wxpayAmount">
          <el-input v-model="form.wxpayAmount" placeholder="请输入微信支付总额(元)" />
        </el-form-item>
        <el-form-item label="微信笔数" prop="wxpayNumber">
          <el-input v-model="form.wxpayNumber" placeholder="请输入微信笔数" />
        </el-form-item>
        <el-form-item label="支付宝支付总额(元)" prop="alipayAmount">
          <el-input v-model="form.alipayAmount" placeholder="请输入支付宝支付总额(元)" />
        </el-form-item>
        <el-form-item label="支付宝笔数" prop="alipayNumber">
          <el-input v-model="form.alipayNumber" placeholder="请输入支付宝笔数" />
        </el-form-item>
        <el-form-item label="优惠券全免总额(元)" prop="ticketAmount">
          <el-input v-model="form.ticketAmount" placeholder="请输入优惠券全免总额(元)" />
        </el-form-item>
        <el-form-item label="优惠券全免笔数" prop="ticketNumber">
          <el-input v-model="form.ticketNumber" placeholder="请输入优惠券全免笔数" />
        </el-form-item>
        <el-form-item label="现金总额(元)" prop="cashAmount">
          <el-input v-model="form.cashAmount" placeholder="请输入现金总额(元)" />
        </el-form-item>
        <el-form-item label="现金笔数" prop="cashNumber">
          <el-input v-model="form.cashNumber" placeholder="请输入现金笔数" />
        </el-form-item>
        <el-form-item label="免费笔数" prop="freeNumber">
          <el-input v-model="form.freeNumber" placeholder="请输入免费笔数" />
        </el-form-item>
        <el-form-item label="小程序笔数" prop="appletNumber">
          <el-input v-model="form.appletNumber" placeholder="请输入小程序笔数" />
        </el-form-item>
        <el-form-item label="小程序总额(元)" prop="appletAmount">
          <el-input v-model="form.appletAmount" placeholder="请输入小程序总额(元)" />
        </el-form-item>
        <el-form-item label="PDA笔数" prop="pdaNumber">
          <el-input v-model="form.pdaNumber" placeholder="请输入PDA笔数" />
        </el-form-item>
        <el-form-item label="PDA总额(元)" prop="pdaAmount">
          <el-input v-model="form.pdaAmount" placeholder="请输入PDA总额(元)" />
        </el-form-item>
        <el-form-item label="H5笔数" prop="h5Number">
          <el-input v-model="form.h5Number" placeholder="请输入H5笔数" />
        </el-form-item>
        <el-form-item label="H5总额(元)" prop="h5Amount">
          <el-input v-model="form.h5Amount" placeholder="请输入H5总额(元)" />
        </el-form-item>
        <el-form-item label="ETC笔数" prop="etcNumber">
          <el-input v-model="form.etcNumber" placeholder="请输入ETC笔数" />
        </el-form-item>
        <el-form-item label="ETC总额(元)" prop="etcAmount">
          <el-input v-model="form.etcAmount" placeholder="请输入ETC总额(元)" />
        </el-form-item>
        <el-form-item label="退款笔数" prop="refundNumber">
          <el-input v-model="form.refundNumber" placeholder="请输入退款笔数" />
        </el-form-item>
        <el-form-item label="退款总额(元)" prop="refundAmount">
          <el-input v-model="form.refundAmount" placeholder="请输入退款总额(元)" />
        </el-form-item>
        <el-form-item label="优惠笔数" prop="discountsNumber">
          <el-input v-model="form.discountsNumber" placeholder="请输入优惠笔数" />
        </el-form-item>
        <el-form-item label="优惠总额(元)" prop="discountsAmount">
          <el-input v-model="form.discountsAmount" placeholder="请输入优惠总额(元)" />
        </el-form-item>
        <el-form-item label="总笔数" prop="totalNumber">
          <el-input v-model="form.totalNumber" placeholder="请输入总笔数" />
        </el-form-item>
        <el-form-item label="总总额(元)" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入总总额(元)" />
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
import { listPaymentChannelReport, getPaymentChannelReport, delPaymentChannelReport, addPaymentChannelReport, updatePaymentChannelReport } from "@/api/finance/paymentChannelReport";

export default {
  name: "PaymentChannelReport",
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
      // 收费渠道报表格数据
      paymentChannelReportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 总总额(元)时间范围
      daterangeDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        date: null,
        wxpayAmount: null,
        wxpayNumber: null,
        alipayAmount: null,
        alipayNumber: null,
        ticketAmount: null,
        ticketNumber: null,
        cashAmount: null,
        cashNumber: null,
        freeNumber: null,
        appletNumber: null,
        appletAmount: null,
        pdaNumber: null,
        pdaAmount: null,
        h5Number: null,
        h5Amount: null,
        etcNumber: null,
        etcAmount: null,
        refundNumber: null,
        refundAmount: null,
        discountsNumber: null,
        discountsAmount: null,
        totalNumber: null,
        totalAmount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        date: [
          { required: true, message: "日期不能为空", trigger: "blur" }
        ],
        wxpayAmount: [
          { required: true, message: "微信支付总额(元)不能为空", trigger: "blur" }
        ],
        wxpayNumber: [
          { required: true, message: "微信笔数不能为空", trigger: "blur" }
        ],
        alipayAmount: [
          { required: true, message: "支付宝支付总额(元)不能为空", trigger: "blur" }
        ],
        alipayNumber: [
          { required: true, message: "支付宝笔数不能为空", trigger: "blur" }
        ],
        ticketAmount: [
          { required: true, message: "优惠券全免总额(元)不能为空", trigger: "blur" }
        ],
        ticketNumber: [
          { required: true, message: "优惠券全免笔数不能为空", trigger: "blur" }
        ],
        cashAmount: [
          { required: true, message: "现金总额(元)不能为空", trigger: "blur" }
        ],
        cashNumber: [
          { required: true, message: "现金笔数不能为空", trigger: "blur" }
        ],
        freeNumber: [
          { required: true, message: "免费笔数不能为空", trigger: "blur" }
        ],
        appletNumber: [
          { required: true, message: "小程序笔数不能为空", trigger: "blur" }
        ],
        appletAmount: [
          { required: true, message: "小程序总额(元)不能为空", trigger: "blur" }
        ],
        pdaNumber: [
          { required: true, message: "PDA笔数不能为空", trigger: "blur" }
        ],
        pdaAmount: [
          { required: true, message: "PDA总额(元)不能为空", trigger: "blur" }
        ],
        h5Number: [
          { required: true, message: "H5笔数不能为空", trigger: "blur" }
        ],
        h5Amount: [
          { required: true, message: "H5总额(元)不能为空", trigger: "blur" }
        ],
        etcNumber: [
          { required: true, message: "ETC笔数不能为空", trigger: "blur" }
        ],
        etcAmount: [
          { required: true, message: "ETC总额(元)不能为空", trigger: "blur" }
        ],
        refundNumber: [
          { required: true, message: "退款笔数不能为空", trigger: "blur" }
        ],
        refundAmount: [
          { required: true, message: "退款总额(元)不能为空", trigger: "blur" }
        ],
        discountsNumber: [
          { required: true, message: "优惠笔数不能为空", trigger: "blur" }
        ],
        discountsAmount: [
          { required: true, message: "优惠总额(元)不能为空", trigger: "blur" }
        ],
        totalNumber: [
          { required: true, message: "总笔数不能为空", trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: "总总额(元)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询收费渠道报列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeDate && '' != this.daterangeDate) {
        this.queryParams.params["beginDate"] = this.daterangeDate[0];
        this.queryParams.params["endDate"] = this.daterangeDate[1];
      }
      listPaymentChannelReport(this.queryParams).then(response => {
        this.paymentChannelReportList = response.rows;
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
        financePaymentChannelId: null,
        date: null,
        wxpayAmount: null,
        wxpayNumber: null,
        alipayAmount: null,
        alipayNumber: null,
        ticketAmount: null,
        ticketNumber: null,
        cashAmount: null,
        cashNumber: null,
        freeNumber: null,
        appletNumber: null,
        appletAmount: null,
        pdaNumber: null,
        pdaAmount: null,
        h5Number: null,
        h5Amount: null,
        etcNumber: null,
        etcAmount: null,
        refundNumber: null,
        refundAmount: null,
        discountsNumber: null,
        discountsAmount: null,
        totalNumber: null,
        totalAmount: null,
        createTime: null,
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
      this.daterangeDate = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.financePaymentChannelId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加收费渠道报";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const financePaymentChannelId = row.financePaymentChannelId || this.ids
      getPaymentChannelReport(financePaymentChannelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改收费渠道报";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.financePaymentChannelId != null) {
            updatePaymentChannelReport(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPaymentChannelReport(this.form).then(response => {
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
      const financePaymentChannelIds = row.financePaymentChannelId || this.ids;
      this.$modal.confirm('是否确认删除收费渠道报编号为"' + financePaymentChannelIds + '"的数据项？').then(function() {
        return delPaymentChannelReport(financePaymentChannelIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/paymentChannelReport/export', {
        ...this.queryParams
      }, `paymentChannelReport_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
