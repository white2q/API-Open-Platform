package com.ppf.springbootinit.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author panpengfei
 * @date 2023/8/13
 */
@Data
public class IdRequest implements Serializable {

    private Long id;

    private static final long serialVersionUID = 1L;
}
