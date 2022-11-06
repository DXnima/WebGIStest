import axios from "axios";

// 创建工作空间
export const addWorkspace = p => {
  return axios.post('publish/workspace', p)
};

// 删除工作空间
export const delWorkspace = p => {
  return axios.delete('publish/workspace', {params:p})
};

// 发布样式
export const addStyle1 = p => {
  return axios.post('publish/style/1', p)
};

//发布指定类型的默认样式
export const addDefStyle = p => {
  return axios.post('publish/defaultstyle/1', p)
};

// 指定工作空间创建样式
export const addStyle2 = p => {
  return axios.post('publish/style/2', p)
};

// 指定工作空间创建默认样式
export const addDefStyle2 = p => {
  return axios.post('publish/defaultstyle/2', p)
};

// 修改样式
export const updateStyle = p => {
  return axios.put('publish/style', p)
};

// 删除样式
export const delStyle = p => {
  return axios.delete('publish/style', {params:p})
};

// 发布shp图层并自动创建样式
export const addShpLayer1 = (t, p) => {
  return axios.post(`publish/shplayer/${t}`, p)
};

// 发布shp图层
export const addShpLayer2 = p => {
  return axios.post('publish/shplayer2', p)
};

// 发布PostGIS图层并自动创建样式
export const addPostGISLayer1 = (t, p) => {
  return axios.post(`publish/postgislayer/${t}`, p)
};

// 发布PostGIS图层
export const addPostGISLayer2 = p => {
  return axios.post('publish/postgislayer2', p)
};

// 修改矢量数据存储编码格式
export const updateDataStore = p => {
  return axios.put('publish/datastore', p)
};

// 指定工作空间删除指定数据存储
export const delDataStore = p => {
  return axios.delete('publish/datastore', {params:p})
};

// 更新图层信息
export const updateLayer = p => {
  return axios.put('publish/layer', p)
};

// 指定图层名称删除图层
export const delLayer = p => {
  return axios.delete('publish/layer', {params:p})
};
