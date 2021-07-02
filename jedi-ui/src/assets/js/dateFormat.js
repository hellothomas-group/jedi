function format (date, format) {
  // 年
  if (/YYYY/.test(format)) {
    format = format.replace(/YYYY/, date.getFullYear())
  }
  if (/yyyy/.test(format)) {
    format = format.replace(/yyyy/, date.getFullYear())
  }
  // 月份
  let month = date.getMonth() + 1
  if (/MM/.test(format)) {
    let monthStr = month < 10 ? '0' + month : month
    format = format.replace(/MM/, monthStr)
  } else if (/M/.test(format)) {
    format = format.replace(/M/, month)
  }
  // 日期
  let dates = date.getDate()
  if (/DD/.test(format)) {
    let dateStr = dates < 10 ? '0' + dates : dates
    format = format.replace(/DD/, dateStr)
  } else if (/D/.test(format)) {
    format = format.replace(/D/, dates)
  }
  if (/dd/.test(format)) {
    let dateStr = dates < 10 ? '0' + dates : dates
    format = format.replace(/dd/, dateStr)
  } else if (/D/.test(format)) {
    format = format.replace(/d/, dates)
  }
  // 小时
  let hours = date.getHours()
  if (/HH/.test(format)) {
    let hoursStr = hours < 10 ? '0' + hours : hours
    format = format.replace(/HH/, hoursStr)
  } else if (/H/.test(format)) {
    format = format.replace(/H/, hours)
  } else if (/hh/.test(format)) {
    let hoursMin = hours > 12 ? hours - 12 : hours
    let hoursStr = hoursMin < 10 ? '0' + hoursMin : hoursMin
    format = format.replace(/hh/, hoursStr)
  } else if (/h/.test(format)) {
    let hoursMin = hours > 12 ? hours - 12 : hours
    format = format.replace(/h/, hoursMin)
  }
  // 分
  let minutes = date.getMinutes()
  if (/mm/.test(format)) {
    let minutesStr = minutes < 10 ? '0' + minutes : minutes
    format = format.replace(/mm/, minutesStr)
  } else if (/m/.test(format)) {
    format = format.replace(/m/, minutes)
  }
  // 秒
  let seconds = date.getSeconds()
  if (/ss/.test(format)) {
    let secondsStr = seconds < 10 ? '0' + seconds : seconds
    format = format.replace(/ss/, secondsStr)
  } else if (/s/.test(format)) {
    format = format.replace(/s/, seconds)
  }
  return format
}

export default format
