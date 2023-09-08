import axios from "axios";

// 获取表名
export const getTablesInfo = () => {
  return axios.get('pgedit/table')
};

// 获取指定表数据
export const getTableData = p => {
  return axios.post('pgedit/data', p)
};

// 查询指定表数据
export const findTableData = p => {
  return axios.post('pgedit/find', p)
};

// 添加表数据
export const addTableData = p => {
  return axios.post('pgedit/add', p)
};

// 更新表数据
export const updateTableData = p => {
  return axios.post('pgedit/update', p)
};

// 删除表数据
export const delTableData = p => {
  return axios.post('pgedit/delete', p)
};
