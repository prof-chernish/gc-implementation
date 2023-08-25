package org.example;


import java.util.ArrayList;
import java.util.List;



public class GarbageCollectorImplementation implements GarbageCollector {
  private final List<ApplicationBean> garbage = new ArrayList<>();
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    for (ApplicationBean bean: heap.getBeans().values()) {
      fillGarbageList(bean);
    }
    List<ApplicationBean> beansInStack = stack.
            getStack()
            .stream()
            .flatMap(frame -> frame.getParameters().stream())
            .toList();

    for (ApplicationBean bean: beansInStack) {
      deleteFromGarbageList(bean);
    }

    return garbage;
  }

  private void fillGarbageList(ApplicationBean bean) {
    if (!garbage.contains(bean)) {
      garbage.add(bean);
      bean.getFieldValues()
              .forEach(
                      (key, value) -> {
                        fillGarbageList(value);
                      });

    }
  }

  private void deleteFromGarbageList(ApplicationBean bean) {
    if (garbage.contains(bean)) {
      garbage.remove(bean);
      bean.getFieldValues()
              .forEach(
                      (key, value) -> {
                        deleteFromGarbageList(value);
                      });
    }
  }
}

