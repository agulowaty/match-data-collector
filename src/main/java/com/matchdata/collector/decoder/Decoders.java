package com.matchdata.collector.decoder;

import com.matchdata.collector.MatchState;
import com.matchdata.collector.stream.DataEventListener;

public final class Decoders {
  private Decoders() {
  }

  public static DataEventListener decodeAndSave(MatchState matchState) {
    return new MatchDataDecoder(matchState);
  }
}
