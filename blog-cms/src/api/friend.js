import axios from '@/plugins/axios'

export function getFriendsByQuery(queryInfo) {
	return axios({
		url: 'friends',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function updatePublished(id, published) {
	return axios({
		url: 'friend/published',
		method: 'PUT',
		params: {
			id,
			published
		}
	})
}

export function saveFriend(form) {
	return axios({
		url: 'friend',
		method: 'POST',
		data: {
			...form
		}
	})
}

export function updateFriend(form) {
	return axios({
		url: 'friend',
		method: 'PUT',
		data: {
			...form
		}
	})
}

export function deleteFriendById(id) {
	return axios({
		url: 'friend',
		method: 'DELETE',
		params: {
			id
		}
	})
}