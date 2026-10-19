package com.club.clubsystem.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.club.clubsystem.domain.Club;
import com.club.clubsystem.service.ClubService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/club")
@CrossOrigin("*")
public class ClubController {
    @Resource
    private ClubService clubService;

    // 1. 添加社团 POST /club/add
    @PostMapping("/add")
    public Map<String,Object> add(@RequestBody Club club){
        Map<String,Object> res = new HashMap<>();
        boolean flag = clubService.addClub(club);
        if(flag){
            res.put("code",200);
            res.put("msg","新增成功");
        }else{
            res.put("code",500);
            res.put("msg","新增失败");
        }
        return res;
    }

    // 2. 删除社团 DELETE /club/remove/{id}
    @DeleteMapping("/remove/{id}")
    public Map<String,Object> remove(@PathVariable Integer id){
        Map<String,Object> res = new HashMap<>();
        boolean flag = clubService.removeClub(id);
        if(flag){
            res.put("code",200);
            res.put("msg","删除成功");
        }else{
            res.put("code",500);
            res.put("msg","删除失败");
        }
        return res;
    }

    // 3. 修改社团 PUT /club/update
    @PutMapping("/update")
    public Map<String,Object> update(@RequestBody Club club){
        Map<String,Object> res = new HashMap<>();
        boolean flag = clubService.updateClub(club);
        if(flag){
            res.put("code",200);
            res.put("msg","修改成功");
        }else{
            res.put("code",500);
            res.put("msg","修改失败");
        }
        return res;
    }

    // 4. 单一查询（根据ID）GET /club/get/{id}
    @GetMapping("/get/{id}")
    public Map<String,Object> getById(@PathVariable Integer id){
        Map<String,Object> res = new HashMap<>();
        Club club = clubService.getById(id);
        res.put("code",200);
        if(club == null){
            res.put("msg","未查询到该社团");
            res.put("data",null);
        }else{
            res.put("data",club);
        }
        return res;
    }

    // 5. 多字段三表联合分页查询 GET /club/list
    @GetMapping("/list")
    public Map<String,Object> list(
            @RequestParam(required = false) String clubType,
            @RequestParam(required = false) String stuName,
            @RequestParam(required = false) String memberRole,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "5") long pageSize
    ){
        Map<String,Object> res = new HashMap<>();
        IPage<Club> page = clubService.listClubByCondition(pageNum, pageSize, clubType, stuName, memberRole);
        res.put("code",200);
        Map<String,Object> data = new HashMap<>();
        data.put("records", page.getRecords());
        data.put("total", page.getTotal());
        res.put("data", data);
        return res;
    }
}
