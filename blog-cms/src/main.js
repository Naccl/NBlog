import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//element-ui
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(Element)


Vue.config.productionTip = false

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')
