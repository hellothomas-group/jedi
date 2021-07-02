<template>
  <div>
    <el-header>
      <div class="block" style="margin-left: 60px">
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
          text: '最近1个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
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
            boundaryGap: ['0', '15%'],
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
            boundaryGap: ['0', '15%'],
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
            start: 60,
            end: 100,
            xAxisIndex: [0, 1]
          },
          {
            type: 'inside',
            realtime: true,
            start: 60,
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
      countdownTimer: undefined,
      namespaceName: undefined,
      appId: undefined,
      executorName: undefined,
      pagination: {
        total: 0,
        pageNum: 1,
        pageSize: 200
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
  },
  mounted () {
    this.myChart = this.$echarts.init(document.getElementById('myChart'), 'light') // 初始化echarts, theme为light
    this.myChart.setOption(this.echartsOption) // echarts设置初始化选项
  },
  methods: {
    // 获取当前时间
    getTime: function () {
      var ts = arguments[0] || 0
      var t, h, i, s
      t = ts ? new Date(ts * 1000) : new Date()
      h = t.getHours()
      i = t.getMinutes()
      s = t.getSeconds()
      // 定义时间格式
      return (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i) + ':' + (s < 10 ? '0' + s : s)
    },
    // 自动刷新
    changeQueryTime: function () {
      if (this.queryTime[1].getTime() - this.queryTime[0].getTime() > 3600 * 1000 * 24 * 30) {
        alert('时间跨度最大一个月!')
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
      this.asyncQueryExecutorStatus(this.namespaceName, this.appId, this.executorName, this.queryTime[0], this.queryTime[1])
    },
    asyncQueryExecutorStatus (namespaceName, appId, executorName, startTime, endTime) {
      console.log('asyncQueryExecutorStatus')
      console.log(executorName)
      this.axios.get('/consumer/namespaces/' + namespaceName + '/apps/' + appId + '/executors/' + executorName +
        '/status', {
        params: {
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
          this.asyncQueryExecutorStatus(namespaceName, appId, executorName, startTime, endTime)
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    // 自动刷新
    changeAutoRefresh: function () {
      if (this.isAutoRefresh) {
        this.countdownTimer = setInterval(this.autoRefreshAddData, 3000) // 每三秒更新实时数据到折线图
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
      const start = new Date(end.getTime() - 2 * 1000)
      this.asyncQueryExecutorStatus(this.namespaceName, this.appId, this.executorName, start, end)
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
    }
  }
}
</script>

<style>
  /*设定宽高，不然超出windows会显示不出来*/
  #myChart{
    width: 100%;
    height: 700px;
    margin: 0 auto;
  }
</style>
