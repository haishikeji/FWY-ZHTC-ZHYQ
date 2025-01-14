<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="巡检员" prop="userId">
        <el-select v-model="queryParams.userId" clearable placeholder="请选择巡检员" style="width: 100%">
          <el-option
            v-for="item in sysUserOptions"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId"
          ></el-option>
        </el-select>
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
      <el-form-item label="任务类型" prop="ticketTaskId">
        <el-select v-model="queryParams.ticketTaskId" clearable placeholder="请选择任务类型" style="width: 100%">
          <el-option
            v-for="item in ticketOptions"
            :key="item.ticketTaskId"
            :label="item.name"
            :value="item.ticketTaskId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="dateRangeBegin">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          range-separator="-"
          value-format="yyyy-MM-dd HH:mm:ss"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']">
          align="right">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="超时状态" prop="timeoutStatus">
        <el-select v-model="queryParams.timeoutStatus" clearable placeholder="请选择超时状态" style="width: 100%">
          <el-option
            v-for="dict in dict.type.inspection_task_timeout_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" clearable placeholder="请选择任务状态" style="width: 100%">
          <el-option
            v-for="dict in dict.type.inspection_task_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="inspectionTaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" prop="orderSn" />
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" width="100" />
      <el-table-column label="驶入时间" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.entryTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="停车场" align="center" prop="parkAreaName" />
      <el-table-column label="巡检员" align="center" prop="userName" />
      <el-table-column label="任务开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务类型" align="center" prop="ticketTaskName" />
      <el-table-column label="欠费金额" align="center" prop="arrearsAmount" />
      <el-table-column label="超时状态" align="center" prop="timeoutStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inspection_task_timeout_status" :value="scope.row.timeoutStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="taskStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inspection_task_status" :value="scope.row.taskStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['inspection:inspectionTask:edit']"
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

    <!-- 添加或修改巡检任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="停车场ID" prop="parkAreaId">
          <el-input v-model="form.parkAreaId" placeholder="请输入停车场ID" />
        </el-form-item>
        <el-form-item label="停车场泊位ID" prop="parkAreaBerthId">
          <el-input v-model="form.parkAreaBerthId" placeholder="请输入停车场泊位ID" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderSn">
          <el-input v-model="form.orderSn" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker clearable
            v-model="form.beginTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="巡检人ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入巡检人ID" />
        </el-form-item>
        <el-form-item label="任务小票ID" prop="ticketTaskId">
          <el-input v-model="form.ticketTaskId" placeholder="请输入任务小票ID" />
        </el-form-item>
        <el-form-item label="欠费金额" prop="arrearsAmount">
          <el-input v-model="form.arrearsAmount" placeholder="请输入欠费金额" />
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
import { listInspectionTask, getInspectionTask, delInspectionTask, addInspectionTask, updateInspectionTask } from "@/api/inspection/task";
import { allTickets } from "@/api/config/ticketTask";
import { allUser } from "@/api/system/user";

export default {
  name: "InspectionTask",
  dicts: ['inspection_task_status','inspection_task_timeout_status'],
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
      // 巡检任务表格数据
      inspectionTaskList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkAreaId: null,
        parkAreaBerthId: null,
        orderSn: null,
        beginTime: null,
        endTime: null,
        userId: null,
        ticketTaskId: null,
        arrearsAmount: null,
        timeoutStatus: null,
        taskStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parkAreaId: [
          { required: true, message: "停车场ID不能为空", trigger: "blur" }
        ],
        parkAreaBerthId: [
          { required: true, message: "停车场泊位ID不能为空", trigger: "blur" }
        ],
        orderSn: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
      },
      //系统用户列表
      sysUserOptions:[],
      //PDA任务小票
      ticketOptions:[],
      dateRange: [],
    };
  },
  created() {
    this.getSysUserList();
    this.getList();
  },
  methods: {
    /** 查询系统用户列表 */
    getSysUserList() {
      allUser().then(response => {
        this.sysUserOptions = response.data;
      });
    },
    /** 查询巡检任务列表 */
    getList() {
      this.loading = true;
      listInspectionTask(this.queryParams).then(response => {
        this.inspectionTaskList = response.rows;
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
        taskId: null,
        parkAreaId: null,
        parkAreaBerthId: null,
        orderSn: null,
        beginTime: null,
        endTime: null,
        userId: null,
        ticketTaskId: null,
        arrearsAmount: null,
        timeoutStatus: null,
        taskStatus: null,
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
      this.ids = selection.map(item => item.taskId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加巡检任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getInspectionTask(taskId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改巡检任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskId != null) {
            updateInspectionTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInspectionTask(this.form).then(response => {
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
      const taskIds = row.taskId || this.ids;
      this.$modal.confirm('是否确认删除巡检任务编号为"' + taskIds + '"的数据项？').then(function() {
        return delInspectionTask(taskIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('inspection/inspectionTask/export', {
        ...this.queryParams
      }, `inspectionTask_${new Date().getTime()}.xlsx`)
    },
    getAllTickets(){
      allTickets().then(response => {
        this.ticketOptions = response.data;
      });
    },
  }
};
</script>
