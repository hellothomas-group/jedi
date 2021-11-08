package xyz.hellothomas.jedi.biz.common.constants;

/**
 * @author 80234613 唐圆
 * @date 2021/3/23 12:51
 * @descripton
 * @version 1.0
 */
public class Constants {
    public static final String JEDI_RELEASE_TOPIC = "jedi-release";
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String NAMESPACE_EXECUTOR_VALIDATOR = "[0-9a-zA-Z_.-]+";

    private Constants() {
        throw new IllegalStateException("Constant class");
    }
}
