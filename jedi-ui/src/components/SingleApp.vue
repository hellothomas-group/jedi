<template>
  <div style="height: 85%">
    <el-container>
      <el-container>
        <el-aside width="30%">
          <div style="text-align: center">
            <el-tag type="info" style="cursor: pointer;" @click="forwardNamespace()"
                    onmouseover="this.style.background='#99a9bf';" onmouseleave="this.style.background='#f4f4f5';">所属环境:
              {{this.app.namespaceName}}</el-tag>
          </div>
          <div>
            <ul style="list-style:none; margin:0px; width:80%;">
              <li class="li">
                <div class="title">应用名称</div>
                <div class="value">{{this.app.appId}}</div>
              </li>
              <el-divider></el-divider>
              <li class="li">
                <div class="title">应用描述</div>
                <div class="value">{{this.app.appDescription}}</div>
              </li>
              <el-divider></el-divider>
              <li class="li">
                <div class="title">应用负责人</div>
                <div class="value">{{this.app.ownerName}}</div>
              </li>
            </ul>
          </div>
        </el-aside>
        <el-container>
          <el-main>
            <div style="text-align:right;padding-right: 10px;">
              <el-button type="text" style="font-size: 16px" @click="createExecutorDialogFormVisible = true">创建线程池</el-button>
            </div>
            <el-dialog title="线程池信息" :visible.sync="createExecutorDialogFormVisible">
              <el-form :model="newExecutorForm" :rules="creatExecutorFormRules" ref="newExecutorForm">
                <el-form-item label="环境名称" :label-width="formLabelWidth" prop="namespace">
                  <el-input v-model="newExecutorForm.namespace" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="应用名称" :label-width="formLabelWidth" prop="appId" >
                  <el-input v-model="newExecutorForm.appId" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="线程池名称" :label-width="formLabelWidth" prop="newExecutor">
                  <el-input v-model="newExecutorForm.newExecutor" autocomplete="off" placeholder="1-50位字符"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="createExecutorDialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitCreateExecutorForm('newExecutorForm')">确 定</el-button>
              </div>
            </el-dialog>
            <div style="margin-left: 20px">
              <el-table
                max-height="460"
                :data="executors.filter(data => !search || data.executorName.toLowerCase().includes(search.toLowerCase()))" style="width: 100%"
                :row-class-name="tableRowClassName">
                <el-table-column
                  min-width="100px"
                  label="执行器名称"
                  prop="executorName">
                </el-table-column>
                <el-table-column
                  width="300px"
                  align="right">
                  <template slot="header" slot-scope="scope">
                    <el-input
                      v-model="search"
                      size="mini"
                      placeholder="输入关键字搜索"/>
                  </template>
                  <template slot-scope="scope">
                    <div class="tdr">
                      <el-button
                        size="mini"
                        @click="handleQuery(scope.$index, scope.row)">详情</el-button>
                      <el-button
                        size="mini"
                        @click="handleConfig(scope.$index, scope.row)">配置</el-button>
                      <el-tooltip :disabled="!scope.row.itemModified" content="配置有修改,待发布" placement="top">
                        <el-badge :is-dot="scope.row.itemModified" class="item">
                          <el-button
                            size="mini"
                            @click="handleRelease(scope.$index, scope.row)">发布</el-button>
                        </el-badge>
                      </el-tooltip>
                      <el-button
                        size="mini"
                        type="danger"
                        @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <el-dialog title="更新线程池配置" :visible.sync="updateExecutorDialogFormVisible">
            <el-form :model="updateExecutorForm" :rules="updateExecutorRules" ref="updateExecutorForm">
              <el-form-item label="核心线程数" :label-width="formLabelWidth" prop="corePoolSize">
                <el-input v-model.number="updateExecutorForm.corePoolSize" autocomplete="off"
                          placeholder="正整数" style="width: 300px"></el-input>
              </el-form-item>
              <el-form-item label="最大线程数" :label-width="formLabelWidth" prop="maxPoolSize">
                <el-input v-model.number="updateExecutorForm.maxPoolSize" autocomplete="off"
                          placeholder="正整数" style="width: 300px"></el-input>
              </el-form-item>
              <el-form-item label="队列容量" :label-width="formLabelWidth" prop="queueCapacity">
                <el-input v-model.number="updateExecutorForm.queueCapacity" autocomplete="off"
                          placeholder="正整数,若改小则重启时生效" style="width: 300px"></el-input>
              </el-form-item>
              <el-form-item label="空闲线程超时时间(s)" :label-width="formLabelWidth" prop="keepAliveSeconds">
                <el-input v-model.number="updateExecutorForm.keepAliveSeconds" autocomplete="off"
                          placeholder="正整数" style="width: 300px"></el-input>
              </el-form-item>
              <el-form-item label="打点频率(ms)" :label-width="formLabelWidth" prop="tickerCycle">
                <el-input v-model.number="updateExecutorForm.tickerCycle" autocomplete="off"
                          placeholder="正整数" style="width: 300px"></el-input>
              </el-form-item>
              <el-form-item label="是否允许核心线程池超时" :label-width="formLabelWidth" prop="allowCoreThreadTimeOut">
                <el-switch
                  v-model="updateExecutorForm.allowCoreThreadTimeOut"
                  active-color="#13ce66"
                  inactive-color="#ff4949">
                </el-switch>
              </el-form-item>
              <el-form-item label="备注" :label-width="formLabelWidth" prop="comment" >
                <el-input v-model="updateExecutorForm.comment" autocomplete="off"
                          style="width: 300px"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="updateExecutorDialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="submitUpdateExecutorForm('updateExecutorForm')">确 定</el-button>
            </div>
          </el-dialog>
          <el-dialog title="发布线程池配置" :visible.sync="releaseExecutorDialogFormVisible">
            <el-form :model="releaseExecutorForm" :rules="releaseExecutorFormRules" ref="releaseExecutorForm">
              <el-form-item label="发布名称" :label-width="formLabelWidth" prop="name">
                <el-input v-model="releaseExecutorForm.name" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="备注" :label-width="formLabelWidth" prop="comment" >
                <el-input v-model="releaseExecutorForm.comment" autocomplete="off"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="releaseExecutorDialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="submitReleaseExecutorForm('releaseExecutorForm')">确 定</el-button>
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
      </el-container>
    </el-container>
    <el-dialog
      title="提示"
      :visible.sync="centerDialogVisible"
      width="30%"
      center>
      <span>注意,线程池删除后不支持新建同名线程池,请再次确认是否删除</span>
      <span slot="footer" class="dialog-footer">
      <el-button @click="centerDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="asyncDeleteExecutor()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Utils from '../assets/js/util.js'
