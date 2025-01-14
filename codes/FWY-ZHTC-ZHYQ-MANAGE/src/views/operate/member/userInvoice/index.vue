<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="userId">
        <el-select
          v-model="queryParams.userId"
          filterable
          clearable
          remote
          reserve-keyword
          placeholder="请输入关键词"
          :remote-method="loadUserList"
          :loading="loading">
          <el-option
            v-for="item in userOptions"
            :key="item.nickName"
            :label="item.nickName"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="发票类型" prop="invoiceType">
        <el-select v-model="queryParams.invoiceType" placeholder="请选择发票类型" clearable>
          <el-option
            v-for="dict in dict.type.invoice_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入公司名称/抬头名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税号" prop="taxSn">
        <el-input
          v-model="queryParams.taxSn"
          placeholder="请输入税号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="注册地址" prop="regAddress">
        <el-input
          v-model="queryParams.regAddress"
          placeholder="请输入注册地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开户银行" prop="openBankName">
        <el-input
          v-model="queryParams.openBankName"
          placeholder="请输入开户银行"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="银行账号" prop="bankSn">
        <el-input
          v-model="queryParams.bankSn"
          placeholder="请输入银行账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="默认抬头" prop="isDefault">
        <el-select v-model="queryParams.isDefault" placeholder="请选择是否默认" clearable>
          <el-option
            v-for="dict in dict.type.global_boolean"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['operate:userInvoice:add']"
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
          v-hasPermi="['operate:userInvoice:edit']"
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
          v-hasPermi="['operate:userInvoice:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['operate:userInvoice:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userInvoiceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户" align="center" prop="userName" />
      <el-table-column label="发票类型" align="center" prop="invoiceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.invoice_type" :value="scope.row.invoiceType"/>
        </template>
      </el-table-column>
      <el-table-column label="公司名称/抬头名称" align="center" prop="name" />
      <el-table-column label="税号" align="center" prop="taxSn" />
      <el-table-column label="注册地址" align="center" prop="regAddress" />
      <el-table-column label="联系电话" align="center" prop="phonenumber" />
      <el-table-column label="开户银行" align="center" prop="openBankName" />
      <el-table-column label="银行账号" align="center" prop="bankSn" />
      <el-table-column label="是否默认" align="center" prop="isDefault">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.global_boolean" :value="scope.row.isDefault"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['operate:userInvoice:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['operate:userInvoice:remove']"
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

    <!-- 添加或修改发票抬头信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户" prop="userId">
<!--          <el-input v-model="form.userId" placeholder="请输入用户" />-->
          <el-select
            v-model="form.userId"
            filterable
            clearable
            remote
            reserve-keyword
            placeholder="请输入关键词"
            :remote-method="loadUserList"
            :loading="loading">
            <el-option
              v-for="item in userOptions"
              :key="item.nickName"
              :label="item.nickName"
              :value="item.userId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发票类型" prop="invoiceType">
          <el-select v-model="form.invoiceType" placeholder="请选择发票类型" @change="handleInvoiceType">
            <el-option
              v-for="dict in dict.type.invoice_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入公司名称/抬头名称" />
        </el-form-item>
        <el-form-item v-if="form.invoiceType && form.invoiceType == 1" label="税号" prop="taxSn">
          <el-input v-model="form.taxSn" placeholder="请输入税号" />
        </el-form-item>
        <el-form-item v-if="form.invoiceType == 1" label="注册地址" prop="regAddress">
          <el-input v-model="form.regAddress" placeholder="请输入注册地址" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item v-if="form.invoiceType == 1" label="开户银行" prop="openBankName">
          <el-input v-model="form.openBankName" placeholder="请输入开户银行" />
        </el-form-item>
        <el-form-item v-if="form.invoiceType == 1" label="银行账号" prop="bankSn">
          <el-input v-model="form.bankSn" placeholder="请输入银行账号" />
        </el-form-item>
<!--        <el-form-item label="默认抬头" prop="isDefault">-->
<!--          <el-input v-model="form.isDefault" placeholder="请输入默认抬头" />-->
<!--        </el-form-item>-->
        <el-form-item label="是否默认" prop="isDefault">
          <el-select v-model="form.isDefault" placeholder="请选择是否默认" clearable>
            <el-option
              v-for="dict in dict.type.global_boolean"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
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
  import { listUserInvoice, getUserInvoice, delUserInvoice, addUserInvoice, updateUserInvoice } from "@/api/operate/userInvoice";
  import { listUser } from "@/api/operate/user";

export default {
  name: "UserInvoice",
  dicts: ['invoice_type', 'global_boolean'],
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
      // 发票抬头信息表格数据
      userInvoiceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        invoiceType: null,
        name: null,
        taxSn: null,
        regAddress: null,
        phonenumber: null,
        openBankName: null,
        bankSn: null,
        isDefault: null,
      },
      userOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户不能为空", trigger: "blur" }
        ],
        invoiceType: [
          { required: true, message: "发票类型不能为空", trigger: "change" }
        ],
        name: [
          { required: true, message: "公司名称/抬头名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.loadUserList();
  },
  methods: {
    loadUserList(query) {
      listUser({
        nickName: query ? query : null,
      }).then(response => {
        this.userOptions = response.data;
      });
    },

    /** 查询发票抬头信息列表 */
    getList() {
      this.loading = true;
      listUserInvoice(this.queryParams).then(response => {
        this.userInvoiceList = response.rows;
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
        userInvoiceId: null,
        userId: null,
        invoiceType: 1,
        name: null,
        taxSn: null,
        regAddress: null,
        phonenumber: null,
        openBankName: null,
        bankSn: null,
        isDefault: '1',
        delFlag: null,
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
      this.ids = selection.map(item => item.userInvoiceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加发票抬头信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userInvoiceId = row.userInvoiceId || this.ids
      getUserInvoice(userInvoiceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改发票抬头信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userInvoiceId != null) {
            updateUserInvoice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserInvoice(this.form).then(response => {
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
      const userInvoiceIds = row.userInvoiceId || this.ids;
      this.$modal.confirm('是否确认删除发票抬头信息编号为"' + userInvoiceIds + '"的数据项？').then(function() {
        return delUserInvoice(userInvoiceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('operate/userInvoice/export', {
        ...this.queryParams
      }, `userInvoice_${new Date().getTime()}.xlsx`)
    },
    handleInvoiceType(e){
      console.log(e)
    }
  }
};
</script>
<style lang="scss" scoped>
  ::v-deep .el-select{
    width: 100% !important;
  }
  ::v-deep .el-date-editor{
    width: 100% !important;
  }
</style>
