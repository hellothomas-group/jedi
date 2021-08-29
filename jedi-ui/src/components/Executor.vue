<template>
  <div>
    <el-container>
        <el-aside width="15%" style="margin-left: 20px">
          <el-row class="tac">
            <el-col :span="12" class="my-el-col-12">
              <div style="text-align: center;">
                <el-tag type="info" @click="forwardNamespace()"
                        onmouseover="this.style.background='#99a9bf';"
                        onmouseleave="this.style.background='#f4f4f5';" style="margin-bottom: 5px;cursor: pointer;">所属环境:
                  {{this.executor.namespaceName}}</el-tag>
                <el-tag type="info" @click="forwardApp(executor.namespaceName, executor.appId)"
                        onmouseover="this.style.background='#99a9bf';"
                        onmouseleave="this.style.background='#f4f4f5';" style="margin-bottom: 5px;cursor: pointer;">所属应用:
                  {{this.executor.appId}}</el-tag>
                <el-tag type="info" style="margin-bottom: 5px">线程池名称: {{this.executor.executorName}}</el-tag>
              </div>
              <el-menu
                :default-active="defaultView"
                class="el-menu-vertical-demo"
                unique-opened
                router
                @open="handleOpen"
                @close="handleClose"
                @select="handleSelect">
                <el-submenu index="1">
                  <template slot="title">
                    <i class="el-icon-menu"></i>
                    <span>线程池信息</span>
                  </template>
                  <el-menu-item index="/overview"
                                :route="{path:'/overview',query:{namespace:executor.namespaceName,appId:executor.appId,executor:executor.executorName}}">概览
                  </el-menu-item>
                  <el-menu-item index="/executor-status"
                                :route="{path:'/executor-status',query:{namespace:executor.namespaceName,appId:executor.appId,executor:executor.executorName}}">实时状态
                  </el-menu-item>
                  <el-menu-item index="/release" :route="{path:'/release',query:{namespace:executor.namespaceName,appId:executor.appId,executor:executor.executorName}}">发布信息</el-menu-item>
                  <el-submenu index="1-2">
                    <template slot="title">实例信息</template>
                    <el-menu-item index="/instance/active-release"
                                  :route="{path:'/instance/active-release',query:{namespace:executor.namespaceName,appId:executor.appId,executor:executor.executorName}}">最新配置实例</el-menu-item>
                    <el-menu-item index="/instance/inactive-release"
                                  :route="{path:'/instance/inactive-release',query:{namespace:executor.namespaceName,appId:executor.appId,executor:executor.executorName}}">非最新配置实例</el-menu-item>
                  </el-submenu>
                </el-submenu>
                <el-submenu index="2">
                  <template slot="title">
                    <i class="el-icon-menu"></i>
                    <span>线程池任务</span>
                  </template>
                  <el-menu-item index="/task/statistics-now"
                                :route="{path:'/task/statistics-now',query:{namespace:this.executor.namespaceName,appId:this.executor.appId,executor:this.executor.executorName}}">当天任务统计</el-menu-item>
                  <el-menu-item index="/task/statistics-history"
                                :route="{path:'/task/statistics-history',query:{namespace:this.executor.namespaceName,appId:this.executor.appId,executor:this.executor.executorName}}">历史任务统计</el-menu-item>
                  <el-menu-item index="/task/detail"
                                :route="{path:'/task/detail',query:{namespace:this.executor.namespaceName,appId:this.executor.appId,executor:this.executor.executorName}}">任务明细查询</el-menu-item>
                </el-submenu>
                <el-menu-item index="/alarm"
                              :route="{path:'/alarm',query:{namespace:this.executor.namespaceName,appId:this.executor.appId,executor:this.executor.executorName}}">
                  <i class="el-icon-setting"></i>
                  <span slot="title">线程池报警</span>
                </el-menu-item>
              </el-menu>
            </el-col>
          </el-row>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
    </el-container>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data () {
    return {
      executor: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      },
      defaultView: '/overview'
    }
  },
  computed: {},
  watch: {},
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.executor.namespaceName = this.$route.query.namespace
      this.executor.appId = this.$route.query.appId
      this.executor.executorName = this.$route.query.executor
      this.asyncQueryExecutor(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  mounted () {},
  methods: {
    asyncQueryExecutor (namespaceName, appId, executorName) {
      console.log('asyncQueryExecutor')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName
      ).then(res => {
        console.log(res)
        this.executor = res.data
        if (this.$route.path === '/executor') {
          this.defaultView = '/overview'
          this.forwardDefaultActive(namespaceName, appId, executorName)
        } else {
          this.defaultView = this.$route.path
          this.$router.push({
            path: this.$route.path,
            query: {
              namespace: namespaceName,
              appId: appId,
              executor: executorName
            }
          })
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    forwardApp (namespaceName, appId) {
      console.log('forwardApp')
      this.$router.push({
        path: '/app',
        query: {
          namespace: namespaceName,
          appId: appId
        }
      })
    },
    forwardNamespace () {
      console.log('forwardNamespace')
      this.$router.push({
        path: '/home',
        query: {
          namespace: undefined
        }
      })
    },
    forwardDefaultActive (namespaceName, appId, executorName) {
      console.log('forwardDefaultActive')
      this.$router.push({
        path: '/overview',
        query: {
          namespace: namespaceName,
          appId: appId,
          executor: executorName
        }
      })
    }
  }
}

</script>
<style>
  .el-aside {
    color: #333;
  }
  html,body,#app,.el-container{
    /*设置内部填充为0，几个布局元素之间没有间距*/
    padding: 0px;
    /*外部间距也是如此设置*/
    margin: 0px;
    /*统一设置高度为100%*/
    height: 100%;
  }
  .my-el-col-12 {
    width: 100% !important;
  }
</style>
