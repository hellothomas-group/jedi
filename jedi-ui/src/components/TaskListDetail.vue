<template>
  <div>
    <el-header>
      <div class="block" style="text-align: left;margin-left: 20px">
        <el-form :inline="true" :model="queryTaskDetailForm" class="demo-form-inline" :rules="queryTaskDetailRules" ref="queryTaskDetailForm">
          <el-form-item label="日期" prop="queryDate">
            <el-date-picker
              v-model="queryTaskDetailForm.queryDate"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="任务名称" prop="inputTaskName">
            <el-input v-model="queryTaskDetailForm.inputTaskName" placeholder="请输入任务名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="执行结果" prop="executeResult">
            <el-select v-model="queryTaskDetailForm.executeResult" placeholder="执行结果" clearable="">
              <el-option
                v-for="item in executeResultOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="submitQueryTaskList('queryTaskDetailForm')" style="margin-left: 50px;">查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="retryTaskFormVisible = true" style="margin-left: 50px;">手工录入重试
            </el-button>
          </el-form-item>
          <div>
            <el-form-item label="任务附加信息" prop="inputTaskExtraData">
              <el-input v-model="queryTaskDetailForm.inputTaskExtraData" placeholder="请输入任务附加信息" clearable></el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </el-header>
    <el-container>
      <el-main>
        <div style="margin-top: 30px">
          <el-table
            :data="taskList.filter(data => !search || data.taskExtraData.toLowerCase().includes(search.toLowerCase()))"
            max-height="430"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="任务ID"
              min-width="130"
              prop="id">
            </el-table-column>
            <el-table-column
              label="任务名称"
              min-width="80px"
              align="center"
              prop="taskName">
            </el-table-column>
            <el-table-column
              label="执行结果"
              width="80px"
              align="center"
              prop="status"
              :formatter = "taskStatusFormatter">
            </el-table-column>
            <el-table-column
              label="等待时间(ms)"
              width="140px"
              align="center"
              prop="waitTime">
            </el-table-column>
            <el-table-column
              label="执行时间(ms)"
              width="140px"
              align="center"
              prop="executionTime">
            </el-table-column>
            <el-table-column
              label="主机"
              width="80px"
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
              label="失败原因"
              min-width="60px"
              align="center"
              prop="exitMessage">
            </el-table-column>
            <el-table-column
              label="任务附加信息"
              min-width="60px"
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
              <template slot-scope="scope">
                <el-tag type="warning" effect="dark" v-if="scope.row.status !== 2 && scope.row.isRetried">重试Id:
                  {{scope.row.retryId}}</el-tag>
                <el-button
                  type="danger"
                  size="mini"
                  v-if="!scope.row.isByRetryer && scope.row.isPersistent && scope.row.status !== 2 &&
                  !scope.row.isRetried"
                  @click="toRetryTask(scope.$index, scope.row)" style="margin-right: 50%">重试</el-button>
                <el-button
                  type="danger"
                  size="mini"
                  v-if="scope.row.isByRetryer && scope.row.isPersistent && scope.row.status !== 2 &&
                  !scope.row.isRetried"
                  @click="toRetryTask(scope.$index, scope.row)" style="margin-right: 50%">再次重试</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-dialog title="任务重试" :visible.sync="retryTaskFormVisible">
          <el-form :model="retryTaskForm" :rules="retryTaskRules" ref="retryTaskForm">
            <el-form-item label="任务ID" :label-width="formLabelWidth" prop="taskId" >
              <el-input v-model="retryTaskForm.taskId" autocomplete="off" clearable
                        style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="客户端URL" :label-width="formLabelWidth" prop="url" >
              <el-input v-model="retryTaskForm.url" autocomplete="off" clearable
                        style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="数据源名称" :label-width="formLabelWidth" prop="dataSourceName" >
              <el-input v-model="retryTaskForm.dataSourceName" autocomplete="off" clearable
                        style="width: 300px"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="retryTaskFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="submitRetryTaskForm('retryTaskForm')">确 定</el-button>
          </div>
        </el-dialog>
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
import Utils from '../assets/js/util.js'
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
      queryTaskDetailForm: {
        queryDate: [new Date(format(new Date(), 'yyyy/MM/dd')), new Date()],
        inputTaskName: '',
        inputTaskExtraData: '',
        executeResult: undefined
      },
      queryTaskDetailRules: {
        inputTaskName: [
          {required: true, message: '任务名称不能为空', trigger: 'blur'}
        ]
      },
      taskList: [],
      search: '',
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 20
      },
      executeResultOptions: [{
        value: 'true',
        label: '成功'
      }, {
        value: 'false',
        label: '失败'
      }],
      formLabelWidth: '30%',
      retryTaskFormVisible: false,
      retryTaskForm: {
        taskId: undefined,
        url: undefined,
        dataSourceName: undefined
      },
      retryTaskRules: {
        taskId: [
          {required: true, message: '请输入任务ID', trigger: 'blur'}
        ],
        url: [
          {required: true, message: '请输入客户端URL', trigger: 'blur'}
        ]
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
        this.queryTaskDetailForm.queryDate[0] = new Date(this.$route.query.taskDate.replace(/-/g, '/'))
        this.queryTaskDetailForm.queryDate[1] = new Date(this.queryTaskDetailForm.queryDate[0].getTime() + 3600 *
          1000 * 24 - 1)
      }
      if (this.$route.query.taskName) {
        this.queryTaskDetailForm.inputTaskName = this.$route.query.taskName
        this.asyncQueryTaskList()
      }
    }
  },
  mounted () {
    let that = this
    Utils.$on('retryTaskSuccess', function (taskId) {
      console.log(taskId)
      that.retryTaskSuccessNotification(taskId)
    })
    Utils.$on('retryTaskFail', function (exception) {
      that.retryTaskFailNotification(exception)
    })
  },
  methods: {
    asyncQueryTaskList () {
      console.log('asyncQueryTaskList')
      console.log(this.queryTaskDetailForm.inputTaskName)

      this.axios.get('/consumer/namespaces/' + this.namespaceName + '/apps/' + this.appId + '/executors/' +
        this.executorName + '/task-details', {
        params: {
          taskName: this.queryTaskDetailForm.inputTaskName,
          taskExtraData: (this.queryTaskDetailForm.inputTaskExtraData === undefined ||
            this.queryTaskDetailForm.inputTaskExtraData.trim() === '') ? undefined : this.queryTaskDetailForm.inputTaskExtraData.trim(),
          isSuccess: this.queryTaskDetailForm.executeResult === undefined ? undefined : this.queryTaskDetailForm.executeResult,
          startTime: format(this.queryTaskDetailForm.queryDate[0], 'yyyy-MM-dd HH:mm:ss'),
          endTime: format(this.queryTaskDetailForm.queryDate[1], 'yyyy-MM-dd HH:mm:ss'),
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
    submitQueryTaskList (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.queryTaskDetailForm.queryDate[1] = new
          Date(new Date(format(this.queryTaskDetailForm.queryDate[1], 'yyyy/MM/dd')).getTime() + 3600 * 1000 * 24 - 1)
          if (this.queryTaskDetailForm.inputTaskName && this.queryTaskDetailForm.inputTaskName.trim() !== '') {
            this.asyncQueryTaskList()
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    tableRowClassName ({row, rowIndex}) {
      if (row.status === 2) {
        return 'success-row'
      } else {
        return 'warning-row'
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
    taskStatusFormatter (row, column, cellValue, index) {
      if (row.status === 2) {
        return '成功'
      } else if (row.status === 4) {
        return '拒绝'
      } else {
        return '失败'
      }
    },
    toRetryTask (index, row) {
      console.log(index, row)
      this.retryTaskFormVisible = true
      this.retryTaskForm.taskId = row.id
      this.retryTaskForm.url = 'http://' + row.host + ':8080/jedi/tasks/retry'
      this.retryTaskForm.dataSourceName = row.dataSourceName
    },
    submitRetryTaskForm (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.asyncRetryTask(this.retryTaskForm)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncRetryTask (form) {
      console.log('asyncRetryTask')
      console.log('retry...' + form.taskId)
      this.retryTaskFormVisible = false

      this.axios.post('/admin/namespaces/' + this.namespaceName + '/apps/' +
        this.appId + '/executors/' + this.executorName + '/tasks/' + form.taskId + '/retry', null, {
        params: {
          url: form.url,
          dataSourceName: form.dataSourceName
        }
      }).then(res => {
        console.log(form.executorName + ' released')
        Utils.$emit('retryTaskSuccess', this.retryTaskForm.taskId)
      }).catch(function (error) {
        console.log(error)
        Utils.$emit('retryTaskFail', error)
      })
    },
    retryTaskSuccessNotification (taskId) {
      const h = this.$createElement

      this.$notify({
        title: taskId + '重试提交成功!',
        message: h('i', {style: 'color: teal'}, '~~')
      })

      this.asyncQueryTaskList()
    },
    retryTaskFailNotification (exception) {
      let messageText = exception.data.code === undefined ? exception.status + '-' + exception.statusText : exception.data.code + '-' + exception.data.message
      const h = this.$createElement
      this.$notify({
        title: this.retryTaskForm.taskId + '重试提交失败!',
        message: h('i', {style: 'color: #FF0000'}, messageText)
      })

      this.asyncQueryTaskList()
    }
  },
  beforeDestroy () {
    Utils.$off('retryTaskSuccess')
    Utils.$off('retryTaskFail')
  }
}
</script>

<style scoped>
  .el-header {
    color: #333;
    text-align: center;
    line-height: 40px;
  }

  .el-footer {
    height: 40px !important;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-table .warning-row {
    background: #fdb5a8;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
</style>
