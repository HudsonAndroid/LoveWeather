package com.hudson.loveweather.utils.LogUtils;


import com.hudson.loveweather.utils.LogUtils.parser.Parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static com.hudson.loveweather.utils.LogUtils.Constants.BR;

/**
 * 对象string化
 */
public class ObjectUtil {

    /**
     * 将对象转化为String
     *
     * @param object
     * @return
     */
    public static String objectToString(Object object) {
        return objectToString(object, 0);
    }

    /**
     * 是否为静态内部类
     *
     * @param cla
     * @return
     */
    public static boolean isStaticInnerClass(Class cla) {
        if (cla != null && cla.isMemberClass()) {
            int modifiers = cla.getModifiers();
            if ((modifiers & Modifier.STATIC) == Modifier.STATIC) {
                return true;
            }
        }
        return false;
    }

    public static String objectToString(Object object, int childLevel) {
        if (object == null) {
            return Constants.STRING_OBJECT_NULL;
        }
        if (childLevel > Constants.MAX_CHILD_LEVEL) {
            return object.toString();
        }
        List<Parser> parserList = LogConfig.getInstance().getParseList();
        if (parserList != null && parserList.size() > 0) {
            for (Parser parser : parserList) {
                if (parser.parseClassType().isAssignableFrom(object.getClass())) {
                    return parser.parseString(object);
                }
            }
        }
        if (ArrayUtil.isArray(object)) {
            return ArrayUtil.parseArray(object);
        }
        if (object.toString().startsWith(object.getClass().getName() + "@")) {
            StringBuilder builder = new StringBuilder();
            getClassFields(parserList,object.getClass(), builder, object, false, childLevel);
            Class superClass = object.getClass().getSuperclass();
            if (superClass != null) {
                while (!superClass.equals(Object.class)) {
                    getClassFields(parserList,superClass, builder, object, true, childLevel);
                    superClass = superClass.getSuperclass();
                }
            } else {
                builder.append(object.toString());
            }
            return builder.toString();
        } else {
            // 若对象重写toString()方法默认走toString()
            return object.toString();
        }
    }

    /**
     * 拼接class的字段和值
     *
     * @param cla
     * @param builder
     * @param o           对象
     * @param isSubClass  死否为子class
     * @param childOffset 递归解析属性的层级
     */
    private static void getClassFields(List<Parser> parserList,Class cla, StringBuilder builder, Object o, boolean isSubClass,
                                       int childOffset) {
        if (cla.equals(Object.class)) {
            return;
        }
        if (isSubClass) {
            builder.append(BR + BR + "=> ");
        }
//        String breakLine = childOffset == 0 ? BR : "";
        String breakLine = "";
        builder.append(cla.getSimpleName() + " {");
        Field[] fields = cla.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);
            if (cla.isMemberClass() && !isStaticInnerClass(cla) && i == 0) {
                continue;
            }
            Object subObject = null;
            try {
                subObject = field.get(o);
            } catch (IllegalAccessException e) {
                subObject = e;
            } finally {
                if (subObject != null) {
                    if (!isStaticInnerClass(cla) && (field.getName().equals("$change") || field.getName().equalsIgnoreCase("this$0"))) {
                        continue;
                    }
                    if (subObject instanceof String) {
                        subObject = "\"" + subObject + "\"";
                    } else if (subObject instanceof Character) {
                        subObject = "\'" + subObject + "\'";
                    }
                    if (childOffset < Constants.MAX_CHILD_LEVEL) {
                        subObject = objectToString(subObject, childOffset + 1);
                    }
                }
                String formatString = breakLine + "%s = %s, ";
                builder.append(String.format(formatString, field.getName(),
                        subObject == null ? "null" : subObject.toString()));
            }
        }
        if (builder.toString().endsWith("{")) {
            builder.append("}");
        } else {
            builder.replace(builder.length() - 2, builder.length() - 1, breakLine + "}");
        }
    }
}
