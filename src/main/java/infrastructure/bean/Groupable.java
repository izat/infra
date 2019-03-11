package infrastructure.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author mxchen 2018/8/20 11:00
 */
public interface Groupable<R> {
    Object key();

    void setList(List<R> list);

    R construct();

    static <T extends Groupable<R>, R> List<T> grouping(List<T> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Groupable::key, LinkedHashMap::new, Collectors.toList()))
                .values().stream()
                .map(list -> {
                    T t = list.get(0);
                    List<R> collect = list.stream().map(Groupable::construct).filter(Objects::nonNull).collect(Collectors.toList());
                    if (!collect.isEmpty()) {
                        t.setList(collect);
                    }
                    return t;
                }).collect(Collectors.toList());
    }
}
