<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="停车场类型" prop="parkAreaTypeId">
        <el-select v-model="queryParams.parkAreaTypeId" placeholder="请选择停车场类型">
          <el-option
            v-for="item in areaTypeOptions"
            :key="item.parkAreaTypeId"
            :label="item.name"
            :value="item.parkAreaTypeId"
            :disabled="item.status == 1"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
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
          v-hasPermi="['business:FreeRule:add']"
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
          v-hasPermi="['business:FreeRule:edit']"
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
          v-hasPermi="['business:FreeRule:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['business:FreeRule:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="FreeRuleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="停车场类型名称" align="center" prop="parkAreaTypeName" />
      <el-table-column label="停车场名称" align="center" prop="parkAreaNames" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="计费周期" align="center" prop="chargeCycleType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_charge_cycle_type" :value="scope.row.chargeCycleType"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:FreeRule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:FreeRule:remove']"
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

    <!-- 添加或修改包月收费规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="收费规则名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入收费规则名称" />
        </el-form-item>
        <el-form-item label="停车场类型" prop="parkAreaTypeId">
          <el-select v-model="form.parkAreaTypeId" placeholder="请选择停车场类型" @change="handleAreaTypeChange">
            <el-option
              v-for="item in areaTypeOptions"
              :key="item.parkAreaTypeId"
              :label="item.name"
              :value="item.parkAreaTypeId"
              :disabled="item.status == 1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否同时生效" prop="isEffect">
          <el-radio-group v-model="form.isEffect">
            <el-radio :label="0">是</el-radio>
            <el-radio :label="1">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="停车场" prop="parkAreaIds">
          <el-select v-model="form.parkAreaIds" multiple clearable placeholder="请选择停车场">
            <el-option
              v-for="item in areaOptions"
              :key="item.parkAreaId"
              :label="item.name"
              :value="item.parkAreaId"
              :disabled="item.status == 1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="计费周期" prop="chargeCycleType">
          <el-select v-model="form.chargeCycleType" placeholder="请选择计费周期">
            <el-option
              v-for="dict in dict.type.bus_charge_cycle_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
<!--          <el-input v-model="form.price" placeholder="请输入价格" />-->
          <el-input-number v-model="form.price" :precision="2" placeholder="请输入价格" :min="0" />
        </el-form-item>
        <el-form-item label="优惠价" prop="preferentialPrice">
<!--          <el-input v-model="form.preferentialPrice" placeholder="请输入优惠价" />-->
          <el-input-number v-model="form.preferentialPrice" :precision="2" placeholder="请输入优惠价" :min="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { pageFreeRule, getFreeRule, delFreeRule, addFreeRule, updateFreeRule } from "@/api/business/FreeRule";
import { listParkAreaType } from "@/api/business/ParkAreaType";
import { listParkArea } from "@/api/business/ParkArea";

export default {
  name: "FreeRule",
  dicts: ['bus_charge_cycle_type'],
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
      // 包月收费规则表格数据
      FreeRuleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkAreaTypeId: null,
        name: null,
        chargeCycleType: null,
        isEffect: null,
        beginTime: null,
        endTime: null,
        price: null,
        preferentialPrice: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parkAreaTypeId: [
          { required: true, message: "停车场类型ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        chargeCycleType: [
          { required: true, message: "计费周期不能为空", trigger: "change" }
        ],
        price: [
          { required: true, message: "价格不能为空", trigger: "blur" }
        ],
        preferentialPrice: [
          { required: true, message: "优惠价格不能为空", trigger: "blur" }
        ],
      },
      areaTypeOptions:[],
      areaOptions:[]
    };
  },
  created() {
    this.getList();
    this.getTypeList();
    // this.getAreaList();
  },
  methods: {
    /** 查询停车场类型列表 */
    getTypeList() {
      this.loading = true;
      listParkAreaType().then(response => {
        this.areaTypeOptions = response.data;
        this.loading = false;
      });
    },
    /** 查询停车场列表 */
    getAreaList(queryParams) {
      // this.loading = true;
      listParkArea(queryParams).then(response => {
        this.areaOptions = response.data;
        // this.loading = false;
      });
    },
    /** 查询包月收费规则列表 */
    getList() {
      this.loading = true;
      pageFreeRule(this.queryParams).then(response => {
        this.FreeRuleList = response.rows;
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
        freeRuleId: null,
        parkAreaTypeId: null,
        name: null,
        chargeCycleType: null,
        isEffect: 0,
        beginTime: null,
        endTime: null,
        price: null,
        preferentialPrice: null,
        remark: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        parkAreaIds:[]
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
      this.ids = selection.map(item => item.freeRuleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加包月收费规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const freeRuleId = row.freeRuleId || this.ids
      getFreeRule(freeRuleId).then(response => {
        this.title = "修改包月收费规则";
        this.form = response.data;
        if(response.data.parkAreaTypeId){
          let param = {
            parkAreaTypeId:response.data.parkAreaTypeId
          }
          this.getAreaList(param)
        }
        this.open = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log(this.form)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.freeRuleId != null) {
            updateFreeRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFreeRule(this.form).then(response => {
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
      const freeRuleIds = row.freeRuleId || this.ids;
      this.$modal.confirm('是否确认删除包月收费规则编号为"' + freeRuleIds + '"的数据项？').then(function() {
        return delFreeRule(freeRuleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/FreeRule/export', {
        ...this.queryParams
      }, `FreeRule_${new Date().getTime()}.xlsx`)
    },
    /**
     * 停车场类型改变 - 搜索该类型下的停车场
     */
    handleAreaTypeChange(e){
      let param = {
        parkAreaTypeId:this.form.parkAreaTypeId
      }
      this.getAreaList(param)
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
</style>
