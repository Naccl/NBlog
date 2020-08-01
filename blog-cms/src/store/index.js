import Vue from 'vue'
import Vuex from 'vuex'
import {SAVE_NAV_STATE} from './mutations-types'

Vue.use(Vuex)

const state = {
	activePath: ''
}

const actions = {
	saveNavState({commit}, activePath) {
		commit(SAVE_NAV_STATE, {activePath})
	}
}

const mutations = {
	[SAVE_NAV_STATE](state, {activePath}) {
		state.activePath = activePath
	}
}

export default new Vuex.Store({
	state,
	actions,
	mutations,
	modules: {}
})
