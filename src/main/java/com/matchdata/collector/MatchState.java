package com.matchdata.collector;

import com.google.auto.value.AutoValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.summingInt;

public class MatchState {
  private final static Logger logger = LoggerFactory.getLogger(MatchState.class);
  private Deque<MatchStateEvent> events = new LinkedList<>();

  public void push(MatchStateEvent matchStateEvent) {
//    if (!events.isEmpty()) {
//      checkElapsedTime(matchStateEvent);
//      checkTotalCountConsistency(matchStateEvent);
//    }

    if (!events.contains(matchStateEvent)) {
      events.addLast(matchStateEvent);
      logger.info("Added new event: " + matchStateEvent.toString());
    }
  }

  private void checkTotalCountConsistency(MatchStateEvent matchStateEvent) {
    Integer pointsScored = events.stream()
            .filter(e -> e.team() == matchStateEvent.team())
            .collect(summingInt(e -> e.pointsScored()));
    if (pointsScored + matchStateEvent.pointsScored() != matchStateEvent.totalForTeam()) {
    }
  }

  private void checkElapsedTime(MatchStateEvent matchStateEvent) {
    if (events.peekLast().elapsedSeconds() > matchStateEvent.elapsedSeconds()) {
    }
  }

  public Collection<MatchStateEvent> allEvents() {
    return unmodifiableCollection(events);
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

    public int totalForTeam() {
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
