package com.prodigal.commons;

import com.google.common.base.CaseFormat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.StringJoiner;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
public class SqlUtils {

    /**
     * sql添加分页
     * <p>
     * prefix 为空时不给null，而是使用 ""
     * </p>
     *
     * @param pageable 分页
     * @return 返回sql
     */
    public static String applyPage(Pageable pageable) {
        StringBuilder sqlBuilder = applySort(pageable.getSort());
        sqlBuilder.append(" limit ").append(pageable.getOffset())
                .append(",").append(pageable.getPageSize());
        return sqlBuilder.toString();
    }

    public static StringBuilder applySort(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return new StringBuilder(" ORDER BY A.id desc ");
        }
        StringJoiner sortSql = new StringJoiner(" , ");
        sort.iterator().forEachRemaining((o) -> {
            //保留前缀的大小写
            String[] split = o.getProperty().split("\\.");
            String sortedPropertyName;
            if (split.length == 2) {
                sortedPropertyName = underLineToCamel(split[1]);
                sortedPropertyName = split[0] + "." + sortedPropertyName;
            } else {
                sortedPropertyName = underLineToCamel(split[0]);
            }
            String sortedProperty = o.isIgnoreCase() ? "LOWER(" + sortedPropertyName + ")"
                    : sortedPropertyName;
            sortSql.add(sortedProperty + (o.isAscending() ? " ASC" : " DESC"));
        });
        return new StringBuilder(" ORDER BY " + sortSql);
    }

    private static String underLineToCamel(String source) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, source);
    }

    /**
     * 拼接and( a =1 or a=2)类型的sql语句
     *
     * @param sql     需拼接的sql
     * @param str     判断的字符
     * @param sqlName 数据库字段名称
     */
    public static void andOrSql(StringBuilder sql, String str, String sqlName) {
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                sql.append(" and (").append(sqlName).append(" like '%").append(split[i]).append("%'");
            }
            if (i == split.length - 1) {
                sql.append(" or ").append(sqlName).append(" like '%").append(split[i]).append("%')");
            } else {
                sql.append(" or ").append(sqlName).append(" like '%").append(split[i]).append("%'");
            }
        }
    }
}