package com.matchdata.collector;

import com.google.auto.value.AutoValue;

public class MatchState {

  public void push(MatchStateEvent matchStateEvent) {

  }

  @AutoValue
  public abstract static class MatchStateEvent {
    public static Builder matchStateEvent() {
      return new AutoValue_MatchState_MatchStateEvent.Builder();
    }

    public abstract int pointsScored();

    public abstract int team();

    public abstract int totalFirstTeam();

    public abstract int totalSecondTeam();

    public abstract int elapsedSeconds();

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder pointsScored(int points);

      public abstract MatchStateEvent build();

      public abstract Builder team(int team);

      public abstract Builder totalFirstTeam(int total);

      public abstract Builder totalSecondTeam(int total);

      public abstract Builder elapsedSeconds(int secs);
    }
  }

}
