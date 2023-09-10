package com.ppf.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ppf.apicommon.model.entity.InterfaceInfo;
import com.ppf.apicommon.service.InnerInterfaceInfoService;
import com.ppf.springbootinit.common.ErrorCode;
import com.ppf.springbootinit.exception.BusinessException;
import com.ppf.springbootinit.mapper.InterfaceInfoMapper;
import com.ppf.springbootinit.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部接口信息
 *
 * @author panpengfei
 * @date 2023/9/3
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String host, String url, String method) {
        if(StringUtils.isAnyBlank(host, url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("host", host);
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);

        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        if(interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口信息不存在");
        }
        return interfaceInfo;
    }
}
