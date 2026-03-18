package com.gmall.order.dto;

import lombok.Data;

/**
 * 收货地址响应 DTO
 */
@Data
public class AddressVO {

    /**
     * 地址 ID
     */
    private Long id;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详细地址
     */
    private String detail;
}
