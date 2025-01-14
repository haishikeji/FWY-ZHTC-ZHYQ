<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="停车场" prop="parkAreaId">
        <el-select v-model="queryParams.parkAreaId" clearable placeholder="请选择停车场">
          <el-option
            v-for="item in areaOptions"
            :key="item.parkAreaId"
            :label="item.name"
            :value="item.parkAreaId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="考勤状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="考勤状态"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_attendance_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="签到时间">
        <el-date-picker
          v-model="dateRangeSignIn"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="签退时间">
        <el-date-picker
          v-model="dateRangeSignOut"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
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

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['system:clockIn:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:clockIn:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['system:clockIn:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:clockIn:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="clockInList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日期" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="userName" />
      <el-table-column label="部门" align="center" prop="deptName" />
      <el-table-column label="所属停车场" align="center" prop="parkAreaName" />
      <el-table-column label="停车场类型" align="center" prop="parkAreaTypeName" />
      <el-table-column label="签到时间" align="center" prop="signInTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signInTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="签退时间" align="center" prop="signOutTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signOutTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="考勤状态" align="center" key="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_attendance_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:clockIn:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:clockIn:remove']"
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

    <!-- 添加或修改用户打卡记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="停车场ID" prop="parkAreaId">
          <el-input v-model="form.parkAreaId" placeholder="请输入停车场ID" />
        </el-form-item>
        <el-form-item label="签到时间" prop="signInTime">
          <el-date-picker clearable
            v-model="form.signInTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择签到时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="签退时间" prop="signOutTime">
          <el-date-picker clearable
            v-model="form.signOutTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择签退时间">
          </el-date-picker>
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
import { listClockIn, getClockIn, delClockIn, addClockIn, updateClockIn } from "@/api/system/clockIn";
import { listParkArea } from "@/api/business/ParkArea";

export default {
  name: "ClockIn",
  dicts: ['sys_attendance_status'],
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
      // 用户打卡记录表格数据
      clockInList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        parkAreaId: null,
        type: null,
        signInTime: null,
        signOutTime: null,
        status: null,
        orderByColumn:"createTime",
        isAsc:"desc",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
      },
      //停车场数据
      areaOptions:[],
      // 日期范围
      dateRangeSignIn: [],
      // 日期范围
      dateRangeSignOut: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询停车场列表 */
    getAreaList() {
      listParkArea().then(response => {
        this.areaOptions = response.data;
      });
    },
    /** 查询用户打卡记录列表 */
    getList() {
      this.loading = true;
      listClockIn(
        this.addDateRange(this.queryParams, this.dateRangeSignIn, 'SignIn'),
        this.addDateRange(this.queryParams, this.dateRangeSignOut, 'SignOut')
      ).then(response => {
        this.clockInList = response.rows;
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
        clockInId: null,
        userId: null,
        parkAreaId: null,
        type: null,
        signInTime: null,
        signOutTime: null,
        status: null,
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
      this.ids = selection.map(item => item.clockInId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户打卡记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const clockInId = row.clockInId || this.ids
      getClockIn(clockInId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户打卡记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.clockInId != null) {
            updateClockIn(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addClockIn(this.form).then(response => {
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
      const clockInIds = row.clockInId || this.ids;
      this.$modal.confirm('是否确认删除用户打卡记录编号为"' + clockInIds + '"的数据项？').then(function() {
        return delClockIn(clockInIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/clockIn/export', {
        ...this.queryParams
      }, `clockIn_${new Date().getTime()}.xlsx`)
    }
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
