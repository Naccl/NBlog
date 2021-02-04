<template>
	<svg :class="`svg-icon ${className}`" aria-hidden="true">
		<use :xlink:href="iconClass"/>
	</svg>
</template>

<script>
	//参考阿里iconfont, assets/css/icon/demo_index.html Symbol
	export default {
		name: 'SvgIcon',
		props: {
			iconClass: {
				type: String,
				required: true
			},
			className: {
				type: String,
				default: ''
			}
		}
	}
</script>

<style scoped>
	.svg-icon {
		width: 1em;
		height: 1em;
		vertical-align: -0.15em;
		fill: currentColor;
		overflow: hidden;
	}
</style>