//index.js
//获取应用实例
const app = getApp()
const config = require('../../libs/config.js'); //自定义的配置文件

Page({
  data: {
    userInfo: {},
    config: config.config,
    bankTypeEnum: {
      'DEBIT': '借记卡',
      'CREDIT': '信用卡'
    },
    userId: '',
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    swiperOptions: {
      indicatorDots: true,
      indicatorColor: 'rgba(0, 0, 0, .3)',
      indicatorActiveColor: '#000000',
      autoPlay: false,
      current: 0,
      interval: 5000,
      duration: 500,
      circular: false,
      vertical: false,
      previousMargin: 20,
      nextMargin: 20,
      displayMultipleItems: 1,
      skipHiddenItemLayout: false
    },
    ownedCards: []
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function() {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          let userInfo = res.userInfo
          app.wxLogin().then(res => {
            if (res.id) {
              wx.request({
                url: config.config.baseUrl + '/wx/updateUserInfo' + '/' + res.id,
                method: 'post',
                data: userInfo,
                success: res => {
                  if (res.data.success) {
                    app.globalData.userInfo = res.data.content
                    this.setData({
                      userInfo: res.data.content,
                      hasUserInfo: true
                    })
                  } else {
                    wx.showModal({
                      title: '提示',
                      content: res.data.msg,
                      showCancel: false,
                    })
                  }
                },
                fail: res => {
                  wx.showModal({
                    title: '提示',
                    content: '更新用户信息失败！' + res.errMsg + '！',
                    showCancel: false,
                  })
                }
              })
            }
          }).catch(error => {
            wx.showModal({
              title: '提示',
              content: '获取用户信息失败！',
              showCancel: false,
            })
          })
        },
        fail: res => {
          wx.showModal({
            title: '提示',
            content: '获取用户信息失败！' + res.errMsg + '！',
            showCancel: false,
          })
        }
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
  onShow: function() {
    app.globalData.verification = false
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    this.setData({
      ownedCards: []
    })
    app.wxLogin().then(res => {
      if (res && res.id) {
        this.setData({
          userId: res.id
        })
        // 获取用户持有的卡列表
        wx.request({
          url: config.config.baseUrl + 'card/all',
          data: {
            userId: res.id
          },
          success: res => {
            if (res.data.success) {
              this.setData({
                ownedCards: res.data.content
              })
            } else {
              wx.showModal({
                title: '提示',
                content: res.data.msg,
                showCancel: false,
              })
            }
            wx.hideLoading()
          },
          fail: res => {
            wx.hideLoading()
            wx.showModal({
              title: '提示',
              content: '获取卡列表失败！' + res.errMsg + '！',
              showCancel: false,
            })
          }
        })
      } else {
        wx.hideLoading()
        wx.showModal({
          title: '提示',
          content: '获取用户信息失败！',
          showCancel: false,
        })
      }
    }).catch(error => {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '获取用户信息失败！',
        showCancel: false,
      })
    })
  },

  // 获取用户信息
  getUserInfo: function(e) {
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    app.wxLogin().then(res => {
      if (res && res.id) {
        this.setData({
          userInfo: res,
          hasUserInfo: true
        })
      } else {
        wx.showModal({
          title: '提示',
          content: '获取用户信息失败！',
          showCancel: false,
        })
      }
      wx.hideLoading()
    }).catch(error => {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '获取用户信息失败！',
        showCancel: false,
      })
      console.log(error)
    })
  },
  // 查看卡详情
  getCardDetail(e) {
    if (e.currentTarget.dataset.detail) {
      if (e.currentTarget.dataset.detail.type === 'DEBIT' || e.currentTarget.dataset.detail.type === 'CREDIT') {
        // 查看银行卡详情
        wx.navigateTo({
          url: '../bank-card/bank-card-detail/bank-card-detail' + '?id=' + e.currentTarget.dataset.detail.id,
        })
      } else if (e.currentTarget.dataset.detail.type === 'OTHER') {
        // 查看其他卡详情
        wx.navigateTo({
          url: '../other-card/add-other-card/add-other-card' + '?id=' + e.currentTarget.dataset.detail.id,
        })
      }
    }
  },
  // 长按删除卡
  deleteCard(e) {
    if (e.currentTarget.dataset.detail) {
      wx.showModal({
        title: '提示',
        content: '确定要删除该卡吗？',
        success: res => {
          if (res.confirm) {
            let cardId = e.currentTarget.dataset.detail.id
            if (cardId) {
              wx.showLoading({
                title: '加载中',
                mask: true
              })
              wx.request({
                url: config.config.baseUrl + 'card/delete',
                method: 'post',
                header: {
                  'content-type': 'application/x-www-form-urlencoded'
                },
                data: {
                  'ids[]': [cardId]
                },
                success: res => {
                  if (res.data.success) {
                    wx.showModal({
                      title: '提示',
                      content: '删除所选卡成功！',
                      showCancel: false,
                      success: res => {
                        if (res.confirm) {
                          // 获取用户持有的卡列表
                          wx.request({
                            url: config.config.baseUrl + 'card/all',
                            data: {
                              userId: this.data.userId
                            },
                            success: res => {
                              if (res.data.success) {
                                this.setData({
                                  ownedCards: res.data.content,
                                  'swiperOptions.current': 0
                                })
                              } else {
                                wx.showModal({
                                  title: '提示',
                                  content: res.data.msg,
                                  showCancel: false,
                                })
                              }
                              wx.hideLoading()
                            },
                            fail: res => {
                              wx.hideLoading()
                              wx.showModal({
                                title: '提示',
                                content: '获取卡列表失败！' + res.errMsg + '！',
                                showCancel: false,
                              })
                            }
                          })
                        }
                      }
                    })
                  } else {
                    wx.showModal({
                      title: '提示',
                      content: res.data.msg,
                      showCancel: false,
                    })
                  }
                  wx.hideLoading()
                },
                fail: res => {
                  wx.hideLoading()
                  wx.showModal({
                    title: '提示',
                    content: '删除所选卡失败！' + res.errMsg + '！',
                    showCancel: false,
                  })
                }
              })
            } else {
              wx.showModal({
                title: '提示',
                content: '删除所选卡失败！卡id不能为空！',
                showCancel: false,
              })
            }
          }
        }
      })
    }
  },
  // 点击添加银行卡按钮
  addBankCard(e) {
    wx.navigateTo({
      url: '../bank-card/add-bank-card/add-bank-card',
    })
  },
  // 点击添加其他卡按钮
  addOtherCard(e) {
    wx.navigateTo({
      url: '../other-card/other-card',
    })
  }
})