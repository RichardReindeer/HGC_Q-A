package com.bambi.straw.portal.service.impl;

import com.bambi.straw.portal.model.Permission;
import com.bambi.straw.portal.mapper.PermissionMapper;
import com.bambi.straw.portal.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MR.Bambi
 * @since 2021-04-14
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
