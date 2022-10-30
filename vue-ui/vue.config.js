module.exports = {
  //设置服务器访问和端口
  /* devServer: {
     disableHostCheck: true,
     host: "0.0.0.0",
     port: 8008,
     https: false,
     hotOnly: false,
     proxy: null
   },*/
  //自定义打包入口
  chainWebpack: config => {
    //发布模式
    config.when(process.env.NODE_ENV === 'production', config => {
      config.entry('app').clear().add('./src/main-prod.js')
      //加载外部CDN资源
      config.set('externals', {
        vue: 'Vue',
        'vue-router': 'VueRouter',
        'ol': 'ol',
        axios: 'axios',
        lodash: '_',
        echarts: 'echarts',
        nprogress: 'NProgress'
      })
      //发布模式导入cdn资源
      config.plugin('html').tap(args => {
        args[0].isProd = true
        return args
      })
    })

    //开发模式
    config.when(process.env.NODE_ENV === 'development', config => {
      config.entry('app').clear().add('./src/main-dev.js')
      //开发模式不导入cdn资源
      config.plugin('html').tap(args => {
        args[0].isProd = false
        return args
      })
    })
  }
};
