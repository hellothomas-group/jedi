package xyz.hellothomas.jedi.admin.application.permission;

import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.biz.domain.monitor.App;

/**
 * @author Thomas
 * @date 2021/11/28 9:46
 * @description
 * @version 1.0
 */
public interface RoleInitializationService {
    void initSystemRoles();

    void initAppRoles(App app);

    void initExecutorRoles(Executor executor);
}
