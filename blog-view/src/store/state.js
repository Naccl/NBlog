export default {
	siteInfo: '',
	introduction: {
		avatar: '',
		name: '',
		rollText: [],
	},
	commentQuery: {
		page: 0,
		blogId: null,
		pageNum: 1,
		pageSize: 5
	},
	commentCount: 0,
	commentTotalPage: 0,
	comments: [],
	parentCommentId: -1,
	commentForm: {
		content: '',
		nickname: '',
		email: '',
		website: '',
		notice: true
	},
	//博客文章渲染完成的标记
	isBlogRenderComplete: false,
	//受保护文章密码对话框
	blogPasswordDialogVisible: false,
	blogPasswordForm: {
		blogId: 0,
		password: ''
	},
	//专注模式
	focusMode: false,
	//文章页面路由到首页的标记
	isBlogToHome: false,
	//可视窗口大小
	clientSize: {
		clientHeight: 0,
		clientWidth: 0
	}
}