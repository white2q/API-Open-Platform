package com.ppf.springbootinit.service.impl.inner;

import com.ppf.apicommon.service.InnerUserInterfaceInfoService;
import com.ppf.springbootinit.common.ErrorCode;
import com.ppf.springbootinit.exception.BusinessException;
import com.ppf.springbootinit.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户-信息
 *
 * @author panpengfei
 * @date 2023/9/3
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if(interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
       return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
