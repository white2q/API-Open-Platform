package com.ppf.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ppf.apicommon.model.entity.InterfaceInfo;
import com.ppf.springbootinit.common.ErrorCode;
import com.ppf.springbootinit.exception.BusinessException;
import com.ppf.springbootinit.exception.ThrowUtils;
import com.ppf.springbootinit.mapper.InterfaceInfoMapper;
import com.ppf.springbootinit.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 25137
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-07-31 11:40:33
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名词过长");
        }
    }
}




