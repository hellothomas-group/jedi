// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueAxios from 'vue-axios'
import echarts from 'echarts'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(VueAxios, axios)
// Vue.use(echarts)
Vue.prototype.$echarts = echarts
// Vue.prototype.$http = axios

// axios.defaults.baseURL = 'http://localhost:8082/'// 后端环境地址
// axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'// 配置请求头信息

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
