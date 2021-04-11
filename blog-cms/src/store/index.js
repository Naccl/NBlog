import Vue from 'vue'
import Vuex from 'vuex'
import {SAVE_NAV_STATE, SAVE_WEB_TITLE_SUFFIX} from './mutations-types'

Vue.use(Vuex)

const state = {
	activePath: '',
	webTitleSuffix: ''
}

const actions = {}

const mutations = {
	[SAVE_NAV_STATE](state, activePath) {
		state.activePath = activePath
	},
	[SAVE_WEB_TITLE_SUFFIX](state, webTitleSuffix) {
		state.webTitleSuffix = webTitleSuffix
	}
}

export default new Vuex.Store({
	state,
	actions,
	mutations,
	modules: {}
})
