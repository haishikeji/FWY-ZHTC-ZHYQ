<template>
  <div id="app">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";

export default {
  name: "App",
  components: { ThemePicker },
  metaInfo() {
    return {
      title:
        this.$store.state.settings.dynamicTitle &&
        this.$store.state.settings.title,
      titleTemplate: (title) => {
        return title
          ? `${title} - ${process.env.VUE_APP_TITLE}`
          : process.env.VUE_APP_TITLE;
      },
    };
  },
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
/deep/ .el-menu-item.is-active {
  background-color: #1890ff !important;
  color: #fff !important;
  z-index: 999;
}
/deep/ .el-menu-item:hover {
  background-color: #1890ff !important;
  color: #fff !important;
  z-index: 999;
}
/* /deep/ .el-submenu__title:hover {
  background-color: #1890ff !important;
  color: #fff !important;
  z-index: 999;
} */
/deep/ #app .sidebar-container .theme-dark .el-submenu .el-menu-item {
  background-color: #1890ff !important;
  color: #fff !important;
  z-index: 999;
}
/deep/ .submenu-title-noDropdown:hover {
  background-color: #1890ff !important;
}
</style>
