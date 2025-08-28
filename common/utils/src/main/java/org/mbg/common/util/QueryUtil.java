/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.mbg.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 *
 * @author linhlh2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryUtil {

    public static final Long ALL = -1L;

    public static final int FIRST_INDEX = 0;

    public static final int PAGE_SIZE_DEFAULT = 10;
    
    public static final int PAGE_SIZE_MAX = 100;

    public static final int MAX_RESULT = 20;
    
    public static final Integer DEFAULT_BATCH_INDEX_SIZE = 50;
    
    public static final Integer DEFAULT_FETCH_SIZE = 50;

    public static final String ASC = StringPool.ASC;

    public static final String DESC = StringPool.DESC;

    private static final String HIBERNATE_ESCAPE_CHAR = StringPool.BACK_SLASH;

    public static String addOrder(Class<?> c, String orderByColumn, String orderByType, String alias) {
        StringBuilder sb = new StringBuilder(6);

        if (ReflectionUtil.hasProperty(c, orderByColumn)) {
            sb.append(" order by ");

            if (Objects.nonNull(alias)) {
                sb.append(alias);
                sb.append(StringPool.PERIOD);
            }

            sb.append(orderByColumn);
            sb.append(StringPool.SPACE);

            sb.append(orderByType);
        }

        return sb.toString();
    }

    public static String createOrderQuery(Class<?> entityClass, String orderByType, String orderByColumn) {
        if (Validator.isNull(orderByType) || Validator.isNull(orderByColumn)) {
            return "order by e.lastModifiedDate desc";
        }

        StringBuilder sql = new StringBuilder();

        orderByType = QueryUtil.ASC.equalsIgnoreCase(orderByType) ? QueryUtil.ASC : QueryUtil.DESC;

        sql.append(addOrder(entityClass, orderByColumn, orderByType, "e"));

        if (sql.length() <= 0) {
            sql.append(" order by e.lastModifiedDate desc");
        } else if (!orderByColumn.contains("lastModifiedDate")) {
            sql.append(", e.lastModifiedDate desc");
        }

        return sql.toString();

    }

    public static String getFullStringParam(String param) {
        StringBuilder sb = new StringBuilder(5);

        param = StringUtil.trim(param);

        sb.append(StringPool.PERCENT);
        sb.append(replaceSpecialCharacter(param));
        sb.append(StringPool.PERCENT);

        return sb.toString();
    }

    public static String getFullWildcardParam(String param) {
        StringBuilder sb = new StringBuilder(5);

        param = StringUtil.trim(param);

        sb.append(StringPool.STAR);
        sb.append(replaceSpecialCharacter(param).toLowerCase());
        sb.append(StringPool.STAR);

        return sb.toString();
    }

    public static String getLeftStringParam(String param) {
        StringBuilder sb = new StringBuilder(2);

        param = StringUtil.trim(param);

        sb.append(StringPool.PERCENT);
        sb.append(replaceSpecialCharacter(param));

        return sb.toString();
    }

    public static String getLeftWildcardParam(String param) {
        StringBuilder sb = new StringBuilder(2);

        param = StringUtil.trim(param);

        sb.append(StringPool.STAR);
        sb.append(replaceSpecialCharacter(param).toLowerCase());

        return sb.toString();
    }

    public static String getRightStringParam(String param) {
        StringBuilder sb = new StringBuilder(2);

        param = StringUtil.trim(param);

        sb.append(replaceSpecialCharacter(param));
        sb.append(StringPool.PERCENT);

        return sb.toString();
    }

    public static String getRightWildcardParam(String param) {
        StringBuilder sb = new StringBuilder(2);

        param = StringUtil.trim(param);

        sb.append(replaceSpecialCharacter(param).toLowerCase());
        sb.append(StringPool.STAR);

        return sb.toString();
    }

    public static String getStringParam(String param) {
        StringBuilder sb = new StringBuilder(1);

        param = StringUtil.trim(param);

        sb.append(replaceSpecialCharacter(param));

        return sb.toString();
    }

    private static String replaceSpecialCharacter(String param) {
        return param.replace(StringPool.PERCENT, HIBERNATE_ESCAPE_CHAR + StringPool.PERCENT)
                    .replace(StringPool.UNDERLINE, HIBERNATE_ESCAPE_CHAR + StringPool.UNDERLINE)
                    .replace(StringPool.EXCLAMATION, HIBERNATE_ESCAPE_CHAR + StringPool.EXCLAMATION);
    }
}
