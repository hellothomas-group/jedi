<template>
  <div>
    <el-container>
      <el-tag type="info" style="font-size: 16px;text-align: center;color: #0000FF">发布名称: {{this.activeRelease.name}}</el-tag>
      <el-main>
        <div style="height: 90%">
        <el-table
        :data="instances.filter(data => !search || data.ip.toLowerCase().includes(search.toLowerCase()))"
        style="width: 100%"
        :row-class-name="tableRowClassName">
        <el-table-column
        label="ip"
        prop="ip">
        </el-table-column>
        <el-table-column
        label="配置获取时间"
        prop="configs[0].dataChangeLastModifiedTime">
        </el-table-column>
        <el-table-column
        align="right">
        <template slot="header" slot-scope="scope">
        <el-input
        v-model="search"
        size="mini"
        placeholder="输入关键字搜索"/>
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
export default {
  data () {
    return {
      instances: [],
      search: '',
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      activeRelease: {
        id: undefined,
        releaseKey: undefined,
        name: ' 无 ',
        namespaceName: undefined,
        appId: undefined,
        executorName: undefined,
        configurations: undefined,
        comment: undefined,
        isAbandoned: undefined,
        dataChangeCreatedBy: undefined,
        dataChangeLastModifiedBy: undefined,
        dataChangeCreatedTime: undefined,
        dataChangeLastModifiedTime: undefined
      },
      instance: {
        id: undefined,
        namespaceName: undefined,
        appId: undefined,
        ip: undefined,
        dataChangeCreatedTime: undefined,
        configs: undefined
      },
      instanceConfig: {
        release: undefined,
        releaseDeliveryTime: undefined,
        dataChangeLastModifiedTime: undefined
      }
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.asyncActiveRelease(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncActiveRelease (namespaceName, appId, executorName) {
      console.log('asyncActiveRelease')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/releases/latest').then(res => {
        console.log(res)
        if (res.data !== null) {
          this.activeRelease = res.data
          this.asyncQueryInstance(this.activeRelease.id)
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    asyncQueryInstance (releaseId) {
      console.log('asyncQueryInstance')
      console.log(releaseId)

      this.axios.get('/admin/instances/by-release', {
        params: {
          releaseId: releaseId,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.instances = res.data.content
        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
      }).catch(function (error) {
        console.log(error)
      })
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
      this.asyncQueryInstance(this.activeRelease.id)
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryInstance(this.activeRelease.id)
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryInstance(this.activeRelease.id)
    }
  }
}
</script>

<style scoped>
  .el-header {
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
