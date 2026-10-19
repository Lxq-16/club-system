package com.club.clubsystem.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.clubsystem.domain.ClubMember;
import com.club.clubsystem.mapper.ClubMemberMapper;
import com.club.clubsystem.service.ClubMemberService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
@Service
public class ClubMemberServiceImpl extends ServiceImpl<ClubMemberMapper, ClubMember> implements ClubMemberService {
    @Resource
    private ClubMemberMapper clubMemberMapper;
}