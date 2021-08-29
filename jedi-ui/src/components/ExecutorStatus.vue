<template>
  <div>
    <el-header>
      <div class="block" style="margin-left: 60px">
        <el-form :model="selectInstanceForm" :rules="rules" size="medium" label-width="100px"
                 style="display: inline-block;">
          <el-form-item label="实例" prop="instance" style="width: 250px">
            <el-select v-model="selectInstanceForm.ip" placeholder="请选择下拉选择" clearable style="width:100%"
                       @change="resetMyChart()">
              <el-option v-for="(item, index) in latestReleaseInstances" :key="index" :label="item.ip"
                         :value="item.ip"
                         :disabled="false" ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span style="margin-left: 20px">时间</span>
        <el-date-picker
          v-model="queryTime"
          type="datetimerange"
          :picker-options="pickerOptions"
          @change="changeQueryTime()"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
        <el-button
          type="primary"
          @click="submitQueryExecutorStatus()" style="margin-left: 10px">查询</el-button>
        <el-switch
          v-model="isAutoRefresh"
          style="margin-left: 50px"
          active-color="#13ce66"
          inactive-color="#ff4949" active-text="自动刷新" @change="changeAutoRefresh()">
        </el-switch>
      </div>
    </el-header>
    <div id="myChart"></div>
  </div>
</template>

