<template>
  <div>
    <el-container>
      <el-main>
        <div style="height: 90%">
          <el-button
            size="mini"
            @click="alarmConfigDialogFormVisible = true" style="margin-right: 50%">修改</el-button>
          <el-dialog title="报警配置详情" :visible.sync="alarmConfigDialogFormVisible">
            <el-form>
              <el-form-item label="configuration" :label-width="formLabelWidth">
                <el-input v-model="updateAlarmConfigForm.configuration" autocomplete="off" :disabled="true"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="alarmConfigDialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="submitUpdateAlarmConfigForm('updateAlarmConfigForm')">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import Utils from '../assets/js/util.js'
export default {
  data () {
    return {
      formLabelWidth: '20%',
      alarmConfig: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        configuration: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      },
      alarmConfigDialogFormVisible: false,
      updateAlarmConfigForm: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        configuration: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      }
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.asyncQueryAlarmConfig(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncQueryAlarmConfig (namespaceName, appId, executorName) {
      console.log('asyncQueryAlarmConfig')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/releases/all').then(res => {
        console.log(res)
        this.releases = res.data.content
      }).catch(function (error) {
        console.log(error)
      })
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
      this.createExecutorDialogFormVisible = false

      this.axios.post('/admin/namespaces/' + form.namespace + '/apps/' +
        form.appId + '/executors/' + form.newExecutor, null, {
        params: {
          operator: '80234613'
        }
      }).then(res => {
        console.log(form.newExecutor + ' created')
        Utils.$emit('createExecutorSuccess', form.newExecutor)
      }).catch(function (error) {
        console.log(error)
        Utils.$emit('createExecutorFail', error)
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
