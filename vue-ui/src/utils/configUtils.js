/**
 * Copyright 2015, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

let defaultConfig = {
    // TODO: these should be changed tp relative paths, without /mapstore/ or / (to avoid the needing of overriding in default cases)
    proxyUrl: "/mapstore/proxy/?url=",
    geoStoreUrl: "/rest/geostore/",
    printUrl: "/mapstore/print/info.json",
    translationsPath: "translations",
    extensionsRegistry: "extensions/extensions.json",
    extensionsFolder: "extensions/",
    configurationFolder: "configs/",
    contextPluginsConfiguration: "configs/pluginsConfig.json",
    projectionDefs: [],
    themePrefix: "ms2",
    bingApiKey: null,
    mapquestApiKey: null,
    defaultSourceType: "gxp_wmssource",
    backgroundGroup: "background",
    userSessions: {
        enabled: false
    }
};

export const getConfigProp = function(prop) {
    return defaultConfig[prop];
};
export const setConfigProp = function(prop, value) {
    defaultConfig[prop] = value;
};
export const removeConfigProp = function(prop) {
    delete defaultConfig[prop];
};

const ConfigUtils = {
    getConfigProp,
    setConfigProp,
    removeConfigProp
};

export default ConfigUtils;
