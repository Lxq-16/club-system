package com.club.clubsystem.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.club.clubsystem.domain.Club;
import com.club.clubsystem.mapper.ClubMapper;
import com.club.clubsystem.service.ClubService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

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

    @Override
    public IPage<Club> listClubByCondition(long pageNum, long pageSize, String clubType, String stuName, String memberRole) {
        Page<Club> page = new Page<>(pageNum, pageSize);
        return clubMapper.selectClubByCondition(page, clubType, stuName, memberRole);
    }
}
