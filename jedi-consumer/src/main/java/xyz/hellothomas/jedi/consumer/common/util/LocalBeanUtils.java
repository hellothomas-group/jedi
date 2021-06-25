package xyz.hellothomas.jedi.consumer.common.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;
import xyz.hellothomas.jedi.consumer.infrastructure.exception.BeanUtilsException;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @author Thomas
 * @date 2021/3/27 17:35
 * @description
 * @version 1.0
 */
public class LocalBeanUtils {
    private LocalBeanUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * <pre>
     *     List<UserBean> userBeans = userDao.queryUsers();
     *     List<UserDTO> userDTOs = BeanUtil.batchTransform(UserDTO.class, userBeans);
     * </pre>
     */
    public static <T> List<T> batchTransform(final Class<T> clazz, List<?> srcList) {
        if (CollectionUtils.isEmpty(srcList)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(srcList.size());
        for (Object srcObject : srcList) {
            result.add(transform(clazz, srcObject));
        }
        return result;
    }

    /**
     * 封装{@link org.springframework.beans.BeanUtils#copyProperties}，惯用与直接将转换结果返回
     *
     * <pre>
     *      UserBean userBean = new UserBean("username");
     *      return BeanUtil.transform(UserDTO.class, userBean);
     * </pre>
     */
    public static <T> T transform(Class<T> clazz, Object src) {
        if (src == null) {
            return null;
        }
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            throw new BeanUtilsException(e);
        }
        org.springframework.beans.BeanUtils.copyProperties(src, instance, getNullPropertyNames(src));
        return instance;
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