<script>
import format from '../assets/js/dateFormat.js'
export default {
  name: 'DynamicLineChart',
  data () {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近1小时',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近6小时',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 6)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近1天',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近1周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近10天',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 10)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      queryTime: [new Date(new Date().getTime() - 600 * 1000), new Date()],
      isAutoRefresh: false,
      // 实时数据数组
      date: [],
      queueSize: [],
      poolActivation: [],
      rejectCount: [],
      // 折线图echarts初始化选项
      echartsOption: {
        legend: {
          top: 10,
          data: ['队列已使用容量', '线程池活跃度', '拒绝任务数']
        },
        grid: [
          {
            left: 100,
            right: 100,
            height: '35%'
          },
          {
            left: 100,
            right: 100,
            top: '55%',
            height: '25%'
          }
        ],
        xAxis: [
          {
            nameTextStyle: {
              fontWeight: 600,
              fontSize: 18
            },
            type: 'time',
            splitLine: {
              show: false
            }
          },
          {
            gridIndex: 1,
            nameTextStyle: {
              fontWeight: 600,
              fontSize: 18
            },
            type: 'time',
            splitLine: {
              show: false
            }
          }
        ],
        yAxis: [
          {
            name: '队列已使用容量',
            nameTextStyle: {
              fontWeight: 'bolder',
              fontSize: 12
            },
            type: 'value',
            scale: true,
            min: 0,
            max: function (value) {
              return value.max + 5
            },
            axisLabel: {
              interval: 'auto',
              formatter: '{value}'
            },
            splitLine: {
              show: false
            }
          },
          {
            name: '线程池活跃度',
            nameTextStyle: {
              fontWeight: 'bolder',
              fontSize: 12
            },
            type: 'value',
            scale: true,
            min: 0,
            max: 1,
            axisLabel: {
              interval: 'auto',
              formatter: function (value, index) {
                return value * 100 + ' %'
              }
            },
            splitLine: {
              show: false
            }
          },
          {
            gridIndex: 1,
            // inverse: true,
            show: true,
            name: '拒绝任务数',
            nameTextStyle: {
              fontWeight: 'bolder',
              fontSize: 12
            },
            type: 'value',
            scale: true,
            min: 0,
            max: function (value) {
              return value.max + 5
            },
            axisLabel: {
              interval: 'auto',
              formatter: '{value}'
            },
            splitLine: {
              show: false
            }
          }
        ],
        tooltip: {
          trigger: 'axis'
        },
        axisPointer: {
          link: {xAxisIndex: 'all'}
        },
        toolbox: {
          right: 150,
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            restore: {},
            saveAsImage: {}
          }
        },
        dataZoom: [
          {
            show: true,
            realtime: true,
            start: 0,
            end: 100,
            xAxisIndex: [0, 1]
          },
          {
            type: 'inside',
            realtime: true,
            start: 0,
            end: 100,
            xAxisIndex: [0, 1]
          }
        ],
        series: [
          {
            name: '队列已使用容量',
            type: 'line',
            smooth: true,
            data: this.queueSize, // 绑定实时数据数组
            yAxisIndex: 0
          },
          {
            name: '线程池活跃度',
            type: 'line',
            smooth: true,
            data: this.poolActivation, // 绑定实时数据数组
            yAxisIndex: 1
          },
          {
            name: '拒绝任务数',
            type: 'line',
            smooth: true,
            data: this.rejectCount, // 绑定实时数据数组
            xAxisIndex: 1,
            yAxisIndex: 2
          }
        ]
      },
      selectInstanceForm: {
        ip: undefined
      },
      latestReleaseInstances: [],
      rules: {
        ip: [{
          required: true,
          message: '请选择下拉选择',
          trigger: 'change'
        }]
      },
      countdownTimer: undefined,
      namespaceName: undefined,
      appId: undefined,
      executorName: undefined,
      refreshIntervalSeconds: 3,
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 1000
      }
    }
  },
  created () {
    console.log(this.$route.query.executor)
    if (this.$route.query) {
      this.namespaceName = this.$route.query.namespace
      this.appId = this.$route.query.appId
      this.executorName = this.$route.query.executor
    }
    this.queryLatestReleaseInstances()
  },
  mounted () {
    this.myChart = this.$echarts.init(document.getElementById('myChart'), 'light') // 初始化echarts, theme为light
    this.myChart.setOption(this.echartsOption) // echarts设置初始化选项
  },
  methods: {
    queryLatestReleaseInstances () {
      this.asyncLatestRelease(this.namespaceName, this.appId, this.executorName)
    },
    // 变更查询时间
    changeQueryTime: function () {
      if (this.queryTime[1].getTime() - new Date().getTime() > 0) {
        this.$message('截止时间不能晚于现在时间!')
        this.queryTime[1] = new Date()
        this.queryTime[0] = new Date(this.queryTime[1].getTime() - 600 * 1000)
      }
      if (new Date().getTime() - this.queryTime[0].getTime() > 3600 * 1000 * 24 * 11) {
        this.$message('开始时间不能早于10天前!')
        this.queryTime[1] = new Date()
        this.queryTime[0] = new Date(this.queryTime[1].getTime() - 600 * 1000)
      }
      if (this.queryTime[1].getTime() - this.queryTime[0].getTime() > 3600 * 1000 * 24 * 10) {
        this.$message('时间跨度最大10天!')
        this.queryTime[1] = new Date()
        this.queryTime[0] = new Date(this.queryTime[1].getTime() - 600 * 1000)
      }
    },
    // 提交查询
    submitQueryExecutorStatus: function () {
      this.queueSize = []
      this.poolActivation = []
      this.rejectCount = []
      this.pagination.total = 0
      this.pagination.pageNum = 1

      this.asyncQueryExecutorStatus(this.namespaceName, this.appId, this.executorName,
        this.selectInstanceForm.ip, this.queryTime[0],
        this.queryTime[1])
    },
    asyncQueryExecutorStatus (namespaceName, appId, executorName, instanceIp, startTime, endTime) {
      console.log('asyncQueryExecutorStatus')
      console.log(executorName)
      this.axios.get('/consumer/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/status', {
        params: {
          instanceIp: instanceIp,
          startTime: format(startTime, 'yyyy-MM-dd HH:mm:ss'),
          endTime: format(endTime, 'yyyy-MM-dd HH:mm:ss'),
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
      }).then(res => {
        console.log(res)
        this.addData(res.data.content)

        this.pagination.total = res.data.total
        this.pagination.pageNum = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize

        let totalPageCount = Math.ceil(this.pagination.total / this.pagination.pageSize)

        if (this.pagination.pageNum < totalPageCount) {
          this.pagination.pageNum = this.pagination.pageNum + 1
          this.asyncQueryExecutorStatus(namespaceName, appId, executorName, instanceIp, startTime,
            endTime)
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    // 自动刷新
    changeAutoRefresh: function () {
      if (this.isAutoRefresh) {
        this.countdownTimer = setInterval(this.autoRefreshAddData, this.refreshIntervalSeconds * 1000) // 每N秒更新实时数据到折线图
      } else {
        clearInterval(this.countdownTimer)
      }
    },
    // 添加实时数据
    autoRefreshAddData: function () {
      this.$once('hook:beforeDestroy', () => {
        clearInterval(this.countdownTimer)
      })
      this.pagination.total = 0
      this.pagination.pageNum = 1
      const end = new Date()
      const start = new Date(end.getTime() - (this.refreshIntervalSeconds - 1) * 1000)
      this.asyncQueryExecutorStatus(this.namespaceName, this.appId, this.executorName, this.selectInstanceForm.ip,
        start, end)
    },
    buildDateAndValue: function (date, actualValue) {
      return {
        name: date,
        value: [
          date,
          actualValue
        ]
      }
    },
    addData: function (executorStatusResponseList) {
      let list = Array.from(executorStatusResponseList)
      console.log(list)
      list.forEach((item, index) => {
        this.queueSize.push(this.buildDateAndValue(item.recordTime, item.queueSize))
        this.poolActivation.push(this.buildDateAndValue(item.recordTime, item.poolActivation))
        this.rejectCount.push(this.buildDateAndValue(item.recordTime, item.rejectCount))
      })

      // 重新将数组赋值给echarts选项
      this.echartsOption.series[0].data = this.queueSize
      this.echartsOption.series[1].data = this.poolActivation
      this.echartsOption.series[2].data = this.rejectCount
      this.myChart.setOption(this.echartsOption)
    },
    asyncLatestRelease (namespaceName, appId, executorName) {
      console.log('asyncLatestRelease')
      console.log(executorName)

      this.axios.get('/admin/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/releases/latest').then(res => {
        console.log(res)
        let latestRelease = res.data
        this.asyncQueryInstance(latestRelease.id)
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
          pageNum: 1,
          pageSize: 200
        }
      }).then(res => {
        console.log(res)
        this.latestReleaseInstances = res.data.content
        if (this.latestReleaseInstances.length > 0) {
          this.selectInstanceForm.ip = this.latestReleaseInstances[0].ip
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    resetMyChart () {
      console.log('resetMyChart')
      this.myChart.clear()
      this.echartsOption.series[0].data = []
      this.echartsOption.series[1].data = []
      this.echartsOption.series[2].data = []
      this.myChart.setOption(this.echartsOption)
    }
  }
}
</script>

<style>
  /*设定宽高，不然超出windows会显示不出来*/
  #myChart{
    margin-left: 20px !important;
    width: 90%;
    height: 700px;
    margin: 0 auto;
  }
</style>
