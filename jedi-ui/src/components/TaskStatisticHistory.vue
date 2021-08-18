<template>
  <div>
    <el-container>
      <el-main>
        <div style="height: 90%">
          <el-table
            :data="statisticsList.filter(data => !search || data.taskName.toLowerCase().includes(search.toLowerCase()))"
            style="width: 100%"
            :row-class-name="tableRowClassName">
            <el-table-column
              label="任务名称"
              prop="taskName"
              :formatter = "isDefaultOneFormatter">
            </el-table-column>
            <el-table-column
              label="执行总数"
              prop="total">
            </el-table-column>
            <el-table-column
              label="执行失败总数"
              prop="failure">
            </el-table-column>
            <el-table-column
              label="执行失败比例"
              prop="failureRatio">
            </el-table-column>
            <el-table-column
              label="执行最长时间(ms)"
              prop="executionTimeMax">
            </el-table-column>
            <el-table-column
              label="执行最短时间(ms)"
              prop="executionTimeMin">
            </el-table-column>
            <el-table-column
              label="更新时间"
              prop="dataChangeLastModifiedTime">
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  size="mini"
                  placeholder="输入关键字搜索"/>
              </template>
              <!--<template slot-scope="scope">-->
                <!--<el-button-->
                  <!--size="mini"-->
                  <!--@click="handleReleaseDetail(scope.$index, scope.row)" style="margin-right: 50%">明细</el-button>-->
              <!--</template>-->
            </el-table-column>
          </el-table>
          <el-dialog title="明细" :visible.sync="selectedReleaseDialogFormVisible">
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
import format from '../assets/js/dateFormat.js'
export default {
  data () {
    return {
      formLabelWidth: '20%',
      statisticsList: [],
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
      this.asyncQueryStatisticsList(this.$route.query.namespace, this.$route.query.appId, this.$route.query.executor)
    }
  },
  methods: {
    asyncQueryStatisticsList (namespaceName, appId, executorName) {
      console.log('asyncQueryStatisticsList')
      console.log(executorName)

      this.axios.get('/consumer/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/task-statistics/all', {
        params: {
          statisticsDate: format(new Date(), 'yyyy-MM-dd'),
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.statisticsList = res.data.content
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
      if (rowIndex === 0) {
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
    },
    isDefaultOneFormatter (row, column, cellValue, index) {
      if (row.taskName === 'DEFAULT') {
        row.taskName = '全部任务(含未命名)'
      }
      return row.taskName
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
