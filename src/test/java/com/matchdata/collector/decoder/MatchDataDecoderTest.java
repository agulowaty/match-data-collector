package com.matchdata.collector.decoder;

import com.matchdata.collector.MatchState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.matchdata.collector.MatchState.MatchStateEvent.matchStateEvent;
import static com.matchdata.collector.stream.DataEventListener.DataReceived.dataReceivedEvent;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MatchDataDecoderTest {

  @Mock
  private MatchState matchState;

  @InjectMocks
  private MatchDataDecoder decoder;

  @Test
  public void parsesPointScored() throws Exception {
    decoder.onDataReceived(dataReceivedEvent("0x781002"));

    verify(matchState).push(matchStateEvent().pointsScored(2).build());
  }

}