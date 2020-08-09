import axios from '@/plugins/axios'

export function getSiteSettingList() {
	return axios({
		url: 'siteSettings',
		method: 'GET'
	})
}