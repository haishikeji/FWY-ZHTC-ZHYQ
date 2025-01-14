<template>
  <div class="box">
    <div class="box-shadow">
      <span class="title">实时车流量</span>
      <div class="roadside">
        <div class="roadside_center">
          <span class="num">{{realTimeTraffic.roadsideEnter}}</span>
          <span class="text">路侧入口</span>
        </div>
        <div class="roadside_center">
          <span class="num">{{realTimeTraffic.roadsideOut}}</span>
          <span class="text">路侧出口</span>
        </div>
      </div>
      <div class="warehouse">
        <div class="roadside_center">
          <span class="num">{{realTimeTraffic.warehouseEnter}}</span>
          <span class="text">场库入口</span>
        </div>
        <div class="roadside_center">
          <span class="num">{{realTimeTraffic.warehouseOut}}</span>
          <span class="text">场库出口</span>
        </div>
      </div>
    </div>
    <div class="box-shadow">
      <span class="title">违法停车&套牌监控</span>
      <div class="roadside">
        <div class="roadside_center">
          <span class="num_red">{{illegalStopDeck.todayIllegalStop}}</span>
          <span class="text">今日违停</span>
        </div>
        <div class="roadside_center">
          <span class="num_yellow">{{illegalStopDeck.todayDeck}}</span>
          <span class="text">今日套牌</span>
        </div>
      </div>
      <div class="warehouse">
        <div class="roadside_center">
          <span class="num">{{illegalStopDeck.illegalStop}}</span>
          <span class="text">历史违停</span>
        </div>
        <div class="roadside_center">
          <span class="num">{{illegalStopDeck.deck}}</span>
          <span class="text">历史套牌</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { getRealTimeTrafficVolumeStat, getIllegalStopDeck } from "@/api/bigdata/largeScreen";
export default {
  data() {
    return {
      realTimeTraffic:{},
      illegalStopDeck:{}
    }
  },
  created() {
    this.getRealTimeTrafficVolumeStat();
    this.getIllegalStopDeck();
  },
  methods: {
    getRealTimeTrafficVolumeStat() {
      getRealTimeTrafficVolumeStat().then(response => {
        this.realTimeTraffic = response.data;
      })
    },
    getIllegalStopDeck() {
      getIllegalStopDeck().then(response => {
        this.illegalStopDeck = response.data;
      })
    }
  }

}
</script>


<style scoped lang="scss">
  .box{
    padding: 20px;
    display: flex;
    justify-content: space-between;
    height: 100%;
  }
  .box-shadow{
    flex: 0 1 45%;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .title{
      color: #a6c6f3;
      font-size: 20px;
      font-weight: bold;
    }
    .roadside,.warehouse{
      display: flex;
      .roadside_center{
        flex: 0 1 50%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        .num{
          color: #1FC6FF;
          font-size: 20px;
        }
        .text{
          color: #fff;
        }
        .num_red{
          color: #ff2e38;
      }
        .num_yellow{
          color: #f3d200;
        }
      }
    }

  }
</style>
