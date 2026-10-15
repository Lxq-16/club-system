package com.club.clubsystem.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.clubsystem.domain.Student;
import com.club.clubsystem.mapper.StudentMapper;
import com.club.clubsystem.service.StudentService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Resource
    private StudentMapper studentMapper;
}