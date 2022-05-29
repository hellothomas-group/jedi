<template>
  <div>
    <el-header style="line-height: 40px">
      <div class="block" style="text-align: left;margin-left: 20px">
        <el-form :inline="true" :model="queryTaskDetailForm" class="demo-form-inline" :rules="queryTaskDetailRules" ref="queryTaskDetailForm">
          <el-form-item label="任务ID" prop="inputTaskId">
            <el-input v-model="queryTaskDetailForm.inputTaskId" placeholder="请输入任务ID" clearable
                      style="width: 350px"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="submitQueryTaskDetail('queryTaskDetailForm')" style="margin-left: 50px;">查询
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-header>
    <el-container>
      <el-main>
        <div style="margin-left: 20px">
          <el-descriptions class="margin-top" title="任务信息" border>
            <el-descriptions-item label="任务名称" label-class-name="my-label" content-class-name="my-content">{{this
              .taskData.taskName}}</el-descriptions-item>
            <el-descriptions-item label="任务附加信息"  label-class-name="my-label" content-class-name="my-content">{{this.taskData
              .taskExtraData}}</el-descriptions-item>
            <el-descriptions-item label="是否需要故障恢复" label-class-name="my-label" content-class-name="my-content">{{this.taskData
              .isRecoverable}}</el-descriptions-item>
            <el-descriptions-item label="父任务ID">
              <router-link :to="{path:'/task/single-detail', query:{namespace:namespaceName,appId:appId,executor:executorName,taskId:this.taskData.parentId}}">{{this.taskData.parentId}}</router-link>
            </el-descriptions-item>
            <el-descriptions-item label="是否持久化">{{this.taskData.isPersistent}}</el-descriptions-item>
            <el-descriptions-item label="数据源名称">{{this.taskData.dataSourceName}}</el-descriptions-item>
          </el-descriptions>
          <el-descriptions style="margin-top:20px" class="margin-top" title="任务执行信息" border>
            <el-descriptions-item label="任务等待时间(ms)" label-class-name="my-label" content-class-name="my-content">{{this.taskData.waitTime}}</el-descriptions-item>
            <el-descriptions-item label="任务执行时间(ms)" label-class-name="my-label" content-class-name="my-content">{{this.taskData.executionTime}}</el-descriptions-item>
            <el-descriptions-item label="任务结束时间" label-class-name="my-label" content-class-name="my-content">{{this.taskData.endTime}}</el-descriptions-item>
            <el-descriptions-item label="任务状态">{{taskStatusFormatter(this.taskData.status)}}</el-descriptions-item>
            <el-descriptions-item label="返回码">{{this.taskData.exitCode}}</el-descriptions-item>
            <el-descriptions-item label="返回信息">{{this.taskData.exitMessage}}</el-descriptions-item>
            <el-descriptions-item label="是否在父任务线程执行">{{this.taskData.isExecutedByParentTaskThread}}</el-descriptions-item>
            <el-descriptions-item label="是否故障恢复触发">{{this.taskData.isRecovered}}</el-descriptions-item>
            <el-descriptions-item label="tracdId">{{this.taskData.traceId}}</el-descriptions-item>
            <el-descriptions-item label="主机">{{this.taskData.host}}</el-descriptions-item>
            <el-descriptions-item label="记录时间">{{this.taskData.recordTime}}</el-descriptions-item>
          </el-descriptions>
          <el-descriptions style="margin-top:20px" class="margin-top" title="重试信息" border>
            <el-descriptions-item label="是否已重试" label-class-name="my-label" content-class-name="my-content">{{this.taskData.isRetried}}</el-descriptions-item>
            <el-descriptions-item label="重试新任务ID" label-class-name="my-label" content-class-name="my-content">
              <router-link :to="{path:'/task/single-detail', query:{namespace:namespaceName,appId:appId,executor:executorName,taskId:this.taskData.retryId}}">{{this.taskData.retryId}}</router-link>
            </el-descriptions-item>
            <el-descriptions-item label="重试操作人员" label-class-name="my-label" content-class-name="my-content">{{this.taskData.updateUser}}</el-descriptions-item>
            <el-descriptions-item label="是否重试触发">{{this.taskData.isByRetryer}}</el-descriptions-item>
            <el-descriptions-item label="重试前任务ID">
              <router-link :to="{path:'/task/single-detail', query:{namespace:namespaceName,appId:appId,executor:executorName,taskId:this.taskData.previousId}}">{{this.taskData.previousId}}</router-link>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>

export default {
  name: 'TaskSingleDetail',
  data () {
    return {
      namespaceName: undefined,
      appId: undefined,
      executorName: undefined,
      queryTaskDetailForm: {
        inputTaskId: ''
      },
      queryTaskDetailRules: {
        inputTaskId: [
          {required: true, message: '任务ID不能为空', trigger: 'blur'}
        ]
      },
      taskData: {
        id: undefined,
        taskName: undefined,
        waitTime: undefined,
        executionTime: undefined,
        status: undefined,
        exitCode: undefined,
        exitMessage: undefined,
        taskExtraData: undefined,
        endTime: undefined,
        isRecoverable: undefined,
        isRecovered: undefined,
        host: undefined,
        traceId: undefined,
        isByRetryer: undefined,
        previousId: undefined,
        parentId: undefined,
        isExecutedByParentTaskThread: undefined,
        dataSourceName: undefined,
        isPersistent: undefined,
        recordTime: undefined,
        isRetried: undefined,
        retryId: undefined,
        updateUser: undefined
      }
    }
  },
  created () {
    console.log(this.$route.query.taskId)
    if (this.$route.query) {
      this.namespaceName = this.$route.query.namespace
      this.appId = this.$route.query.appId
      this.executorName = this.$route.query.executor
      this.queryTaskDetailForm.inputTaskId = this.$route.query.taskId
      if (this.queryTaskDetailForm.inputTaskId && this.queryTaskDetailForm.inputTaskId.trim() !== '') {
        this.asyncQueryTaskDetail()
      }
    }
  },
  methods: {
    submitQueryTaskDetail (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.queryTaskDetailForm.inputTaskId && this.queryTaskDetailForm.inputTaskId.trim() !== '') {
            this.asyncQueryTaskDetail()
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncQueryTaskDetail () {
      console.log('asyncQueryTaskDetail')
      console.log(this.queryTaskDetailForm.inputTaskId)
      let that = this

      this.axios.get('/collector/tasks/' + this.queryTaskDetailForm.inputTaskId).then(res => {
        console.log(res)
        this.taskData = res.data
      }).catch(function (error) {
        console.log(error)
        console.log('1')
        that.$message.error(error.data.code + '-' + error.data.message)
      })
    },
    taskStatusFormatter (taskStatus) {
      if (taskStatus) {
        if (taskStatus === 2) {
          return '成功'
        } else if (taskStatus === 4) {
          return '拒绝'
        } else {
          return '失败'
        }
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.my-label {
  width: 12%;
}

.my-content {
  width: 21%;
}
</style>
