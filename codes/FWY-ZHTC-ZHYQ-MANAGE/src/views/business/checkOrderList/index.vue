<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
      <el-form-item label="停车场" prop="parkAreaId">
        <el-select v-model="queryParams.parkAreaId" filterable placeholder="请选择停车场">
          <el-option
            v-for="item in areaOptions"
            :key="item.parkAreaId"
            :label="item.name"
            :value="item.parkAreaId"
          />
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

    <el-table v-loading="loading" :data="OrderInfoList" @selection-change="handleSelectionChange">
      <!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="订单号" align="center" prop="240"  width="240" >
        <template slot-scope="scope">
          <el-button type="text" @click="handleOrderInfo(scope.row)">{{scope.row.orderSn}}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" width="100" />
      <el-table-column label="车牌颜色" align="center" prop="licensePlateType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_order_license_plate_type" :value="scope.row.licensePlateType"/>
        </template>
      </el-table-column>

      <el-table-column label="订单类型" align="center" prop="orderType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_order_type" :value="scope.row.orderType"/>
        </template>
      </el-table-column>
      <el-table-column label="停车类型" align="center" prop="parkingType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_order_parking_type" :value="scope.row.parkingType"/>
        </template>
      </el-table-column>
      <el-table-column label="泊位号" align="center" prop="parkAreaBerthCode" width="100" />
      <el-table-column label="驶入时间" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.entryTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="驶离时间" align="center" prop="departureTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.departureTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额(元)" align="center" prop="orderAmount" width="120" />
      <el-table-column label="实付金额(元)" align="center" prop="orderRealityAmount" width="120" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleCheck(scope.row)"
            v-hasPermi="['business:OrderInfo:check']"
          >审核</el-button>
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

    <!-- 订单明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="订单号">
              <span>{{form.orderSn}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单状态">
              <dict-tag :options="dict.type.bus_order_status" :value="form.status"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车牌号">
              <span>{{form.licensePlateNumber}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="驶入时间">
              <span>{{form.entryTime}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="驶离时间">
              <span>{{form.departureTime}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="停车时长">
              <span>{{form.parkingDuration}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="停车场">
              <span>{{form.parkAreaName}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="停车类型">
              <dict-tag :options="dict.type.bus_order_parking_type" :value="form.parkingType"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单金额">
              <span>{{form.orderAmount}} 元</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车牌照片">
              <el-image class="img-plate" :src="preUrl+form.licencePlateImg">
                <div slot="error" class="image-slot">
                  暂无
                </div>
              </el-image>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车牌颜色">
              <dict-tag :options="dict.type.bus_order_license_plate_type" :value="form.licensePlateType"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="驶入照片">
              <div class="img-container">
                <el-image class="img" :src="preUrl + form.entryImg">
                  <div slot="error" class="image-slot">
                    暂无
                  </div>
                </el-image>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="停稳照片" >
              <div class="img-container">
                <el-image class="img" :src="preUrl + form.entrySteadyImg">
                  <div slot="error" class="image-slot">
                    暂无
                  </div>
                </el-image>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预驶离照片">
              <div class="img-container">
                <el-image class="img" :src="preUrl + form.departurePrepareImg">
                  <div slot="error" class="image-slot">
                    暂无
                  </div>
                </el-image>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="驶离照片" >
              <div class="img-container">
                <el-image class="img" :src="preUrl + form.departureImg">
                  <div slot="error" class="image-slot">
                    暂无
                  </div>
                </el-image>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核 -->
    <el-dialog title="审核" :visible.sync="openCheck" width="500px" append-to-body>
      <el-form ref="formCheck" :model="formCheck" :rules="rulesCheck" label-width="80px">
        <el-form-item label="审核状态" prop="checkStatus">
          <el-radio-group v-model="formCheck.checkStatus">
            <el-radio
              v-for="dict in dict.type.order_check_status"
              :key="dict.value"
              :label="dict.value"
              v-if="dict.value != '1'"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核说明" prop="checkRemark">
          <el-input v-model="formCheck.checkRemark" type="textarea" :rows="5" placeholder="请输入审核说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormCheck">确 定</el-button>
        <el-button @click="cancelCheck">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listForCheck, getOrderInfo, checkOrder } from "@/api/business/OrderInfo";
  import { listParkArea } from "@/api/business/ParkArea";

  export default {
    name: "OrderInfo",
    dicts: ['bus_order_payment_platform', 'bus_order_payment_type', 'bus_order_status', 'bus_order_parking_type'
      , 'bus_order_license_plate_type', 'global_boolean', 'bus_order_type', 'order_check_status'],
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
        // 订单列表表格数据
        OrderInfoList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        //停车场信息
        areaOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        preUrl: process.env.VUE_APP_BASE_API,
        form:{},
        rules:{},
        openCheck:false,
        formCheck:{},
        rulesCheck:{}
      };
    },
    created() {
      this.getAreaList();
      this.getList();
    },
    methods: {
      /** 查询停车场列表 */
      getAreaList() {
        listParkArea().then(response => {
          this.areaOptions = response.data;
        });
      },
      /** 查询订单列表列表 */
      getList() {
        this.loading = true;
        listForCheck(this.queryParams).then(response => {
          this.OrderInfoList = response.rows;
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
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.orderId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('business/OrderInfo/export', {
          ...this.queryParams
        }, `订单记录_${new Date().getTime()}.xlsx`)
      },
      /**
       * 停车场改变，获取该停车场下的泊位信息
       */
      handleAreaChange(value) {
        this.form.parkAreaBerthIds = []
        let param = {
          parkAreaId: value
        }
        this.getAreaBerthList(param);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          orderId: null,
          orderType: null,
          orderSn: null,
          status: null,
          parkingType: null,
          licensePlateNumber: null,
          licensePlateType: null,
          entryTime: null,
          departureTime: null,
          duration: null,
          parkAreaId: null,
          parkAreaBerthId: null,
          orderAmount: null,
          orderRealityAmount: null,
          paymentType: null,
          paymentPlatform: null,
          paymentTime: null,
          licencePlateImg: null,
          entryImg: null,
          entrySteadyImg: null,
          departurePrepareImg: null,
          departureImg: null,
          isAbnormal: null,
          delFlag: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null
        };
        this.resetForm("form");
      },
      handleOrderInfo(row) {
        this.reset();
        const orderId = row.orderId || this.ids
        getOrderInfo(orderId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "订单详情";
        });
      },
      handleCheck(row){
        this.resetCheck();
        const orderId = row.orderId || this.ids
        getOrderInfo(orderId).then(response => {
          this.formCheck = response.data;
          this.openCheck = true;
        });
      },
      // 表单重置
      resetCheck() {
        this.formCheck = {
          orderId: null,
          checkStatus: null,
          checkRemark: null,
        };
        this.resetForm("formCheck");
      },
      /** 提交按钮 */
      submitFormCheck() {
        this.$refs["formCheck"].validate(valid => {
          if (valid) {
            if (this.formCheck.orderId != null) {
              checkOrder(this.formCheck).then(response => {
                this.$modal.msgSuccess("审核成功");
                this.openCheck = false;
                this.getList();
              });
            }
          }
        })
      },
      // 取消按钮
      cancelCheck() {
        this.openCheck = false;
        this.resetCheck();
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
  .img-container{

  }
  .img-plate{
    width: 200px;
    height: 60px;
  }
  .img-container{
    width: 300px;
  }
  .img{
    width: 100%;
  }
</style>
