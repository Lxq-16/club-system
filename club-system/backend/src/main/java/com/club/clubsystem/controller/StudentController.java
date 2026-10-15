package com.club.clubsystem.controller;
import com.club.clubsystem.domain.Student;
import com.club.clubsystem.service.StudentService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@CrossOrigin("*")
public class StudentController {
    @Resource
    private StudentService studentService;

    // 查询全部学生（下拉框使用）
    @GetMapping("/list")
    public Map<String,Object> listAll(){
        Map<String,Object> res = new HashMap<>();
        List<Student> list = studentService.list();
        res.put("code",200);
        res.put("data",list);
        return res;
    }
}