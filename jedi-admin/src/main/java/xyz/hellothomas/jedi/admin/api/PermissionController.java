package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.admin.application.permission.RolePermissionService;

/**
 * @author Thomas
 * @date 2021/11/27 17:04
 * @description
 * @version 1.0
 */
@Api(value = "permission", tags = "permission")
@RestController
public class PermissionController {
    private final RolePermissionService rolePermissionService;

    public PermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }
}
