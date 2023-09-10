package com.ppf.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ppf.apicommon.model.entity.InterfaceInfo;

/**
* @author 25137
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-07-31 11:40:33
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);



}
