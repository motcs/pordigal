package com.prodigal.commons;

import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-25 星期二
 */
public class ConversionNumberUtil {

    //创建对应的汉字
    static final List<String> CHINESE_NUMBER = List.of("零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "百", "千", "万", "亿");
    //创建与汉字对应的阿拉伯数字
    static final List<Integer> NUMBER = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100, 1000, 10000, 100000000);

    /**
     * 中文数字转换为阿拉伯数字
     *
     * @param str 需要转换的字符
     * @return 返回转换信息，为空则返回00
     * @author motcs
     * @since 2023-07-25
     */
    public static String convertNumber(String str) {
        if (ObjectUtils.isEmpty(str)) {
            return "";
        }
        //判断是否十纯中文汉字，不是则直接返回00
        if (!isChineseNum(str)) {
            return "";
        }
        List<Integer> collect = str.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .map(CHINESE_NUMBER::indexOf)
                .map(NUMBER::get)
                .collect(Collectors.toList());
        // 临时变量，存储最终的值
        int temp = 0;
        // 下标，进行到了哪里
        int index = 0;
        // 下一个数字是否是单位数字 大于等10则为单位
        boolean isTemp2 = false;
        for (Integer integer : collect) {
            // 如果下一个是单位，则此时不使用当前数字直接跳过
            if (isTemp2) {
                // 重置判断条件
                isTemp2 = false;
                // 下标正常增加
                index++;
                continue;
            }
            if (integer >= 10) {
                if (index != 0) {
                    temp = temp * integer;
                } else {
                    temp = integer;
                }
            } else {
                // 如果下一个数字是单位，下一个不是最后一个时，此时临时 变量直接 + 当前位数字 * 下一位单位
                if (collect.get(index + 1) >= 10 && index + 2 < collect.size()) {
                    temp += integer * collect.get(index + 1);
                    isTemp2 = true;
                } else {
                    // 否则直接加当前数字即可
                    temp += integer;
                }
            }
            index++;
        }
        return String.valueOf(temp);
    }

    /**
     * 判断传入的字符串是否全是汉字数字
     *
     * @param chineseStr 中文
     * @return 是否全是中文数字
     */
    private static boolean isChineseNum(String chineseStr) {
        char[] ch = chineseStr.toCharArray();
        String allChineseNum = "零一二三四五六七八九十百千万亿";
        for (char c : ch) {
            if (!allChineseNum.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(convertNumber("二十八万"));
    }

}
