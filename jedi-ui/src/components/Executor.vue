<template>
  <div style="height: 100%">
    <el-container>
      <el-container>
        <el-header>
          <h2>Jedi线程池管理平台</h2>
        </el-header>
        <el-container>
          <el-aside width="20%">
            <div>
              <el-tag type="info">所属环境: {{this.executor.namespaceName}}</el-tag>
              <el-tag type="info">所属应用: {{this.executor.appId}}</el-tag>
            </div>
            <div>
              <ul style="list-style:none; margin:0px; width:80%;">
                <li>
                  <div class="title">线程池名称</div>
                  <div class="value">{{this.executor.executorName}}</div>
                </li>
                <el-divider></el-divider>
                <li>
                  <div class="title">线程池负责人</div>
                  <div class="value">{{this.executor.dataChangeCreatedBy}}</div>
                </li>
                <el-divider></el-divider>
                <li>
                  <div class="title">线程池配置</div>
                  <div class="value">{{this.item.configuration}}</div>
                </li>
                <el-divider></el-divider>
                <li>
                  <div class="title">配置最近更新时间</div>
                  <div class="value">{{this.item.dataChangeLastModifiedTime}}</div>
                </li>
              </ul>
            </div>
          </el-aside>
          <el-main><h3>仪表板</h3></el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
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
      },
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 5
      }
    }
  },
  computed: {},
  watch: {},
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.executor.namespaceName = this.$route.query.namespace
      this.executor.appId = this.$route.query.appId
      this.executor.executorName = this.$route.query.executor
      this.asyncQueryExecutor(this.$route.query.namespace, this.$route.query.appId, this.executor.executorName)
    }
  },
  mounted () {},
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
    },
    nextPage (pageNum) {
      this.pagination.pageNum = pageNum
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
    }
  }
}

</script>
<style>
  .el-header {
    /*background-color: #F5F5F5;*/
    color: #333;
    line-height: 20px;
  }
  li{
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
    padding-left: 20%;
  }
  .el-aside {
    color: #333;
  }
  .el-pagination {
    text-align: right;
  }
  html,body,#app,.el-container{
    /*设置内部填充为0，几个布局元素之间没有间距*/
    padding: 0px;
    /*外部间距也是如此设置*/
    margin: 0px;
    /*统一设置高度为100%*/
    height: 100%;
  }
</style>
