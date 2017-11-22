package grallary.lee.com.grallery.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description 判断是否为空
 * @Version
 */
public final class AssertUtil {

    private AssertUtil() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return isNull(collection) || collection.size() < 1;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.size() < 1;
    }

    public static boolean isEmpty(Object object) {
        if (object instanceof Collection) {
            return isEmpty((Collection<?>) object);
        } else if (object instanceof Map) {
            return isEmpty((Map<?, ?>) object);
        }
        return isNull(object) || "".equals(object);
    }

    public static boolean isEmpty(Object[] object) {
        return isNull(object) || object.length < 1;
    }

    public static boolean isEmpty(String text) {
        return isNull(text) || text.trim().length() < 1;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean isNotEmpty(Object[] object) {
        return !isEmpty(object);
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    private static boolean isNull(Object object) {
        return object == null;
    }

    private static boolean isNotNull(Object object) {
        return object != null;
    }

    public static boolean hasLength(String text) {
        return text != null && text.length() > 0;
    }

    public static boolean hasText(String text) {
        if (!hasLength(text)) {
            return false;
        }
        int strLen = text.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContain(String textToSearch, String substring) {
        if (hasLength(textToSearch) && hasLength(substring) && textToSearch.contains(substring)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAssignable(Class<?> superType, Class<?> subType) {
        if (isNotNull(superType) && isNotNull(subType) && superType.isAssignableFrom(subType)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInstanceOf(Class<?> type, Object object) {
        if (isNotNull(type) && type.isInstance(object)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串的内容是不是全是数字
     * @param text
     * @return
     */
    public static boolean isNumeric(String text) {
        if (isEmpty(text)) {
            return false;
        }
        int size = text.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
