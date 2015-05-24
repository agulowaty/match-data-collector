package com.matchdata.collector;

import com.matchdata.collector.stream.DataEventListener;
import com.matchdata.collector.stream.MatchEventStreams;

public class App {
  public static void main(String[] args) {
    MatchEventStreams.fromFile(new DataEventListener() {
      @Override
      public void onDataReceived(DataReceived event) {
        System.out.println("event = " + event);
      }
    }, fromClassPath("sample1.txt")).process();
  }

  private static String fromClassPath(String fileName) {
    return ClassLoader.getSystemResource(fileName).getPath();
  }
}
