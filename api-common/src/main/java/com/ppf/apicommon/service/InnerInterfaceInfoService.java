package com.ppf.apicommon.service;

import com.ppf.apicommon.model.entity.InterfaceInfo;

/**
 * 接口信息
 *
 * @author panpengfei
 * @date 2023/9/3
 */
public interface InnerInterfaceInfoService {

    InterfaceInfo getInterfaceInfo(String host, String url, String method);
}
