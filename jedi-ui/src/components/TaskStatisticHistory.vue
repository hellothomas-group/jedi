<template>
  <div>
    <el-header>
      <div class="block" style="text-align: left;margin-left: 20px">
        <span class="demonstration">日期</span>
        <el-date-picker
          v-model="queryDate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
        <span class="demonstration" style="margin-left: 50px">任务名称</span>
        <el-input style="width: 200px"
          placeholder="请输入任务名称"
          v-model="inputTaskName"
          clearable>
        </el-input>
        <el-button
          type="primary"
          @click="asyncQueryStatisticsList()" style="margin-left: 20px">查询
        </el-button>
      </div>
    </el-header>
    <el-container>
      <el-main>
        <div>
          <el-table
            :data="statisticsList.filter(data => !search || data.taskName.toLowerCase().includes(search.toLowerCase()))"
            max-height="420"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="任务日期"
              width="95px"
              prop="statisticsDate">
            </el-table-column>
            <el-table-column
              label="任务名称"
              min-width="85px"
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
              width="105px"
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
              prop="updateTime">
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
                  @click="handleTaskListDetail(scope.$index, scope.row)" style="margin-right: 50%">明细</el-button>
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
      statisticsList: [],
      search: '',
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近三天',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 3)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近七天',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近十天',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 10)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      queryDate: [new Date(new Date().getTime() - 3600 * 1000 * 24), new Date(new Date().getTime() - 3600 * 1000 * 24)],
      inputTaskName: ''
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.namespaceName = this.$route.query.namespace
      this.appId = this.$route.query.appId
      this.executorName = this.$route.query.executor
      this.asyncQueryStatisticsList()
    }
  },
  methods: {
    asyncQueryStatisticsList () {
      console.log('asyncQueryStatisticsList')
      console.log(this.executorName)

      this.axios.get('/consumer/namespaces/' + this.namespaceName + '/apps/' + this.appId + '/executors/' + this.executorName +
        '/task-statistics-history/all', {
        params: {
          startDate: format(this.queryDate[0], 'yyyy-MM-dd'),
          endDate: format(this.queryDate[1], 'yyyy-MM-dd'),
          taskName: this.inputTaskName.trim() === '' ? undefined : this.inputTaskName.trim(),
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
    handleTaskListDetail (index, row) {
      console.log(index, row)
      this.forwardTaskListDetail(this.namespaceName, this.appId, this.executorName, row.statisticsDate,
        row.taskName)
    },
    forwardTaskListDetail (namespace, appId, executor, taskDate, taskName) {
      console.log('forwardTaskListDetail')
      this.$router.push({
        path: '/task/list-detail',
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
