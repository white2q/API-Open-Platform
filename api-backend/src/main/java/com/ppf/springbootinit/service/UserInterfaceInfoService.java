package com.ppf.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ppf.apicommon.model.entity.UserInterfaceInfo;

/**
* @author 25137
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-08-25 11:42:01
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用次数 +1
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
