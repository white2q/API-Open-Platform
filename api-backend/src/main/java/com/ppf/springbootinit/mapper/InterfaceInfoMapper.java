package com.ppf.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppf.apicommon.model.entity.InterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25137
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2023-07-31 11:40:33
* @Entity com.ppf.springbootinit.model.entity.InterfaceInfo
*/
@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

}




