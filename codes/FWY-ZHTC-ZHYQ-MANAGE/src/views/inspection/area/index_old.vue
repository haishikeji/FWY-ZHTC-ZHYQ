<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="区域名称" prop="areaName">
        <el-input
          v-model="queryParams.areaName"
          placeholder="请输入区域名称"
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
          class="green-button"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['inspection:area:add']"
        >添加</el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          class="green-button"-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--          v-hasPermi="['inspection:area:edit']"-->
      <!--        >修改</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          class="red-button"-->
      <!--          icon="el-icon-delete"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleDelete"-->
      <!--          v-hasPermi="['inspection:area:remove']"-->
      <!--        >删除</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['inspection:area:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="areaList"
      row-key="areaId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      @selection-change="handleSelectionChange">
      <!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="Id" align="center" prop="areaId" />
      <el-table-column label="区域名称" align="center" prop="areaName" />
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['inspection:area:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['inspection:area:add']"
          >新增</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['inspection:area:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--    <pagination-->
    <!--      v-show="total>0"-->
    <!--      :total="total"-->
    <!--      :page.sync="queryParams.pageNum"-->
    <!--      :limit.sync="queryParams.pageSize"-->
    <!--      @pagination="getList"-->
    <!--    />-->

    <!-- 添加或修改【巡检区域】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级区域" prop="parentId" >
          <treeselect v-model="form.parentId" :options="areaOptions" :normalizer="normalizer" placeholder="选择上级区域" />
        </el-form-item>
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入区域名称" />
        </el-form-item>
        <el-form-item label="区域编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入区域编码" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" style="width: 100%" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" :row="3" type="textarea" placeholder="请输入内容" />
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
  import { listAreaTree, getArea, delArea, addArea, updateArea, listAreaExcludeChild } from "@/api/inspection/area";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Area",
    components: { Treeselect },
    data() {
      const checkAreaName = (rule, value, callback) => {
        if (!value || value === '' || value.trim().length == 0) {
          callback(new Error("区域名称不能为空"));
        } else {
          callback();
        }
      };

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
        // 表格数据
        areaList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否展开，默认全部展开
        isExpandAll: true,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          parentId: null,
          areaName: null,
          orderNum: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          areaName:[
            // { validator: checkAreaName, trigger: 'blur' }
            { required: true, message: "请输入区域名称", trigger: "blur" },
            { required: true, validator: checkAreaName, trigger: "blur" }
          ]
        },
        // 区域树选项
        areaOptions: [],
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询【巡检区域】列表 */
      getList() {
        this.loading = true;
        listAreaTree(this.queryParams).then(response => {
          // this.areaList = response.rows;
          // this.total = response.total;

          this.areaList = this.handleTree(response.data, "areaId");
          this.loading = false;
        });
      },
      /** 转换数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.areaId,
          label: node.areaName,
          children: node.children
        };
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          areaId: null,
          parentId: null,
          areaName: null,
          orderNum: null,
          delFlag: null,
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
        this.ids = selection.map(item => item.areaId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        console.log(row)
        this.reset();
        if (row != undefined) {
          this.form.parentId = row.areaId;
        }
        this.open = true;
        this.title = "添加";
        listAreaTree().then(response => {
          let data = [{
            areaId: 0,
            areaName:"全部",
            children:[]
          }]
          if (response.data.length > 0){
            data = data.concat(response.data)
          }
          this.areaOptions = this.handleTree(data, "areaId");
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const areaId = row.areaId || this.ids
        getArea(areaId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改";
          listAreaTree().then(response => {
            this.areaOptions = this.handleTree(response.data, "areaId");
          });
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.areaId != null) {
              updateArea(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addArea(this.form).then(response => {
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
        const areaIds = row.areaId || this.ids;
        this.$modal.confirm('是否确认删除编号为"' + areaIds + '"的数据项？').then(function() {
          return delArea(areaIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('inspection/area/export', {
          ...this.queryParams
        }, `巡检区域_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>

<!--<template>-->
<!--  <div class="app-container">-->
<!--    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">-->
<!--      <el-form-item label="区域名称" prop="areaName">-->
<!--        <el-input-->
<!--          v-model="queryParams.areaName"-->
<!--          placeholder="请输入区域名称"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['inspection:area:add']"-->
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
<!--          v-hasPermi="['inspection:area:edit']"-->
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
<!--          v-hasPermi="['inspection:area:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['inspection:area:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

<!--    <el-table v-loading="loading" :data="areaList" @selection-change="handleSelectionChange">-->
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="areaId" />-->
<!--      <el-table-column label="区域名称" align="center" prop="areaName" />-->
<!--      <el-table-column label="权重" align="center" prop="orderNum" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['inspection:area:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-plus"-->
<!--            @click="handleAdd(scope.row)"-->
<!--            v-hasPermi="['inspection:area:add']"-->
<!--          >新增</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['inspection:area:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->

<!--    <pagination-->
<!--      v-show="total>0"-->
<!--      :total="total"-->
<!--      :page.sync="queryParams.pageNum"-->
<!--      :limit.sync="queryParams.pageSize"-->
<!--      @pagination="getList"-->
<!--    />-->

<!--    &lt;!&ndash; 添加或修改对话框 &ndash;&gt;-->
<!--    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>-->
<!--      <el-form ref="form" :model="form" :rules="rules" label-width="80px">-->
<!--        <el-form-item label="父部门id" prop="parentId">-->
<!--          <treeselect v-model="form.parentId" :options="areaOptions" :normalizer="normalizer" placeholder="选择上级部门" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="区域名称" prop="areaName">-->
<!--          <el-input v-model="form.areaName" placeholder="请输入区域名称" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="排序" prop="orderNum">-->
<!--          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import { listArea, listAreaTree, getArea, delArea, addArea, updateArea } from "@/api/inspection/area";-->
<!--import Treeselect from '@riophae/vue-treeselect';-->
<!--import "@riophae/vue-treeselect/dist/vue-treeselect.css";-->

<!--export default {-->
<!--  name: "Area",-->
<!--  components: { Treeselect },-->
<!--  data() {-->
<!--    return {-->
<!--      // 遮罩层-->
<!--      loading: true,-->
<!--      // 选中数组-->
<!--      ids: [],-->
<!--      // 非单个禁用-->
<!--      single: true,-->
<!--      // 非多个禁用-->
<!--      multiple: true,-->
<!--      // 显示搜索条件-->
<!--      showSearch: true,-->
<!--      // 总条数-->
<!--      total: 0,-->
<!--      // 表格数据-->
<!--      areaList: [],-->
<!--      // 弹出层标题-->
<!--      title: "",-->
<!--      // 是否显示弹出层-->
<!--      open: false,-->
<!--      // 查询参数-->
<!--      queryParams: {-->
<!--        pageNum: 1,-->
<!--        pageSize: 10,-->
<!--        parentId: null,-->
<!--        areaName: null,-->
<!--        orderNum: null,-->
<!--      },-->
<!--      // 表单参数-->
<!--      form: {},-->
<!--      // 表单校验-->
<!--      rules: {-->
<!--      },-->
<!--      // 区域树选项-->
<!--      areaOptions: [],-->
<!--    };-->
<!--  },-->
<!--  created() {-->
<!--    this.getList();-->
<!--  },-->
<!--  methods: {-->
<!--    /** 查询列表 */-->
<!--    getList() {-->
<!--      this.loading = true;-->
<!--      listArea(this.queryParams).then(response => {-->
<!--        this.areaList = response.rows;-->
<!--        this.total = response.total;-->
<!--        this.loading = false;-->
<!--      });-->
<!--    },-->
<!--    /** 转换数据结构 */-->
<!--    normalizer(node) {-->
<!--      if (node.children && !node.children.length) {-->
<!--        delete node.children;-->
<!--      }-->
<!--      return {-->
<!--        id: node.areaId,-->
<!--        label: node.areaName,-->
<!--        children: node.children-->
<!--      };-->
<!--    },-->
<!--    // 取消按钮-->
<!--    cancel() {-->
<!--      this.open = false;-->
<!--      this.reset();-->
<!--    },-->
<!--    // 表单重置-->
<!--    reset() {-->
<!--      this.form = {-->
<!--        areaId: null,-->
<!--        parentId: null,-->
<!--        areaName: null,-->
<!--        orderNum: null,-->
<!--        delFlag: null,-->
<!--        createBy: null,-->
<!--        createTime: null,-->
<!--        updateBy: null,-->
<!--        updateTime: null-->
<!--      };-->
<!--      this.resetForm("form");-->
<!--    },-->
<!--    /** 搜索按钮操作 */-->
<!--    handleQuery() {-->
<!--      this.queryParams.pageNum = 1;-->
<!--      this.getList();-->
<!--    },-->
<!--    /** 重置按钮操作 */-->
<!--    resetQuery() {-->
<!--      this.resetForm("queryForm");-->
<!--      this.handleQuery();-->
<!--    },-->
<!--    // 多选框选中数据-->
<!--    handleSelectionChange(selection) {-->
<!--      this.ids = selection.map(item => item.areaId)-->
<!--      this.single = selection.length!==1-->
<!--      this.multiple = !selection.length-->
<!--    },-->
<!--    /** 新增按钮操作 */-->
<!--    handleAdd(row) {-->
<!--      this.reset();-->
<!--      if (row != undefined) {-->
<!--        this.form.parentId = row.areaId;-->
<!--      }-->
<!--      this.open = true;-->
<!--      this.title = "添加";-->
<!--      listAreaTree().then(response => {-->
<!--        this.areaOptions = this.handleTree(response.data, "areaId");-->
<!--      });-->
<!--    },-->
<!--    /** 修改按钮操作 */-->
<!--    handleUpdate(row) {-->
<!--      this.reset();-->
<!--      const areaId = row.areaId || this.ids-->
<!--      getArea(areaId).then(response => {-->
<!--        this.form = response.data;-->
<!--        this.open = true;-->
<!--        this.title = "修改";-->
<!--        listAreaTree().then(response => {-->
<!--          this.areaOptions = this.handleTree(response.data, "areaId");-->
<!--        });-->
<!--      });-->
<!--    },-->
<!--    /** 提交按钮 */-->
<!--    submitForm() {-->
<!--      this.$refs["form"].validate(valid => {-->
<!--        if (valid) {-->
<!--          if (this.form.areaId != null) {-->
<!--            updateArea(this.form).then(response => {-->
<!--              this.$modal.msgSuccess("修改成功");-->
<!--              this.open = false;-->
<!--              this.getList();-->
<!--            });-->
<!--          } else {-->
<!--            addArea(this.form).then(response => {-->
<!--              this.$modal.msgSuccess("新增成功");-->
<!--              this.open = false;-->
<!--              this.getList();-->
<!--            });-->
<!--          }-->
<!--        }-->
<!--      });-->
<!--    },-->
<!--    /** 删除按钮操作 */-->
<!--    handleDelete(row) {-->
<!--      const areaIds = row.areaId || this.ids;-->
<!--      this.$modal.confirm('是否确认删除编号为"' + areaIds + '"的数据项？').then(function() {-->
<!--        return delArea(areaIds);-->
<!--      }).then(() => {-->
<!--        this.getList();-->
<!--        this.$modal.msgSuccess("删除成功");-->
<!--      }).catch(() => {});-->
<!--    },-->
<!--    /** 导出按钮操作 */-->
<!--    handleExport() {-->
<!--      this.download('inspection/area/export', {-->
<!--        ...this.queryParams-->
<!--      }, `area_${new Date().getTime()}.xlsx`)-->
<!--    }-->
<!--  }-->
<!--};-->
<!--</script>-->
