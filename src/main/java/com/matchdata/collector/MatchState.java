package com.matchdata.collector;

import com.google.auto.value.AutoValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

public class MatchState {
  private final static Logger logger = LoggerFactory.getLogger(MatchState.class);
  private final LinkedList<MatchStateEvent> events;

  public MatchState() {
    events = new LinkedList<>();
  }

  public void push(MatchStateEvent incomingEvent) {
    if (isDuplicated(incomingEvent)) {
      logger.warn("Duplicated event: " + incomingEvent);
      return;
    }
    addElement(incomingEvent);
  }

  private void addElement(MatchStateEvent incomingEvent) {
    if (!totalConsistent(incomingEvent)) {
      logger.warn("Event inconsistent in totals dropped: " + incomingEvent);
      return;
    }
    if (outOfOrder(incomingEvent)) {
      Optional<MatchStateEvent> eventToAddBefore = lastEventSuchThat(e -> e.elapsedSeconds() > incomingEvent.elapsedSeconds());
      int addAt = eventToAddBefore.map(e -> events.lastIndexOf(e)).orElse(events.size());
      events.add(addAt, incomingEvent);
    } else {
      events.add(incomingEvent);
    }
    logger.info("Added new event: " + incomingEvent.toString());
  }

  private boolean outOfOrder(MatchStateEvent incomingEvent) {
    return !events.isEmpty() && events.getLast().elapsedSeconds() > incomingEvent.elapsedSeconds();
  }

  private boolean isDuplicated(MatchStateEvent incomingEvent) {
    return !events.isEmpty() && events.getLast().equals(incomingEvent);
  }

  private boolean totalConsistent(MatchStateEvent incomingEvent) {
    Optional<MatchStateEvent> found = lastEventSuchThat(e -> e.team() == incomingEvent.team());
    return found
            .map(e -> e.totalOfScoringTeam() + incomingEvent.pointsScored() == incomingEvent.totalOfScoringTeam())
            .orElse(true);
  }

  private Optional<MatchStateEvent> lastEventSuchThat(Predicate<MatchStateEvent> predicate) {
    Iterator<MatchStateEvent> it = events.descendingIterator();
    MatchStateEvent found = null;
    while (it.hasNext() && found == null) {
      MatchStateEvent event = it.next();
      if (predicate.test(event)) {
        found = event;
      }
    }
    return Optional.ofNullable(found);
  }

  public List<MatchStateEvent> allEvents() {
    return Collections.unmodifiableList(events);
  }

  public Optional<MatchStateEvent> lastEvent() {
    return events.isEmpty() ? Optional.empty() : Optional.of(events.getLast());
  }

  public List<MatchStateEvent> lastN(int n) {
    ArrayList<MatchStateEvent> result = new ArrayList<>();
    Iterator<MatchStateEvent> iterator = events.descendingIterator();
    for (int i = 0; i < n && iterator.hasNext(); i++) {
      result.add(iterator.next());
    }
    return result;
  }

  @AutoValue
  public abstract static class MatchStateEvent {
    public static Builder matchStateEvent() {
      return new AutoValue_MatchState_MatchStateEvent.Builder();
    }

    public abstract int pointsScored();

    public abstract int team();

    public abstract int totalFirstTeam();

    public abstract int totalSecondTeam();

    public abstract int elapsedSeconds();

    public int totalOfScoringTeam() {
      return team() == 1 ? totalFirstTeam() : totalSecondTeam();
    }

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder pointsScored(int points);

      public abstract MatchStateEvent build();

      public abstract Builder team(int team);

      public abstract Builder totalFirstTeam(int total);

      public abstract Builder totalSecondTeam(int total);

      public abstract Builder elapsedSeconds(int secs);
    }
  }

}
