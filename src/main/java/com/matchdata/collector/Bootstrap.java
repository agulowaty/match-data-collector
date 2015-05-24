package com.matchdata.collector;

import com.matchdata.collector.stream.MatchEventStreams;

import static com.matchdata.collector.decoder.Decoders.decodeAndSave;

public class Bootstrap {

  private final MatchState matchState = new MatchState();

  Bootstrap() {
  }

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  void run() {
    MatchEventStreams.fromFile(decodeAndSave(matchState), classPathFile("sample1.txt")).process();
  }

  private static String classPathFile(String fileName) {
    return ClassLoader.getSystemResource(fileName).getPath();
  }
}
