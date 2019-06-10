// pages/add-card/add-card.js
const config = require('../../../libs/config.js'); //自定义的配置文件
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    BANK_CARD_TYPE: {
      '0': '不能识别', // 默认：'不能识别'
      '1': '借记卡',
      '2': '信用卡'
    },
    BANK_CARD_TYPE_ENUM: {
      '不能识别': 'UNRECOGNIZABLE',
      '借记卡': 'DEBIT',
      '信用卡': 'CREDIT'
    },
    password: {
      errMsg: ''
    },
    accessToken: '', // 请求百度AIP的API服务所需要的access_token
    imageFilePath: '', // 图片的src
    canvasWidth: 300,
    canvasHeight: 200,
    cardInfo: {
      userId: '',
      number: '',
      type: '',
      org: '',
      validDate: '',
      password: '',
      imageUrl: ''
    } // 银行卡信息
  },
  // 选取照片
  takePicture(e) {
    // 从本地相册选择图片或使用相机拍照
    wx.chooseImage({
      count: 1, // 最多可以选择的图片张数，默认9
      sizeType: ['original', 'compressed'], // original 原图，compressed 压缩图，默认二者都有
      sourceType: ['album', 'camera'], // album 从相册选图，camera 使用相机，默认二者都有
      success: res => {
        wx.showLoading({
          title: '识别图片中',
          mask: true
        })
        const tempFiles = res.tempFiles
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePath = res.tempFilePaths[0]
        const tempFile = res.tempFiles[0]
        this.setData({
          imageFilePath: tempFilePath
          // 文件的临时途径，在小程序本次启用期间可以正常使用，
          // 如需持久保存，需再主动调用wx.saveFile，在下次小程序启动时才能访问得到
          // ,['cardInfo.imageUrl']: tempFilePath
        })
        // 判断图片大小，如果大于4MB，则先进行压缩处理
        if (!tempFile.size > 1024 * 1024 * 4) {
          // 获得原始图片信息
          wx.getImageInfo({
            src: tempFilePath,
            success: res => {
              const imagePath = res.path // 图片路径
              const originWidth = res.width // 图片原始宽度
              const originHeight = res.height // 图片原始高度
              const maxWidth = 600 // 最大宽度限制，600px
              const maxHeight = 600 //最大高度限制，600px
              let targetWidth = originWidth // 目标宽度
              let targetHeight = originHeight // 目标高度
              // 如果图片原始尺寸超过最大尺寸限制600*600，则进行等比例压缩
              if (originWidth > maxWidth || originHeight > maxHeight) {
                // 如果原始宽度大于原始高度，则原始宽度优先，按照原始宽度限定目标尺寸
                if (originWidth / originHeight > maxWidth / maxHeight) {
                  // 要求目标高度 = 原始宽度 * (原生图片比例)
                  targetWidth = maxWidth
                  targetHeight = Math.round(maxWidth * (originHeight / originWidth))
                } else {
                  // 否则，原始高度优先，按照原始高度限定目标尺寸
                  // 要求目标宽度 = 原始高度 * (原生图片比例)
                  targetHeight = maxHeight
                  targetWidth = Math.round(maxHeight * (originWidth / originHeight))
                }
              }
              // 更新canvas尺寸
              this.setData({
                canvasWidth: targetWidth,
                canvasHeight: targetHeight
              })
              // 创建并绘制画布，用于获取压缩后的图片路径
              const ctx = wx.createCanvasContext('image-canvas')
              // 绘制图像到画布
              ctx.drawImage(imagePath, 0, 0, targetWidth, targetHeight)
              ctx.draw(false, () => {
                // 把当前画布指定区域的内容导出生成指定大小的图片
                // 在 draw() 回调里调用该方法才能保证图片导出成功
                wx.canvasToTempFilePath({
                  x: 0, // 指定的画布区域的左上角横坐标，默认值0
                  y: 0, // 指定的画布区域的左上角纵坐标，默认值0
                  width: targetWidth, // 指定的画布区域的宽度，默认值canvas宽度-x
                  height: targetHeight, // 指定的画布区域的高度，默认值canvas高度-y
                  destWidth: targetWidth, // 输出的图片的宽度，默认值width*屏幕像素密度
                  destHeight: targetHeight, // 输出的图片的高度，默认值height*屏幕像素密度
                  canvasId: 'image-canvas', // 画布标识，传入 <canvas> 组件的 canvas-id
                  fileType: 'jpg', // 目标文件的类型
                  quality: 0.6, // 图片的质量，目前仅对 jpg 有效。取值范围为 (0, 1]，不在范围内时当作 1.0 处理。
                  success: res => {
                    this.ocr(res.tempFilePath)
                  },
                  fail: res => {
                    wx.hideLoading()
                    wx.showModal({
                      title: '提示',
                      content: '压缩图片失败！' + res.errMsg + '！' + '请重试！',
                      showCancel: false
                    })
                  }
                })
              })
            },
            fail: res => {
              wx.hideLoading()
              wx.showModal({
                title: '提示',
                content: '获取图片信息失败！' + res.errMsg + '！' + '请重试！',
                showCancel: false
              })
            }
          })
        } else {
          this.ocr(tempFilePath)
        }
      },
      fail: res => {}
    })
  },
  // 识别照片
  ocr(filePath) {
    // 1.将图片转为base64编码
    let base64Data = wx.getFileSystemManager().readFileSync(filePath, "base64")
    // 2.将得到的图片base64编码转为二进制去除特殊符号，再转回base64编码
    // 3.百度AI识别的图片的base64编码是不包含图片头的，如（data:image/jpg;base64,）
    const base64UrlEncode = wx.arrayBufferToBase64(wx.base64ToArrayBuffer(base64Data))
    // 向百度AIP的API服务地址使用POST发送请求，必须在URL中带上参数access_token，
    // 注意：access_token的有效期为30天，需要每30天进行定期更换
    // 4.获取Access Token
    wx.request({
      url: 'https://aip.baidubce.com/oauth/2.0/token',
      method: 'GET',
      data: {
        grant_type: 'client_credentials', // 必须参数，固定为client_credentials
        client_id: config.config.baiduAIP.APIKey, // 必须参数，应用的API Key
        client_secret: config.config.baiduAIP.SecretKey // 必须参数，应用的Secret Key
      },
      success: res => {
        if (res.statusCode === 200) {
          const accessToken = res.data.access_token || ''
          // 5.发送请求调用api识别银行卡并返回卡号、有效期、发卡行和卡片类型
          wx.request({
            url: 'https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard?access_token=' + accessToken,
            method: 'POST',
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            data: {
              image: base64UrlEncode
            },
            success: res => {
              wx.hideLoading()
              if (res.statusCode === 200) {
                if (res.data.error_msg) {
                  wx.showModal({
                    title: '图片识别失败',
                    content: '识别银行卡失败！' + res.data.error_msg + '！' + '请重试！',
                    showCancel: false
                  })
                } else {
                  let result = res.data.result
                  // 识别银行卡成功，保存返回的卡信息
                  this.setData({
                    ['cardInfo.number']: res.data.result.bank_card_number || '',
                    ['cardInfo.type']: this.data.BANK_CARD_TYPE[res.data.result.bank_card_type],
                    ['cardInfo.org']: res.data.result.bank_name || '',
                    ['cardInfo.validDate']: res.data.result.valid_date && res.data.result.valid_date !== 'NO VALID' ? res.data.result.valid_date : ''
                  })
                }
              } else {
                wx.showModal({
                  title: '图片识别失败',
                  content: '识别银行卡失败！' + res.data.error_description + '！' + '请重试！',
                  showCancel: false
                })
              }
            },
            fail: res => {
              wx.hideLoading()
              wx.showModal({
                title: '图片识别失败',
                content: '无法识别银行卡！' + res.errMsg + '！' + '请重试！',
                showCancel: false
              })
            }
          })
        } else {
          wx.hideLoading()
          wx.showModal({
            title: '图片识别失败',
            content: '获取百度AIP的Access Token失败！' + res.data.error_description + '！' + '请重试！',
            showCancel: false
          })
        }
      },
      fail: res => {
        wx.hideLoading()
        wx.showModal({
          title: '图片识别失败',
          content: '无法获取百度AIP的Access Token！' + res.errMsg + '！' + '请重试！',
          showCancel: false
        })
      }
    })
  },
  /**
   * 密码输入框内容改变时触发
   */
  handleInputChange(e) {
    if (!/^$|^\d{6}$/.test(e.detail.value)) {
      this.setData({
        ['password.errMsg']: '密码格式为六位纯数字'
      })
    } else {
      this.setData({
        ['password.errMsg']: '',
        ['cardInfo.password']: e.detail.value
      })
    }
  },
  /**
   * 添加银行卡
   */
  addBankCard(e) {
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    app.wxLogin().then(res => {
      if (res.id) {
        // 处理银行卡信息
        let cardInfo = { ...this.data.cardInfo
        }
        cardInfo.userId = res.id
        cardInfo.name = cardInfo.org + cardInfo.type
        cardInfo.type = this.data.BANK_CARD_TYPE_ENUM[cardInfo.type]
        wx.request({
          url: config.config.baseUrl + '/card/save',
          method: 'post',
          data: cardInfo,
          success: res => {
            wx.hideLoading()
            if (res.data.success) {
              wx.showModal({
                title: '提示',
                content: '添加银行卡【' + res.data.content.number + '】成功！',
                showCancel: false,
                success: res => {
                  if (res.confirm) {
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
            wx.hideLoading()
            wx.showModal({
              title: '提示',
              content: '添加银行卡失败！' + res.errMsg + '！',
              showCancel: false,
            })
          }
        })
      } else {
        wx.hideLoading()
        wx.showModal({
          title: '提示',
          content: '添加银行卡失败！用户id不能为空！',
          showCancel: false,
        })
      }
    }).catch(error => {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '添加银行卡失败！',
        showCancel: false,
      })
      console.log(error)
    })
  },
  /**
   * 刷新当前页，将页面数据置为初始值
   */
  refreshPage() {
    this.setData({
      password: {
        errMsg: ''
      },
      accessToken: '',
      imageFilePath: '',
      cardInfo: {
        userId: '',
        number: '',
        type: '',
        org: '',
        validDate: '',
        password: '',
        imageUrl: ''
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {},

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
  onPullDownRefresh() {
    setTimeout(() => {
      this.refreshPage()
      wx.stopPullDownRefresh()
    }, 1500)
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})