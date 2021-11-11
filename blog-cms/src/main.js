import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//normalize.css
import 'normalize.css/normalize.css' // A modern alternative to CSS resets
//element-ui
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//global css
import '@/assets/styles/index.scss'
//icon
import '@/icons'

//moment
import './util/dateTimeFormatUtils.js'
//mavonEditor
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
//v-viewer
import 'viewerjs/dist/viewer.css'
import Viewer from 'v-viewer'
// directive
import './util/directive'

Vue.use(mavonEditor)
Vue.use(Element)
Vue.use(Viewer)

Vue.prototype.msgSuccess = function (msg) {
	this.$message.success(msg)
}

Vue.prototype.msgError = function (msg) {
	this.$message.error(msg)
}

Vue.config.productionTip = false

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')