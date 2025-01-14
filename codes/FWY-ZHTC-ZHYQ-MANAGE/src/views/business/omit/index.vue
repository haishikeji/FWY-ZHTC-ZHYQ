<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="98px">
<!--      <el-form-item label="停车场" prop="parkAreaId">-->
<!--        <el-input-->
<!--          v-model="queryParams.parkAreaId"-->
<!--          placeholder="请输入停车场"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="停车场" prop="parkAreaId">
        <el-select v-model="queryParams.parkAreaId" placeholder="请选择停车场">
          <el-option
            v-for="item in areaOptions"
            :key="item.parkAreaId"
            :label="item.name"
            :value="item.parkAreaId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="遗漏总订单数" prop="missingOrderNumber">
        <el-input
          v-model="queryParams.missingOrderNumber"
          placeholder="请输入遗漏总订单数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="omitList" @selection-change="handleSelectionChange">
      <el-table-column label="停车场" align="center" prop="parkAreaName"/>
      <el-table-column label="遗漏总订单数" align="center" prop="missingOrderNumber"/>
      <el-table-column label="遗漏次数最高泊位" align="center" prop="parkAreaBerthNames"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改巡检遗漏报对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
<!--        <el-form-item label="停车场" prop="parkAreaId">-->
<!--          <el-input v-model="form.parkAreaId" placeholder="请输入停车场"/>-->
<!--        </el-form-item>-->
        <el-form-item label="停车场" prop="parkAreaId">
          <el-select v-model="form.parkAreaId" clearable placeholder="请选择停车场">
            <el-option
              v-for="item in areaOptions"
              :key="item.parkAreaId"
              :label="item.name"
              :value="item.parkAreaId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="遗漏总订单数" prop="missingOrderNumber">
<!--          <el-input v-model="form.missingOrderNumber" placeholder="请输入遗漏总订单数"/>-->
          <el-input-number v-model="form.missingOrderNumber" :precision="0" :min="0" placeholder="请输入遗漏总订单数" />
        </el-form-item>
        <el-form-item label="遗漏次数最高泊位" prop="parkAreaBerthIds">
          <el-input v-model="form.parkAreaBerthIds" type="textarea" placeholder="请输入内容"/>
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
  import {listOmit, getOmit, delOmit, addOmit, updateOmit} from "@/api/business/omit";
  import { listParkArea } from "@/api/business/ParkArea";

  export default {
    name: "Omit",
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
        // 巡检遗漏报表格数据
        omitList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          parkAreaId: null,

          missingOrderNumber: null,
          parkAreaBerthIds: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        areaOptions:[],
      };
    },
    created() {
      this.getList();
      this.getAreaList();
    },
    methods: {
      /** 查询停车场列表 */
      getAreaList() {
        listParkArea().then(response => {
          this.areaOptions = response.data;
        });
      },
      /** 查询巡检遗漏报列表 */
      getList() {
        this.loading = true;
        listOmit(this.queryParams).then(response => {
          this.omitList = response.rows;
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
          orderOmitId: null,
          parkAreaId: null,

          missingOrderNumber: null,
          parkAreaBerthIds: null,
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
        this.ids = selection.map(item => item.orderOmitId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加巡检遗漏报";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const orderOmitId = row.orderOmitId || this.ids
        getOmit(orderOmitId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改巡检遗漏报";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.orderOmitId != null) {
              updateOmit(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addOmit(this.form).then(response => {
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
        const orderOmitIds = row.orderOmitId || this.ids;
        this.$modal.confirm('是否确认删除巡检遗漏报编号为"' + orderOmitIds + '"的数据项？').then(function () {
          return delOmit(orderOmitIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('business/omit/export', {
          ...this.queryParams
        }, `omit_${new Date().getTime()}.xlsx`)
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
