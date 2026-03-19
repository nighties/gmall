package com.gmall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String province;

    private String city;

    private String district;

    private String detail;

    private String receiverName;

    private String receiverPhone;

    private Integer isDefault;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
