package xyz.hellothomas.jedi.core.constants;

/**
 * @author 80234613 唐圆
 * @date 2021/3/23 12:51
 * @descripton
 * @version 1.0
 */
public class Constants {
    public static final String NAMESPACE_EXECUTOR_SEPARATOR = "+";
    public static final long NOTIFICATION_ID_PLACEHOLDER = -1;
    public static final String NO_APPID_PLACEHOLDER = "JediNoAppIdPlaceHolder";
    public static final String JEDI_DEFAULT_TASK_NAME = "jedi-default-task";

    private Constants() {
        throw new IllegalStateException("Constant class");
    }
}
