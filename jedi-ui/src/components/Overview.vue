<template>
  <div class="hello">
    <el-container>
        <el-card class="box-card" style="margin-left: 50px">
          <div slot="header" class="clearfix">
            <span>当前配置</span>
          </div>
          <table border="1" style="margin: auto;line-height: 60px;table-layout:fixed;">
            <tr>
              <th>线程池负责人</th>
              <td style="width: 60%;text-align: center">{{this.executor.createUser}}</td>
            </tr>
            <tr>
              <th>线程池配置</th>
              <td style="width: 60%;text-align: center;word-break:break-all;line-height: 20px;padding: 10px">{{this.release
                .configurations}}</td>
            </tr>
            <tr>
              <th>配置最近更新时间</th>
              <td style="width: 60%;text-align: center">{{this.release.updateTime}}</td>
            </tr>
          </table>
        </el-card>
      <el-card class="box-card" style="margin-left: 50px">
        <div slot="header" class="clearfix">
          <span>任务汇总</span>
        </div>
        <table border="1" style="margin: auto;line-height: 60px;table-layout:fixed;">
          <tr>
            <th>任务总数</th>
            <td style="width: 60%;text-align: center">{{this.taskSummary.total}}</td>
          </tr>
          <tr>
            <th>失败任务总数</th>
            <td style="width: 60%;text-align: center">{{this.taskSummary.failure}}</td>
          </tr>
          <tr>
            <th>任务最长时间(ms)</th>
            <td style="width: 60%;text-align: center">{{this.taskSummary.executionTimeMax}}</td>
          </tr>
          <tr>
            <th>任务最短时间(ms)</th>
            <td style="width: 60%;text-align: center">{{this.taskSummary.executionTimeMin}}</td>
          </tr>
        </table>
      </el-card>
    </el-container>
  </div>
</template>

<script>
import format from '../assets/js/dateFormat.js'
export default {
  data () {
    return {
      executor: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        createUser: undefined,
        updateUser: undefined,
        createTime: undefined,
        updateTime: undefined
      },
      release: {
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        configurations: '未发布,客户端使用本地配置',
        comment: undefined,
        createUser: undefined,
        updateUser: undefined,
        createTime: undefined,
        updateTime: '无'
      },
      taskSummary: {
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        statisticsDate: undefined,
        total: undefined,
        failure: undefined,
        failureRatio: undefined,
        executionTimeMax: undefined,
        executionTimeMin: undefined,
        executionTimeLine95: undefined,
        executionTimeLine99: undefined,
        createTime: undefined,
        updateTime: undefined
      }
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.asyncQueryExecutor(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
      this.asyncQueryTaskSummary(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncQueryExecutor (namespaceName, appId, executorName) {
      console.log('asyncQueryExecutor')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName
      ).then(res => {
        console.log(res)
        this.executor = res.data
        this.asyncQueryRelease(namespaceName, appId, executorName)
      }).catch(function (error) {
        console.log(error)
      })
    },
    asyncQueryRelease (namespaceName, appId, executorName) {
      console.log('asyncQueryRelease')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName + '/releases/latest'
      ).then(res => {
        console.log(res)
        if (res.data !== null) {
          this.release = res.data
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    asyncQueryTaskSummary (namespaceName, appId, executorName) {
      console.log('asyncQueryTaskSummary')
      console.log(executorName)

      this.axios.get('/collector/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/task-summary', {
        params: {
          statisticsDate: format(new Date(), 'yyyy-MM-dd')
        }
      }).then(res => {
        console.log(res)
        if (res.data !== null) {
          this.taskSummary = res.data
        }
      }).catch(function (error) {
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  .el-main {
    color: #333;
    text-align: center;
    line-height: 160px;
  }

  .box-card {
    width: 480px;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

</style>
