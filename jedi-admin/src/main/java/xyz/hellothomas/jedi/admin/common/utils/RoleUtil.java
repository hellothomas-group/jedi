package xyz.hellothomas.jedi.admin.common.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import xyz.hellothomas.jedi.admin.common.enums.RoleTypeEnum;
import xyz.hellothomas.jedi.core.constants.Constants;

/**
 * @author Thomas
 * @date 2021/11/28 15:53
 * @description
 * @version 1.0
 */
public class RoleUtil {
    private static final Joiner STRING_JOINER = Joiner.on(Constants.NAMESPACE_EXECUTOR_SEPARATOR).
            skipNulls();
    private static final Splitter STRING_SPLITTER = Splitter.on(Constants.NAMESPACE_EXECUTOR_SEPARATOR)
            .omitEmptyStrings().trimResults();

    private RoleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String buildSystemManagerRoleName() {
        return RoleTypeEnum.SYSTEM_MANAGER.getValue();
    }

    public static String buildAppManagerRoleName(String namespace, String appId) {
        return STRING_JOINER.join(RoleTypeEnum.APP_MANAGER.getValue(), namespace, appId);
    }
}
