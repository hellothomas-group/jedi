<template>
  <div>
    <div>
      <el-form ref="elForm" :model="selectNamespaceForm" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="环境选择" prop="namespace" style="width: 250px;margin-top: 22px">
          <el-select v-model="selectNamespaceForm.namespaceId" placeholder="请选择下拉选择" clearable style="width:100%"
                     @change="asyncApps(selectNamespaceForm.namespaceId)">
            <el-option v-for="(item, index) in namespaces" :key="index" :label="item.name" :value="item.id"
                       :disabled="false" ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div style="text-align:right;padding-right: 25px;">
        <el-button type="text" style="font-size: 16px" @click="createExecutorDialogFormVisible = true">创建应用</el-button>
      </div>
      <el-dialog title="应用信息" :visible.sync="createExecutorDialogFormVisible">
        <el-form :model="newAppForm" :rules="newAppRules" ref="form">
          <el-form-item label="环境名称" :label-width="formLabelWidth" prop="namespaceName">
            <el-input v-model="newAppForm.namespaceName" autocomplete="off" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="应用名称" :label-width="formLabelWidth" prop="appId" >
            <el-input v-model="newAppForm.appId" autocomplete="off" placeholder="1-50位字符"></el-input>
          </el-form-item>
          <el-form-item label="应用描述" :label-width="formLabelWidth" prop="description">
            <el-input v-model="newAppForm.description" autocomplete="off" placeholder="1-50位字符"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="createExecutorDialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm('form')">确 定</el-button>
        </div>
      </el-dialog>
      <el-row :gutter="50" style="margin-left: 2px;margin-right: 2px; text-align: center">
        <div>
          <el-col :span="6" v-for="(item, index) in apps" :key="index">
            <div class="grid-content bg-purple" style="display: flex; align-items: center; justify-content: center;
            cursor: pointer;"
                 @click="forwardSingleApp(item)" onmouseover="this.style.background='#99a9bf';" onmouseleave="this.style.background='#d3dce6';">
              <h2>{{item.appId}}<br>{{item.appDescription}}</h2>
            </div>
          </el-col>
        </div>
      </el-row>
    </div>
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
  </div>
</template>
<script>
import Utils from '../assets/js/util.js'
export default {
  components: {},
  props: [],
  data () {
    return {
      selectNamespaceForm: {
        namespaceId: undefined
      },
      rules: {
        namespaceId: [{
          required: true,
          message: '请选择下拉选择',
          trigger: 'change'
        }]
      },
      namespaces: [],
      apps: [],
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 20
      },
      createExecutorDialogFormVisible: false,
      newAppForm: {
        namespaceName: undefined,
        appId: undefined,
        description: undefined
      },
      formLabelWidth: '120px',
      newAppRules: {
        appId: [
          {required: true, message: '请输入应用名称', trigger: 'blur'},
          {min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入应用描述', trigger: 'blur'},
          {min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  computed: {},
  watch: {},
  created () {
    this.asyncNamespaces()
  },
  mounted () {
    let that = this
    Utils.$on('createAppSuccess', function (appId) {
      console.log(appId)
      that.createAppSuccessNotification(appId)
    })
    Utils.$on('createAppFail', function (exception) {
      that.createAppFailNotification(exception)
    })
  },
  methods: {
    asyncNamespaces () {
      this.axios.get('/admin/namespaces').then(res => {
        console.log(res)
        console.log(res.data)

        this.namespaces = res.data
        console.log(this.namespaces)
        this.namespaces.sort(this.sortByProperty('name'))
        console.log(this.namespaces)

        this.selectNamespaceForm.namespaceId = this.namespaces[0].id
        console.log(this.selectNamespaceForm.namespaceId)
        this.newAppForm.namespaceName = this.namespaces[0].name

        this.asyncApps(this.namespaces[0].id)
      }).catch(function (error) {
        console.log(error)
      })
    },
    sortByProperty (property) {
      return function (a, b) {
        let value1 = a[property]
        let value2 = b[property]
        return ((value1 < value2) ? -1 : ((value1 > value2) ? 1 : 0))
      }
    },
    asyncApps (selectedNamespaceId) {
      console.log('asyncApps')
      if (selectedNamespaceId === undefined || selectedNamespaceId === '') {
        return
      }

      let selectedNamespace = this.namespaces.filter(function (namespace) {
        return namespace.id === selectedNamespaceId
      })
      console.log(selectedNamespace[0].name)
      this.newAppForm.namespace = selectedNamespace[0].name

      this.axios.get('/admin/namespaces/' + selectedNamespace[0].name + '/apps', {
        params: {
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.apps = res.data.content
        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
      }).catch(function (error) {
        console.log(error)
      })
    },
    forwardSingleApp (singleApp) {
      console.log('forwardSingleApp')
      this.$router.push({
        path: '/app',
        query: {
          namespace: singleApp.namespaceName,
          appId: singleApp.appId
        }
      })
    },
    nextPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncApps(this.selectNamespaceForm.namespaceId)
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncApps(this.selectNamespaceForm.namespaceId)
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncApps(this.selectNamespaceForm.namespaceId)
    },
    submitForm (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.asyncCreateApp(this.newAppForm)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncCreateApp (form) {
      console.log('asyncCreateApp')
      console.log('creating...' + form.appId)
      this.createExecutorDialogFormVisible = false

      this.axios.post('/admin/namespaces/' + form.namespaceName + '/apps/' +
        form.appId, {
        'appDescription': form.description,
        'appId': form.appId,
        'namespaceName': form.namespaceName
      }).then(res => {
        console.log(form.appId + ' created')
        Utils.$emit('createAppSuccess', form.appId)
      }).catch(function (error) {
        console.log(error)
        Utils.$emit('createAppFail', error)
      })
    },
    createAppSuccessNotification (appId) {
      const h = this.$createElement

      this.$notify({
        title: appId + '创建成功!',
        message: h('i', {style: 'color: teal'}, '~~')
      })

      this.asyncApps(this.selectNamespaceForm.namespaceId)
    },
    createAppFailNotification (exception) {
      const h = this.$createElement
      this.$notify({
        title: this.newAppForm.appId + '创建失败!',
        message: h('i', {style: 'color: #FF0000'}, exception.toString())
      })

      this.asyncExecutors(this.app)
    }
  },
  beforeDestroy () {
    Utils.$off('createAppSuccess')
    Utils.$off('createAppFail')
  }
}

</script>
<style>
  .el-header {
    /*background-color: #F5F5F5;*/
    color: #333;
    line-height: 60px;
  }
  .el-row {
    margin-bottom: 20px;
  }
  .el-col {
    border-radius: 4px;
    margin-bottom: 20px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
    height: 100px
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .el-pagination {
    text-align: right;
  }
</style>
