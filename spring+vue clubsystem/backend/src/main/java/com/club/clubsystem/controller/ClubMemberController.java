package com.club.clubsystem.controller;
import com.club.clubsystem.domain.ClubMember;
import com.club.clubsystem.service.ClubMemberService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@CrossOrigin("*")
public class ClubMemberController {
    @Resource
    private ClubMemberService memberService;

    // 添加成员
    @PostMapping("/add")
    public Map<String,Object> add(@RequestBody ClubMember member){
        Map<String,Object> res = new HashMap<>();
        boolean flag = memberService.save(member);
        res.put("code",flag?200:500);
        res.put("msg",flag?"加入社团成功":"加入失败");
        return res;
    }
}