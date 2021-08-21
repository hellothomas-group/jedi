<template>
  <div>
    <el-container>
      <el-header>
          <el-tag type="info" style="font-size: 16px;text-align: center;color: #0000FF; width: 100%;">报警配置详情
            <el-button
              size="mini"
              type="primary" icon="el-icon-setting"
              @click="alarmConfigEditable = true" style="margin-left: 10px"></el-button>
          </el-tag>
      </el-header>
      <el-main>
        <div style="height: 90%;margin-left: 30%">
            <el-form :model="updateAlarmConfigForm" :rules="alarmConfigFormRules" ref="updateAlarmConfigForm">
              <el-form-item label="报警是否启用" :label-width="formLabelWidth" prop="alarmEnabled">
                <el-switch
                  v-model="updateAlarmConfigForm.alarmEnabled"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  :disabled="!alarmConfigEditable">
                </el-switch>
              </el-form-item>
              <el-form-item label="队列容量阈值" :label-width="formLabelWidth" prop="queueThreshold">
                <el-input v-model.number="updateAlarmConfigForm.queueThreshold" autocomplete="off"
                          :disabled="!alarmConfigEditable" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="线程池饱和度阈值(%)" :label-width="formLabelWidth" prop="poolActivationThreshold">
                <el-input v-model.number="updateAlarmConfigForm.poolActivationThreshold" autocomplete="off"
                          :disabled="!alarmConfigEditable" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="任务拒绝数阈值" :label-width="formLabelWidth" prop="rejectCountThreshold">
                <el-input v-model.number="updateAlarmConfigForm.rejectCountThreshold" autocomplete="off"
                          :disabled="!alarmConfigEditable" style="width: 200px"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: right">
              <el-button @click="cancelUpdateAlarmConfigForm('updateAlarmConfigForm')" v-if="alarmConfigEditable">取
                消</el-button>
              <el-button type="primary" @click="submitUpdateAlarmConfigForm('updateAlarmConfigForm')" v-if="alarmConfigEditable">确 定</el-button>
            </div>
          <!--</el-dialog>-->
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script>
export default {
  data () {
    return {
      formLabelWidth: '20%',
      alarmConfig: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        configuration: '{}',
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      },
      alarmConfigEditable: false,
      updateAlarmConfigForm: {
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        alarmEnabled: 'false',
        queueThreshold: undefined,
        poolActivationThreshold: undefined,
        rejectCountThreshold: undefined
      },
      alarmConfiguration: {
        alarmEnabled: 'false',
        queueThreshold: undefined,
        poolActivationThreshold: undefined,
        rejectCountThreshold: undefined
      },
      alarmConfigFormRules: {
        queueThreshold: [
          {type: 'number', message: '队列容量阈值必须为数字值'}
        ],
        poolActivationThreshold: [
          {type: 'number', message: '线程池饱和度阈值必须为数字值'}
        ],
        rejectCountThreshold: [
          {type: 'number', message: '任务拒绝数阈值必须为数字值'}
        ]
      }
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.updateAlarmConfigForm.namespaceName = this.$route.query.namespace
      this.updateAlarmConfigForm.appId = this.$route.query.appId
      this.updateAlarmConfigForm.executorName = this.$route.query.executor
      this.asyncQueryAlarmConfig(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncQueryAlarmConfig (namespaceName, appId, executorName) {
      console.log('asyncQueryAlarmConfig')
      console.log(executorName)
      let _this = this
      this.axios.get('/consumer/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/alarm-configs'
      ).then(res => {
        console.log(res)
        this.alarmConfig = res.data
        this.alarmConfiguration = JSON.parse(res.data.configuration)
        this.updateAlarmConfigForm.alarmEnabled = this.alarmConfiguration.alarmEnabled
        this.updateAlarmConfigForm.queueThreshold = this.alarmConfiguration.queueThreshold
        this.updateAlarmConfigForm.poolActivationThreshold = this.alarmConfiguration.poolActivationThreshold
        this.updateAlarmConfigForm.rejectCountThreshold = this.alarmConfiguration.rejectCountThreshold
      }).catch(function (error) {
        console.log(error)
        _this.asyncCreateAlarmConfig(_this.updateAlarmConfigForm)
      })
    },
    cancelUpdateAlarmConfigForm (formName) {
      console.log(formName)
      this.updateAlarmConfigForm.alarmEnabled = this.alarmConfiguration.alarmEnabled
      this.updateAlarmConfigForm.queueThreshold = this.alarmConfiguration.queueThreshold
      this.updateAlarmConfigForm.poolActivationThreshold = this.alarmConfiguration.poolActivationThreshold
      this.updateAlarmConfigForm.rejectCountThreshold = this.alarmConfiguration.rejectCountThreshold
      this.alarmConfigEditable = false
    },
    submitUpdateAlarmConfigForm (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.asyncUpdateAlarmConfig(this.updateAlarmConfigForm)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncUpdateAlarmConfig (form) {
      console.log('asyncUpdateAlarmConfig')
      console.log('update alarmConfig...' + form.executorName)
      this.alarmConfigEditable = false

      this.alarmConfiguration.alarmEnabled = this.updateAlarmConfigForm.alarmEnabled
      this.alarmConfiguration.queueThreshold = this.updateAlarmConfigForm.queueThreshold === undefined ? undefined : this.updateAlarmConfigForm.queueThreshold
      this.alarmConfiguration.poolActivationThreshold = (this.updateAlarmConfigForm.poolActivationThreshold === undefined || this.updateAlarmConfigForm.poolActivationThreshold.trim() === '') ? undefined : this.updateAlarmConfigForm.poolActivationThreshold.trim()
      this.alarmConfiguration.rejectCountThreshold = this.updateAlarmConfigForm.rejectCountThreshold === undefined ? undefined : this.updateAlarmConfigForm.rejectCountThreshold
      let that = this

      this.axios.put('/admin/namespaces/' + form.namespaceName + '/apps/' +
        form.appId + '/executors/' + form.executorName + '/alarm-configs', null, {
        params: {
          configuration: JSON.stringify(this.alarmConfiguration)
        }
      }).then(res => {
        console.log(form.executorName + 'alarmConfig updated')
        this.asyncQueryAlarmConfig(form.namespaceName, form.appId, form.executorName)
      }).catch(function (error) {
        console.log(error)
        console.log(that.alarmConfig.configuration)
        if (that.alarmConfig.configuration) {
          this.alarmConfiguration = JSON.parse(that.alarmConfig.configuration)
          this.updateAlarmConfigForm.alarmEnabled = this.alarmConfiguration.alarmEnabled
          this.updateAlarmConfigForm.queueThreshold = this.alarmConfiguration.queueThreshold
          this.updateAlarmConfigForm.poolActivationThreshold = this.alarmConfiguration.poolActivationThreshold
          this.updateAlarmConfigForm.rejectCountThreshold = this.alarmConfiguration.rejectCountThreshold
        }
      })
    },
    asyncCreateAlarmConfig (form) {
      console.log('asyncCreateAlarmConfig')
      console.log('create alarmConfig...' + form.executorName)
      this.alarmConfigEditable = false

      this.alarmConfiguration.alarmEnabled = this.updateAlarmConfigForm.alarmEnabled
      this.alarmConfiguration.queueThreshold = this.updateAlarmConfigForm.queueThreshold === undefined ? undefined : this.updateAlarmConfigForm.queueThreshold
      this.alarmConfiguration.poolActivationThreshold = (this.updateAlarmConfigForm.poolActivationThreshold === undefined || this.updateAlarmConfigForm.poolActivationThreshold.trim() === '') ? undefined : this.updateAlarmConfigForm.poolActivationThreshold.trim()
      this.alarmConfiguration.rejectCountThreshold = this.updateAlarmConfigForm.rejectCountThreshold === undefined ? undefined : this.updateAlarmConfigForm.rejectCountThreshold

      this.axios.post('/admin/namespaces/' + form.namespaceName + '/apps/' +
        form.appId + '/executors/' + form.executorName + '/alarm-configs', null, {
        params: {
          configuration: JSON.stringify(this.alarmConfiguration)
        }
      }).then(res => {
        console.log(form.executorName + 'alarmConfig created')
        this.asyncQueryAlarmConfig(form.namespaceName, form.appId, form.executorName)
      }).catch(function (error) {
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
  body > .el-container {
    margin-bottom: 40px;
  }
</style>
