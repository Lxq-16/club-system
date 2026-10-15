package com.club.clubsystem.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.clubsystem.domain.Club;
import java.util.List;

public interface ClubService extends IService<Club> {
    // 新增社团
    boolean addClub(Club club);
    // 根据id删除社团
    boolean removeClub(Integer id);
    // 修改社团
    boolean updateClub(Club club);
    // 单id查询社团
    Club getClubById(Integer id);
    // 多字段跨表查询
    List<Club> listClubByCondition(String clubType, String stuName, String memberRole);
}