// pages/bank-card/bank-card-detail/bank-card-detail.js
const config = require('../../../libs/config.js'); //自定义的配置文件
Page({
  /**
   * 页面的初始数据
   */
  data: {
    showModal: false,
    config: config.config,
    bankTypeEnum: {
      'DEBIT': '借记卡',
      'CREDIT': '信用卡'
    },
    newPass: {
      value: '',
      error: ''
    },
    confirmPass: {
      value: '',
      error: ''
    },
    cardInfo: {},
    showPassword: false
  },

  getCardDetail(id) {
    if (id) {
      wx.showLoading({
        title: '加载中',
        mask: true
      })
      // 获取银行卡详情信息
      wx.request({
        url: config.config.baseUrl + '/card/detail' + '/' + id,
        success: res => {
          if (res.data.success) {
            this.setData({
              cardInfo: res.data.content
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
            content: '获取银行卡详情信息失败！' + res.errMsg + '！',
            showCancel: false,
          })
        }
      })
    } else {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '获取银行卡详情信息失败！银行卡id不能为空！',
        showCancel: false,
      })
    }
  },

  // 切换密码的显示和隐藏
  togglePasswordShow() {
    this.setData({
      showPassword: !this.data.showPassword
    })
  },

  // 点击复制到粘贴板
  copyNumber(e) {
    wx.setClipboardData({
      data: e.currentTarget.dataset.number,
      success: function(res) {
        wx.getClipboardData({
          success: function(res) {
            console.log('复制成功！')
          }
        })
      }
    })
  },

  // 查看附近网点
  getNearbyMap(e) {
    if (e.currentTarget.dataset.org) {
      wx.navigateTo({
        url: '../../nearby-map/nearby-map' + '?keyword=' + e.currentTarget.dataset.org,
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.getCardDetail(options.id)
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
  onPullDownRefresh: function() {},

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

  /**
   * 弹窗
   */
  showDialogBtn: function() {
    this.setData({
      showModal: true
    })
  },
  /**
   * 弹出框蒙层截断touchmove事件
   */
  preventTouchMove: function() {},
  /**
   * 隐藏模态对话框
   */
  hideModal: function() {
    this.setData({
      showModal: false
    });
  },
  /**
   * 对话框取消按钮点击事件
   */
  onCancel: function() {
    this.hideModal();
  },
  /**
   * 对话框确认按钮点击事件
   */
  onConfirm: function() {
    if (!this.data.newPass.error && !this.data.confirmPass.error) {
      wx.showLoading({
        title: '加载中',
        mask: true
      })
      // 修改密码
      wx.request({
        url: config.config.baseUrl + '/card/updatePassword' + '/' + this.data.cardInfo.id,
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
        data: {
          password: this.data.confirmPass.value
        },
        success: res => {
          if (res.data.success) {
            let result = res
            this.hideModal()
            wx.showModal({
              title: '提示',
              content: res.data.msg,
              showCancel: false,
              success: res => {
                if (res.confirm) {
                  wx.showLoading({
                    title: '加载中',
                    mask: true
                  })
                  setTimeout(() => {
                    this.setData({
                      cardInfo: result.data.content
                    })
                    wx.hideLoading()
                  }, 1200)
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
            content: '修改银行卡密码失败！' + res.errMsg + '！',
            showCancel: false,
          })
        }
      })
    }
  },
  /**
   * 改变新密码回调时间
   */
  changePass: function(event) {
    let value = event.detail.value;
    this.setData({
      'newPass.value': value
    })
    if (value === '') {
      this.setData({
        'newPass.error': '密码不能为空'
      })
    } else if (value.length < 6) {
      this.setData({
        'newPass.error': '密码长度须为六位'
      })
    } else {
      this.setData({
        'newPass.error': ''
      })
    }
    if (this.data.confirmPass.value.length > 0 && (this.data.newPass.value !== this.data.confirmPass.value)) {
      this.setData({
        'confirmPass.error': '两次输入的密码不一致，请重新输入'
      })
    }
  },
  /**
   * 改变确认密码回调时间
   */
  changeConfirmPass: function(event) {
    let value = event.detail.value;
    this.setData({
      'confirmPass.value': value
    })
    if (value === '') {
      this.setData({
        'confirmPass.error': '确认密码不能为空'
      })
    } else if (this.data.newPass.value !== this.data.confirmPass.value) {
      this.setData({
        'confirmPass.error': '两次输入的密码不一致，请重新输入'
      })
    } else {
      this.setData({
        'confirmPass.error': ''
      })
    }
  }
})