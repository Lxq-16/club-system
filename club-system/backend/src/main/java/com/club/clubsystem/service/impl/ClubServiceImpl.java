package com.club.clubsystem.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.clubsystem.domain.Club;
import com.club.clubsystem.mapper.ClubMapper;
import com.club.clubsystem.service.ClubService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {
    @Resource
    private ClubMapper clubMapper;

    @Override
    public boolean addClub(Club club) {
        return this.save(club);
    }

    @Override
    public boolean removeClub(Integer id) {
        return this.removeById(id);
    }

    @Override
    public boolean updateClub(Club club) {
        return this.updateById(club);
    }

    @Override
    public Club getClubById(Integer id) {
        return this.getById(id);
    }

    // 新增memberRole参数传递到mapper
    @Override
    public List<Club> listClubByCondition(String clubType, String stuName, String memberRole) {
        return clubMapper.selectClubByCondition(clubType, stuName, memberRole);
    }
}