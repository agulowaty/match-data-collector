package com.matchdata.collector;

import com.matchdata.collector.MatchState.MatchStateEvent;
import org.junit.Test;

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
    MatchStateEvent event = matchStateEvent().elapsedSeconds(1).pointsScored(1).team(1).totalFirstTeam(1).totalSecondTeam(1).build();

    matchState.push(event);
    matchState.push(event);

    assertThat(matchState.allEvents()).hasSize(1);
  }
}