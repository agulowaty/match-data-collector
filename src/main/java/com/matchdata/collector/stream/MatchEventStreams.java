package com.matchdata.collector.stream;

public final class MatchEventStreams {
  private MatchEventStreams() {
  }

  public static MatchEventStream fromFile(DataEventListener dataEventListener, String fileName) {
    return new FileMatchEventStream(fileName, dataEventListener);
  }
}
