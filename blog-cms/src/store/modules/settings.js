import defaultSettings from '@/settings'

const {title, logo, fixedHeader, sidebarLogo, defaultOpeneds} = defaultSettings

const state = {
	title: title,
	logo: logo,
	fixedHeader: fixedHeader,
	sidebarLogo: sidebarLogo,
	defaultOpeneds: defaultOpeneds,
}

const mutations = {
	CHANGE_SETTING: (state, {key, value}) => {
		// eslint-disable-next-line no-prototype-builtins
		if (state.hasOwnProperty(key)) {
			state[key] = value
		}
	}
}

const actions = {
	changeSetting({commit}, data) {
		commit('CHANGE_SETTING', data)
	}
}

export default {
	namespaced: true,
	state,
	mutations,
	actions
}

