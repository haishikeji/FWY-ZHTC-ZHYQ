<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="88px">
      <el-form-item label="停车场类型" prop="parkAreaTypeId">
        <el-select v-model="queryParams.parkAreaTypeId" clearable  placeholder="请选择停车场类型" @change="handleAreaTypeChangeForQuery">
          <el-option
            v-for="item in areaTypeQueryOptions"
            :key="item.parkAreaTypeId"
            :label="item.name"
            :value="item.parkAreaTypeId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="停车场" prop="parkAreaId">
        <el-select v-model="queryParams.parkAreaId" clearable placeholder="请选择停车场">
          <el-option
            v-for="item in areaQueryOptions"
            :key="item.parkAreaId"
            :label="item.name"
            :value="item.parkAreaId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="行为" prop="actionType">
        <el-select v-model="queryParams.actionType" placeholder="请选择行为" clearable>
          <el-option
            v-for="dict in dict.type.bus_order_berth_action_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="停车类型" prop="parkingType">
        <el-select v-model="queryParams.parkingType" placeholder="请选择停车类型" clearable>
          <el-option
            v-for="dict in dict.type.bus_order_parking_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="行为时间" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          style="width: 280px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="车牌号码" prop="licensePlateNumber">
        <el-input
          v-model="queryParams.licensePlateNumber"
          placeholder="请输入车牌号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="泊位号" prop="parkAreaBerthCode">
        <el-input
          v-model="queryParams.parkAreaBerthCode"
          placeholder="请输入泊位号"
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
          @click="handleEntry"
          v-hasPermi="['business:OrderBerthRecord:add']"
        >新增入场</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleOut"
          v-hasPermi="['business:OrderBerthRecord:add']"
        >新增出场</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="OrderBerthRecordList">
      <el-table-column label="车牌号码" align="center" prop="licensePlateNumber" />
      <el-table-column label="行为类型" align="center" prop="actionType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_order_berth_action_type" :value="scope.row.actionType"/>
        </template>
      </el-table-column>
      <el-table-column label="行为时间" align="center" prop="actionTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actionTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="停车场名称" align="center" prop="parkAreaName" />
      <el-table-column label="泊位号" align="center" prop="parkAreaBerthCode" />
      <el-table-column label="停车场类型" align="center" prop="parkAreaTypeName" />

      <el-table-column label="停车类型" align="center" prop="parkingType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_order_parking_type" :value="scope.row.parkingType"/>
        </template>
      </el-table-column>
      <el-table-column label="上传时间" align="center" prop="createTime" width="150">
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
            @click="handlePreviewImg(scope.row)"
            v-hasPermi="['business:OrderBerthRecord:edit']"
          >查看照片</el-button>
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

    <!-- 添加或预览照片活动对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
