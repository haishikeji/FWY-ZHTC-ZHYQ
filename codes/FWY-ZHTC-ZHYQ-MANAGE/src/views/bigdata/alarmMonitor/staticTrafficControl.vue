<template>
  <div class="data_screen_content ">
    <div class="data_screen_content_left jointly">
      <!--        车场统计，泊位统计-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <leftFirst/>
        </dv-border-box-12>
      </div>
      <!--        泊位占用率-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <berthageOccupy/>
        </dv-border-box-12>
      </div>
      <!--        泊位利用率-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <utilizationRate/>
        </dv-border-box-12>
      </div>
      <!--        泊位周转次数-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <turnoverTrain/>
        </dv-border-box-12>
      </div>
    </div>

    <div class="data_screen_content_center">
      <div class="center_title">
        <div class="center_title_data">
          <span>进车流量</span>
          <span>{{trafficVolume.enterTrafficVolume}}</span>
        </div>
        <div class="center_title_data">
          <span>出车流量</span>
          <span>{{trafficVolume.outTrafficVolume}}</span>
        </div>
        <div class="center_title_data">
          <span>总车流量</span>
          <span>{{trafficVolume.totalTrafficVolume}}</span>
        </div>
      </div>
      <div class="center_bottom">
        <div class="center_bottom_left">
          <h3>车场饱和度</h3>
          <div v-for="item in saturation" :key="item.isclass" class="quantity">
            <div :class="item.isclass" class="colour"></div>
            <div>{{ item.quantity }}</div>
          </div>
        </div>
        <div class="center_bottom_right">
          <div>
            <svg-icon icon-class="park" class="input-icon"/>
            <span>路侧停车场</span>
          </div>
          <div>
            <svg-icon icon-class="park_indoor" class="input-icon"/>
            <span>场库停车场</span>
          </div>
        </div>
      </div>
    </div>

    <div class="data_screen_content_right jointly">
      <!--        车流量，违停，套牌-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <rightFirst/>
        </dv-border-box-12>
      </div>
      <!--        车流量实时监控-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <flowMonitor/>
        </dv-border-box-12>
      </div>
      <!--        饱和度-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <saturaTion/>
        </dv-border-box-12>
      </div>
      <!--        归属地统计-->
      <div class="over_size">
        <dv-border-box-12 class="left_first">
          <homePlace/>
        </dv-border-box-12>
      </div>
    </div>
  </div>
</template>

<script>
  import { getTrafficVolume } from "@/api/bigdata/largeScreen";

  import leftFirst from '@/components/Screen/leftFirst.vue'//车场统计，泊位统计
  import berthageOccupy from '@/components/Screen/berthageOccupy.vue'//泊位占用率
  import utilizationRate from '@/components/Screen/utilizationRate.vue'//泊位利用率
  import turnoverTrain from '@/components/Screen/turnoverTrain.vue'//泊位周转次数
  import rightFirst from '@/components/Screen/rightFirst.vue'//车流量，违停，套牌
  import flowMonitor from '@/components/Screen/flowMonitor.vue'//车流量实时监测
  import saturaTion from '@/components/Screen/saturaTion.vue'//饱和度
  import homePlace from '@/components/Screen/homePlace.vue'//归属地统计

  export default {
    name: "staticTrafficControl",
    components: {
      leftFirst,
      berthageOccupy,
      utilizationRate,
      turnoverTrain,
      rightFirst,
      flowMonitor,
      saturaTion,
      homePlace
    },
    data() {
      return {
        type:1,
        currentDate: new Date(),
        saturation: [{
          isclass: 'red',
          quantity: '90%以上'
        }, {
          isclass: 'yellow',
          quantity: '60%~90%'
        }, {
          isclass: 'blue',
          quantity: '30%~60%'
        }, {
          isclass: 'green',
          quantity: '30%以下'
        }],
        trafficVolume:{}
      }
    },
    computed: {
      currentTime() {
        const hours = this.currentDate.getHours().toString().padStart(2, '0')
        const minutes = this.currentDate.getMinutes().toString().padStart(2, '0')
        const seconds = this.currentDate.getSeconds().toString().padStart(2, '0')
        return `${hours}:${minutes}:${seconds}`
      },
      currentDateFormatted() {
        const date = new Date()
        const year = date.getFullYear()
        const month = (date.getMonth() + 1).toString().padStart(2, '0')
        const day = date.getDate().toString().padStart(2, '0')
        return `${year}年${month}月${day}日`
      },
      currentDayOfWeek() {
        return this.currentDate.toLocaleDateString('zh-CN', { weekday: 'long' })
      }
    },
    created() {
      //获取车流量统计数据
      this.getTrafficVolume()

      setInterval(() => {
        this.currentDate = new Date()
      }, 1000)
    },
    methods: {
      getTrafficVolume(){
        getTrafficVolume().then(response => {
          this.trafficVolume = response.data
        })
      },
      parkOperCenter(){
        this.type = 1;
      },
      staticTrafficControl(){
        this.type = 2;
      },
      gisMap(){
        this.type = 0;
      },
    }
  }
</script>

<style scoped lang="scss">

  .data_screen_content {
    display: flex;

    .jointly {
      flex: 0 1 30%;
      height: 90vh;
      display: flex;
      flex-direction: column;
      z-index: 20;

      .over_size {
        flex: 1;
        height: 25%;
        width: 100%;
        display: flex;
        background-color: rgba(0, 35, 126, 0.9);

        .left_first {
          flex: 1;
        }
      }
    }

    .data_screen_content_center {
      flex: 0 1 40%;
      position: relative;

      .center_title {
        position: relative;
        z-index: 20;
        width: 100%;
        height: 10vh;
        background-color: rgba(16, 35, 73, 0.9);
        display: flex;
        justify-content: space-around;
        align-items: center;
        color: #FFFFFF;

        .center_title_data {
          display: flex;
          flex-direction: column;
          align-items: center;
          font-size: 17px;

          span:nth-child(2) {
            color: #01e5fc;
            font-size: 30px;
            font-weight: bold;
          }
        }
      }

      .center_bottom_left {
        position: absolute;
        bottom: 0;
        left: 10px;
        z-index: 20;
        color: #FFFFFF;
        display: flex;
        flex-direction: column;
        width: 35%;
        height: 23%;
        background-color: rgba(15, 40, 92, 0.8);

        h3 {
          text-align: center;
        }

        .quantity {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 5px 20px;

          .colour {
            width: 70px;
            height: 20px;
          }
        }

        .red {
          background-color: #FF483D;
        }

        .yellow {
          background-color: #FF911F;
        }

        .blue {
          background-color: #1FC6FF;
        }

        .green {
          background-color: #06F7A1;
        }
      }

      .center_bottom_right {
        position: absolute;
        bottom: 0;
        right: 10px;
        z-index: 20;
        color: #FFFFFF;
        width: 35%;
        height: 23%;
        background-color: rgba(15, 40, 92, 0.8);
        display: flex;
        flex-direction: column;
        justify-content: space-around;

        div {
          display: flex;
          justify-content: space-around;
          align-items: center;

          .input-icon {
            width: 30px;
            height: 30px;
          }
        }
      }
    }

    .data_screen_content_right {
      flex: 0 1 30%;
      display: flex;
      flex-direction: column;
    }
  }


</style>
