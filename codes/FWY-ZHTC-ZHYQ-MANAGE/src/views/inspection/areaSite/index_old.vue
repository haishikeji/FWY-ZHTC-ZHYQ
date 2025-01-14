<template>
  <div class="app-container">
    <div class="tips">
      <div style="font-weight: bold;">温馨提示</div>
      <div>1.请点击巡检点列表的检查项管理，依次添加配置检查项，否则移动端巡检详情将没有检查项</div>
      <div>2.请提前安装《地图位置(经纬度)选择插件》，为了保证经纬度的一致性，定位地图请选择高德地图</div>
    </div>

    <el-row :gutter="20">
      <!--区域数据-->
      <el-col :span="4" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>巡检区域列表</span>
          </div>
          <div class="head-container">
            <el-row :gutter="20">
              <el-col :span="18">
                <el-input
                  v-model="areaName"
                  placeholder="请输入区域名称"
                  clearable
                  size="small"
                  prefix-icon="el-icon-search"
                  style="margin-bottom: 20px"
                />
              </el-col>
              <el-col :span="6">
                <el-button type="primary" icon="el-icon-refresh" size="small" @click="handleRefresh"></el-button>
              </el-col>
            </el-row>
          </div>
          <div class="head-container">
            <el-tree
              :data="areaDatas"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="handleNodeClick"
            >
              <span slot-scope="{ node }" style="font-size: 13px; color: #000">
              <i style="margin-right: 5px" class="el-icon-folder" />
              <span>{{ node.label }}</span>
            </span>
            </el-tree>
          </div>
        </el-card>
      </el-col>
      <!--巡检点数据-->
      <el-col :span="20" :xs="24">
<!--        style="max-height: 700px;overflow-y: scroll;"-->
        <div>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>巡检点列表</span>
            </div>
            <div>
              <el-form :model="querySiteParams" ref="querySiteForm" size="small" :inline="true" v-show="siteShowSearch" label-width="88px">
                <el-form-item label="巡检点名称" prop="siteName">
                  <el-input
                    v-model="querySiteParams.siteName"
                    placeholder="请输入巡检点名称"
                    clearable
                    style="width: 240px"
                    @keyup.enter.native="handleSiteQuery"
                  />
                </el-form-item>
                <el-form-item label="添加时间" prop="dateRange">
                  <el-date-picker
                    v-model="dateRange"
                    type="datetimerange"
                    :picker-options="pickerOptions"
                    range-separator="-"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']">
                    align="right">
                  </el-date-picker>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" size="mini" @click="handleSiteQuery">搜索</el-button>
                  <el-button icon="el-icon-refresh" size="mini" @click="resetSiteQuery">重置</el-button>
                </el-form-item>
              </el-form>
              <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                  <el-button
                    type="primary"
                    class="green-button"
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleSiteAdd"
                    v-hasPermi="['inspection:areaSite:add']"
                  >添加</el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
                    type="primary"
                    class="green-button"
                    icon="el-icon-edit"
                    size="mini"
                    :disabled="siteSingle"
                    @click="handleSiteUpdate"
                    v-hasPermi="['inspection:areaSite:edit']"
                  >修改</el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
                    type="danger"
                    class="red-button"
                    icon="el-icon-delete"
                    size="mini"
                    :disabled="siteMultiple"
                    @click="handleSiteDelete"
                    v-hasPermi="['inspection:areaSite:remove']"
                  >删除</el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
                    type="warning"
                    icon="el-icon-download"
                    size="mini"
                    @click="handleSiteExport"
                    v-hasPermi="['inspection:areaSite:export']"
                  >导出</el-button>
                </el-col>
                <right-toolbar :showSearch.sync="siteShowSearch" @queryTable="getSiteList" :columns="siteColumns"></right-toolbar>
              </el-row>

              <el-table v-loading="siteLoading"
                        :data="siteList"
                        highlight-current-row
                        @selection-change="handleSiteSelectionChange">
                <el-table-column type="selection" width="50" align="center" />
