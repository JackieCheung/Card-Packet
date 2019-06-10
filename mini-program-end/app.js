//app.js
const config = require('./libs/config.js'); //自定义的配置文件
App({
  // 微信登录异步函数
  wxLogin() {
    return new Promise((resolve, reject) => {
      // 登录
      wx.login({
        success: res => {
          const code = res.code || '' // 用户登录临时凭证code，有效期五分钟
          // 发送 res.code 到后台换取 openid, session_key, unionid
          wx.request({
            url: config.config.baseUrl + '/wx/oauth',
            method: 'post',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              code: code
            },
            success: res => {
              if (res.data.success) {
                this.globalData.userInfo = res.data.content
                this.getWxUserInfo(this.globalData.userInfo.id).then(res => {
                  resolve(this.globalData.userInfo)
                }).catch(error => {
                  resolve(this.globalData.userInfo)
                })
              } else {
                wx.showModal({
                  title: '提示',
                  content: res.data.msg,
                  showCancel: false,
                })
                resolve(res)
              }
            },
            fail: res => {
              wx.showModal({
                title: '提示',
                content: '获取用户openid失败！' + res.errMsg + '！',
                showCancel: false,
              })
              reject(res)
            }
          })
        },
        fail: res => {
          wx.showModal({
            title: '提示',
            content: '获取用户临时登录凭证失败！' + res.errMsg + '！',
            showCancel: false,
          })
          reject(res)
        }
      })
    })
  },
  // 获取微信用户信息异步函数
  getWxUserInfo(userId) {
    return new Promise((resolve, reject) => {
      wx.getSetting({
        success: res => {
          if (res.authSetting['scope.userInfo']) {
            // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
            wx.getUserInfo({
              success: res => {
                // 可以将 res 发送给后台解码出 unionId 和 用户敏感信息
                // 发送获取到的用户信息到后端，更新用户信息
                wx.request({
                  url: config.config.baseUrl + '/wx/updateUserInfo' + '/' + userId,
                  method: 'post',
                  data: res.userInfo,
                  success: res => {
                    if (res.data.success) {
                      this.globalData.userInfo = res.data.content
                      if (this.userInfoReadyCallback) {
                        // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                        // 所以此处加入 callback 以防止这种情况
                        this.userInfoReadyCallback(this.globalData.userInfo)
                      }
                      resolve(this.globalData.userInfo)
                    } else {
                      wx.showModal({
                        title: '提示',
                        content: res.data.msg,
                        showCancel: false,
                      })
                      resolve(res)
                    }
                  },
                  fail: res => {
                    wx.showModal({
                      title: '提示',
                      content: '更新用户信息失败！' + res.errMsg + '！',
                      showCancel: false,
                    })
                    reject(res)
                  }
                })
                // // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                // // 所以此处加入 callback 以防止这种情况
                // if (this.userInfoReadyCallback) {
                //   this.userInfoReadyCallback(res)
                // }
              },
              fail: res => {
                wx.showModal({
                  title: '提示',
                  content: '获取用户信息失败！' + res.errMsg + '！',
                  showCancel: false,
                })
                reject(res)
              }
            })
          } else {
            resolve(res)
          }
        },
        fail: res => {
          wx.showModal({
            title: '提示',
            content: '获取用户信息失败！' + res.errMsg + '！',
            showCancel: false,
          })
          reject(res)
        }
      })
    })
  },
  // onShow: function () {
  //   this.globalData.verification = false
  // },
  onLaunch() {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    this.wxLogin().then().catch(error => {
      console.log(error)
    })
  },
  globalData: {
    userInfo: null,
    verification: false // 指纹识别开关
  }
})