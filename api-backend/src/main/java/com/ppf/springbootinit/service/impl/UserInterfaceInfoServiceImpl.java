package com.ppf.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ppf.apicommon.model.entity.UserInterfaceInfo;
import com.ppf.springbootinit.common.ErrorCode;
import com.ppf.springbootinit.exception.BusinessException;
import com.ppf.springbootinit.mapper.UserInterfaceInfoMapper;
import com.ppf.springbootinit.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author 25137
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-08-25 11:42:01
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if(userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if(add) {
            if(userInterfaceInfo.getId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if(userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数小于0");
        }

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if(interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户或接口不存在");
        }

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId", userId);

        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }
}




