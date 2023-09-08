/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
import axios from 'axios';
import urlUtil from 'url';
import assign from 'object-assign';
import WFS from "ol/format/wfs";
import Feature from "ol/feature";
import GeoJSON from "ol/format/geojson";
import {formatDate, getLikeSQL, getRequestAll} from "@/utils/searchUtils";

export const toDescribeURL = (url, typeName) => {
    const parsed = urlUtil.parse(url, true);
    return urlUtil.format(
        {
            ...parsed,
            search: undefined, // this allows to merge parameters correctly
            query: {
                ...parsed.query,
                service: "WFS",
                version: "1.1.0",
                typeName,
                outputFormat: 'application/json',
                request: "DescribeFeatureType"
            }
        });
};
/**
 * Simple getFeature using http GET method with json format
 */
export const getFeatureSimple = function(baseUrl, params) {
    return axios.get(baseUrl + 'geoserver/wfs?service=WFS&version=1.1.0&request=GetFeature', {
        params: assign({
            outputFormat: "application/json"
        }, params)
    }).then((response) => {
        if (typeof response.data !== 'object') {
            return JSON.parse(response.data);
        }
        return response.data;
    });
};

export const getCapabilitiesURL = (url, {version = "1.1.0"} = {}) => {
    const parsed = urlUtil.parse(url, true);
    return urlUtil.format(assign({}, parsed, {
        query: assign({
            service: "WFS",
            version,
            request: "GetCapabilities"
        }, parsed.query)
    }));
};

export const getFeatureURL = (url, typeName, { version = "1.1.0", ...params } = {}) => {
    const parsed = urlUtil.parse(url, true);
    return urlUtil.format(assign({}, parsed, {
        query: assign({
            service: "WFS",
            typeName,
            version,
            request: "GetFeature",
            ...params
        }, parsed.query)
    }));
};

export const getJsonFeatureURL = (url, typeName, params) => {
    axios.defaults.headers.post["Content-Type"] = "text/xml";
    let param = {
        pageNum: params.pageNum || 0,
        pageSize: params.pageSize || 20,
        name: params.name || '',
        crs: params.crs || 'EPSG:3857'
    };
    let xml = '<wfs:GetFeature xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" service="WFS" version="1.1.0" xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd" ' +
        '  startIndex="' + (param.pageNum - 1) * param.pageSize + '" maxFeatures="' + param.pageSize + '">' +
        '  <wfs:Query typeName="' + typeName + '" srsName="' + param.crs + '">' +
        '    <wfs:SortBy>' +
        '      <wfs:SortProperty>' +
        '        <ogc:PropertyName>' + param.name + '</ogc:PropertyName>' +
        '        <wfs:SortOrder>A</wfs:SortOrder>' +
        '      </wfs:SortProperty>' +
        '    </wfs:SortBy>' +
        '    <ogc:Filter>' +
        '      <ogc:And>undefined</ogc:And>' +
        '    </ogc:Filter>' +
        '  </wfs:Query>' +
        '</wfs:GetFeature>';
    return axios.post(url + '?outputFormat=json', xml).then((response) => {
        if (response.data instanceof Object) {
            return response.data;
        } else return undefined;
    })
}

export const getFeature = (url, typeName, params ) => {
    return axios.get(getFeatureURL(url+'geoserver/wfs', typeName, params));
};

export const getJsonFeature = (url, typeName, params ) => {
    return getJsonFeatureURL(url+'geoserver/wfs', typeName, params);
};

export const getCapabilities = function(url) {
    return axios.get(getCapabilitiesURL(url+'geoserver/wfs'));
};

export const describeFeatureType = function(url, typeName) {
    return axios.get(toDescribeURL(url+'geoserver/wfs', typeName)).then(({data}) => data);
};


const postResult = (params) => {
  // 1、构造Feature
  let ft = new GeoJSON().readFeature(params.geoJson);
  ft.setId(params.row.id)
  let options = {
    featureNS: params.typeName.split(":")[0], //工作空间名称
    featureType: params.typeName.split(":")[1], //图层名称
    srsName: "EPSG:3857"
  };
  delete params.row._X_ROW_KEY
  delete params.row.id
  ft.setProperties(params.row);
  let geom = ft.getGeometry()
  // 避免出现报错PointOutsideEnvelopeException: 1 outside of (-90.0,90
  // 进行经纬度调换
  geom.applyTransform((flatCoordinates, flatCoordinates2, stride) => {
    for (let j = 0; j < flatCoordinates.length; j += stride) {
      let y = flatCoordinates[j]
      let x = flatCoordinates[j + 1]
      flatCoordinates[j] = x
      flatCoordinates[j + 1] = y
    }
  })
  ft.setGeometryName(params.geomField)
  ft.setGeometry(geom)
  return {
    ft:ft,
    options:options
  }
}

export const addFeatureByWfs = function (url, typeName, fields, row){
  const result = postResult(typeName, fields, row)
  // 2、更新到后台
  let featObject = new WFS().writeTransaction([result.ft],
    null, null, result.options )
  let serializer = new XMLSerializer()
  let featString = serializer.serializeToString(featObject);
  return axios.post(url + 'geoserver/wfs?',
    featString,
    {
      headers: {
        //根据接扣文档需要加的请求头
        "Content-Type": "application/xml"
      }
    })
}

export const updateFeatureByWfs = function (url, params){
  const result = postResult(params)
  // 2、更新到后台
  let featObject = new WFS().writeTransaction(null,
    [result.ft], null, result.options )
  let serializer = new XMLSerializer()
  let featString = serializer.serializeToString(featObject);
  return axios.post(url + 'geoserver/wfs?',
    featString,
    {
      headers: {
        //根据接扣文档需要加的请求头
        "Content-Type": "application/xml"
      }
    })
}

export const deleteFeatureByWfs = function (url, typeName, fields, row){
  const result = postResult(typeName, fields, row)
  // 2、更新到后台
  let featObject = new WFS().writeTransaction(null,
    null, [result.ft], result.options )
  let serializer = new XMLSerializer()
  let featString = serializer.serializeToString(featObject);
  return axios.post(url + 'geoserver/wfs?',
    featString,
    {
      headers: {
        //根据接扣文档需要加的请求头
        "Content-Type": "application/xml"
      }
    })
}


