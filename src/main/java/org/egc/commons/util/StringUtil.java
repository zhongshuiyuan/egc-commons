package org.egc.commons.util;

/**
 * <pre/>字符串操作工具类
 * 一般的判空等字符串操作使用
 * {@link com.google.common.base.Strings} 或 {@link org.apache.commons.lang3.StringUtils}
 *
 * @author houzhiwei
 * @date 2017/6/29 16:31
 */
public class StringUtil {
    /**
     * 移除第一个下划线
     *
     * @param src the src
     * @return string
     * @date 2016年2月1日下午5:38:54
     */
    public static String removeFirstUnderline(String src)
    {
        if (src.indexOf('_') == 0)
            src = src.substring(1);
        return src;
    }
}
