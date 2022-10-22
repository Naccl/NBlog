import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//自定义css
import './assets/css/base.css'
//阿里icon
import './assets/css/icon/iconfont.css'
//typo.css
import "./assets/css/typo.css";
//semantic-ui
import 'semantic-ui-css/semantic.min.css'
//element-plus
import Element from 'element-plus'
import 'element-plus/theme-chalk/index.css'
//moment
import './util/dateTimeFormatUtils.js'
//v-viewer
import 'viewerjs/dist/viewer.css'
import Viewer from 'v-viewer'
//directive
import './util/directive'

console.log(
    '%c NBlog %c By Naccl %c https://github.com/Naccl/NBlog',
    'background:#35495e ; padding: 1px; border-radius: 3px 0 0 3px;  color: #fff',
    'background:#41b883 ; padding: 1px; border-radius: 0 3px 3px 0;  color: #000',
    'background:transparent'
)

const app = createApp(App,
    {
    render(){
        return h => h(App)
    }
});
app.use(Element);
app.use(Viewer);

app.config.globalProperties.msgSuccess = function (msg) {
    this.$message.success(msg)
}

app.config.globalProperties.msgError = function (msg) {
    this.$message.error(msg)
}

app.config.globalProperties.msgInfo = function (msg) {
    this.$message.info(msg);
}

const cubic = value => Math.pow(value, 3);
const easeInOutCubic = value => value < 0.5 ? cubic(value * 2) / 2 : 1 - cubic((1 - value) * 2) / 2;

//滚动至页面顶部，使用 Element-ui 回到顶部 组件中的算法
app.config.globalProperties.scrollToTop = function () {
    const el = document.documentElement
    const beginTime = Date.now()
    const beginValue = el.scrollTop
    const rAF = window.requestAnimationFrame || (func => setTimeout(func, 16))
    const frameFunc = () => {
        const progress = (Date.now() - beginTime) / 500;
        if (progress < 1) {
            el.scrollTop = beginValue * (1 - easeInOutCubic(progress))
            rAF(frameFunc)
        } else {
            el.scrollTop = 0
        }
    }
    rAF(frameFunc)
}

app.config.productionTip = false

// app.use(
//     Element,
//     Viewer,
//     router,
//     store
//     // ,npm
//     // render: h => h(App)
// );

app.use(router);
app.use(store);

app.mount('#app')
