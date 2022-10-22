<template>
	<div id="app">
<!--		<router-view/>-->
    <router-view v-slot="{Component}">
      <component :is="Component"/>
    </router-view>
	</div>
</template>

<script>
	export default {
		name: 'app'
	}
</script>

<style>

</style>
