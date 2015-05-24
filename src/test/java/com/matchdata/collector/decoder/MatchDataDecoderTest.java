package com.matchdata.collector.decoder;

import com.matchdata.collector.MatchState;
import com.matchdata.collector.MatchState.MatchStateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.matchdata.collector.stream.DataEventListener.DataReceived.dataReceivedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MatchDataDecoderTest {
  private static final String AFTER_15_SECS = "0x781002"; // Team 1 scores 2 points
  private static final String AFTER_30_SECS = "0xf0101f"; // Team 2 replies with 3 points
  @Mock
  private MatchState matchState;
  @InjectMocks
  private MatchDataDecoder decoder;

  @Test
  public void parsesPointScored() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_15_SECS));

    assertThat(pushSent().pointsScored()).isEqualTo(2);
  }

  @Test
  public void parsesTeamWhoScoredPoint() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_15_SECS));

    assertThat(pushSent().team()).isEqualTo(1);
  }

  @Test
  public void parsesAnotherTeam() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_30_SECS));

    assertThat(pushSent().team()).isEqualTo(2);
  }

  @Test
  public void parsesTotalsAfter30Secs() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_30_SECS));

    assertThat(pushSent().totalSecondTeam()).isEqualTo(3);
    assertThat(pushSent().totalFirstTeam()).isEqualTo(2);
  }

  @Test
  public void parsesTotalsAfter15Secs() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_15_SECS));

    assertThat(pushSent().totalFirstTeam()).isEqualTo(2);
    assertThat(pushSent().totalSecondTeam()).isEqualTo(0);
  }

  @Test
  public void parsesElapsed15Secs() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_15_SECS));

    assertThat(pushSent().elapsedSeconds()).isEqualTo(15);
  }

  @Test
  public void parsesElapsed30Secs() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(AFTER_30_SECS));

    assertThat(pushSent().elapsedSeconds()).isEqualTo(30);
  }

  private MatchStateEvent pushSent() {
    ArgumentCaptor<MatchStateEvent> argumentCaptor = ArgumentCaptor.forClass(MatchStateEvent.class);
    verify(matchState).push(argumentCaptor.capture());
    return argumentCaptor.getValue();
  }
}