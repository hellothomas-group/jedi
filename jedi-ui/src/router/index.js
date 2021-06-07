import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import My from '@/components/My'
import Namespace from '@/components/Namespace'
import SingleApp from '../components/SingleApp'
import Executor from '../components/Executor'

Vue.use(Router)

// 公共路由
// export const constantRoutes = [
//   {
//     path: '',
//     component: Layout,
//     redirect: 'index',
//     children: [
//       {
//         path: 'index',
//         component: (resolve) => require(['@/views/index'], resolve),
//         name: '首页',
//         meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
//       }
//     ]
//   }
// ]

export default new Router({
  mode: 'history', // 去掉url中的#
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/my',
      name: 'My',
      component: My
    },
    {
      path: '/home',
      name: 'Namespace',
      component: Namespace
    },
    {
      path: '/app',
      name: 'SingleApp',
      component: SingleApp
    },
    {
      path: '/executor',
      name: 'Executor',
      component: Executor
    }
  ]
})