import format from '../assets/js/dateFormat.js'
export default {
  components: {},
  props: [],
  data () {
    return {
      app: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        appDescription: undefined,
        ownerName: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      },
      executors: [],
      search: '',
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      centerDialogVisible: false,
      selectedExecutor: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined,
        itemModified: undefined
      },
      createExecutorDialogFormVisible: false,
      updateExecutorDialogFormVisible: false,
      releaseExecutorDialogFormVisible: false,
      newExecutorForm: {
        namespace: undefined,
        appId: undefined,
        newExecutor: undefined
      },
      updateExecutorForm: {
        namespace: undefined,
        appId: undefined,
        executorName: undefined,
        // configuration
        corePoolSize: undefined,
        maxPoolSize: undefined,
        queueCapacity: undefined,
        keepAliveSeconds: undefined,
        tickerCycle: undefined,
        allowCoreThreadTimeOut: undefined,
        comment: undefined
      },
      releaseExecutorForm: {
        namespace: undefined,
        appId: undefined,
        executorName: undefined,
        name: undefined,
        comment: undefined
      },
      formLabelWidth: '30%',
      creatExecutorFormRules: {
        newExecutor: [
          {required: true, message: '请输入线程池名称', trigger: 'blur'},
          {min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur'}
        ]
      },
      releaseExecutorFormRules: {
        name: [
          {required: true, message: '请输入发布名称', trigger: 'blur'}
        ]
      },
      updateExecutorRules: {
        corePoolSize: [
          {required: true, message: '核心线程数不能为空'},
          {type: 'number', message: '核心线程数必须为数字值'}
        ],
        maxPoolSize: [
          {required: true, message: '最大线程数不能为空'},
          {type: 'number', message: '最大线程数必须为数字值'}
        ],
        queueCapacity: [
          {required: true, message: '队列容量不能为空'},
          {type: 'number', message: '队列容量必须为数字值'}
        ],
        keepAliveSeconds: [
          {required: true, message: '空闲线程超时时间不能为空'},
          {type: 'number', message: '空闲线程超时时间必须为数字值'}
        ],
        tickerCycle: [
          {required: true, message: '打点频率不能为空'},
          {type: 'number', message: '打点频率必须为数字值'}
        ]
      },
      itemConfiguration: {
        corePoolSize: undefined,
        maxPoolSize: undefined,
        queueCapacity: undefined,
        keepAliveSeconds: undefined,
        tickerCycle: undefined,
        allowCoreThreadTimeOut: undefined
      }
    }
  },
  computed: {},
  watch: {},
  created () {
    console.log(this.$route.query.appId)
    if (this.$route.query) {
      this.app.namespaceName = this.$route.query.namespace
      this.app.appId = this.$route.query.appId
      this.newExecutorForm.namespace = this.app.namespaceName
      this.newExecutorForm.appId = this.app.appId
      this.queryExecutors(this.$route.query.namespace, this.$route.query.appId)
    }
  },
  mounted () {
    let that = this
    Utils.$on('deleteExecutorSuccess', function (executorName) {
      console.log(executorName)
      that.deleteExecutorSuccessNotification(executorName)
    })
    Utils.$on('deleteExecutorFail', function (exception) {
      that.deleteExecutorFailNotification(exception)
    })
    Utils.$on('createExecutorSuccess', function (executorName) {
      console.log(executorName)
      that.createExecutorSuccessNotification(executorName)
    })
    Utils.$on('createExecutorFail', function (exception) {
      that.createExecutorFailNotification(exception)
    })
  },
  methods: {
    sortByProperty (property) {
      return function (a, b) {
        let value1 = a[property]
        let value2 = b[property]
        return ((value1 < value2) ? -1 : ((value1 > value2) ? 1 : 0))
      }
    },
    asyncExecutors (selectedApp) {
      console.log('asyncExecutors')
      console.log(selectedApp)

      this.axios.get('/admin/namespaces/' + selectedApp.namespaceName + '/apps/' + selectedApp.appId +
        '/executors', {
        params: {
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.executors = res.data.content
        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
      }).catch(function (error) {
        console.log(error)
      })
    },
    handleQuery (index, row) {
      console.log(index, row)
      this.selectedExecutor = row
      this.forwardSingleExecutor(row)
    },
    handleConfig (index, row) {
      this.selectedExecutor = row
      console.log(index, row)
      this.updateExecutorForm.namespace = this.selectedExecutor.namespaceName
      this.updateExecutorForm.appId = this.selectedExecutor.appId
      this.updateExecutorForm.executorName = this.selectedExecutor.executorName

      this.asyncQueryExecutorItem(this.selectedExecutor.namespaceName, this.selectedExecutor.appId, this.selectedExecutor.executorName)
    },
    handleDelete (index, row) {
      console.log(index, row)
      this.selectedExecutor = row
      this.centerDialogVisible = true
    },
    handleRelease (index, row) {
      console.log(index, row)
      this.selectedExecutor = row
      this.releaseExecutorForm.namespace = this.selectedExecutor.namespaceName
      this.releaseExecutorForm.appId = this.selectedExecutor.appId
      this.releaseExecutorForm.executorName = this.selectedExecutor.executorName
      this.releaseExecutorForm.name = format(new Date(), 'YYYYMMDDHHmmss') + '-release'
      this.releaseExecutorForm.comment = ''

      this.releaseExecutorDialogFormVisible = true
    },
    queryExecutors (namespace, appId) {
      console.log('queryExecutors')
      console.log(appId)

      this.axios.get('/admin/namespaces/' + namespace + '/apps/' + appId
      ).then(res => {
        console.log(res)
        this.app = res.data
        this.asyncExecutors(this.app)
      }).catch(function (error) {
        console.log(error)
      })
    },
    forwardSingleExecutor (singleExecutor) {
      console.log('forwardSingleExecutor')
      console.log(singleExecutor)
      this.$router.push({
        path: '/executor',
        query: {
          namespace: singleExecutor.namespaceName,
          appId: singleExecutor.appId,
          executor: singleExecutor.executorName
        }
      })
    },
    nextPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncExecutors(this.app)
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncExecutors(this.app)
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncExecutors(this.app)
    },
    deleteExecutorSuccessNotification (executorName) {
      const h = this.$createElement

      this.$notify({
        title: executorName + '删除成功!',
        message: h('i', {style: 'color: teal'}, '~~')
      })

      this.asyncExecutors(this.app)
    },
    deleteExecutorFailNotification (exception) {
      let messageText = exception.data.code === undefined ? exception.status + '-' + exception.statusText : exception.data.code + '-' + exception.data.message
      const h = this.$createElement
      this.$notify({
        title: this.selectedExecutor.executorName + '删除失败!',
        message: h('i', {style: 'color: #FF0000'}, messageText)
      })

      this.asyncExecutors(this.app)
    },
    createExecutorSuccessNotification (executorName) {
      const h = this.$createElement

      this.$notify({
        title: executorName + '创建成功!',
        message: h('i', {style: 'color: teal'}, '~~')
      })

      this.asyncExecutors(this.app)
    },
    createExecutorFailNotification (exception) {
      let messageText = exception.data.code === undefined ? exception.status + '-' + exception.statusText : exception.data.code + '-' + exception.data.message
      const h = this.$createElement
      this.$notify({
        title: this.newExecutorForm.newExecutor + '创建失败!',
        message: h('i', {style: 'color: #FF0000'}, messageText)
      })

      this.asyncExecutors(this.app)
    },
    asyncDeleteExecutor () {
      console.log('asyncQueryExecutor')
      console.log('deleting...' + this.selectedExecutor.executorName)
      this.centerDialogVisible = false

      this.axios.delete('/admin/namespaces/' + this.selectedExecutor.namespaceName + '/apps/' + this.selectedExecutor.appId + '/executors/' + this.selectedExecutor.executorName).then(res => {
        console.log(this.selectedExecutor.executorName + ' deleted')
        Utils.$emit('deleteExecutorSuccess', this.selectedExecutor.executorName)
      }).catch(function (error) {
        console.log(error)
        Utils.$emit('deleteExecutorFail', error)
      })
    },
    submitCreateExecutorForm (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.asyncCreateExecutor(this.newExecutorForm)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncCreateExecutor (form) {
      console.log('asyncCreateExecutor')
      console.log('creating...' + form.newExecutor)
      this.createExecutorDialogFormVisible = false

      this.axios.post('/admin/namespaces/' + form.namespace + '/apps/' +
        form.appId + '/executors/' + form.newExecutor).then(res => {
        console.log(form.newExecutor + ' created')
        Utils.$emit('createExecutorSuccess', form.newExecutor)
      }).catch(function (error) {
        console.log(error)
        Utils.$emit('createExecutorFail', error)
      })
    },
    tableRowClassName ({row, rowIndex}) {
      if (rowIndex % 2 === 1) {
        return 'success-row'
      } else {
        return ''
      }
    },
    submitUpdateExecutorForm (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.asyncUpdateExecutorItem(this.updateExecutorForm)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncUpdateExecutorItem (form) {
      console.log('asyncUpdateExecutorItem')
      console.log('updating...' + form.executorName)
      this.updateExecutorDialogFormVisible = false

      this.itemConfiguration.corePoolSize = form.corePoolSize
      this.itemConfiguration.maxPoolSize = form.maxPoolSize
      this.itemConfiguration.queueCapacity = form.queueCapacity
      this.itemConfiguration.keepAliveSeconds = form.keepAliveSeconds
      this.itemConfiguration.tickerCycle = form.tickerCycle
      this.itemConfiguration.allowCoreThreadTimeOut = form.allowCoreThreadTimeOut

      console.log('更新的配置为: ' + JSON.stringify(this.itemConfiguration))

      this.axios.put('/admin/namespaces/' + form.namespace + '/apps/' +
        form.appId + '/executors/' + form.executorName + '/items', null, {
        params: {
          configuration: JSON.stringify(this.itemConfiguration),
          comment: form.comment
        }
      }).then(res => {
        console.log(form.executorName + ' updated')
        Utils.$emit('updateExecutorItemSuccess', form.executorName)
        this.asyncExecutors(this.app)
      }).catch(function (error) {
        console.log(error)
        Utils.$emit('updateExecutorItemFail', error)
      })
    },
    asyncQueryExecutorItem (namespaceName, appId, executorName) {
      console.log('asyncQueryExecutorItem')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName + '/items'
      ).then(res => {
        console.log(res)
        this.itemConfiguration = JSON.parse(res.data.configuration)
        this.updateExecutorForm.corePoolSize = this.itemConfiguration.corePoolSize
        this.updateExecutorForm.maxPoolSize = this.itemConfiguration.maxPoolSize
        this.updateExecutorForm.queueCapacity = this.itemConfiguration.queueCapacity
        this.updateExecutorForm.keepAliveSeconds = this.itemConfiguration.keepAliveSeconds
        this.updateExecutorForm.tickerCycle = this.itemConfiguration.tickerCycle
        this.updateExecutorForm.allowCoreThreadTimeOut = this.itemConfiguration.allowCoreThreadTimeOut
        this.updateExecutorForm.comment = res.data.comment
        this.updateExecutorDialogFormVisible = true
      }).catch(function (error) {
        console.log(error)
      })
    },
    submitReleaseExecutorForm (formName) {
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.asyncRelease(this.releaseExecutorForm)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    asyncRelease (form) {
      console.log('asyncRelease')
      console.log('releasing...' + form.executorName)
      this.releaseExecutorDialogFormVisible = false

      this.axios.post('/admin/namespaces/' + form.namespace + '/apps/' +
        form.appId + '/executors/' + form.executorName + '/releases', null, {
        params: {
          name: form.name,
          comment: form.comment
        }
      }).then(res => {
        console.log(form.executorName + ' released')
        Utils.$emit('releaseExecutorSuccess', this.selectedExecutor)
        this.asyncExecutors(this.app)
      }).catch(function (error) {
        console.log(error)
        let messageText = error.data.code === undefined ? error.status + '-' + error.statusText : error.data.code + '-' + error.data.message
        alert(form.executorName + '发布失败, ' + messageText)
        Utils.$emit('releaseExecutorFail', error)
      })
    },
    forwardNamespace () {
      console.log('forwardNamespace')
      this.$router.push({
        path: '/home',
        query: {
          namespace: undefined
        }
      })
    }
  },
  beforeDestroy () {
    Utils.$off('deleteExecutorSuccess')
    Utils.$off('createExecutorSuccess')
    Utils.$off('updateExecutorItemSuccess')
    Utils.$off('releaseExecutorSuccess')
    Utils.$off('deleteExecutorFail')
    Utils.$off('createExecutorFail')
    Utils.$off('updateExecutorItemFail')
    Utils.$off('updateExecutorItemFail')
    Utils.$off('releaseExecutorFail')
  }
}

</script>
<style>
  .el-header {
    /*background-color: #F5F5F5;*/
    color: #333;
    line-height: 60px;
  }
  .el-aside {
    color: #333;
  }
  .li{
    position:relative;
    margin-top: 50px;
  }
  .title{
    position: absolute;
    width: 30%;
    text-align: justify;
    text-align-last: justify;
  }
  .title:before{
    position: absolute;
    left: 100%;
    content: '\FF1A';
  }
  .value{
    padding-left: 35%;
    text-align: center;
  }
  .el-pagination {
    text-align: right;
  }
  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
  html,body,#app,.el-container{
    /*设置内部填充为0，几个布局元素之间没有间距*/
    padding: 0px;
    /*外部间距也是如此设置*/
    margin: 0px;
    /*统一设置高度为100%*/
    height: 100%;
  }
  .item {
    margin-top: 5px;
    margin-right: 15px;
    margin-left: 15px;
  }
  .tdr {
    display: flex;
    justify-content: flex-end;
    align-items: flex-end;
  }

</style>