<!--      <div class="img-plate-container">-->
<!--        <div class="demonstration">车牌照片</div>-->
<!--        <el-image class="img-plate" :src="preUrl+formImg.licencePlateImg">-->
<!--          <div slot="error" class="image-slot">-->
<!--            无-->
<!--          </div>-->
<!--        </el-image>-->
<!--      </div>-->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="img-container">
            <div class="demonstration">车牌照片</div>
            <el-image class="img-plate" :src="formImg.licencePlateImg">
              <div slot="error" class="image-slot">
                暂无
              </div>
            </el-image>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="img-container">
            <div class="demonstration"><i class="el-icon-collection-tag"></i> 驶入照片</div>
            <el-image class="img-1" :src="formImg.entryImg">
              <div slot="error" class="image-slot">
                暂无
              </div>
            </el-image>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="img-container">
            <div class="demonstration"><i class="el-icon-collection-tag"></i> 停稳照片</div>
            <el-image class="img-1" :src="formImg.entrySteadyImg">
              <div slot="error" class="image-slot">
                暂无
              </div>
            </el-image>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="img-container">
            <div class="demonstration"><i class="el-icon-collection-tag"></i> 预驶离照片</div>
            <el-image class="img-1" :src="formImg.departurePrepareImg">
              <div slot="error" class="image-slot">
                暂无
              </div>
            </el-image>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="img-container">
            <div class="demonstration"><i class="el-icon-collection-tag"></i> 驶离照片</div>
            <el-image class="img-1" :src="formImg.departureImg">
              <div slot="error" class="image-slot">
                暂无
              </div>
            </el-image>
          </div>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 新增入场对话框 -->
    <el-dialog title="车辆入场" :visible.sync="openEntry" width="900px" append-to-body>
      <el-form ref="formEntry" :model="formEntry" :rules="rulesEntry" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌号码" prop="licensePlateNumber">
              <el-input v-model="formEntry.licensePlateNumber" placeholder="请输入车牌号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车牌颜色" prop="licensePlateType">
              <el-select v-model="formEntry.licensePlateType" placeholder="请选择车牌颜色">
                <el-option
                  v-for="dict in dict.type.bus_order_license_plate_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停车场" prop="parkAreaId">
              <el-select v-model="formEntry.parkAreaId" filterable clearable placeholder="请选择停车场" @change="handleAreaChange">
                <el-option
                  v-for="item in areaOptions"
                  :key="item.parkAreaId"
                  :label="item.name"
                  :value="item.parkAreaId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="泊位" prop="parkAreaBerthId">
              <el-select v-model="formEntry.parkAreaBerthId" filterable placeholder="请选择泊位">
                <el-option
                  v-for="item in areaBerthOptions"
                  :key="item.parkAreaBerthId"
                  :label="item.name"
                  :value="item.parkAreaBerthId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="驶入时间" prop="actionTime">
              <el-date-picker clearable
                              v-model="formEntry.actionTime"
                              type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择驶入时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车牌照片" prop="licencePlateImg">
              <image-upload :limit="1" v-model="formEntry.licencePlateImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="驶入照片" prop="entryImg">
              <image-upload :limit="1" v-model="formEntry.entryImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="停稳照片" prop="entrySteadyImg">
              <image-upload :limit="1" v-model="formEntry.entrySteadyImg"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormEntry">确 定</el-button>
        <el-button @click="cancelEntry">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 新增出场对话框 -->
    <el-dialog title="车辆出场" :visible.sync="openOut" width="900px" append-to-body>
      <el-form ref="formOut" :model="formOut" :rules="rulesOut" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌号码" prop="licensePlateNumber">
              <el-input v-model="formOut.licensePlateNumber" placeholder="请输入车牌号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车牌颜色" prop="licensePlateType">
              <el-select v-model="formOut.licensePlateType" placeholder="请选择车牌颜色">
                <el-option
                  v-for="dict in dict.type.bus_order_license_plate_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停车场" prop="parkAreaId">
              <el-select v-model="formOut.parkAreaId" filterable clearable placeholder="请选择停车场" @change="handleAreaChange">
                <el-option
                  v-for="item in areaOptions"
                  :key="item.parkAreaId"
                  :label="item.name"
                  :value="item.parkAreaId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="泊位" prop="parkAreaBerthId">
              <el-select v-model="formOut.parkAreaBerthId" filterable placeholder="请选择泊位">
                <el-option
                  v-for="item in areaBerthOptions"
                  :key="item.parkAreaBerthId"
                  :label="item.name"
                  :value="item.parkAreaBerthId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="驶离时间" prop="actionTime">
              <el-date-picker clearable
                              v-model="formOut.actionTime"
                              type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择驶入时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车牌照片" prop="licencePlateImg">
              <image-upload :limit="1" v-model="formOut.licencePlateImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预驶离照片" prop="departurePrepareImg">
              <image-upload :limit="1" v-model="formOut.departurePrepareImg"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="驶离照片" prop="departureImg">
              <image-upload :limit="1" v-model="formOut.departureImg"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormOut">确 定</el-button>
        <el-button @click="cancelOut">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrderBerthRecord, getOrderBerthRecord, addCarEntry, addCarOut } from "@/api/business/OrderBerthRecord";
import { listParkAreaType } from "@/api/business/ParkAreaType";
import { listParkArea } from "@/api/business/ParkArea";
import { listParkAreaBerth } from "@/api/business/ParkAreaBerth";

