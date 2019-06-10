// pages/boot/boot.js
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    guid: ''
  },

  generateGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      let r = Math.random() * 16 | 0
      let v = c === 'x' ? r : (r & 0x3 | 0x8)
      return v.toString(16)
    })
  },

  // 指纹识别
  fingerprint() {
    /******** 指纹识别start ********/
    // 检测当前微信版本是否支持指纹识别
    if (wx.canIUse('checkIsSupportSoterAuthentication')) {
      // 判断当前设备是否可以调用指纹识别
      wx.checkIsSupportSoterAuthentication({
        success: res => {
          if (res.supportMode.indexOf('fingerPrint') > -1) {
            // 支持指纹识别
            console.log('当前设备支持指纹识别')
            // 检测当前设备是否录有指纹
            wx.checkIsSoterEnrolledInDevice({
              checkAuthMode: 'fingerPrint',
              success: res => {
                if (res.isEnrolled == 1) {
                  wx.startSoterAuthentication({
                    requestAuthModes: ['fingerPrint'],
                    challenge: this.data.guid,
                    authContent: '请验证已有的指纹以继续',
                    success: res => {
                      console.log("指纹识别验证成功", res)
                      wx.navigateTo({
                        url: '../index/index',
                      })
                    },
                    fail: res => {
                      console.log("指纹识别识别失败", res)
                      wx.showModal({
                        title: '提示',
                        content: '指纹识别识别失败，无法进入小程序！',
                        showCancel: false,
                      })
                    }
                  })
                } else if (res.isEnrolled == 0) {
                  console.log('当前设备支持指纹识别，但是没有录入指纹！')
                  wx.showModal({
                    title: '提示',
                    content: '当前设备支持指纹识别，但是没有录入指纹！无法进入小程序！',
                    showCancel: false,
                  })
                }
              },
              fail: res => {
                console.log('检测当前设备是否录有指纹失败！')
                wx.showModal({
                  title: '提示',
                  content: '检测当前设备是否录有指纹失败！无法进入小程序！',
                  showCancel: false,
                })
              }
            })
          } else {
            console.log('当前设备不支持指纹识别')
            wx.showModal({
              title: '提示',
              content: '当前设备不支持指纹识别！无法进入小程序！',
              showCancel: false,
            })
          }
        },
        fail: res => {
          console.log('检测当前设备是否支持生物认证失败！')
          wx.showModal({
            title: '提示',
            content: '检测当前设备是否支持生物认证失败！无法进入小程序！',
            showCancel: false,
          })
        }
      })
    } else {
      wx.showModal({
        title: '提示',
        content: '您当前的微信版本太低，不支持指纹识别，请注意升级！',
        showCancel: false,
      })
    }
    /******** 指纹识别end ********/
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    if (!this.data.guid) {
      this.setData({
        guid: this.generateGuid()
      })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function(options) {
    if (app.globalData.verification) {
      app.globalData.verification = false
      return
    } else {
      app.globalData.verification = true
      this.fingerprint()
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {}
})