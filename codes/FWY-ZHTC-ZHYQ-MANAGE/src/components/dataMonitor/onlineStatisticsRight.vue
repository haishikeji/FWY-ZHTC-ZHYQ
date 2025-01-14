<template>
  <div class="onlineStatisticsRight">
    <div class="title">
      <!-- <div class="statistics"></div> -->
      <div class="name">在线统计</div>
    </div>
    <!-- <dv-decoration-2 style="width:170px;height:5px; margin-left: 20px; margin-top: -10px;" /> -->
    <div class="content">
      <div class="left">
        <div>
          <div class="title">设备在线</div>
          <div class="percent">
            <div>
              {{deviceOnlineData.deviceOnlineNumber}} / {{deviceOnlineData.deviceNumber}}
            </div>
            <div>
              <el-progress :percentage="deviceOnlineData.deviceOnlineRate" color="#00e3ff" text-color="#fff" ></el-progress>
            </div>
          </div>
        </div>
        <div class="user">
          <div class="title">人员在岗</div>
          <div class="percent">
            <div>
              {{userWorkData.userWorkNumber}} / {{userWorkData.userNumber}}
            </div>
            <div>
              <el-progress :percentage="userWorkData.userWorkRate" color="#00e3ff" text-color="#fff" ></el-progress>
            </div>
          </div>
        </div>
      </div>
      <div class="right">
        <div class="per">
          <el-progress type="circle" :percentage="todayPaymentRateData.paymentRate" :width="120" color="#ff860c" text-color="#fff"></el-progress>
          <div class="todayPercent">今日缴费率</div>
        </div>
        <div class="per last">
          <el-progress type="circle" :percentage="thirtyPaymentRateData.paymentRate" :width="120" color="#00e3ff" text-color="#fff"></el-progress>
          <div class="todayPercent">近30天缴费率</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { getDeviceOnlineStat, getUserWorkStat, getTodayPaymentRate, getThirtyPaymentRate } from "@/api/bigdata/largeScreen";

  export default {
    name: '',
    data() {
      return {
        deviceOnlineData:{},
        userWorkData:{},
        todayPaymentRateData:{},
        thirtyPaymentRateData:{}
      }
    },
    created() {
      this.getDeviceOnlineStat();
      this.getUserWorkStat();
      this.getTodayPaymentRate();
      this.getThirtyPaymentRate();
    },
    methods: {
      getDeviceOnlineStat(){
        getDeviceOnlineStat().then(response => {
          this.deviceOnlineData = response.data
          this.deviceOnlineData.deviceOnlineRate = parseFloat(response.data.deviceOnlineRateStr)
        })
      },
      getUserWorkStat(){
        getUserWorkStat().then(response => {
          this.userWorkData = response.data
          this.userWorkData.userWorkRate = parseFloat(response.data.userWorkRateStr)
        })
      },
      getTodayPaymentRate(){
        getTodayPaymentRate().then(response => {
          this.todayPaymentRateData = response.data
          this.todayPaymentRateData.paymentRate = parseFloat(response.data.paymentRateStr)
        })
      },
      getThirtyPaymentRate(){
        getThirtyPaymentRate().then(response => {
          this.thirtyPaymentRateData = response.data
          this.thirtyPaymentRateData.paymentRate = parseFloat(response.data.paymentRateStr)
        })
      },
    },
  }
</script>

<style scoped lang='scss'>
  .onlineStatisticsRight {
    .title {
      display: flex;
      flex-direction: row;
      align-items: center;
      padding: 20px;
      .statistics {
        width: 25px;
        height: 25px;
        background-image: url(../../assets/images/dataMonitoring/statistics.svg);
        margin-left: 20px;
      }
      .name {
        color: #a6c6f3;
        font-size: 18px;
        font-weight: 600;
        // margin-left: 20px;
      }

    }
    .content {
      display: flex;
      flex-direction: row;
      align-items: center;
      .left {
        width: 45%;
        .user{
          padding-top: 10px;
        }
        ::v-deep .el-progress-bar{
          width: 90% !important;
        }
        .title {
          color: #69c2ff;
          padding: 10px 20px;
        }
        .percent {
          color: #69c2ff;
          margin-left: 20px;
        }
      }
      .right {
        display: flex;
        flex-direction: row;
        align-items: center;
        padding-top: 20px;
        margin-left: 10px;
        .per {
          display: flex;
          flex-direction: column;
          align-items: center;
          .todayPercent {
            color: #69c2ff;
            margin-top: 20px;
          }
        }
        .last{
          margin-left: 20px;
        }
      }
    }
  }
</style>
