package com.ppf.apicommon.service;

import com.ppf.apicommon.model.entity.User;

/**
 * 用户信息
 *
 * @author panpengfei
 * @date 2023/9/3
 */
public interface InnerUserService {

    User getInvokeUser(String accessKey);
}
