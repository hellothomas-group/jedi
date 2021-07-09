import Vue from 'vue'
import Router from 'vue-router'
import Namespace from '@/components/Namespace'
import SingleApp from '../components/SingleApp'
import Executor from '../components/Executor'
import Item from '../components/Item'
import Release from '../components/Release'
import Task from '../components/Task'
import Alarm from '../components/Alarm'
import Custom from '../components/Custom'
import ActiveReleaseInstance from '../components/ActiveReleaseInstance'
import InactiveReleaseInstance from '../components/InactiveReleaseInstance'
import ExecutorStatus from '../components/ExecutorStatus'
import Login from '../components/Login'

Vue.use(Router)

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new Router({
  mode: 'history', // 去掉url中的#
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
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
      component: Executor,
      children: [
        {
          path: '/item',
          name: 'Item',
          component: Item
        },
        {
          path: '/executor-status',
          name: 'ExecutorStatus',
          component: ExecutorStatus
        },
        {
          path: '/release',
          name: 'Release',
          component: Release
        },
        {
          path: '/instance/active-release',
          name: 'ActiveReleaseInstance',
          component: ActiveReleaseInstance
        },
        {
          path: '/instance/inactive-release',
          name: 'InactiveReleaseInstance',
          component: InactiveReleaseInstance
        },
        {
          path: '/task',
          name: 'Task',
          component: Task
        },
        {
          path: '/alarm',
          name: 'Alarm',
          component: Alarm
        },
        {
          path: '/custom',
          name: 'Custom',
          component: Custom
        }
      ]
    }
  ]
})

// 导航守卫
// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
  } else {
    let token = localStorage.getItem('Authorization')

    if (token === null || token === '') {
      next({
        path: '/login',
        query: { redirect: to.fullPath } // 将要跳转路由的path作为参数，传递到登录页面
      })
    } else {
      next()
    }
  }
})

export default router
