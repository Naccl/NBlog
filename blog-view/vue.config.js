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
	optimization: {
		splitChunks: {
			cacheGroups: {
				vendor: {
					test: /[\\/]node_modules[\\/]/,
					name(module) {
						// get the name. E.g. node_modules/packageName/not/this/part.js
						// or node_modules/packageName
						const packageName = module.context.match(
							/[\\/]node_modules[\\/](.*?)([\\/]|$)/
						)[1];
						// npm package names are URL-safe, but some servers don't like @ symbols
						return `npm.${packageName.replace("@", "")}`;
					},
					chunks: "all",
					enforce: true,
					priority: 10,
					minSize: 50000, // 50KB
					maxSize: 200000,
					reuseExistingChunk: true,
				},
			},
		},
	},
}