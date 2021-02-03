<template>
	<el-date-picker type="datetimerange" align="center" size="small" unlink-panels
	                v-model="thisDate" :picker-options="pickerOptions" @change="setDate(thisDate)"
	                value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00', '23:59:59']"
	                start-placeholder="开始时间" end-placeholder="结束时间" range-separator="-">
	</el-date-picker>
</template>

<script>
	export default {
		name: "DateTimeRangePicker",
		props: {
			date: {
				required: true
			},
			setDate: {
				type: Function,
				required: true
			}
		},
		data() {
			return {
				//子组件不能直接修改父组件的值
				thisDate: this.date,
				pickerOptions: {
					shortcuts: [
						{
							text: '今天',
							onClick(picker) {
								const start = new Date()
								const end = new Date()
								start.setHours(0)
								start.setMinutes(0)
								start.setSeconds(0)
								end.setHours(23)
								end.setMinutes(59)
								end.setSeconds(59)
								picker.$emit('pick', [start, end])
							}
						},
						{
							text: '昨天',
							onClick(picker) {
								const date = new Date()
								const start = new Date()
								const end = new Date()
								start.setTime(date.getTime() - 3600 * 1000 * 24)
								end.setTime(date.getTime() - 3600 * 1000 * 24)
								start.setHours(0)
								start.setMinutes(0)
								start.setSeconds(0)
								end.setHours(23)
								end.setMinutes(59)
								end.setSeconds(59)
								picker.$emit('pick', [start, end])
							}
						},
						{
							text: '最近三天',
							onClick(picker) {
								const end = new Date()
								const start = new Date()
								start.setTime(start.getTime() - 3600 * 1000 * 24 * 3)
								picker.$emit('pick', [start, end])
							}
						},
						{
							text: '最近一周',
							onClick(picker) {
								const end = new Date()
								const start = new Date()
								start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
								picker.$emit('pick', [start, end])
							}
						},
						{
							text: '最近一个月',
							onClick(picker) {
								const end = new Date()
								const start = new Date()
								start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
								picker.$emit('pick', [start, end])
							}
						},
					]
				},
			}
		}
	}
</script>

<style scoped>

</style>