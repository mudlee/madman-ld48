package hu.mudlee.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MessageBus {
  private static final Map<Event, List<Consumer<Object>>> consumers = new HashMap<>();
  private static final Map<Event, List<Runnable>> runnables = new HashMap<>();

  public static void broadcast(Event key, Object param) {
    if(!consumers.containsKey(key)) {
      return;
    }

    for (Consumer<Object> consumer : consumers.get(key)) {
      consumer.accept(param);
    }
  }

  public static void broadcast(Event key) {
    if(!runnables.containsKey(key)) {
      return;
    }

    for (Runnable runnable : runnables.get(key)) {
      runnable.run();
    }
  }

  public static void register(Event key, Consumer<Object> consumer) {
    consumers.putIfAbsent(key, new ArrayList<>());
    consumers.get(key).add(consumer);
  }

  public static void register(Event key, Runnable runnable) {
    runnables.putIfAbsent(key, new ArrayList<>());
    runnables.get(key).add(runnable);
  }
}
