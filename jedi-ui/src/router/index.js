import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import My from '@/components/My'
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
import Instance from '../components/Instance'

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
      component: Executor,
      children: [
        {
          path: '/item',
          name: 'Item',
          component: Item
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
          path: '/instance/all',
          name: 'Instance',
          component: Instance
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
