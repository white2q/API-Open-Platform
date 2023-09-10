package com.ppf.springbootinit.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @author panpengfei
 * @date 2023/8/29
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}
