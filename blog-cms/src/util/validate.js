/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/img
 * @param {string} fileName
 * @returns {Boolean}
 */
export function isImgExt(fileName) {
  return /\.(apng|avif|bmp|gif|ico|cur|jpg|jpeg|jfif|pjpeg|pjp|png|svg|tif|tiff|webp)$/i.test(fileName)
}