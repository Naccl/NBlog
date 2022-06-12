module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        'assets': '@/assets',
        'common': '@/common',
        'components': '@/components',
        'api': '@/api',
        'views': '@/views',
        'plugins': '@/plugins'
      }
    }
  },
//   [Vue warn]: Failed to resolve component: meting-js
// If this is a native custom element, make sure to exclude it from component resolution via compilerOptions.isCustomElement.
//     at <MyAPlayer>
// at <BlogIndex onVnodeUnmounted=fn<onVnodeUnmounted> ref=Ref< undefined > >
//     at <RouterView>
//     at <App render=fn<render> >
  chainWebpack: config => {
    config.module
        .rule('vue')
        .use('vue-loader')
        .tap(options => {
          options['compilerOptions'] = {
            ...options.compilerOptions || {},
            isCustomElement: tag => tag === 'meting-js'
          };
          return options;
        })
  },
  lintOnSave:false,//关闭eslintre语法检查
}