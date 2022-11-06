import axios from "axios";

export const getWmsMap = p => {
  return axios.get('geotools/wms', {params:p})
};
