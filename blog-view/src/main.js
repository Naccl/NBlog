import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//自定义css
import './assets/css/base.css'
//阿里icon
import './assets/css/icon/iconfont.css'
//typo.css
import "./assets/css/typo.css";
//semantic-ui-vue
import SuiVue from 'semantic-ui-vue'
import 'semantic-ui-css/semantic.min.css'
//element-ui
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//moment
import './util/dateTimeFormatUtils.js'
//v-viewer
import 'viewerjs/dist/viewer.css'
import Viewer from 'v-viewer'
//directive
import './util/directive'

console.log("\n %c Naccl's Blog %c https://naccl.top \n", "color: #48dbfb; background: #1b1c1d; padding:5px 0;", "background: #fadfa3; padding:5px 0;")

Vue.use(SuiVue)
Vue.use(Element)
Vue.use(Viewer)

Vue.prototype.msgSuccess = function (msg) {
	this.$message.success(msg)
}

Vue.prototype.msgError = function (msg) {
	this.$message.error(msg)
}

Vue.prototype.msgInfo = function (msg) {
	this.$message.info(msg);
}

const cubic = value => Math.pow(value, 3);
const easeInOutCubic = value => value < 0.5 ? cubic(value * 2) / 2 : 1 - cubic((1 - value) * 2) / 2;
//滚动至页面顶部，使用 Element-ui 回到顶部 组件中的算法
Vue.prototype.scrollToTop = function () {
	const el = document.documentElement
	const beginTime = Date.now()
	const beginValue = el.scrollTop
	const rAF = window.requestAnimationFrame || (func => setTimeout(func, 16))
	const frameFunc = () => {
		const progress = (Date.now() - beginTime) / 500;
		if (progress < 1) {
			el.scrollTop = beginValue * (1 - easeInOutCubic(progress))
			rAF(frameFunc)
		} else {
			el.scrollTop = 0
		}
	}
	rAF(frameFunc)
}


Vue.config.productionTip = false

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')
