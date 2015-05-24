package com.matchdata.collector.stream;

import com.google.auto.value.AutoValue;

public interface DataEventListener {
  void onDataReceived(DataReceived event);

  @AutoValue
  abstract static class DataReceived {
    static DataReceived dataReceivedEvent(String rawData) {
      return new AutoValue_DataEventListener_DataReceived(rawData);
    }
    abstract String data();
  }
}
