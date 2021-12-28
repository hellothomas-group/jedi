<template>
  <div id="app">
    <!--<img src="./assets/logo.png">-->
    <div style="font-size: 20px;padding: 20px;float: right" v-if="this.$route.path != '/login'">
      <el-dropdown @command="handleCommand">
        <span>
          {{this.$store.state.RealName}}/{{this.$store.state.UserName}}<i class="el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="loginOff">注销</el-dropdown-item>
          <el-dropdown-item command="updatePwd">修改密码</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div style="font-size: 2em;font-weight: bold;padding: 20px;text-align: center">Jedi线程池管理平台</div>
    <router-view/>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  name: 'App',
  methods: {
    ...mapMutations(['changeLogin']),
    handleCommand (command) {
      if (command === 'loginOff') {
        let _this = this
        _this.changeLogin({ Authorization: '',
          UserName: '',
          RealName: ''
        })
        console.log(window.location.href)
        location.href = window.location.href
      } else {
        this.$message('click on item ' + command)
        // window.open('http://www.baidu.com')
      }
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
