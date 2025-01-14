<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日期" prop="date">
        <el-date-picker clearable
          v-model="queryParams.date"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择日期">
        </el-date-picker>
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
          v-hasPermi="['finance:roadsideFeeReport:add']"
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
          v-hasPermi="['finance:roadsideFeeReport:edit']"
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
          v-hasPermi="['finance:roadsideFeeReport:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['finance:roadsideFeeReport:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roadsideFeeReportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="financeRoadsideFeeId" />
      <el-table-column label="日期" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="互联网收费金额(元)" align="center" prop="appChargeFee" />
      <el-table-column label="互联网收费占比" align="center" prop="appChargeFeeRate" />
      <el-table-column label="ETC无感收费金额" align="center" prop="etcChargeFee" />
      <el-table-column label="ETC无感收费占比" align="center" prop="etcChargeFeeRate" />
      <el-table-column label="现场收费金额(元)" align="center" prop="offlineChargeFee" />
      <el-table-column label="现场收费占比" align="center" prop="offlineChargeFeeRate" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['finance:roadsideFeeReport:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['finance:roadsideFeeReport:remove']"
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

    <!-- 添加或修改路侧收费终端占比对话框 -->
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
        <el-form-item label="互联网收费金额(元)" prop="appChargeFee">
          <el-input v-model="form.appChargeFee" placeholder="请输入互联网收费金额(元)" />
        </el-form-item>
        <el-form-item label="互联网收费占比" prop="appChargeFeeRate">
          <el-input v-model="form.appChargeFeeRate" placeholder="请输入互联网收费占比" />
        </el-form-item>
        <el-form-item label="ETC无感收费金额" prop="etcChargeFee">
          <el-input v-model="form.etcChargeFee" placeholder="请输入ETC无感收费金额" />
        </el-form-item>
        <el-form-item label="ETC无感收费占比" prop="etcChargeFeeRate">
          <el-input v-model="form.etcChargeFeeRate" placeholder="请输入ETC无感收费占比" />
        </el-form-item>
        <el-form-item label="现场收费金额(元)" prop="offlineChargeFee">
          <el-input v-model="form.offlineChargeFee" placeholder="请输入现场收费金额(元)" />
        </el-form-item>
        <el-form-item label="现场收费占比" prop="offlineChargeFeeRate">
          <el-input v-model="form.offlineChargeFeeRate" placeholder="请输入现场收费占比" />
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
import { listRoadsideFeeReport, getRoadsideFeeReport, delRoadsideFeeReport, addRoadsideFeeReport, updateRoadsideFeeReport } from "@/api/finance/roadsideFeeReport";

export default {
  name: "RoadsideFeeReport",
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
      // 路侧收费终端占比表格数据
      roadsideFeeReportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        date: null,
        appChargeFee: null,
        appChargeFeeRate: null,
        etcChargeFee: null,
        etcChargeFeeRate: null,
        offlineChargeFee: null,
        offlineChargeFeeRate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        date: [
          { required: true, message: "日期不能为空", trigger: "blur" }
        ],
        appChargeFee: [
          { required: true, message: "互联网收费金额(元)不能为空", trigger: "blur" }
        ],
        appChargeFeeRate: [
          { required: true, message: "互联网收费占比不能为空", trigger: "blur" }
        ],
        etcChargeFee: [
          { required: true, message: "ETC无感收费金额不能为空", trigger: "blur" }
        ],
        etcChargeFeeRate: [
          { required: true, message: "ETC无感收费占比不能为空", trigger: "blur" }
        ],
        offlineChargeFee: [
          { required: true, message: "现场收费金额(元)不能为空", trigger: "blur" }
        ],
        offlineChargeFeeRate: [
          { required: true, message: "现场收费占比不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询路侧收费终端占比列表 */
    getList() {
      this.loading = true;
      listRoadsideFeeReport(this.queryParams).then(response => {
        this.roadsideFeeReportList = response.rows;
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
        financeRoadsideFeeId: null,
        date: null,
        appChargeFee: null,
        appChargeFeeRate: null,
        etcChargeFee: null,
        etcChargeFeeRate: null,
        offlineChargeFee: null,
        offlineChargeFeeRate: null,
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.financeRoadsideFeeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加路侧收费终端占比";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const financeRoadsideFeeId = row.financeRoadsideFeeId || this.ids
      getRoadsideFeeReport(financeRoadsideFeeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改路侧收费终端占比";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.financeRoadsideFeeId != null) {
            updateRoadsideFeeReport(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRoadsideFeeReport(this.form).then(response => {
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
      const financeRoadsideFeeIds = row.financeRoadsideFeeId || this.ids;
      this.$modal.confirm('是否确认删除路侧收费终端占比编号为"' + financeRoadsideFeeIds + '"的数据项？').then(function() {
        return delRoadsideFeeReport(financeRoadsideFeeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/roadsideFeeReport/export', {
        ...this.queryParams
      }, `roadsideFeeReport_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