<!--                <el-table-column-->
<!--                  type="index"-->
<!--                  width="50">-->
<!--                  <el-radio></el-radio>-->
<!--                </el-table-column>-->
                <el-table-column label="Id" align="center" key="siteId" prop="siteId" v-if="siteColumns[0].visible" width="60" />
                <el-table-column label="巡检点名称" align="center" key="siteName" prop="siteName" v-if="siteColumns[1].visible" :show-overflow-tooltip="true" />
                <el-table-column label="验证方式" align="center" key="verify" prop="verify" v-if="siteColumns[2].visible" width="80">
                  <template slot-scope="scope">
                    <span>{{scope.row.verify == 0?"不验证":"定位"}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="经纬度" align="center" key="longitude" prop="dept.longitude" v-if="siteColumns[3].visible" :show-overflow-tooltip="true">
                  <template slot-scope="scope">
                    <span>
                      {{ (scope.row.longitude != null && scope.row.latitude != null)
                      ?(scope.row.longitude+","+scope.row.latitude)
                      :"" }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="有效距离(米)" align="center" key="distance" prop="distance" v-if="siteColumns[4].visible"/>
                <el-table-column label="添加时间" align="center" prop="createTime" v-if="siteColumns[5].visible" width="150">
                  <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="center"
                  width="240"
                  class-name="small-padding fixed-width"
                >
                  <template slot-scope="scope" v-if="scope.row.userId !== 1">
                    <el-button
                      class="area-item"
                      size="mini"
                      type="warning"
                      icon="el-icon-s-order"
                      @click="handleItems(scope.row)"
                      v-hasPermi="['inspection:areaItem:list']"
                    >检查项管理</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      @click="handleSiteUpdate(scope.row)"
                      v-hasPermi="['inspection:areaSite:edit']"
                    >修改</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleSiteDelete(scope.row)"
                      v-hasPermi="['inspection:areaSite:remove']"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>

              <pagination
                v-show="siteTotal>0"
                :total="siteTotal"
                :page.sync="querySiteParams.pageNum"
                :limit.sync="querySiteParams.pageSize"
                :page-size="5"
                layout="total, prev, pager, next"
                @pagination="getSiteList"
              />
            </div>
          </el-card>

          <el-divider></el-divider>

          <!-- 巡检检查项-->
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>检查项列表</span>
              <el-tag class="ml10" type="danger" v-if="selectAreaSiteId > 0">当前选择巡检点【{{selectAreaSiteName}}】</el-tag>
            </div>
            <div>
              <el-form :model="queryItemParams" ref="queryItemForm" size="small" :inline="true" v-show="itemShowSearch" label-width="88px">
                <el-form-item label="Id" prop="itemId">
                  <el-input
                    v-model="queryItemParams.itemId"
                    placeholder="Id"
                    clearable
                    style="width: 240px"
                    @keyup.enter.native="handleItemQuery"
                  />
                </el-form-item>
                <el-form-item label="检查项名称" prop="itemName">
                  <el-input
                    v-model="queryItemParams.itemName"
                    placeholder="请输入检查项名称"
                    clearable
                    style="width: 240px"
                    @keyup.enter.native="handleItemQuery"
                  />
                </el-form-item>
                <el-form-item label="排序" prop="orderNum">
                  <el-input
                    v-model="queryItemParams.orderNum"
                    placeholder="排序"
                    clearable
                    style="width: 240px"
                    @keyup.enter.native="handleItemQuery"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" size="mini" @click="handleItemQuery">搜索</el-button>
                  <el-button icon="el-icon-refresh" size="mini" @click="resetItemQuery">重置</el-button>
                </el-form-item>
              </el-form>

              <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleItemAdd"
                    v-hasPermi="['inspection:areaItem:add']"
                    :disabled="selectAreaSiteId == 0"
                  >添加</el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
                    type="primary"
                    icon="el-icon-edit"
                    size="mini"
                    :disabled="itemSingle"
                    @click="handleItemUpdate"
                    v-hasPermi="['inspection:areaItem:edit']"
                  >修改</el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
                    type="danger"
                    icon="el-icon-delete"
                    size="mini"
                    :disabled="itemMultiple"
                    @click="handleItemDelete"
                    v-hasPermi="['inspection:areaItem:remove']"
                  >删除</el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
                    type="warning"
                    icon="el-icon-download"
                    size="mini"
                    @click="handleItemExport"
                    v-hasPermi="['inspection:areaItem:export']"
                  >导出</el-button>
                </el-col>
                <right-toolbar :showSearch.sync="itemShowSearch" @queryTable="getItemList" :columns="itemColumns"></right-toolbar>
              </el-row>

              <el-table v-loading="itemLoading" :data="itemList" @selection-change="handleItemSelectionChange">
                <el-table-column type="selection" width="50" align="center" />
                <el-table-column label="Id" align="center" key="itemId" prop="itemId" v-if="itemColumns[0].visible" />
                <el-table-column label="检查项名称" align="center" key="itemName" prop="itemName" v-if="itemColumns[1].visible" :show-overflow-tooltip="true" />
                <el-table-column label="排序" align="center" key="orderNum" prop="orderNum" v-if="itemColumns[2].visible" :show-overflow-tooltip="true" />
                <el-table-column
                  label="操作"
                  align="center"
                  width="160"
                  class-name="small-padding fixed-width"
                >
                  <template slot-scope="scope" v-if="scope.row.userId !== 1">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      @click="handleItemUpdate(scope.row)"
                      v-hasPermi="['inspection:areaItem:edit']"
                    >修改</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleItemDelete(scope.row)"
                      v-hasPermi="['inspection:areaItem:remove']"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>

              <pagination
                v-show="itemTotal>0"
                :total="itemTotal"
                :page.sync="queryItemParams.pageNum"
                :limit.sync="queryItemParams.pageSize"
                :page-size="5"
                layout="total, prev, pager, next"
                @pagination="getItemList"
              />
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!-- 添加或修改【巡检点】对话框 -->
    <el-dialog :title="siteTitle" :visible.sync="siteOpen" width="800px" append-to-body>
      <el-form ref="siteForm" :model="siteForm" :rules="siteRules" label-width="100px">
        <el-form-item label="巡检区域" prop="areaId">
          <treeselect v-model="siteForm.areaId" :options="areaOptions" :normalizer="normalizer" placeholder="请选择巡检区域" />
        </el-form-item>
        <el-form-item label="巡检点名称" prop="siteName">
          <el-input v-model="siteForm.siteName" placeholder="请输入巡检点名称" />
        </el-form-item>
        <el-form-item label="验证方式" prop="verify">
          <el-radio-group v-model="siteForm.verify">
            <el-radio
              v-for="dict in dict.type.inspection_verify_way"
              :key="parseInt(dict.value)"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="经纬度" prop="longitude">
          <el-row :gutter="10">
            <el-col :span="6"><el-input-number v-model="siteForm.longitude" style="width: 100%" controls-position="right" :min="0" /></el-col>
            <el-col :span="6"><el-input-number v-model="siteForm.latitude" style="width: 100%" controls-position="right" :min="0" /></el-col>
            <el-col :span="5"><el-button type="info" icon="el-icon-s-flag" @click="handleChoseAddress">选择地址</el-button></el-col>
            <!-- <el-col :span="6"><el-button type="primary" icon="el-icon-location" @click="handleMobileLocation">手机定位工具</el-button></el-col> -->
          </el-row>
        </el-form-item>
        <el-form-item v-if="siteForm.verify == 1" label="有效距离(米)" prop="longitude">
          <el-input-number v-model="siteForm.distance" style="width: 80%" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="siteForm.orderNum" style="width: 80%" controls-position="right" :min="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSiteForm">确 定</el-button>
        <el-button @click="cancelSite">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改【巡检检查项】对话框 -->
    <el-dialog :title="itemTitle" :visible.sync="itemOpen" width="700px" append-to-body>
      <el-form ref="itemForm" :model="itemForm" :rules="itemRules" label-width="100px">
        <el-form-item label="检查项名称" prop="itemName">
          <el-input v-model="itemForm.itemName" placeholder="请输入检查项名称" />
        </el-form-item>
        <el-form-item label="检查项类型" prop="type">
          <el-radio-group v-model="itemForm.type">
            <el-radio
              v-for="dict in dict.type.inspection_item_type"
              :key="parseInt(dict.value)"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="itemForm.orderNum" style="width: 100%" controls-position="right" :min="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitItemForm">确 定</el-button>
        <el-button @click="cancelItem">取 消</el-button>
      </div>
    </el-dialog>

    <!--  地图位置选择  -->
    <el-dialog :visible.sync="mapOpen" title="位置选择" width="700px" append-to-body>
      <lnglat @mapOk="mapOk"></lnglat>
    </el-dialog>
  </div>
</template>

<script>
  import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from "@/api/system/user";
  import { listAreaTree, listArea, getArea, delArea, addArea, updateArea, areaTreeSelect } from "@/api/inspection/area";
  import { listSite, getSite, delSite, addSite, updateSite } from "@/api/inspection/areaSite";
  import { listItem, getItem, delItem, addItem, updateItem } from "@/api/inspection/areaItem";
  import { getToken } from "@/utils/auth";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import dateModule from "@/utils/date"
  import Lnglat from '../lnglat/index'

  export default {
    name: "AreaSite",
    dicts: ['inspection_verify_way', 'inspection_item_type'],
    components: { Lnglat, Treeselect },
    data() {
      const checkAreaSiteName = (rule, value, callback) => {
        if (!value || value === '' || value.trim().length == 0) {
          callback(new Error("巡检点名称不能为空"));
        } else {
          callback();
        }
      };

      const checkAreaItemName = (rule, value, callback) => {
        if (!value || value === '' || value.trim().length == 0) {
          callback(new Error("检查项名称不能为空"));
        } else {
          callback();
        }
      };

      return {
        // 区域名称
        areaName: undefined,
        // 默认密码
        initPassword: undefined,
        // 岗位选项
        postOptions: [],
        // 角色选项
        roleOptions: [],
        defaultProps: {
          children: "children",
          label: "label"
        },

        // 区域树选项
        areaDatas: [],

        //巡检检查项
        // 遮罩层
        itemLoading: false,
        // 总条数
        itemTotal: 0,
        // 用户表格数据
        itemList: null,
        // 弹出层标题
        itemTitle: "",
        // 是否显示弹出层
        itemOpen: false,
        // 表单参数
        itemForm: {},
        // 表单校验
        itemRules: {
          itemName:[
            { required: true, message: "请输入检查项名称", trigger: "blur" },
            { required: true, validator: checkAreaItemName, trigger: "blur" }
          ]
        },
        // 选中数组
        itemIds: [],
        // 非单个禁用
        itemSingle: true,
        // 非多个禁用
        itemMultiple: true,
        // 显示搜索条件
        itemShowSearch: true,
        // 查询参数
        queryItemParams: {
          pageNum: 1,
          pageSize: 5,
          areaSiteId:undefined,
          itemName: undefined,
        },
        // 列信息
        itemColumns: [
          { key: 0, label: `Id`, visible: true },
          { key: 1, label: `检查项名称`, visible: true },
          { key: 2, label: `排序`, visible: true },
        ],


        //巡检点
        // 查询参数
        querySiteParams: {
          pageNum: 1,
          pageSize: 5,
          siteName: undefined,
          areaId: undefined,
          createTime: null,
        },
        // 列信息
        siteColumns: [
          { key: 0, label: `Id`, visible: true },
          { key: 1, label: `巡检点名称`, visible: true },
          { key: 2, label: `验证方式`, visible: true },
          { key: 3, label: `经纬度`, visible: true },
          { key: 4, label: `有效距离(米)`, visible: true },
          { key: 5, label: `添加时间`, visible: true }
        ],
        // 巡检点表格数据
        siteList: null,
        // 总条数
        siteTotal: 0,
        // 遮罩层
        siteLoading: true,
        // 弹出层标题
        siteTitle: "",
        // 是否显示弹出层
        siteOpen: false,
        // 表单参数
        siteForm: {},
        // 表单校验
        siteRules: {
          areaId:[
            { required: true, message: "请选择巡检区域", trigger: "blur" },
          ],
          siteName:[
            { required: true, message: "请输入巡检点名称", trigger: "blur" },
            { required: true, validator: checkAreaSiteName, trigger: "blur" }
          ]
        },
        // 巡检点区域树选项
        areaOptions: [],
        // 选中数组
        siteIds: [],
        // 非单个禁用
        siteSingle: true,
        // 非多个禁用
        siteMultiple: true,
        // 显示搜索条件
        siteShowSearch: true,
        selectAreaSiteId:0,//选中的检查点ID
        selectAreaSiteName:"",//选中的检查点
        pickerOptions: {
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                picker.$emit('pick', [ new Date(dateModule.today()[0]),new Date(dateModule.today()[1])]);
              }
            },{
              text: '昨天',
              onClick(picker) {
                picker.$emit('pick', [ new Date(dateModule.yesterday()[0]),new Date(dateModule.yesterday()[1])]);
              }
            },{
              text: '最近7天',
              onClick(picker) {
                picker.$emit('pick', [ new Date(dateModule.sevenDays()[0]),new Date(dateModule.sevenDays()[1])]);
              }
            },{
              text: '最近30天',
              onClick(picker) {
                picker.$emit('pick', [ new Date(dateModule.thirtyDays()[0]),new Date(dateModule.thirtyDays()[1])]);
              }
            },{
            text: '本月',
            onClick(picker) {
              picker.$emit('pick', [ new Date(dateModule.getCurrentMonth()[0]),new Date(dateModule.getCurrentMonth()[1])]);
            }
          }, {
            text: '上月',
            onClick(picker) {
              picker.$emit('pick', [ new Date(dateModule.getLastMonth()[0]),new Date(dateModule.getLastMonth()[1])]);
            }
          }]
        },
        // 日期范围
        dateRange: [],
        // 是否显示弹出层
        mapOpen: false,
      };
    },
    watch: {
      // 根据名称筛选部门树
      areaName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getAreaTree();

      this.getSiteList();
    },
    methods: {
      /**
       * 选中坐标后
       */
      mapOk(e){
        console.log('%cindex.vue line:610 e', 'color: white; background-color: #26bfa5;', e);
        this.siteForm.longitude = e.lng;
        this.siteForm.latitude = e.lat;
        this.mapOpen = false;
      },
      /** 查询巡检区域下拉树结构 */
      getAreaTree() {
        areaTreeSelect().then(response => {
          this.areaDatas = response.data;
        });
      },
      /** 刷新巡检区域下拉树结构 */
      handleRefresh(){
        this.areaDatas = [];
        this.getAreaTree();
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.querySiteParams.areaId = data.id > 0?data.id:null;
        this.handleSiteQuery();
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
      // 表单重置
      reset(type) {
        if (type  == 1){
          this.siteForm = {
            siteId: undefined,
            areaId: undefined,
            siteName: undefined,
            verify: null,
            orderNum: null
          };
          this.resetForm("siteForm");
        }else{
          this.itemForm = {
            itemId: null,
            areaSiteId: null,
            itemName: null,
            type: null,
            orderNum: null,
          };
          this.resetForm("itemForm");
        }
      },
      //巡检点操作
      /** 查询巡检点列表 */
      getSiteList() {
        this.siteLoading = true;
        listSite(this.addDateRange(this.querySiteParams, this.dateRange)).then(response => {
            this.siteList = response.rows;
            this.siteTotal = response.total;
            this.siteLoading = false;
          }
        );
      },
      /** 搜索按钮操作 */
      handleSiteQuery() {
        this.querySiteParams.pageNum = 1;
        //重置选择巡检点
        this.selectAreaSiteId = 0;
        this.selectAreaSiteName = "";
        this.queryItemParams.areaSiteId = null;
        this.itemList = [];

        this.getSiteList();
      },
      /** 重置按钮操作 */
      resetSiteQuery() {
        this.dateRange = [];
        this.resetForm("querySiteForm");
        this.querySiteParams.deptId = undefined;
        this.$refs.tree.setCurrentKey(null);
        this.handleSiteQuery();
      },
      // 取消按钮
      cancelSite() {
        this.siteOpen = false;
        this.reset(1);
      },
      /** 提交按钮 */
      submitSiteForm: function() {
        console.log(this.siteForm.verify);
        if (this.siteForm.verify == 1) {
          if ( !this.siteForm.longitude || !this.siteForm.latitude) {
            this.$modal.msgError("请先选择经纬度");
            return;
          }
          if( !this.siteForm.distance || this.siteForm.distance <= 0){
            this.$modal.msgError("请先输入有效距离");
            return;
          }
        }
        this.$refs["siteForm"].validate(valid => {
          if (valid) {
            if (this.siteForm.siteId != undefined) {
              updateSite(this.siteForm).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.siteOpen = false;
                this.getSiteList();
              });
            } else {
              addSite(this.siteForm).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.siteOpen = false;
                this.getSiteList();
              });
            }
          }
        });
      },
      /** 新增按钮操作 */
      handleSiteAdd() {
        this.reset(1);
        this.siteOpen = true;
        this.siteTitle = "添加";
        this.siteForm.verify = 0;
        listAreaTree().then(response =>{
          console.log(response)
          this.areaOptions = this.handleTree(response.data, "areaId");
        });
      },
      /** 修改按钮操作 */
      handleSiteUpdate(row) {
        this.reset(1);
        const siteId = row.siteId || this.siteIds
        getSite(siteId).then(response => {
          this.siteForm = response.data;
          this.siteOpen = true;
          this.siteTitle = "修改";
          listAreaTree().then(response_tree => {
            this.areaOptions = this.handleTree(response_tree.data, "areaId");
          });
        });
      },
      /** 删除按钮操作 */
      handleSiteDelete(row) {
        const siteIds = row.siteId || this.siteIds;
        this.$modal.confirm('是否确认删除巡检点编号为"' + siteIds + '"的数据项？').then(function() {
          return delSite(siteIds);
        }).then(() => {
          this.getSiteList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      handleItems(row){
        this.selectAreaSiteId = row.siteId;
        this.selectAreaSiteName = row.siteName;
        this.queryItemParams.areaSiteId = row.siteId;
        //刷新检查项列表
        this.getItemList()
      },
      // 多选框选中数据
      handleSiteSelectionChange(selection) {
        this.siteIds = selection.map(item => item.siteId);
        this.siteSingle = selection.length != 1;
        this.siteMultiple = !selection.length;
      },

      //巡检检查项
      /** 查询用户列表 */
      getItemList() {
        this.itemLoading = true;
        listItem(this.queryItemParams).then(response => {
            this.itemList = response.rows;
            this.itemTotal = response.total;
            this.itemLoading = false;
          }
        );
      },
      /** 搜索按钮操作 */
      handleItemQuery() {
        this.queryItemParams.pageNum = 1;
        this.getItemList();
      },
      /** 重置按钮操作 */
      resetItemQuery() {
        this.resetForm("queryItemForm");
        // this.queryItemParams.areaSiteId = undefined;
        this.handleItemQuery();
      },
      // 取消按钮
      cancelItem() {
        this.itemOpen = false;
        this.reset(2);
      },
      /** 提交按钮 */
      submitItemForm: function() {
        this.$refs["itemForm"].validate(valid => {
          if (valid) {
            if (this.itemForm.itemId != undefined) {
              updateItem(this.itemForm).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.itemOpen = false;
                this.getItemList();
              });
            } else {
              this.itemForm.areaSiteId = this.selectAreaSiteId;
              addItem(this.itemForm).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.itemOpen = false;
                this.getItemList();
              });
            }
          }
        });
      },
      /** 新增按钮操作 */
      handleItemAdd() {
        this.reset();
        this.itemOpen = true;
        this.itemForm.type = 1;
        this.itemTitle = "添加检查项";
      },
      /** 修改按钮操作 */
      handleItemUpdate(row) {
        this.reset();
        const itemId = row.itemId || this.itemIds;
        getItem(itemId).then(response => {
          this.itemForm = response.data;
          this.itemOpen = true;
          this.itemTitle = "修改检查项";
        });
      },
      /** 删除按钮操作 */
      handleItemDelete(row) {
        const itemIds = row.itemId || this.itemIds;
        this.$modal.confirm('是否确认删除检查项编号为"' + itemIds + '"的数据项？').then(function() {
          return delItem(itemIds);
        }).then(() => {
          this.getItemList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      // 多选框选中数据
      handleItemSelectionChange(selection) {
        this.itemIds = selection.map(item => item.itemId);
        this.itemSingle = selection.length != 1;
        this.itemMultiple = !selection.length;
      },
      /** 导出按钮操作 */
      handleSiteExport() {
        this.download('inspection/areaSite/export', {
          ...this.querySiteParams
        }, `巡检点_${new Date().getTime()}.xlsx`)
      },
      /** 导出按钮操作 */
      handleItemExport() {
        this.download('inspection/areaItem/export', {
          ...this.queryItemParams
        }, `检查项_${new Date().getTime()}.xlsx`)
      },
      handleChoseAddress(){
        //选择地址
        this.mapOpen = true;
      },
      handleMobileLocation(){
        //手机定位
      }
    }
  };
</script>
<style lang="scss" scoped>
  .el-card__header{
    background: #e8edf0;
  }
  .area-item{
    padding: 7px 5px !important;
  }
</style>
