// pages/nearby-map/nearby-map.js
const amapFile = require('../../libs/amap-wx.js'); //引入高德地图js
const config = require('../../libs/config.js'); //自定义的配置文件
Page({

  /**
   * 页面的初始数据
   */
  data: {
    longitude: '', // 经度
    latitude: '', // 纬度
    markers: '', // 标记点
    scale: 14, // 缩放级别，取值范围为5-18
    showLocation: true, // 显示带有方向的当前定位点
    includePoints: true, // 缩放视野以包含所有给定的坐标点
    enable3D: true, // 展示3D楼块(工具暂不支持）
    showCompass: true, // 显示指南针
    enableOverlooking: true, // 开启俯视
    enableZoom: true, // 是否支持缩放
    enableScroll: true, // 是否支持拖动
    enableRotate: true // 是否支持旋转
  },
  // // 点击标记点时触发，会返回marker的id
  // handleMarkerTap(e) {
  //   let marker = this.data.markers.filter(m => {
  //     return m.id === e.markerId
  //   })
  //   if (marker.length > 0) {
  //     wx.openLocation({
  //       ...marker[0],
  //       ...{
  //         scale: 18,
  //         success: res => {},
  //         fail: res => {}
  //       }
  //     })
  //   }
  // },

  // 点击标记点对应的气泡时触发，会返回marker的id
  handleCalloutTap(e) {
    let marker = this.data.markers.filter(m => {
      return m.id === e.markerId
    })
    if (marker.length > 0) {
      wx.openLocation({
        ...marker[0],
        ...{
          scale: 18,
          success: res => {},
          fail: res => {}
        }
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    wx.getLocation({
      type: 'gcj02',
      success: res => {
        this.setData({
          latitude: res.latitude,
          longitude: res.longitude
        })
        let myAmapFun = new amapFile.AMapWX({
          key: config.config.amap.APIKey
        })
        myAmapFun.getPoiAround({
          // iconPath: '../../images/loc.png',
          // iconPathSelected: '../../images/loc-selected.png',
          location: this.data.longitude && this.data.latitude ? this.data.longitude + ',' + this.data.latitude : '',
          querykeywords: options.keyword,
          success: data => {
            //成功回调
            let markers = [...data.markers].map(m => {
              m.width = ''
              m.height = ''
              return m
            })
            this.setData({
              markers: markers
            })
          },
          fail: info => {
            wx.showModal({
              title: '提示',
              content: '获取附近网点失败！',
              showCancel: false
            })
          }
        })
      },
      fail: res => {
        wx.showModal({
          title: '获取当前地理位置失败',
          content: '请先授权小程序获取您的地理位置！',
          showCancel: false,
          complete: res => {
            wx.navigateBack({
              delta: 1
            })
          }
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})