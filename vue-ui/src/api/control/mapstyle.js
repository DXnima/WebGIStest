import axios from "axios";

// 指定样式名称修改
export const changeStyleByStyleName = (n, p) => {
  return axios.post(`style/1/${n}`, p)
};

// 修改指定图层的样式
export const changeStyleByLayerName = (n, p) => {
  return axios.post(`style/2/${n}`, p)
};

// 根据样式名称获取样式参数
export const getStyleValueByStyleName = p => {
  return axios.get(`style/getvalue/1`, {params:p})
};

// 根据图层获取样式参数
export const getStyleValueByLayerName = p => {
  return axios.get(`style/getvalue/2`, {params:p})
};

// 指定样式更改为默认样式
export const changeDefaultStyle = p => {
  return axios.post(`style/default`, p)
};
