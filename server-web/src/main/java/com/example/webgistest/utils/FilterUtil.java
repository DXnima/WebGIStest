package com.example.webgistest.utils;

import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.IsEqualsToImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

import java.io.UnsupportedEncodingException;

/**
 *解决GeoTools中CQL解析中文字段名的问题
 * @author wnm
 * @date 2021/6/11
 * @param
 * @return
 */
public class FilterUtil {

    @SuppressWarnings("unchecked")
    public static void decodeFilter(Filter filter, String fieldName)
            throws UnsupportedEncodingException {
        if (filter instanceof IsEqualsToImpl) {
            IsEqualsToImpl impl = (IsEqualsToImpl) filter;
            decodeExpression(impl.getExpression1(),fieldName);
        }
    }

    private static void decodeExpression(Expression exp, String fieldName)
            throws UnsupportedEncodingException {

        if (exp instanceof AttributeExpressionImpl)
        {
            AttributeExpressionImpl impl = (AttributeExpressionImpl) exp;
            impl.setPropertyName(fieldName);
        }
    }
}
