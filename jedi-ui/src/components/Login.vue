<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" label-width="80px" class="login-form">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="loginForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="loginForm.password"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('loginForm')">登录</el-button>
        <el-button @click="resetForm('loginForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { mapMutations } from 'vuex'
let Base64 = require('js-base64').Base64
export default {
  data () {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    ...mapMutations(['changeLogin']),
    onSubmit (formName) {
      console.log('submit!')
      this.asyncLogin(this.loginForm)
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    asyncLogin (form) {
      console.log('asyncLogin')
      console.log('user login...' + form.username)
      let _this = this

      this.axios.post('/admin/login/', {
        username: form.username,
        password: Base64.encode(form.password)
      }).then(res => {
        console.log(res.data)
        _this.userToken = res.data.token
        _this.userName = res.data.userName
        _this.realName = res.data.realName
        _this.changeLogin({ Authorization: _this.userToken,
          UserName: _this.userName,
          RealName: _this.realName
        })
        if (_this.$route.query.redirect) { // 如果存在参数
          _this.$router.push(_this.$route.query.redirect)// 则跳转至进入登录页前的路由
        } else {
          _this.$router.push('/home')// 否则跳转至首页
        }
      }).catch(function (error) {
        console.log(error)
        alert(error.data.message)
      })
    }
  }
}
</script>

<style acoped>
  .login-form {
    width: 350px;
    margin: 100px auto; /* 上下间距160px，左右自动居中*/
    background-color: rgba(255, 255, 255, 0.8); /* 透明背景色 */
    padding: 30px;
    border-radius: 20px; /* 圆角 */
  }

  /* 背景 */
  .login-container {
    position: absolute;
    width: 100%;
  }
  html,body {
    /*设置内部填充为0，几个布局元素之间没有间距*/
    padding: 0px;
    /*外部间距也是如此设置*/
    margin: 0px;
    /*统一设置高度为100%*/
    height: 100%;
  }
</style> -->
