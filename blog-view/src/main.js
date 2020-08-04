import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//semantic-ui-vue
import SuiVue from 'semantic-ui-vue'
import 'semantic-ui-css/semantic.min.css'
//moment
import './util/dateTimeFormatUtils.js'

Vue.use(SuiVue)


Vue.config.productionTip = false

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')
