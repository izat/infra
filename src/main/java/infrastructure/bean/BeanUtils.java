package infrastructure.bean;

import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BeanUtils extends org.springframework.beans.BeanUtils {
    /**
     * 根据 list 获取 key set 以便查询
     *
     * @param list        列表
     * @param keyFunction 获取key属性的方法
     * @param <T>         列表元素类型
     * @param <Key>       key类型
     * @return key set
     * @see BeanUtils#leftJoin(List, Function, List, Function)
     */
    public static <T, Key> Set<Key> getKeySet(List<T> list, Function<T, Key> keyFunction) {
        return list.stream().map(keyFunction).collect(Collectors.toSet());
    }

    /**
     * 根据 key 将相应的 left 和 right 组合成 pair 返回。
     * <p>
     * 此方法模拟 sql 查询的 join。参数中，left是左表的记录，right是右表的记录，使用相同的key值作为连接条件。
     * <p>
     * 此方法大约等价于：
     * <pre>
     * SELECT l, r FROM left
     * LEFT JOIN right
     * ON left.leftKey = right.rightKey
     * </pre>
     * <p>
     * 例如：考虑类型 Student 和 School，使用 student.schoolId 和 school.id 连接，此时应：
     * <ol><li>查询出 Student 列表 students
     * <li>调用 getKeySet(students, Student::getSchoolId) 获取 schoolIds
     * <li>根据 schoolIds 查出 School 列表 schools
     * <li>调用 leftJoin(students, Student::getSchoolId, schools, School::getId) 获得 Pair&lt;Student, School&gt; 列表
     * </p>
     *
     * @param leftList  左表记录
     * @param leftKey   坐标的key属性
     * @param rightList 右表记录
     * @param rightKey  右表的key属性
     * @param <Left>    左表类型
     * @param <Right>   右表类型
     * @param <Key>     key类型
     * @return pair of (left, right), right might be null
     */
    public static <Left, Right, Key> List<Pair<Left, Right>> leftJoin(
            List<Left> leftList, Function<Left, Key> leftKey,
            List<Right> rightList, Function<Right, Key> rightKey) {
        Map<Key, Right> paramMap = rightList.stream().collect(Collectors.toMap(rightKey, Function.identity()));
        return leftList.stream().map(l -> {
            Key k = leftKey.apply(l);
            Right r = paramMap.getOrDefault(k, null);
            return new Pair<>(l, r);
        }).collect(Collectors.toList());
    }


    public static <Left, Right, Key> List<Pair<Left, List<Right>>> leftJoinMany(
            List<Left> leftList, Function<Left, Key> leftKey,
            List<Right> rightList, Function<Right, Key> rightKey) {
        Map<Key, List<Right>> paramMap = rightList.stream().collect(Collectors.groupingBy(rightKey));
        return leftList.stream().map(l -> {
            Key k = leftKey.apply(l);
            List<Right> r = paramMap.getOrDefault(k, null);
            return new Pair<Left, List<Right>>(l, r);
        }).collect(Collectors.toList());
    }


    public static void copyUsingSetter(Object source, Object destination) {
        Class<?> src = source.getClass();
        Class<?> dest = destination.getClass();

        Arrays.stream(dest.getMethods())
                .filter(m -> m.getName().startsWith("set")
                        && !m.getName().equals("setMetaClass")) // ignore groovy meta class
                .forEach(setter -> {
                    String propertyName = setter.getName().substring(3);
                    getGetter(src, propertyName).ifPresent(getter -> {
                        try {
                            setter.invoke(destination, getter.invoke(source));
                        } catch (Exception ex) {
                            // ignore mismatched properties
//                            throw new RuntimeException(ex.getMessage() + ": " + propertyName, ex);
                        }
                    });
                });
    }

    private static Optional<Method> getGetter(Class<?> clazz, String propertyName) {
        try {
            return Optional.of(clazz.getMethod("get" + propertyName));
        } catch (NoSuchMethodException ex) { // bypass
        }
        try {
            return Optional.of(clazz.getMethod("is" + propertyName));
        } catch (NoSuchMethodException ex) { // bypass
        }
        return Optional.empty();
    }
}
