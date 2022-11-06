// 创建工作空间
import axios from "axios";

//Geoserver配置服务的url,用户名，密码
export const addWorkspace = p => {
  return axios.post('config/geoserver', p)
};

export const getTableInfo = p => {
  return axios.post('config/postgis', p)
};
