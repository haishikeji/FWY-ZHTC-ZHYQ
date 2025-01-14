<template>
  <div class="app-container">
<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['finance:subscriptionRecovery:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="subscriptionRecoveryList" @selection-change="handleSelectionChange">
      <el-table-column label="业务日期" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="推送类型" align="center" prop="type" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_subscription_push_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="推送订阅人数/车辆数" align="center" prop="pushNumber" />
      <el-table-column label="已缴会员数/车辆数" align="center" prop="paidNumber" />
      <el-table-column label="已缴订单笔数" align="center" prop="paidOrderNumber" />
      <el-table-column label="已缴订单金额(元)" align="center" prop="paidOrderAmount" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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

    <!-- 添加或修改订阅消息追缴对话框 -->
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
        <el-form-item label="推送类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择推送类型">
            <el-option
              v-for="dict in dict.type.bus_subscription_push_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="推送订阅人数/车辆数" prop="pushNumber">
          <el-input v-model="form.pushNumber" placeholder="请输入推送订阅人数/车辆数" />
        </el-form-item>
        <el-form-item label="已缴会员数/车辆数" prop="paidNumber">
          <el-input v-model="form.paidNumber" placeholder="请输入已缴会员数/车辆数" />
        </el-form-item>
        <el-form-item label="已缴订单笔数" prop="paidOrderNumber">
          <el-input v-model="form.paidOrderNumber" placeholder="请输入已缴订单笔数" />
        </el-form-item>
        <el-form-item label="已缴订单金额(元)" prop="paidOrderAmount">
          <el-input v-model="form.paidOrderAmount" placeholder="请输入已缴订单金额(元)" />
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
import { listSubscriptionRecovery, getSubscriptionRecovery, delSubscriptionRecovery, addSubscriptionRecovery, updateSubscriptionRecovery } from "@/api/finance/subscriptionRecovery";

export default {
  name: "SubscriptionRecovery",
  dicts: ['bus_subscription_push_type'],
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
      // 订阅消息追缴表格数据
      subscriptionRecoveryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        date: null,
        type: null,
        pushNumber: null,
        paidNumber: null,
        paidOrderNumber: null,
        paidOrderAmount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订阅消息追缴列表 */
    getList() {
      this.loading = true;
      listSubscriptionRecovery(this.queryParams).then(response => {
        this.subscriptionRecoveryList = response.rows;
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
        subscriptionRecoveryId: null,
        date: null,
        type: null,
        pushNumber: null,
        paidNumber: null,
        paidOrderNumber: null,
        paidOrderAmount: null,
        createTime: null
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
      this.ids = selection.map(item => item.subscriptionRecoveryId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订阅消息追缴";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const subscriptionRecoveryId = row.subscriptionRecoveryId || this.ids
      getSubscriptionRecovery(subscriptionRecoveryId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订阅消息追缴";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.subscriptionRecoveryId != null) {
            updateSubscriptionRecovery(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSubscriptionRecovery(this.form).then(response => {
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
      const subscriptionRecoveryIds = row.subscriptionRecoveryId || this.ids;
      this.$modal.confirm('是否确认删除订阅消息追缴编号为"' + subscriptionRecoveryIds + '"的数据项？').then(function() {
        return delSubscriptionRecovery(subscriptionRecoveryIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/subscriptionRecovery/export', {
        ...this.queryParams
      }, `subscriptionRecovery_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
