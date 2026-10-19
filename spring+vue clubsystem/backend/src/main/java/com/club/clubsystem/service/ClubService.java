package com.club.clubsystem.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.club.clubsystem.domain.Club;

public interface ClubService extends IService<Club> {
    // 新增社团
    boolean addClub(Club club);
    // 根据id删除社团
    boolean removeClub(Integer id);
    // 修改社团
    boolean updateClub(Club club);
    // 单id查询社团
    Club getClubById(Integer id);
    // 多字段跨表分页查询
    IPage<Club> listClubByCondition(long pageNum, long pageSize, String clubType, String stuName, String memberRole);
}
