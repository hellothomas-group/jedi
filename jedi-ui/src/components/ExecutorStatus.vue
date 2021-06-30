<template>
  <div id="myChart"></div>
</template>

<script>
// import echarts from 'echarts'
export default {
  name: 'DynamicLineChart',
  data () {
    return {
      // 实时数据数组
      date: [],
      queueSize: [],
      poolActivation: [],
      rejectCount: [],
      // 折线图echarts初始化选项
      echartsOption: {
        legend: {
          data: ['队列已使用容量', '线程活跃度', '拒绝任务数']
        },
        xAxis: {
          name: '时间',
          nameTextStyle: {
            fontWeight: 600,
            fontSize: 18
          },
          type: 'category',
          boundaryGap: false,
          data: this.date // 绑定实时数据数组
        },
        yAxis: [
          {
            name: '队列已使用容量',
            nameTextStyle: {
              fontWeight: 600,
              fontSize: 18
            },
            type: 'value',
            scale: true,
            boundaryGap: ['0', '15%'],
            axisLabel: {
              interval: 'auto',
              formatter: '{value}'
            }
          },
          {
            name: '线程活跃度',
            nameTextStyle: {
              fontWeight: 600,
              fontSize: 18
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
            }
          }
        ],
        tooltip: {
          trigger: 'axis'
        },
        series: [
          {
            name: '队列已使用容量',
            type: 'line',
            smooth: true,
            data: this.queueSize, // 绑定实时数据数组
            yAxisIndex: 0
          },
          {
            name: '线程活跃度',
            type: 'line',
            smooth: true,
            data: this.poolActivation, // 绑定实时数据数组
            yAxisIndex: 1
          },
          {
            name: '拒绝任务数',
            type: 'line',
            smooth: true,
            data: this.rejectCount // 绑定实时数据数组
          }
        ],
        countdownTimer: undefined
      }
    }
  },
  mounted () {
    this.myChart = this.$echarts.init(document.getElementById('myChart'), 'light') // 初始化echarts, theme为light
    this.myChart.setOption(this.echartsOption) // echarts设置初始化选项
    this.countdownTimer = setInterval(this.addData, 3000) // 每三秒更新实时数据到折线图
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
    // 添加实时数据
    addData: function () {
      this.$once('hook:beforeDestroy', () => {
        clearInterval(this.countdownTimer)
      })

      // 从接口获取数据并添加到数组
      this.axios.get('/admin/test').then((res) => {
        console.log(res)

        this.queueSize.push(res.data.queueSize)
        this.poolActivation.push(res.data.poolActivation)
        this.rejectCount.push(res.data.rejectCount)
        this.date.push(this.getTime(Math.round(new Date().getTime() / 1000)))
        // 重新将数组赋值给echarts选项
        this.echartsOption.xAxis.data = this.date
        this.echartsOption.series[0].data = this.queueSize
        this.echartsOption.series[1].data = this.poolActivation
        this.echartsOption.series[2].data = this.rejectCount
        this.myChart.setOption(this.echartsOption)
      })
    }
  }
}
</script>

<style>
  /*设定宽高，不然超出windows会显示不出来*/
  #myChart{
    width: 100%;
    height: 500px;
    margin: 0 auto;
  }
</style>
