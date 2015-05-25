package com.matchdata.collector;

import com.matchdata.collector.MatchState.MatchStateEvent;
import org.junit.Test;

import java.util.Comparator;

import static com.matchdata.collector.EventTimeLine.startAt;
import static com.matchdata.collector.MatchState.MatchStateEvent.matchStateEvent;
import static org.assertj.core.api.Assertions.assertThat;

public class MatchStateTest {

  private MatchState matchState = new MatchState();

  @Test
  public void savesNewEvent() throws Exception {
    MatchStateEvent event = matchStateEvent().elapsedSeconds(1).pointsScored(1).team(1).totalFirstTeam(1).totalSecondTeam(1).build();

    matchState.push(event);

    assertThat(matchState.allEvents()).contains(event);
  }

  @Test
  public void discardsDuplicatedEvents() throws Exception {
    startAt(1)
            .at(1).teamScores(1, 1).totals(1, 1).andThen()
            .at(1).teamScores(1, 1).totals(1, 1).andThen().pushEventsTo(matchState);

    assertThat(matchState.allEvents()).hasSize(1);
  }

  @Test
  public void addsEventToProperPositionIfOutOfOrder() throws Exception {
    startAt(1)
            .at(9).teamScores(1, 1).totals(1, 5).andThen()
            .at(3).teamScores(2, 5).totals(0, 5).andThen()
            .at(17).teamScores(2, 9).totals(1, 14).andThen()
            .pushEventsTo(matchState);

    assertThat(matchState.allEvents()).isSortedAccordingTo(Comparator.comparingInt(e -> e.elapsedSeconds()));
  }

  @Test
  public void discardsEventsWithInconsistentTotals() throws Exception {
    MatchStateEvent inconsistent = matchStateEvent().elapsedSeconds(69).team(1).pointsScored(2).totalFirstTeam(3).totalSecondTeam(6).build();
    startAt(67)
            .at(68).teamScores(1, 4).totals(4, 6).andThen(inconsistent)
            .pushEventsTo(matchState);

    assertThat(matchState.allEvents()).doesNotContain(inconsistent);
  }
}