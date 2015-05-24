package com.matchdata.collector.decoder;

import com.matchdata.collector.MatchState;
import com.matchdata.collector.stream.DataEventListener;

import static com.matchdata.collector.MatchState.MatchStateEvent.matchStateEvent;

public class MatchDataDecoder implements DataEventListener {

  private final MatchState matchState;

  MatchDataDecoder(MatchState matchState) {
    this.matchState = matchState;
  }

  @Override
  public void onDataReceived(DataReceived event) {
    String data = event.data();
    matchState.push(parseToEvent(Integer.decode(data)));
  }

  private MatchState.MatchStateEvent parseToEvent(int dataAsInt) {
    return matchStateEvent()
              .pointsScored(BitPatternMask.POINTS_SCORED.applyTo(dataAsInt))
              .team(BitPatternMask.TEAM.applyTo(dataAsInt))
              .build();
  }

  private enum BitPatternMask {
    POINTS_SCORED(0b11),
    TEAM(0b100);
    private final int mask;

    BitPatternMask(int mask) {
      this.mask = mask;
    }

    public int applyTo(int num) {
      return num & mask;
    }
  }

}
