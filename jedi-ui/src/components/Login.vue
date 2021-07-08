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
        password: form.password
      }).then(res => {
        console.log(res.data)
        if (res.data.code === 'CODE000') {
          _this.userToken = res.data.data
          _this.changeLogin({ Authorization: _this.userToken })
          _this.$router.push('/home')
        } else {
          alert('登录失败,原因:' + res.data.message)
        }
      }).catch(function (error) {
        console.log(error)
        alert('登录失败')
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