export default {
  name: "OrderBerthRecord",
  dicts: ['bus_order_berth_action_type', 'bus_order_license_plate_type', 'bus_order_parking_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 车辆进出记录表格数据
      OrderBerthRecordList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parkAreaTypeId: null,
        parkAreaId: null,
        actionType: null,
        parkingType: null,
        dateRange: null,
        licensePlateNumber: null,
        parkAreaBerthCode: null,
      },
      //停车场类型-查询
      areaTypeQueryOptions:[],
      //停车场数据-查询
      areaQueryOptions:[],
      // 日期范围
      dateRange: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      formImg: {
        licencePlateImg: null,
        entryImg: null,
        entrySteadyImg: null,
        departurePrepareImg: null,
        departureImg: null,
      },
      openEntry:false,
      openOut:false,
      formEntry:{ },
      rulesEntry:{
        licensePlateNumber: [
          { required: true, message: "车牌号不能为空", trigger: "change" }
        ],
        parkAreaId: [
          { required: true, message: "停车场不能为空", trigger: "blur" }
        ],
        parkAreaBerthId: [
          { required: true, message: "停车场泊位不能为空", trigger: "blur" }
        ],
        licencePlateImg: [
          { required: true, message: "车牌照片不能为空", trigger: "change" }
        ],
        entryImg: [
          { required: true, message: "驶入照片不能为空", trigger: "change" }
        ],
        entrySteadyImg: [
          { required: true, message: "停稳照片不能为空", trigger: "change" }
        ],
      },
      areaOptions:[],
      areaBerthOptions: [],// 停车场泊位信息
      formOut:{ },
      rulesOut:{
        licensePlateNumber: [
          { required: true, message: "车牌号不能为空", trigger: "change" }
        ],
        parkAreaId: [
          { required: true, message: "停车场不能为空", trigger: "blur" }
        ],
        parkAreaBerthId: [
          { required: true, message: "停车场泊位不能为空", trigger: "blur" }
        ],
        licencePlateImg: [
          { required: true, message: "车牌照片不能为空", trigger: "change" }
        ],
        departurePrepareImg: [
          { required: true, message: "预驶离照片不能为空", trigger: "change" }
        ],
        departureImg: [
          { required: true, message: "驶离照片不能为空", trigger: "change" }
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getAreaTypeList();
    this.getAreaList();
  },
  methods: {
    /** 查询停车场类型列表 */
    getAreaTypeList() {
      listParkAreaType().then(response => {
        this.areaTypeQueryOptions = response.data;
      });
    },
    /** 查询停车场列表 */
    getAreaList() {
      listParkArea().then(response => {
        this.areaQueryOptions = response.data;
        this.areaOptions = response.data;
      });
    },
    /** 查询车辆进出记录列表 */
    getList() {
      this.loading = true;
      listOrderBerthRecord(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.OrderBerthRecordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    /** 预览照片按钮操作 */
    handlePreviewImg(row) {
      const orderBerthRecordId = row.orderBerthRecordId
      getOrderBerthRecord(orderBerthRecordId).then(response => {
        this.formImg = response.data;
        this.open = true;
        this.title = "照片";
      });
    },
    //获取某类型下停车场数据
    handleAreaTypeChangeForQuery(value){
      let param = {
        parkAreaTypeId: value
      }
      listParkArea(param).then(response => {
        this.areaQueryOptions = response.data;
      });
    },
    handleEntry(){
      this.openEntry = true;
      this.resetEntry();
    },
    // 取消按钮
    cancelEntry() {
      this.openEntry = false;
      this.resetEntry();
    },
    // 表单重置
    resetEntry() {
      this.formEntry = {
        licensePlateNumber: null,
        licensePlateType: null,
        actionTime: null,
        parkAreaId: null,
        parkAreaBerthId: null,
        licencePlateImg: null,
        entryImg: null,
        entrySteadyImg: null,
      };
      this.resetForm("formEntry");
    },
    /** 提交按钮 */
    submitFormEntry() {
      this.$refs["formEntry"].validate(valid => {
        if (valid) {
          addCarEntry(this.formEntry).then(response => {
            this.$modal.msgSuccess("入场成功");
            this.openEntry = false;
            this.getList();
          });
        }
      });
    },
    /**
     * 停车场改变，获取该停车场下的泊位信息
     */
    handleAreaChange(value) {
      this.formEntry.parkAreaBerthIds = []
      let param = {
        parkAreaId: value
      }
      this.getAreaBerthList(param);
    },
    /** 查询停车场泊位列表 */
    getAreaBerthList(param) {
      listParkAreaBerth(param).then(response => {
        this.areaBerthOptions = response.data;
      });
    },
    handleOut(){
      this.openOut = true;
      this.resetOut();
    },
    // 取消按钮
    cancelOut() {
      this.openOut = false;
      this.resetOut();
    },
    // 表单重置
    resetOut() {
      this.formOut = {
        licensePlateNumber: null,
        actionTime: null,
        licencePlateImg: null,
        departurePrepareImg: null,
        departureImg: null,
      };
      this.resetForm("formOut");
    },
    /** 提交按钮 */
    submitFormOut() {
      this.$refs["formOut"].validate(valid => {
        if (valid) {
          addCarOut(this.formOut).then(response => {
            this.$modal.msgSuccess("出场成功");
            this.openOut = false;
            this.getList();
          });
        }
      });
    },
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
  .img-plate-container{
    display:flex ;
    align-items: center;
  }
  .img-container{
    margin-top: 20px;
  }
  .demonstration{
    font-size: 16px;
    padding-bottom: 5px;
  }
  .img-plate{
    width: 300px;
    height: 60px;
  }
  .img-1{
    width: 300px;
  }
</style>
