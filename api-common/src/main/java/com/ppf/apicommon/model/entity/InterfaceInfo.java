package com.ppf.apicommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 套接字地址
     */
    @TableField(value = "host")
    private String host;

    /**
     * 接口地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 请求参数
     */
    @TableField(value = "requestParams")
    private String requestParams;

    /**
     * 请求头
     */
    @TableField(value = "requestHeader")
    private String requestHeader;

    /**
     * 响应头
     */
    @TableField(value = "responseHeader")
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 请求类型
     */
    @TableField(value = "method")
    private String method;

    /**
     * 创建人
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}