package com.prodigal.commons;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
public class HttpCommonsUtils {

    /**
     * 截取字符串?号以前的内容
     *
     * @param str 需要截取的字符串
     * @return 截取后的字符串
     */
    public static String substringDoubt(String str, String intercept) {
        int questionMarkIndex = str.indexOf(intercept);
        if (questionMarkIndex != -1) {
            return str.substring(0, questionMarkIndex);
        }
        return str;
    }

}
