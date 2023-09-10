package com.ppf.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ppf.apiclientsdk.client.ApiClient;
import com.ppf.apicommon.model.entity.User;
import com.ppf.apicommon.model.entity.InterfaceInfo;
import com.ppf.apicommon.model.entity.UserInterfaceInfo;
import com.ppf.springbootinit.annotation.AuthCheck;
import com.ppf.springbootinit.common.*;
import com.ppf.springbootinit.constant.CommonConstant;
import com.ppf.springbootinit.constant.UserConstant;
import com.ppf.springbootinit.exception.BusinessException;
import com.ppf.springbootinit.exception.ThrowUtils;
import com.ppf.springbootinit.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.ppf.springbootinit.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.ppf.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.ppf.springbootinit.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.ppf.springbootinit.model.enums.InterfaceInfoStatusEnum;
import com.ppf.springbootinit.service.InterfaceInfoService;
import com.ppf.springbootinit.service.UserInterfaceInfoService;
import com.ppf.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private ApiClient apiClient;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员或本人进行操作）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        User loginUser = userService.getLoginUser(request);
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅管理员或本人进行操作
        if(!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo result = interfaceInfoService.getById(id);
        return ResultUtils.success(result);
    }

    /**
     * 获取列表信息（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfo>> getInterfaceInfoList(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if(interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        List<InterfaceInfo> result = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> getInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if(interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        long current = interfaceInfoQueryRequest.getCurrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfo.getDescription();
        interfaceInfo.setDescription(null);
        // 限制爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        queryWrapper.like(StringUtils.isNotBlank(description),"description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> result = interfaceInfoService.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }

    /**
     * 接口发布（仅管理员或创建人可操作）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    public BaseResponse<Boolean>onlineInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = idRequest.getId();
        if(idRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在");
        }

        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();

        // TODO 判断该接口是否可用
        com.ppf.apiclientsdk.model.User user = new com.ppf.apiclientsdk.model.User();
        user.setName("test");
        String testResult = apiClient.getUserNameByPOST(user);
        if(testResult == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 仅管理员或创建人可操作
        if(!interfaceInfo.getUserId().equals(userId) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(result);
    }

    /**
     * 接口下线（仅管理员或创建人可操作）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean>offlineInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = idRequest.getId();
        if(idRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在");
        }

        // 判断接口是否已经为下线状态
        if(interfaceInfo.getStatus().equals(InterfaceInfoStatusEnum.OFFLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "操作失败,该接口已下线");
        }

        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();

        // 仅管理员或创建人可操作
        if(!interfaceInfo.getUserId().equals(userId) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(result);
    }

    /**
     * 接口调用
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        // 1.数据有效
        if(interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2.拿到参数
        long interfaceInfoId = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();

        // 3.判断该接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        if(oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 4.判断接口是否发布
        Integer status = oldInterfaceInfo.getStatus();
        if(InterfaceInfoStatusEnum.OFFLINE.getValue() == status) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该接口已下线");
        }

        // 5.判断用户调用次数是否支持
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", loginUser.getId());

        // 6.判断是否还有调用次数
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if(leftNum <= 0) {
            // 剩余调用次数不足
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "剩余调用次数不足");
        }

        // 7. 调用模拟接口 格式{“name”:"xxx"}
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        ApiClient apiClient = new ApiClient(accessKey, secretKey);
        com.ppf.apiclientsdk.model.User user = GSON.fromJson(userRequestParams, com.ppf.apiclientsdk.model.User.class);
        String result = apiClient.getUserNameByPOST(user);
        return ResultUtils.success(result);
    }
}
