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
        <div style="height: 90%">
            <el-form :model="updateAlarmConfigForm" :rules="alarmConfigFormRules" ref="updateAlarmConfigForm">
              <el-form-item label="alarmEnabled" :label-width="formLabelWidth">
                <el-switch
                  v-model="updateAlarmConfigForm.configuration.alarmEnabled"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  :disabled="!alarmConfigEditable">
                </el-switch>
              </el-form-item>
              <el-form-item label="queueThreshold" :label-width="formLabelWidth">
                <el-input v-model="updateAlarmConfigForm.configuration.queueThreshold" autocomplete="off"
                          :disabled="!alarmConfigEditable"></el-input>
              </el-form-item>
              <el-form-item label="poolActivationThreshold" :label-width="formLabelWidth">
                <el-input v-model="updateAlarmConfigForm.configuration.poolActivationThreshold" autocomplete="off"
                          :disabled="!alarmConfigEditable"></el-input>
              </el-form-item>
              <el-form-item label="rejectCountThreshold" :label-width="formLabelWidth">
                <el-input v-model="updateAlarmConfigForm.configuration.rejectCountThreshold" autocomplete="off"
                          :disabled="!alarmConfigEditable"></el-input>
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
    const isNum = (rule, value, callback) => {
      const validData = /^[0-9]*$/
      if (!validData.test(value)) {
        callback(new Error('只能为数字'))
      } else {
        callback()
      }
    }
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
        configuration: {
          alarmEnabled: 'false',
          queueThreshold: undefined,
          poolActivationThreshold: undefined,
          rejectCountThreshold: undefined
        }
      },
      alarmConfigFormRules: {
        queueThreshold: [
          {required: true, validator: isNum, message: '请输入json字符串', trigger: 'blur'}
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
        this.updateAlarmConfigForm.configuration = JSON.parse(res.data.configuration)
      }).catch(function (error) {
        console.log(error)
        _this.asyncCreateAlarmConfig(_this.updateAlarmConfigForm)
      })
    },
    cancelUpdateAlarmConfigForm (formName) {
      console.log(formName)
      if (this.alarmConfig.configuration) {
        this.updateAlarmConfigForm.configuration = JSON.parse(this.alarmConfig.configuration)
      }
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

      for (let attr in form.configuration) {
        if (attr === 'alarmEnabled') continue
        if (!form.configuration[attr]) {
          form.configuration[attr] = undefined
        }
      }
      let that = this

      this.axios.put('/admin/namespaces/' + form.namespaceName + '/apps/' +
        form.appId + '/executors/' + form.executorName + '/alarm-configs', null, {
        params: {
          configuration: JSON.stringify(form.configuration)
        }
      }).then(res => {
        console.log(form.executorName + 'alarmConfig updated')
        this.asyncQueryAlarmConfig(form.namespaceName, form.appId, form.executorName)
      }).catch(function (error) {
        console.log(error)
        console.log(that.alarmConfig.configuration)
        if (that.alarmConfig.configuration) {
          that.updateAlarmConfigForm.configuration = JSON.parse(that.alarmConfig.configuration)
        }
      })
    },
    asyncCreateAlarmConfig (form) {
      console.log('asyncCreateAlarmConfig')
      console.log('create alarmConfig...' + form.executorName)
      this.alarmConfigEditable = false

      for (let attr in form.configuration) {
        if (attr === 'alarmEnabled') continue
        if (!form.configuration[attr]) {
          form.configuration[attr] = undefined
        }
      }

      this.axios.post('/admin/namespaces/' + form.namespaceName + '/apps/' +
        form.appId + '/executors/' + form.executorName + '/alarm-configs', null, {
        params: {
          configuration: JSON.stringify(form.configuration),
          operator: '80234613'
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
