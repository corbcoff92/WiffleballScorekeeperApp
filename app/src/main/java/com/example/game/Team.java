package com.example.game;

import java.util.LinkedList;

/**
 * The Count class is used to represent a team in wiffleball. It is used to keep track 
 * of team-wide statistics such as total hits, runs, walks, and runs per inning. Used 
 * in the main {@link Game} class.
 */
class Team 
{
    /**
     * This team's name
     */
    String name;
    /**
     * This team's total number of hits 
     */
    int hits;
    /**
     * This team's total number of walks
     */
    int walks;
    private int runs;
    private LinkedList<Integer> runsPerInning;
    
    /**
     * Initializes a new blank Team with the given name. This team's runs, hits, and walks 
     * are set to zero. The {@code inningsRunsScored} begins as an empty list, so {@code newInning}
     * should be called for the first team at the beginning of the game. 
     * @param name  Name for the team that is to be created.
     */
    public Team(String name)
    {
        this.name = name;
        runs = 0;
        hits = 0;
        walks = 0;
        runsPerInning = new LinkedList<Integer>();
    }
    
    /**
     * This constructor is a copy constructor, which provides a way to create a copy of the given existing 
     * {@link Team} instance.
     * @param team  Team instance that is to be copied.
     */
    public Team(final Team team)
    {
        this.name = team.name;
        this.runs = team.runs;
        this.hits = team.hits;
        this.walks = team.walks;
        this.runsPerInning = new LinkedList<Integer>(team.runsPerInning);
    }

    /**
     * Implemenation of this team scoring a run.
     * This method should be called by the controlling class each time a run is scored, 
     * rather than accessing {@code runs} directly. This is done so that the number 
     * of runs for the current inning can be updated concurrently. 
     */
    public void scoreRun()
    {
        // Update total team runs
        runs++;

        // Update runs for current inning
        int inningRuns = runsPerInning.removeLast();
        inningRuns++;
        runsPerInning.add(inningRuns);
    }

    /**
     * Method used for accessing this team's total number of runs. 
     * The total number of runs are not directly accessible so that 
     * the runs per inning can be automatically updated concurrently.
     * The teams total number of runs should be updated using {@code scoreRun}
     * from the controlling class each time a run is scored.
     * @return  Total number of runs this team has scored.
     */
    public int getRuns() {return runs;}
    
    /**
     * Creates a new inning to be tracked for this team. 
     * This method should be called each time a new inning needs to be tracked.
     */
    public void newInning()
    {
        runsPerInning.add(0);
    }

    /**
     * Method used for accessing this team's number of runs per inning. 
     * The {@code runsPerInning} member is not directly accessible so that 
     * it can be automatically updated whenever this team scores a run, using {@code scoreRun}.
     * @return Linked list, with each element being the number of runs that this team scored during an inning.
     */
    public LinkedList<Integer> getRunserPerInning() { return runsPerInning; }
}
