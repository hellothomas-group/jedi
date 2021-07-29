<template>
  <div id="app">
    <!--<img src="./assets/logo.png">-->
    <div style="font-size: 20px;padding: 20px;float: right" v-if="this.$store.state.RealName != null &&
    this.$route.path != '/login'">
      <el-dropdown @command="handleCommand">
        <span>
          {{this.$store.state.RealName}}<i class="el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="loginOut">注销</el-dropdown-item>
          <el-dropdown-item command="updatePwd">修改密码</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div style="font-size: 2em;font-weight: bold;padding: 20px;text-align: center">Jedi线程池管理平台</div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    handleCommand (command) {
      this.$message('click on item ' + command)
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
