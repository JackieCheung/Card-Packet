// 配置文件
const config = {
  baseUrl: 'http://192.168.1.100:8080/',
  // 高德地图配置文件
  amap: {
    APIKey: '442958f9b0125578dce4929e5bed387d'
  },
  // 百度AI平台配置文件
  baiduAIP: {
    AppID: '15900361',
    APIKey: 'a90UGnQ9pqUNX2VFPkQGTF4r',
    SecretKey: 'eg3hG1IU4GpRSMlodGYkx7Tnvea9UdxB'
  },
  bankNameMap: {
    '东莞农村商业银行': 'dgnongshanghang',
    '东莞银行': 'dongguan',
    '工商银行': 'gongshang',
    '光大银行': 'guangda',
    '广发': 'guangfa',
    '华商银行': 'huashang',
    '华夏银行': 'huaxia',
    '建设银行': 'jianshe',
    '交通银行': 'jiaotong',
    '农业银行': 'nongye',
    '浦发银行': 'pufa',
    '兴业银行': 'xingye',
    '邮储银行': 'youchu',
    '招商银行': 'zhaoshang',
    '中国民生银行': 'minsheng',
    '中国银行': 'zhongguo',
    '中信银行': 'zhongxin',
    '北京银行': 'beijing',
    '人民银行': 'renmin',
    '平安银行': 'pingan',
    '汇丰银行': 'huifeng',
    '现金账户': 'xianjin',
    '在线支付账户': 'zaixian',
    '储值账户': 'chuzhi',
    '理财账户': 'licai',
    '股票账户': 'gupiao',
    '基金账户': 'jijin',
    '负债账户': 'fuzhai',
    '债权账户': 'zhaiquan'
  }
}

module.exports.config = config