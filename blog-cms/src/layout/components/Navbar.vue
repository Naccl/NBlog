<template>
	<div class="navbar">
		<hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>

		<breadcrumb class="breadcrumb-container"/>

		<div class="right-menu">
			<el-dropdown class="avatar-container" trigger="click">
				<div class="avatar-wrapper">
					<img :src="user.avatar" class="user-avatar">
				</div>
				<el-dropdown-menu slot="dropdown" class="user-dropdown">
					<a target="_blank" href="https://github.com/Naccl/NBlog">
						<el-dropdown-item>
							<SvgIcon icon-class="github" class-name="svg"/>
							<span>GitHub</span>
						</el-dropdown-item>
					</a>
					<el-dropdown-item @click.native="logout">
						<SvgIcon icon-class="logout" class-name="svg"/>
						<span>退出</span>
					</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
	</div>
</template>

<script>
	import {mapGetters} from 'vuex'
	import Breadcrumb from '@/components/Breadcrumb'
	import Hamburger from '@/components/Hamburger'
	import SvgIcon from '@/components/SvgIcon'

	export default {
		components: {
			Breadcrumb,
			Hamburger,
			SvgIcon
		},
		data() {
			return {
				user: null,
			}
		},
		computed: {
			...mapGetters([
				'sidebar',
			])
		},
		created() {
			this.getUserInfo()
		},
		methods: {
			toggleSideBar() {
				this.$store.dispatch('app/toggleSideBar')
			},
			getUserInfo() {
				this.user = JSON.parse(window.localStorage.getItem('user') || null)
				if (!this.user) {
					this.$router.push('/login')
				}
			},
			logout() {
				window.localStorage.removeItem('token')
				window.localStorage.removeItem('user')
				this.$router.push('/login')
				this.msgSuccess('退出成功')
			}
		}
	}
</script>

<style lang="scss" scoped>
	.navbar {
		height: 50px;
		overflow: hidden;
		position: relative;
		background: #fff;
		box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
		user-select: none;

		.hamburger-container {
			line-height: 46px;
			height: 100%;
			float: left;
			cursor: pointer;
			transition: background .3s;
			-webkit-tap-highlight-color: transparent;

			&:hover {
				background: rgba(0, 0, 0, .025)
			}
		}

		.breadcrumb-container {
			float: left;
		}

		.right-menu {
			float: right;
			height: 100%;
			line-height: 50px;

			&:focus {
				outline: none;
			}

			.right-menu-item {
				display: inline-block;
				padding: 0 8px;
				height: 100%;
				font-size: 18px;
				color: #5a5e66;
				vertical-align: text-bottom;

				&.hover-effect {
					cursor: pointer;
					transition: background .3s;

					&:hover {
						background: rgba(0, 0, 0, .025)
					}
				}
			}

			.avatar-container {
				margin-right: 20px;

				.avatar-wrapper {
					margin-top: 5px;
					position: relative;

					.user-avatar {
						cursor: pointer;
						width: 40px;
						height: 40px;
						border-radius: 10px;
					}

					.el-icon-caret-bottom {
						cursor: pointer;
						position: absolute;
						right: -20px;
						top: 0px;
						font-size: 12px;
					}
				}
			}
		}
	}

	.user-dropdown .svg {
		margin-right: 5px;
	}

	.el-dropdown-menu {
		margin: 7px 0 0 0 !important;
		padding: 0 !important;
		border: 0 !important;
	}
</style>
