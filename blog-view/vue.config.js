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
      },
      // 把 optimization 放到 configureWebpack 里面
      optimization: {
        splitChunks: {
          cacheGroups: {
            vendor: {
              test: /[\\/]node_modules[\\/]/,
              name(module) {
                const packageName = module.context.match(
                  /[\\/]node_modules[\\/](.*?)([\\/]|$)/
                )[1];
                return `npm.${packageName.replace("@", "")}`;
              },
              chunks: "all",
              enforce: true,
              priority: 10,
              minSize: 50000,
              maxSize: 200000,
              reuseExistingChunk: true,
            },
          },
        }
      }
    }
  }