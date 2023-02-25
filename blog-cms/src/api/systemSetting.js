import axios from '@/util/request'

export function getSystemSettingData() {
    return axios({
        url: 'systemSettings',
        method: 'GET'
    })
}

export function update(settings) {
    return axios({
        url: 'systemSettings',
        method: 'POST',
        data: [
            ...settings
        ]
    })
}
