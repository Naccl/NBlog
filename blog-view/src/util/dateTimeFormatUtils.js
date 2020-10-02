import Vue from 'vue'
import moment from 'moment'

//设置moment国际化语言
moment.locale('zh-cn')

Vue.filter('dateFormat', function (value, format = 'YYYY-MM-DD HH:mm:ss') {
	return moment(value).format(format)
})

Vue.filter('dateFromNow', function (value) {
	//相对时间大于一个月，显示详细时间
	if (moment().diff(moment(value)) > 2592000000) {
		return moment(value).format('YYYY-MM-DD HH:mm')
	}
	return moment(value).fromNow()
})