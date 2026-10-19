package com.club.clubsystem.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.club.clubsystem.domain.Club;
import org.apache.ibatis.annotations.Param;

public interface ClubMapper extends BaseMapper<Club> {
    // 三表联查动态SQL分页：第一个参数必须是IPage，分页插件自动包裹count和limit
    IPage<Club> selectClubByCondition(IPage<Club> page,
                                      @Param("clubType") String clubType,
                                      @Param("stuName") String stuName,
                                      @Param("memberRole") String memberRole);
}
