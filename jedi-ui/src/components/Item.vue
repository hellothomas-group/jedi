<template>
  <div class="hello">
    <el-container>
      <el-main>
        <div>
          <table border="1" style="margin: auto;width: 50%;line-height: 60px">
            <tr>
              <th>线程池名称</th>
              <td>{{this.executor.executorName}}</td>
            </tr>
            <tr>
              <th>线程池负责人</th>
              <td>{{this.executor.dataChangeCreatedBy}}</td>
            </tr>
            <tr>
              <th>线程池配置</th>
              <td>{{this.item.configuration}}</td>
            </tr>
            <tr>
              <th>配置最近更新时间</th>
              <td>{{this.item.dataChangeLastModifiedTime}}</td>
            </tr>
          </table>
        </div>
      </el-main>
    </el-container>

  </div>
</template>

<script>
export default {
  data () {
    return {
      executor: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      },
      item: {
        id: undefined,
        executorId: undefined,
        configuration: undefined,
        comment: undefined,
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
      this.asyncQueryExecutor(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncQueryExecutor (namespaceName, appId, executorName) {
      console.log('asyncQueryExecutor')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName
      ).then(res => {
        console.log(res)
        this.executor = res.data
        this.asyncQueryItem(namespaceName, appId, executorName)
      }).catch(function (error) {
        console.log(error)
      })
    },
    asyncQueryItem (namespaceName, appId, executorName) {
      console.log('asyncQueryItem')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName + '/items'
      ).then(res => {
        console.log(res)
        this.item = res.data
      }).catch(function (error) {
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  .el-main {
    color: #333;
    text-align: center;
    line-height: 160px;
  }

</style>
