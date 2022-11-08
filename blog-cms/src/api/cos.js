import COS from 'cos-js-sdk-v5';

// todo 该cos只初始化一次，待优化
// 未配置的情况下，点击腾讯云界面会报错, 请在配置后刷新页面
const txyunConfig = JSON.parse(localStorage.getItem('txyunConfig'))
console.log("cos配置:",txyunConfig)
const cos = new COS({
    SecretId: txyunConfig.secretId,
    SecretKey: txyunConfig.secretKey,
});

export default cos;