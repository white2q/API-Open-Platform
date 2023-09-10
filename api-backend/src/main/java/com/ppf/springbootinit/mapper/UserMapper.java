package com.ppf.springbootinit.mapper;

import com.ppf.apicommon.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25137
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-08-23 14:33:35
* @Entity com.ppf.springbootinit.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




