package com.matchdata.collector;

import com.matchdata.collector.MatchState.MatchStateEvent;

import java.util.ArrayList;
import java.util.List;

import static com.matchdata.collector.MatchState.MatchStateEvent.matchStateEvent;

class EventTimeLine {

  private List<MatchStateEvent> events = new ArrayList<>();

  private EventTimeLine(int elapsed) {
  }

  static EventTimeLine startAt(int secs) {
    return new EventTimeLine(secs);
  }

  OngoingEventProducing at(int secs) {
    return new OngoingEventProducing(secs);
  }

  void pushEventsTo(MatchState matchState) {
    events.forEach(e -> matchState.push(e));
  }

  class OngoingEventProducing {

    private final int elapsed;
    private int team;
    private int secondTeam;
    private int points;
    private int firstTeam;

    private OngoingEventProducing(int elapsed) {
      this.elapsed = elapsed;
    }

    OngoingEventProducing teamScores(int team, int points) {
      this.team = team;
      this.points = points;
      return this;
    }

    OngoingEventProducing totals(int firstTeam, int secondTeam) {
      this.firstTeam = firstTeam;
      this.secondTeam = secondTeam;
      return this;
    }

    EventTimeLine andThen() {
      addCurrentOne();
      return EventTimeLine.this;
    }

    EventTimeLine andThen(MatchStateEvent event) {
      addCurrentOne();
      events.add(event);
      return EventTimeLine.this;
    }

    private void addCurrentOne() {
      events.add(matchStateEvent()
              .elapsedSeconds(elapsed)
              .pointsScored(points)
              .team(team)
              .totalFirstTeam(firstTeam)
              .totalSecondTeam(secondTeam)
              .build());
    }
  }

}
