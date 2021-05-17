package xyz.hellothomas.jedi.biz.common.utils;


import com.google.common.base.Joiner;

import static xyz.hellothomas.jedi.core.constants.Constants.NAMESPACE_EXECUTOR_SEPARATOR;

/**
 * @author Thomas
 */
public class ReleaseMessageKeyGenerator {

    private static final Joiner STRING_JOINER = Joiner.on(NAMESPACE_EXECUTOR_SEPARATOR);

    public static String generate(String namespace, String appId, String executorName) {
        return STRING_JOINER.join(namespace, appId, executorName);
    }
}
