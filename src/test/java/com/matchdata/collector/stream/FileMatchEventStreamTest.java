package com.matchdata.collector.stream;

import org.junit.Test;

import java.util.List;

import static com.matchdata.collector.stream.DataEventListener.DataReceived.dataReceivedEvent;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FileMatchEventStreamTest {

  private FileMatchEventStream eventStream;
  private DataEventListener dataEventListener = mock(DataEventListener.class);
  private List<String> expectedIntegersInSample = asList("0x801002", "0xf81016", "0x1d8102f");

  @Test
  public void readsFileLinesAndSendsParsedIntegersDownstream() throws Exception {
    eventStream = new FileMatchEventStream(pathTo("sample1.txt"), dataEventListener);

    eventStream.process();

    expectedIntegersInSample.forEach(ei -> {
      verify(dataEventListener).onDataReceived(eq(dataReceivedEvent(ei)));
    });
  }

  @Test
  public void skipsEmptyLinesFromStream() throws Exception {
    eventStream = new FileMatchEventStream(pathTo("withEmptyLines.txt"), dataEventListener);

    eventStream.process();

    verify(dataEventListener, atMost(1)).onDataReceived(any(DataEventListener.DataReceived.class));
  }

  private String pathTo(String fileName) {
    return ClassLoader.getSystemResource(fileName).getPath();
  }
}