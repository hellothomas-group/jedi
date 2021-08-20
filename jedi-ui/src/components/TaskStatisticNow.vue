<template>
  <div>
    <el-container>
      <el-main>
        <div style="height: 90%">
          <el-table
            :data="statisticsList.filter(data => !search || data.taskName.toLowerCase().includes(search.toLowerCase()))"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="任务名称"
              prop="taskName"
              :formatter = "isDefaultOneFormatter">
            </el-table-column>
            <el-table-column
              label="执行总数"
              prop="total">
            </el-table-column>
            <el-table-column
              label="执行失败总数"
              prop="failure">
            </el-table-column>
            <el-table-column
              label="执行失败比例"
              prop="failureRatio">
            </el-table-column>
            <el-table-column
              label="执行最长时间(ms)"
              prop="executionTimeMax">
            </el-table-column>
            <el-table-column
              label="执行最短时间(ms)"
              prop="executionTimeMin">
            </el-table-column>
            <el-table-column
              label="更新时间"
              prop="dataChangeLastModifiedTime">
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  size="mini"
                  placeholder="输入关键字搜索"/>
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
      let selectedTask = row
      if (row.taskName === '全部任务(含未命名)') {
        selectedTask.taskName = 'DEFAULT'
      }
      this.forwardTaskDetail(this.namespaceName, this.appId, this.executorName, format(this.currentDate, 'yyyy-MM-dd'), selectedTask.taskName)
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
      if (rowIndex === 0) {
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
    },
    isDefaultOneFormatter (row, column, cellValue, index) {
      if (row.taskName === 'DEFAULT') {
        row.taskName = '全部任务(含未命名)'
      }
      return row.taskName
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
