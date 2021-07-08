import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const store = new Vuex.Store({

  state: {
    // 存储token
    Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization') : '',
    UserName: localStorage.getItem('UserName') ? localStorage.getItem('UserName') : '',
    RealName: localStorage.getItem('RealName') ? localStorage.getItem('RealName') : ''
  },

  mutations: {
    // 修改token，并将token存入localStorage
    changeLogin (state, user) {
      state.Authorization = user.Authorization
      state.UserName = user.UserName
      state.RealName = user.RealName
      localStorage.setItem('Authorization', user.Authorization)
      localStorage.setItem('UserName', user.UserName)
      localStorage.setItem('RealName', user.RealName)
    }
  }
})

export default store
