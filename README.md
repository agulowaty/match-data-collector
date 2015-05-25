Startup:
1. Compile using Maven ('mvn clean install')
2. Run main in Bootstrap

Remarks about implementation details:
1. Implementing elapsed seconds precise error handling requires implementing some timers in real-world live-data application.
2. In case stream will be parallel, MatchState needs proper synchronisation.