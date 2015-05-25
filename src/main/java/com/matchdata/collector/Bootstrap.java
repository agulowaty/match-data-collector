package com.matchdata.collector;

import com.matchdata.collector.stream.MatchEventStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.matchdata.collector.decoder.Decoders.decodeAndSave;

public class Bootstrap {

  private final MatchState matchState = new MatchState();
  private final static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

  Bootstrap() {
  }

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  void run() {
    final String fileName = "sample2.txt";
    // final String fileName = "sample1.txt";
    MatchEventStreams.fromFile(decodeAndSave(matchState), classPathFile(fileName)).process();
    logger.info("LAST: " + matchState.lastEvent());
    logger.info("ALL: " + matchState.allEvents());
    logger.info("LAST 5: " + matchState.lastN(5));
  }

  private static String classPathFile(String fileName) {
    return ClassLoader.getSystemResource(fileName).getPath();
  }
}
