import axios from "axios";

// 向capital表插入数据
export const insertCapital = p => {
  return axios.post('capital/insert', p)
};

// 查询capital表中所有数据
export const getCapital = () => {
  return axios.get('capital/all')
};


