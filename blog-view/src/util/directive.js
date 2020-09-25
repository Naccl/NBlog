import Vue from 'vue'

/**
 * 防抖 单位时间只触发最后一次
 * 例：<el-button v-debounce="[reset,`click`,300]">刷新</el-button>
 * 简写：<el-button v-debounce="[reset]">刷新</el-button>
 */
Vue.directive('debounce', {
	inserted: function (el, binding) {
		let [fn, event = "click", time = 300] = binding.value
		let timer
		el.addEventListener(event, () => {
			timer && clearTimeout(timer)
			timer = setTimeout(() => fn(), time)
		})
	}
})

/**
 * 节流 每单位时间可触发一次
 * 例：<el-button v-throttle="[reset,`click`,300]">刷新</el-button>
 * 传递参数：<el-button v-throttle="[()=>reset(param),`click`,300]">刷新</el-button>
 */
Vue.directive('throttle', {
	inserted: function (el, binding) {
		let [fn, event = "click", time = 300] = binding.value
		let now, preTime
		el.addEventListener(event, () => {
			now = new Date()
			if (!preTime || now - preTime > time) {
				preTime = now
				fn()
			}
		})
	}
})
