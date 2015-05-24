package com.matchdata.collector;

import com.google.auto.value.AutoValue;

public class MatchState {

  public void push(MatchStateEvent matchStateEvent) {

  }

  @AutoValue
  public abstract static class MatchStateEvent {
    abstract int pointsScored();

    public static Builder matchStateEvent() {
      return new AutoValue_MatchState_MatchStateEvent.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder pointsScored(int points);
      public abstract MatchStateEvent build();
    }
  }

}
