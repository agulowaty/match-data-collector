package com.matchdata.collector.decoder;

import com.matchdata.collector.MatchState;
import com.matchdata.collector.stream.DataEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.matchdata.collector.MatchState.MatchStateEvent.matchStateEvent;

public class MatchDataDecoder implements DataEventListener {

  private final MatchState matchState;
  private static final Logger logger = LoggerFactory.getLogger(MatchDataDecoder.class);

  MatchDataDecoder(MatchState matchState) {
    this.matchState = matchState;
  }

  @Override
  public void onDataReceived(DataReceived event) {
    String data = event.data();
    matchState.push(parseToEvent(Integer.decode(data)));
  }

  private MatchState.MatchStateEvent parseToEvent(int data) {
    return matchStateEvent()
            .pointsScored(BitPatternMask.POINTS_SCORED.applyTo(data))
            .team(BitPatternMask.TEAM.applyTo(data) + 1)
            .totalFirstTeam(BitPatternMask.TOTAL_FIRST_TEAM.applyTo(data))
            .totalSecondTeam(BitPatternMask.TOTAL_SECOND_TEAM.applyTo(data))
            .elapsedSeconds(BitPatternMask.ELAPSED_SECONDS.applyTo(data))
            .build();
  }

  private enum BitPatternMask {
    POINTS_SCORED(0b11, 0),
    TEAM(0b1_00, 2),
    TOTAL_SECOND_TEAM(0b11111111_0_00, 3),
    TOTAL_FIRST_TEAM(0b11111111_00000000_0_00, 11),
    ELAPSED_SECONDS(0b111111111111_00000000_00000000_0_00, 19);

    private final int mask;
    private final int shift;

    BitPatternMask(int mask, int shift) {
      this.mask = mask;
      this.shift = shift;
    }

    public int applyTo(int num) {
      return (num & mask) >> shift;
    }
  }

}
