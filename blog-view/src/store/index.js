import Vue from 'vue'
import Vuex from 'vuex'
import {SAVE_WEB_TITLE_SUFFIX, SAVE_INTRODUCTION} from "./mutations-types";

Vue.use(Vuex)

const state = {
	webTitleSuffix: '',
	introduction: {
		avatar: '',
		name: '',
		rollText: [],
	}
}

const actions = {
	saveWebTitleSuffix({commit}, webTitleSuffix) {
		commit(SAVE_WEB_TITLE_SUFFIX, {webTitleSuffix})
	},
	saveIntroduction({commit}, introduction) {
		commit(SAVE_INTRODUCTION, {introduction})
	}
}

const mutations = {
	[SAVE_WEB_TITLE_SUFFIX](state, {webTitleSuffix}) {
		state.webTitleSuffix = webTitleSuffix
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
