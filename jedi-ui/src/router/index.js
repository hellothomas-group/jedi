import Vue from 'vue'
import Router from 'vue-router'
import Namespace from '../components/Namespace'
import SingleApp from '../components/SingleApp'
import Executor from '../components/Executor'
import Overview from '../components/Overview'
import Release from '../components/Release'
import TaskStatisticNow from '../components/TaskStatisticNow'
import Alarm from '../components/Alarm'
import ActiveReleaseInstance from '../components/ActiveReleaseInstance'
import InactiveReleaseInstance from '../components/InactiveReleaseInstance'
import ExecutorStatus from '../components/ExecutorStatus'
import Login from '../components/Login'
import TaskStatisticHistory from '../components/TaskStatisticHistory'
import TaskListDetail from '../components/TaskListDetail'
import TaskSingleDetail from '../components/TaskSingleDetail'

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
          path: '/overview',
          name: 'Overview',
          component: Overview
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
          path: '/task/statistics-now',
          name: 'TaskStatisticsNow',
          component: TaskStatisticNow
        },
        {
          path: '/task/statistics-history',
          name: 'TaskStatisticsHistory',
          component: TaskStatisticHistory
        },
        {
          path: '/task/list-detail',
          name: 'TaskListDetail',
          component: TaskListDetail
        },
        {
          path: '/task/single-detail',
          name: 'TaskSingleDetail',
          component: TaskSingleDetail
        },
        {
          path: '/alarm',
          name: 'Alarm',
          component: Alarm
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
