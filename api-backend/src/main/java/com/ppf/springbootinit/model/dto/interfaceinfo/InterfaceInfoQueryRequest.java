package com.ppf.springbootinit.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ppf.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
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

    private static final long serialVersionUID = 1L;
}