<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="经纬度列表" name="list">
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
          <el-form-item label="时间">
            <el-date-picker
              v-model="dateRange"
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

        <el-table v-loading="loading" :data="clockInList">
          <el-table-column label="姓名" align="center" prop="userName" />
          <el-table-column label="所属停车场" align="center" prop="parkAreaName" />
          <el-table-column label="经度" align="center" prop="lng" />
          <el-table-column label="纬度" align="center" prop="lat" />
          <el-table-column label="时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
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
      </el-tab-pane>
      <el-tab-pane label="轨迹地图" name="map">
        <div id="baiduMap" style="width: 100%;height: 700px">
          <el-bmap id="bmapDemo" :zoom="zoom" :center="center" class="bmap-demo">
            <el-bmap-polyline
              :points="points"
              stroke-color="blue"
              stroke-weight="2"
              stroke-opacity="0.5"
            ></el-bmap-polyline>
          </el-bmap>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import styleJson from '@/assets/styles/mapStyle.json' // 引入地图样式文件
  import { listClockIn, getClockIn, delClockIn, addClockIn, updateClockIn } from "@/api/system/clockIn";
  import { listParkArea } from "@/api/business/ParkArea";

  export default {
    name: "ClockIn",
    dicts: ['sys_attendance_status'],
    data() {
      return {
        activeName: "list",
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
        dateRange: [],
        center: [112.8401729, 28.28703129],
        zoom: 13,
        points:[
          { lng: 116.397128, lat: 39.916527 },
          { lng: 116.400024, lat: 39.917125 },
          { lng: 116.403625, lat: 39.919828 },
          { lng: 116.406022, lat: 39.919233 },
          { lng: 116.407229, lat: 39.918636 },
          { lng: 116.412422, lat: 39.917939 },
          { lng: 116.413729, lat: 39.916732 },
          { lng: 116.416826, lat: 39.915836 },
          { lng: 116.418023, lat: 39.914833 },
          { lng: 116.420121, lat: 39.913138 },
          { lng: 116.422819, lat: 39.912241 },
          { lng: 116.425715, lat: 39.911747 },
          { lng: 116.428312, lat: 39.911254 },
          { lng: 116.430609, lat: 39.911761 },
          { lng: 116.432807, lat: 39.913869 },
          { lng: 116.435704, lat: 39.916575 },
          { lng: 116.437901, lat: 39.918682 },
          { lng: 116.4405, lat: 39.919389 },
          { lng: 116.4428, lat: 39.920787 },
          { lng: 116.445, lat: 39.922185 },
          { lng: 116.4471, lat: 39.924282 },
          { lng: 116.4492, lat: 39.926879 },
          { lng: 116.4513, lat: 39.929977 },
          { lng: 116.4534, lat: 39.933575 },
          { lng: 116.4555, lat: 39.936872 },
          { lng: 116.4576, lat: 39.939669 },
        ]
      };
    },
    computed:{
      mapStyle(){
        let style = {
          styleJson:styleJson
        }
        return style;
      },
    },
    created() {
      this.getList();
    },
    methods: {
      handleClick(tab, event) {
        if (this.activeName == 'list'){
          this.queryParams.pageNum = 1;
          this.getList();
        }
        if (this.activeName == 'map'){
          //加载轨迹地图
        }
      },
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
          this.addDateRange(this.queryParams, this.dateRange),
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
  .bmap-demo {
    height: 100vh;
  }
</style>
