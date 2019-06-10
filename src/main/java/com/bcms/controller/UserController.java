package com.bcms.controller;

import com.bcms.common.JsonResponse;
import com.bcms.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Jackie
 * @className UserController
 * @descrition User控制器
 * @date 2018/12/28 15:05
 */
@Data
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/login")
    public Map<String, Object> handleLogin(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        try {
            if (userServiceImpl.findByAccountAndPassword(account, password).isPresent()) {
                return JsonResponse.getSuccessResult(userServiceImpl.findByAccountAndPassword(account, password).get(), "登录成功！");
            } else {
                return JsonResponse.getErrorResult("登录失败！帐号或密码不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("登录失败！" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable("id") Long id) {
        try {
            if (userServiceImpl.findById(id).isPresent()) {
                return JsonResponse.getSuccessResult(userServiceImpl.findById(id).get(), "获取用户信息成功！");
            } else {
                return JsonResponse.getErrorResult("该用户不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getErrorResult("获取用户信息失败！" + e.getMessage());
        }
    }
}
