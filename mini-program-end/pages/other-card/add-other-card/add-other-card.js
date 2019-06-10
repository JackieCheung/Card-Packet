// pages/other-card/add-other-card/add-other-card.js
const config = require('../../../libs/config.js'); //自定义的配置文件
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    route: '',
    config: config.config,
    originbalance: '',
    cardInfo: {
      userId: '',
      name: '',
      org: '',
      type: 'OTHER',
      remark: '',
      number: '',
      balance: '0.00'
    },
    originCardInfo: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(route) {
    this.setData({
      route: route
    })
    // 查看卡详情
    if (route.id) {
      wx.showLoading({
        title: '加载中',
        mask: true
      })
      // 获取银行卡详情信息
      wx.request({
        url: config.config.baseUrl + '/card/detail' + '/' + route.id,
        success: res => {
          if (res.data.success) {
            this.setData({
              cardInfo: res.data.content,
              originCardInfo: {...res.data.content},
              originbalance: res.data.content.balance
            })
            wx.setNavigationBarTitle({
              title: res.data.content.org + '详情'
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
            content: '获取卡详情信息失败！' + res.errMsg + '！',
            showCancel: false,
          })
        }
      })
    } else if (route.type) {
      this.setData({
        ['cardInfo.org']: route.type
      })
      wx.setNavigationBarTitle({
        title: '添加' + route.type
      })
    }
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
    setTimeout(() => {
      this.refreshPage()
      wx.stopPullDownRefresh()
    }, 1500)
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

  },

  generateGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      let r = Math.random() * 16 | 0
      let v = c === 'x' ? r : (r & 0x3 | 0x8)
      return v.toString(16)
    })
  },

  /**
   * 保存其他类型卡信息
   */
  saveOtherCard(e) {
    if (!this.data.cardInfo.name) {
      wx.showModal({
        title: '提示',
        content: '名称不能为空！',
        showCancel: false,
      })
    } else {
      wx.showLoading({
        title: '加载中',
        mask: true
      })
      app.wxLogin().then(res => {
        if (res.id) {
          // 处理卡信息
          let cardInfo = {
            ...this.data.cardInfo
          }
          cardInfo.userId = res.id
          cardInfo.number = this.generateGuid()
          wx.request({
            url: config.config.baseUrl + '/card/save',
            method: 'post',
            data: cardInfo,
            success: res => {
              if (res.data.success) {
                this.setData({
                  cardInfo: res.data.content,
                  originCardInfo: {...res.data.content}
                })
                wx.showModal({
                  title: '提示',
                  content: !this.data.route.id ? '添加【' + res.data.content.org + ' - ' + res.data.content.name + '】成功！' : '修改卡信息成功！',
                  showCancel: false,
                  success: res => {
                    if (res.confirm) {
                      if (this.data.route.type) {
                        wx.showLoading({
                          title: '加载中',
                          mask: true
                        })
                        setTimeout(() => {
                          this.refreshPage()
                          wx.hideLoading()
                        }, 1500)
                      }
                    }
                  }
                })
              } else {
                wx.showModal({
                  title: '提示',
                  content: '添加' + this.data.route.type + '失败！' + res.data.msg,
                  showCancel: false,
                })
              }
              wx.hideLoading()
            },
            fail: res => {
              wx.hideLoading()
              wx.showModal({
                title: '提示',
                content: '添加' + this.data.route.type + '失败！' + res.errMsg + '！',
                showCancel: false,
              })
            }
          })
        } else {
          wx.hideLoading()
          wx.showModal({
            title: '提示',
            content: '添加' + this.data.route.type + '失败！用户id不能为空！',
            showCancel: false,
          })
        }
      }).catch(error => {
        wx.hideLoading()
        wx.showModal({
          title: '提示',
          content: '添加' + this.data.route.type + '失败！',
          showCancel: false,
        })
        console.log(error)
      })
    }
  },

  /**
   * 刷新当前页，将页面数据置为初始值
   */
  refreshPage() {       
    if (this.data.route.id && this.data.originCardInfo) {
      this.setData({
        originbalance: this.data.originCardInfo.balance,
        cardInfo: {...this.data.originCardInfo}
      })
    } else {
      this.setData({
        originbalance: '',
        cardInfo: {
          userId: '',
          name: '',
          org: this.data.route.type,
          type: 'OTHER',
          remark: '',
          balance: '0.00',
          number: ''
        }
      })
    }
  },

  // 输入名称回调事件
  handleNameChange(e) {
    this.setData({
      ['cardInfo.name']: e.detail.value
    })
  },

  // 输入余额回调事件
  handleBalanceChange(e) {
    this.setData({
      ['cardInfo.balance']: Number(e.detail.value).toFixed(2)
    })
  },

  // 输入备注回调事件
  handleRemarkChange(e) {
    this.setData({
      ['cardInfo.remark']: e.detail.value
    })
  }
})