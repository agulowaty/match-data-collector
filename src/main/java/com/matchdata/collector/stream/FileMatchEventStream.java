package com.matchdata.collector.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.matchdata.collector.stream.DataEventListener.DataReceived.dataReceivedEvent;

public class FileMatchEventStream implements MatchEventStream {
  private String fileName;
  private final DataEventListener dataEventListener;

  FileMatchEventStream(String fileName, DataEventListener dataEventListener) {
    this.fileName = fileName;
    this.dataEventListener = dataEventListener;
  }

  @Override
  public void process() {
    try {
      try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
        lines.filter(l -> !l.isEmpty()).forEach(l -> dataEventListener.onDataReceived(dataReceivedEvent(l)));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
