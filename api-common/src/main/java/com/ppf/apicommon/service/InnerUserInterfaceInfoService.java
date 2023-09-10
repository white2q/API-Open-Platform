package com.ppf.apicommon.service;

/**
 * 用户-接口信息
 *
 * @author panpengfei
 * @date 2023/9/3
 */
public interface InnerUserInterfaceInfoService {

    boolean invokeCount(long interfaceInfoId, long userId);
}
