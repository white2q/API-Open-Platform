package com.ppf.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppf.apicommon.model.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25137
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-08-25 11:42:01
* @Entity com.ppf.springbootinit.model.entity.UserInterfaceInfo
*/
@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

}




