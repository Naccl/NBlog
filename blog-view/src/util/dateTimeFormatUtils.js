import moment from 'moment'


//设置moment国际化语言
moment.locale('zh-cn')


export function dateFormat(value, format) {
	return moment(value).format(format)
}

export function dateFromNow(value) {
	//相对时间大于一个月，显示详细时间
	if (moment().diff(moment(value)) > 2592000000) {
		return moment(value).format('YYYY-MM-DD HH:mm')
	}
	return moment(value).fromNow()
}

// app.filter('dateFormat', function (value, format = 'YYYY-MM-DD HH:mm:ss') {
// 	return moment(value).format(format)
// })
//
// app.filter('dateFromNow', function (value) {
// 	//相对时间大于一个月，显示详细时间
// 	if (moment().diff(moment(value)) > 2592000000) {
// 		return moment(value).format('YYYY-MM-DD HH:mm')
// 	}
// 	return moment(value).fromNow()
// })