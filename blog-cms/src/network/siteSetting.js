import axios from '@/plugins/axios'

export function getSiteSettingData() {
	return axios({
		url: 'siteSettings',
		method: 'GET'
	})
}

export function update(settings, deleteIds) {
	return axios({
		url: 'siteSettings',
		method: 'POST',
		data: {
			settings,
			deleteIds
		}
	})
}