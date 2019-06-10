package com.bcms.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcms.common.AesCbcUtil;
import com.bcms.common.BeanUtils;
import com.bcms.common.JsonResponse;
import com.bcms.common.StringUtils;
import com.bcms.entity.Card;
import com.bcms.entity.GenderEnum;
import com.bcms.entity.User;
import com.bcms.model.UserVO;
import com.bcms.service.UserService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Jackie
 * @className WeChatController
 * @descrition 微信控制器
 * @date 2019/4/10 19:53
 */
@Data
@RestController
@RequestMapping("/wx")
public class WeChatController {
    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;

    @Value("${wx.grantType}")
    private String grantType;

    @Value("${wx.requestUrl}")
    private String requestUrl;


    /**
     * @author Jackie
     * @className UserInfo
     * @descrition 静态内部类，用于封装wx.getUserInfo返回的用户信息
     * @date 2019/4/11 10:53
     */
    @Data
    static class UserInfo {
        /**
         * 用户所在国家
         */
        private String country;
        /**
         * 用户所在省份
         */
        private String province;
        /**
         * 用户所在城市
         */
        private String city;
        /**
         * 用户昵称
         */
        private String nickName;
        /**
         * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
         */
        private Integer gender;
        /**
         * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表132*132正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
         */
        private String avatarUrl;
        /**
         * 用户的语言，简体中文为zh_CN
         */
        private String language;
    }

    private final UserService userServiceImpl;

    @Autowired
    public WeChatController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 获取微信小程序用户openid和session_key
     *
     * @param code 登录临时凭证code
     * @return Map<String, Object>
     */
    @PostMapping("/oauth")
    public Map<String, Object> wxOauth(String code) {
        if (StringUtils.isNotBlank(code)) {
            try {
                // 用于发送request请求
                RestTemplate restTemplate = new RestTemplate();
                // 拼接查询字符串
                String queryParams = "appid=" + this.getAppId() + "&secret=" + this.getAppSecret() + "&js_code=" + code + "&grant_type="
                        + this.getGrantType();
                // 拼接请求url
                String url = this.getRequestUrl() + "?" + queryParams;
                // 发送请求并格式化响应的结构
                JSONObject resultJsonObject = JSONObject.parseObject(restTemplate.getForObject(url, String.class));
                // 获取openid
                String openid = resultJsonObject.get("openid").toString();
                // 增加同步锁
                synchronized (openid.intern()) {
                    // 根据openid判断用户是否已存在
                    if (userServiceImpl.findByOpenId(openid).isPresent()) {
                        // 已存在直接返回用户信息
                        return JsonResponse.getSuccessResult(new UserVO(userServiceImpl.findByOpenId(openid).get()), "获取用户信息成功！");
                    } else {
                        // 不存在则先持久化到数据库
                        User user = new User();
                        user.setOpenId(openid);
                        return JsonResponse.getSuccessResult(new UserVO(userServiceImpl.save(user)), "新增用户成功！");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return JsonResponse.getErrorResult("获取openid和session_key失败，" + e.getMessage());
            }
        } else {
            return JsonResponse.getErrorResult("获取openid和session_key失败，登录临时凭证不能为空！");
        }
    }

    /**
     * 更新用户信息
     *
     * @param id       用户id
     * @param userInfo wx.getUserInfo返回的用户信息
     * @return Map<String, Object>
     */
    @PostMapping("/updateUserInfo/{id}")
    public Map<String, Object> updateUserInfo(@PathVariable("id") Long id, @RequestBody UserInfo userInfo) {
        if (id != null) {
            if (userServiceImpl.existsById(id)) {
                if (userInfo != null) {
                    try {
                        final User[] users = new User[1];
                        userServiceImpl.findById(id).ifPresent(u -> users[0] = u);
                        BeanUtils.copyProperties(userInfo, users[0]);
                        users[0].setProfilePhoto(userInfo.getAvatarUrl());
                        users[0].setGender(userInfo.getGender() == 2 ? GenderEnum.FEMALE : GenderEnum.MALE);
                        return JsonResponse.getSuccessResult(new UserVO(userServiceImpl.save(users[0])), "更新用户信息成功！");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return JsonResponse.getErrorResult("更新用户信息失败，" + e.getMessage());
                    }
                } else {
                    return JsonResponse.getErrorResult("更新用户信息失败，用户信息不能为空！");
                }
            } else {
                return JsonResponse.getErrorResult("更新用户信息失败，该用户不存在！");
            }
        } else {
            return JsonResponse.getErrorResult("更新用户信息失败，用户id不能为空！");
        }
    }

    /**
     * 解密用户敏感信息
     *
     * @param encryptedData 加密数据
     * @param iv            加密算法初始向量
     * @param code          登录临时凭证code
     * @return Map<String, Object>
     */
    @PostMapping("/decodeUserInfo")
    public Map<String, Object> wxDecodeUserInfo(String encryptedData, String iv, String code) {
        // 用于发送request请求
        RestTemplate restTemplate = new RestTemplate();
        // 拼接查询字符串
        String queryParams = "appid=" + this.getAppId() + "&secret=" + this.getAppSecret() + "&js_code=" + code + "&grant_type="
                + this.getGrantType();
        // 拼接请求url
        String url = this.getRequestUrl() + "?" + queryParams;
        // 发送请求并格式化响应的结构
        JSONObject resultJsonObject = JSONObject.parseObject(restTemplate.getForObject(url, String.class));
        // 获取session_key
        String sessionKey = resultJsonObject.get("session_key").toString();
        // 对encryptedData加密数据进行AES解密
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            System.err.println(result);
            if (StringUtils.isNotBlank(result)) {
                JSONObject userInfo = JSONObject.parseObject(result);
                System.err.println(userInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
