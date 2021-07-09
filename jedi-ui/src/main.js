// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueAxios from 'vue-axios'
import echarts from 'echarts'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(VueAxios, axios)
Vue.prototype.$echarts = echarts

// 添加请求拦截器，在请求头中加token
axios.interceptors.request.use(
  config => {
    if (localStorage.getItem('Authorization')) {
      config.headers.Authorization = localStorage.getItem('Authorization')
    }

    return config
  },
  error => {
    return Promise.reject(error)
  })

// 响应拦截器
axios.interceptors.response.use(
  response => {
    // 如果返回的状态码为200且错误码为成功，说明接口请求成功，可以正常拿到数据
    // 否则的话抛出错误
    if (response.status === 200) {
      switch (response.data.code) {
        case 'CODE000':
          return Promise.resolve(response.data)

        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 'CODE005':
          router.replace({
            path: '/login',
            query: {
              redirect: router.currentRoute.fullPath
            }
          })
          break

        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 'CODE004':
          alert('登录过期，请重新登录')
          // 清除token
          localStorage.clear()
          // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
          setTimeout(() => {
            router.replace({
              path: '/login',
              query: {
                redirect: router.currentRoute.fullPath
              }
            })
          }, 1000)
          break
        // 其他错误，具体页面处理
        default:
          console.log(response.data.code)
      }
      return Promise.reject(response)
    } else {
      console.log(response.data.status)
      alert(response.data.message)
      return Promise.reject(response)
    }
  },
  error => {
    if (error.response.data.message && error.response.data.message !== '') {
      alert(error.response.data.message)
    } else {
      alert(error.response.data)
    }
    return Promise.reject(error.response)
  }
)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
