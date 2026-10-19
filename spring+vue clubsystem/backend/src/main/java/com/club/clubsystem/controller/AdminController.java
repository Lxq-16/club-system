package com.club.clubsystem.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.club.clubsystem.domain.Admin;
import com.club.clubsystem.mapper.AdminMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
    @Resource
    private AdminMapper adminMapper;

    // 内存token白名单：token -> username（单机部署够用，生产应换Redis）
    public static final Map<String, String> TOKEN_POOL = new HashMap<>();

    // 登录 POST /admin/login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Admin admin) {
        Map<String, Object> res = new HashMap<>();
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, admin.getUsername())
               .eq(Admin::getPassword, admin.getPassword());
        Admin db = adminMapper.selectOne(wrapper);
        if (db == null) {
            res.put("code", 500);
            res.put("msg", "用户名或密码错误");
            return res;
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_POOL.put(token, db.getUsername());
        res.put("code", 200);
        res.put("msg", "登录成功");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", db.getUsername());
        res.put("data", data);
        return res;
    }

    // 退出 POST /admin/logout
    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> res = new HashMap<>();
        if (token != null) {
            TOKEN_POOL.remove(token);
        }
        res.put("code", 200);
        res.put("msg", "退出成功");
        return res;
    }
}
