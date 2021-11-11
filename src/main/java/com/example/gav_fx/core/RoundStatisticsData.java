package com.example.gav_fx.core;

public class RoundStatisticsData {
    
    private final int roundNumber;
    private final long totalTimeElapsed;
    private final int threadCount; // how many threads were active in given round
    
    public RoundStatisticsData(int roundNumber, long totalTimeElapsed, int threadCount) {
        this.roundNumber = roundNumber;
        this.totalTimeElapsed = totalTimeElapsed;
        this.threadCount = threadCount;
    }
    
    public int getRoundNumber() {return roundNumber;}
    
    public long getTotalTimeElapsed() {return totalTimeElapsed;}
    
    public int getThreadCount() {return threadCount;}
}
