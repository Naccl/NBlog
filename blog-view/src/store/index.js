import Vue from 'vue'
import Vuex from 'vuex'
import {SAVE_INTRODUCTION, SAVE_SITE_INFO} from "./mutations-types";

Vue.use(Vuex)

const state = {
	siteInfo: '',
	introduction: {
		avatar: '',
		name: '',
		rollText: [],
	}
}

const actions = {
	saveSiteInfo({commit}, siteInfo) {
		commit(SAVE_SITE_INFO, {siteInfo})
	},
	saveIntroduction({commit}, introduction) {
		commit(SAVE_INTRODUCTION, {introduction})
	}
}

const mutations = {
	[SAVE_SITE_INFO](state, {siteInfo}) {
		state.siteInfo = siteInfo
	},
	[SAVE_INTRODUCTION](state, {introduction}) {
		state.introduction = introduction
	}
}

export default new Vuex.Store({
	state,
	actions,
	mutations,
	modules: {}
})
