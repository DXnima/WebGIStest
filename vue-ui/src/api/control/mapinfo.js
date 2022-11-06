import axios from "axios";

// 获取所有工作空间
export const getWorkspaceNames = () => {
  return axios.get('mapinfo/workspacenames')
};

// 获取所有数据存储
export const getDatastores = () => {
  return axios.get('mapinfo/datastores')
};

// 获取所有图层名称和标题
export const getLayersInfo = () => {
  return axios.get('mapinfo/layers')
};

// 获取指定图层信息
export const getLayerInfo = p => {
  return axios.get('mapinfo/layer', {params:p})
};

// 获取所有样式名称
export const getStyles1 = () => {
  return axios.get('mapinfo/styles/1')
};

// 指定工作空间获取所有样式名称
export const getStyles2 = p => {
  return axios.get('mapinfo/styles/2', {params:p})
};

// 获取指定样式的SLD
export const getSLD = p => {
  return axios.get('mapinfo/sld', {params:p})
};
