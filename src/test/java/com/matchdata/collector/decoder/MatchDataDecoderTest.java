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

  @Mock
  private MatchState matchState;

  @InjectMocks
  private MatchDataDecoder decoder;
  private String rawData = "0x781002";

  @Test
  public void parsesPointScored() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(rawData));

    assertThat(pushSent().pointsScored()).isEqualTo(2);
  }

  @Test
  public void parsesTeamWhoScoredPoint() throws Exception {
    decoder.onDataReceived(dataReceivedEvent(rawData));

    assertThat(pushSent().team()).isEqualTo(0);
  }

  private MatchStateEvent pushSent() {
    ArgumentCaptor<MatchStateEvent> argumentCaptor = ArgumentCaptor.forClass(MatchStateEvent.class);
    verify(matchState).push(argumentCaptor.capture());
    return argumentCaptor.getValue();
  }
}