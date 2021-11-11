/**
 * 通过生成DOM节点来复制内容至剪贴板
 * @param {string} 需要复制的内容
 */
export function copy(copyCont) {
	let oInput = document.createElement('input')
	oInput.value = copyCont
	document.body.appendChild(oInput)
	oInput.select()
	document.execCommand('Copy')
	oInput.remove()
}