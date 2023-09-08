import axios from "axios";

// 按名称查询
export const getLayerEdit = p => {
  return axios.get('layer_edit/query', {params:p})
};

// 添加新数据
export const addLayerEdit = p => {
  return axios.get('layer_edit/add', {params:p})
};

// 更新数据
export const updateLayerEdit = p => {
  return axios.post('layer_edit/update', p)
};
