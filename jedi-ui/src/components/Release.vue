<template>
  <div>
    <el-container>
      <el-main>
        <div style="height: 90%">
          <el-table
            :data="releases.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="发布key"
              prop="releaseKey">
            </el-table-column>
            <el-table-column
              label="发布name"
              prop="name">
            </el-table-column>
            <el-table-column
              label="发布者"
              prop="dataChangeLastModifiedBy">
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  size="mini"
                  placeholder="输入关键字搜索"/>
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleReleaseDetail(scope.$index, scope.row)" style="margin-right: 50%">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-dialog title="发布详情" :visible.sync="selectedReleaseDialogFormVisible">
            <el-form>
              <el-form-item label="configuration" :label-width="formLabelWidth">
                <el-input v-model="selectedRelease.configurations" autocomplete="off" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="备注" :label-width="formLabelWidth">
                <el-input v-model="selectedRelease.comment" autocomplete="off" :disabled="true"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="selectedReleaseDialogFormVisible = false">确 定</el-button>
            </div>
          </el-dialog>
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
      formLabelWidth: '20%',
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
      releases: [],
      search: '',
      selectedReleaseDialogFormVisible: false,
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      selectedRelease: {
        id: undefined,
        releaseKey: undefined,
        name: undefined,
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
        this.asyncQueryReleases(namespaceName, appId, executorName)
      }).catch(function (error) {
        console.log(error)
      })
    },
    asyncQueryReleases (namespaceName, appId, executorName) {
      console.log('asyncQueryReleases')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/releases/all', {
        params: {
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.releases = res.data.content
        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
      }).catch(function (error) {
        console.log(error)
      })
    },
    handleReleaseDetail (index, row) {
      console.log(index, row)
      this.selectedRelease = row
      this.selectedReleaseDialogFormVisible = true
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
      this.asyncQueryReleases(this.executor.namespaceName, this.executor.appId, this.executor.executorName)
    },
    prevPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryReleases(this.executor.namespaceName, this.executor.appId, this.executor.executorName)
    },
    currentPage (pageNum) {
      this.pagination.pageNum = pageNum
      this.asyncQueryReleases(this.executor.namespaceName, this.executor.appId, this.executor.executorName)
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
