import { createApp } from "vue";
import { createStore } from 'vuex'
import state from './state'
import actions from './actions'
import mutations from './mutations'
import getters from './getters'

const store = createStore({
	state,
	actions,
	mutations,
	getters
})

const app = createApp()

app.use(store)

export default store
