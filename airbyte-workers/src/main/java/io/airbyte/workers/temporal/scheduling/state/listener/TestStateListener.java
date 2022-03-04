/*
 * Copyright (c) 2021 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.workers.temporal.scheduling.state.listener;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestStateListener implements WorkflowStateChangedListener {

  private static final ConcurrentHashMap<UUID, ConcurrentLinkedQueue<ChangedStateEvent>> events = new ConcurrentHashMap<>();

  public static void reset() {
    events.clear();
  }

  @Override
  public Queue<ChangedStateEvent> events(final UUID testId) {
    if (!events.containsKey(testId)) {
      return new LinkedList<>();
    }

    return events.get(testId);
  }

  @Override
  public void addEvent(final UUID testId, final ChangedStateEvent event) {
    ConcurrentLinkedQueue<ChangedStateEvent> eventQueueForTestId = events.get(testId);
    if (eventQueueForTestId == null) {
      eventQueueForTestId = new ConcurrentLinkedQueue<>();
    }
    eventQueueForTestId.add(event);
    events.put(testId, eventQueueForTestId);
  }

}
