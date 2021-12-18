package xyz.hellothomas.jedi.core.internals.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 80234613 唐圆
 * @date 2021/12/16 10:13
 * @descripton
 * @version 1.0
 */
public class AsyncAttributes {
    private static final Logger logger = LoggerFactory.getLogger(AsyncAttributes.class);
    private static final String[] EMPTY_STRING_ARRAY = {};
    private final Map<String, Object> map = new HashMap<>();

    public Object getAttribute(String name) {
        return map.get(name);
    }

    public void setAttribute(String name, Object value) {
        logger.trace("setAttribute: name=[{0}], value=[{1}]]", name, value);
        map.put(name, value);
    }

    public void removeAttribute(String name) {
        map.remove(name);
    }

    public String[] getAttributeNames() {
        return map.keySet().isEmpty() ? EMPTY_STRING_ARRAY : map.keySet().toArray(EMPTY_STRING_ARRAY);
    }
}
