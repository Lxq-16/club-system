package com.club.clubsystem.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.clubsystem.domain.Club;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ClubMapper extends BaseMapper<Club> {
    // 多表三表联查动态SQL，新增memberRole参数
    List<Club> selectClubByCondition(@Param("clubType") String clubType,
                                     @Param("stuName") String stuName,
                                     @Param("memberRole") String memberRole);
}