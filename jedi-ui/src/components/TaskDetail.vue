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
          @click="submitQueryTaskList()" style="margin-left: 20px">查询
        </el-button>
      </div>
    </el-header>
    <el-container>
      <el-main>
        <div>
          <el-table
            :data="taskList.filter(data => !search || data.taskExtraData.toLowerCase().includes(search.toLowerCase()))"
            max-height="430"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="任务名称"
              min-width="100"
              prop="taskName"
              :formatter = "isDefaultOneFormatter">
            </el-table-column>
            <el-table-column
              label="执行时间(ms)"
              width="140px"
              align="center"
              prop="executionTime">
            </el-table-column>
            <el-table-column
              label="主机"
              width="140px"
              align="center"
              prop="host">
            </el-table-column>
            <el-table-column
              label="记录时间"
              width="160px"
              align="center"
              prop="recordTime">
            </el-table-column>
            <el-table-column
              label="任务附加信息"
              min-width="100"
              align="center"
              prop="taskExtraData">
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  size="mini"
                  placeholder="输入任务附加信息搜索"/>
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
      taskName: undefined,
      pickerOptions: {
        shortcuts: [{
          text: '最近三天',
          onClick (picker) {
            const end = new Date()
            const start = new Date(format(new Date(), 'yyyy/MM/dd'))
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 3)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近七天',
          onClick (picker) {
            const end = new Date()
            const start = new Date(format(new Date(), 'yyyy/MM/dd'))
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近十天',
          onClick (picker) {
            const end = new Date()
            const start = new Date(format(new Date(), 'yyyy/MM/dd'))
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 10)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      queryDate: [new Date(format(new Date(), 'yyyy/MM/dd')), new Date()],
      inputTaskName: '',
      taskList: [],
      search: '',
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 20
      }
    }
  },
  created () {
    console.log(this.$route.query.taskDate)
    if (this.$route.query) {
      this.namespaceName = this.$route.query.namespace
      this.appId = this.$route.query.appId
      this.executorName = this.$route.query.executor
      if (this.$route.query.taskDate) {
        this.queryDate[0] = new Date(this.$route.query.taskDate.replace(/-/g, '/'))
        this.queryDate[1] = new Date(this.queryDate[0].getTime() + 3600 * 1000 * 24 - 1)
      }
      this.taskName = this.$route.query.taskName
      if (this.$route.query.taskName) {
        this.inputTaskName = this.$route.query.taskName
        this.asyncQueryTaskList()
      }
    }
  },
  methods: {
    asyncQueryTaskList () {
      console.log('asyncQueryTaskList')
      console.log(this.taskName)

      this.axios.get('/consumer/namespaces/' + this.namespaceName + '/apps/' + this.appId + '/executors/' +
        this.executorName + '/task-details', {
        params: {
          taskName: this.taskName,
          startTime: format(this.queryDate[0], 'yyyy-MM-dd HH:mm:ss'),
          endTime: format(this.queryDate[1], 'yyyy-MM-dd HH:mm:ss'),
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.taskList = res.data.content
        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
      }).catch(function (error) {
        console.log(error)
      })
    },
    submitQueryTaskList () {
      this.queryDate[1] = new Date(new Date(format(this.queryDate[1], 'yyyy/MM/dd')).getTime() + 3600 * 1000 * 24 - 1)
      if (this.inputTaskName && this.inputTaskName.trim() === '') {
        this.taskName = this.inputTaskName.trim()
        this.asyncQueryTaskList()
      }
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
      this.asyncQueryTaskList()
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryTaskList()
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryTaskList()
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
