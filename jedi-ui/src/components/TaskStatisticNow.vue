<template>
  <div>
    <el-container>
      <el-main>
        <div>
          <el-table
            :data="statisticsList.filter(data => !search || data.taskName.toLowerCase().includes(search.toLowerCase()))"
            max-height="470"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="任务名称"
              min-width="100"
              prop="taskName">
            </el-table-column>
            <el-table-column
              label="执行总数"
              width="120px"
              align="center"
              prop="total">
            </el-table-column>
            <el-table-column
              label="执行失败总数"
              width="120px"
              align="center"
              prop="failure">
            </el-table-column>
            <el-table-column
              label="执行失败比例"
              width="120px"
              align="center"
              prop="failureRatio">
            </el-table-column>
            <el-table-column
              label="执行最长时间(ms)"
              width="140px"
              align="center"
              prop="executionTimeMax">
            </el-table-column>
            <el-table-column
              label="执行最短时间(ms)"
              width="140px"
              align="center"
              prop="executionTimeMin">
            </el-table-column>
            <el-table-column
              label="更新时间"
              width="160px"
              align="center"
              prop="dataChangeLastModifiedTime">
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  size="mini"
                  placeholder="输入任务名称搜索"/>
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleTaskDetail(scope.$index, scope.row)" style="margin-right: 50%">明细</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-main>
      <el-footer>
        <div style="margin-right: 25px;">
          <el-pagination
            background
            :page-size=this.pagination.pageSize
            @next-click="nextPage"
            @prev-click="prevPage"
            @current-change="currentPage"
            layout="prev, pager, next"
            :total=this.pagination.total>
          </el-pagination>
        </div>
      </el-footer>
    </el-container>

  </div>
</template>

<script>
import format from '../assets/js/dateFormat.js'

export default {
  data () {
    return {
      namespaceName: undefined,
      appId: undefined,
      executorName: undefined,
      currentDate: new Date(),
      statisticsList: [],
      search: '',
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.namespaceName = this.$route.query.namespace
      this.appId = this.$route.query.appId
      this.executorName = this.$route.query.executor
      this.asyncQueryStatisticsList(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncQueryStatisticsList (namespaceName, appId, executorName) {
      console.log('asyncQueryStatisticsList')
      console.log(executorName)
      this.currentDate = new Date()

      this.axios.get('/consumer/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/task-statistics/all', {
        params: {
          statisticsDate: format(this.currentDate, 'yyyy-MM-dd'),
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.statisticsList = res.data.content
        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
      }).catch(function (error) {
        console.log(error)
      })
    },
    handleTaskDetail (index, row) {
      console.log(index, row)
      this.forwardTaskDetail(this.namespaceName, this.appId, this.executorName, format(this.currentDate,
        'yyyy-MM-dd'), row.taskName)
    },
    forwardTaskDetail (namespace, appId, executor, taskDate, taskName) {
      console.log('forwardTaskDetail')
      this.$router.push({
        path: '/task/detail',
        query: {
          namespace: namespace,
          appId: appId,
          executor: executor,
          taskDate: taskDate,
          taskName: taskName
        }
      })
    },
    tableRowClassName ({row, rowIndex}) {
      if (rowIndex % 2 === 0) {
        return 'success-row'
      } else {
        return ''
      }
    },
    nextPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryStatisticsList(this.namespaceName, this.appId, this.executorName)
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryStatisticsList(this.namespaceName, this.appId, this.executorName)
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryStatisticsList(this.namespaceName, this.appId, this.executorName)
    }
  }
}
</script>

<style scoped>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
</style>
