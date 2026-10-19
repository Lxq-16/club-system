package com.club.clubsystem.domain;
import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;


@Data
@TableName("club")
public class Club {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String clubName;
    private String clubType;
    private Integer leaderId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    // 负责人姓名（多表查询返回字段，不存数据库）
    @TableField(exist = false)
    private String leaderName;

}