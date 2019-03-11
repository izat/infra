package infrastructure.bean;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class Events {

    public interface Event {
    }

    public interface EventListener<E extends Event> {

        Class<E> getEventClass();

        void onEvent(E event);
    }

    public static abstract class AbstractEventListener<E extends Event> implements EventListener<E> {
        private final Class<E> eventType;

        protected AbstractEventListener() {
            ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
            @SuppressWarnings("unchecked")
            Class<E> rawType = (Class<E>) superClass.getActualTypeArguments()[0];
            eventType = rawType;
            Events.registerListener(this);
        }

        @Override
        public Class<E> getEventClass() {
            return eventType;
        }
    }

    private Events() {
    }

    private static final Map<Class, Set<EventListener>> listenerMap = new HashMap<>();

    public static <E extends Event> void registerListener(EventListener<E> listener) {
        Class<E> clazz = listener.getEventClass();
        if (listenerMap.containsKey(clazz)) {
            listenerMap.get(clazz).add(listener);
        } else {
            Set<EventListener> listeners = new LinkedHashSet<>();
            listeners.add(listener);
            listenerMap.put(clazz, listeners);
        }
    }

    public static <E extends Event> void removeListerner(EventListener<E> listener) {
        Class<E> clazz = listener.getEventClass();
        if (listenerMap.containsKey(clazz)) {
            Set<EventListener> listeners = listenerMap.get(clazz);
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listenerMap.remove(clazz);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void publish(Event event) {
        Class clazz = event.getClass();
        if (listenerMap.containsKey(clazz)) {
            listenerMap.get(clazz).forEach(l -> l.onEvent(event));
        }
    }
}