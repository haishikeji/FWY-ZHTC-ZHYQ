<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view v-if="!$route.meta.link" :key="key" />
      </keep-alive>
    </transition>
    <iframe-toggle />
    <div class="hotline">
      <el-button type="primary" circle @click="handleHotline">热线<br/>登记</el-button>
    </div>
    <!-- 添加或修改热线情况登记对话框 -->
    <el-dialog title="添加热线登记" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="hotline-form">
        <el-form-item label="日期" prop="entryTime">
          <el-date-picker clearable
                          v-model="form.entryTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="处理结果" prop="dealResult">
          <el-radio-group v-model="form.dealResult">
            <el-radio
              v-for="dict in dict.type.bus_deal_result"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择问题类型">
            <el-option
              v-for="dict in dict.type.bus_problem_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="phoneNumber">
          <el-input v-model="form.phoneNumber" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="车牌号" prop="carNumber">
          <el-input v-model="form.carNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="反馈事项">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入内容" />
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
  </section>
</template>

<script>
import iframeToggle from "./IframeToggle/index"
import { addHotline } from "@/api/operate/hotline";

export default {
  name: 'AppMain',
  dicts: ['bus_deal_result', 'bus_problem_type'],
  components: { iframeToggle },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    }
  },
  data(){
    return {
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        type: [
          { required: true, message: "问题类型不能为空", trigger: "change" }
        ],
      },
    }
  },
  methods:{
    handleHotline(){
      console.log("点击热线登录")
      this.open = true;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        hotlineId: null,
        entryTime: null,
        dealResult: null,
        type: null,
        phoneNumber: null,
        carNumber: null,
        content: null,
        remark: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addHotline(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
          });
        }
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
.hotline{
  position: absolute;
  right: 10px;
  bottom: 30px;
}

.hotline-form ::v-deep .el-select{
  width: 100% !important;
}
.hotline-form ::v-deep .el-date-editor{
  width: 100% !important;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px;
  }
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
