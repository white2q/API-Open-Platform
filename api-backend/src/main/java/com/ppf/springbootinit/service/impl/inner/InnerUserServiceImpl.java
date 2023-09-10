package com.ppf.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ppf.apicommon.model.entity.User;
import com.ppf.apicommon.service.InnerUserService;
import com.ppf.springbootinit.common.ErrorCode;
import com.ppf.springbootinit.exception.BusinessException;
import com.ppf.springbootinit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户信息
 *
 * @author panpengfei
 * @date 2023/9/3
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if(!StringUtils.isNotBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户不存在");
        }
        return user;
    }
}
