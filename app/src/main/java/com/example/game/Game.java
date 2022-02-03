package com.example.game;


import java.util.LinkedList;
import java.util.HashMap;

/**
 * This main {@code Game} class is a representation of a game of wiffleball.
 * It makes use of the {@link Count} and {@link Team} classes also found in
 * the {@link com.example.game} package.
 */
public class Game {
    /**
     * Boolean flag indicating whether or not this game has ended.
     */
    public boolean isGameOver = false;
    /**
     * Boolean flag indicating whether or not this game is in a waiting state.
     */
    public boolean isWaiting = false;

    // Private members
    private boolean isTopOfInning;
    private String message;
    private int NUM_INNINGS;
    private int inning;
    private Count count;
    private Team homeTeam;
    private Team awayTeam;
    private Team battingTeam;
    private Team pitchingTeam;
    private int outs;
    private LinkedList<Integer> runners;

    /**
     * Initializes a newly started game against the provided team names, that will last the given number of innings.
     * Having the number of innings be an input parameter allows the game length to vary as desired.
     *
     * @param numInnings   The desired number of innings for the game to be played.
     * @param awayTeamName The desired name for the away team.
     * @param homeTeamName The desired name for the home team.
     */
    public Game(int numInnings, String awayTeamName, String homeTeamName) {
        NUM_INNINGS = numInnings;
        inning = 1;
        isTopOfInning = true;
        count = new Count();
        outs = 0;
        awayTeam = new Team(awayTeamName);
        homeTeam = new Team(homeTeamName);
        battingTeam = awayTeam;
        pitchingTeam = homeTeam;
        message = "Play Ball!";
        runners = new LinkedList<>();

        // Call new inning for the away team so that it 
        // can be tracked from the beginning of the game
        battingTeam.newInning();
    }

    /**
     * This constructor is a copy constructor, and provides a way to create a copy of the given existing
     * {@link Game} instance.
     *
     * @param game Game instance that is to be copied.
     */
    public Game(final Game game) {
        isGameOver = game.isGameOver;
        isWaiting = game.isWaiting;
        isTopOfInning = game.isTopOfInning;
        message = game.message;
        NUM_INNINGS = game.NUM_INNINGS;
        inning = game.inning;
        // Must create copies of Counts, Teams, and LinkedLists
        // because they are references to objects.
        count = new Count(game.count);
        homeTeam = new Team(game.homeTeam);
        awayTeam = new Team(game.awayTeam);
        // Set batting & pitching teams based on current inning half
        // Creating copies of batting & pitching teams would result in bugs
        // because they would point to different placed in memory, rather
        // than being pointers to the current teams.
        if (game.isTopOfInning) {
            battingTeam = awayTeam;
            pitchingTeam = homeTeam;
        } else {
            battingTeam = homeTeam;
            pitchingTeam = awayTeam;
        }
        outs = game.outs;
        runners = new LinkedList<>(game.runners);
    }

    /**
     * Implementation of a ball being called and the batter being walked if applicable.
     * This game's {@code Count} is increased by one ball, and if a walk is determined,
     * the runners are advanced, and the {@code isGameOver} condition is update. If the
     * game is not determined to be over, the count is reset.
     */
    public void callBall() {
        message = "Ball";
        count.balls++;

        // Check if batter has walked, and 
        // if so, advance the runner(s)
        if (count.checkWalk()) {
            message = "Walk";
            advanceRunnersWalk();
        }
    }

    /**
     * Implementation of a strike being called and the batter being struckout if applicable.
     * This game's {@code Count} is increased by one strike. If a strikeout is determined,
     * an out is recorded, the {@code isGameOver} condition is updated, and the count is reset.
     * If the game is to continue and the current half-inning has ended, a new half-inning is began.
     */
    public void callStrike() {
        message = "Strike";
        count.strikes++;
        if (count.checkStrikeout()) {
            message = "Struckout";
            outMade();
        }
    }

    /**
     * Implementation of all runners advancing by the given number of bases. This method is used for hits.
     * Any runners resulting in a score are recorded, and the {@code isGameOver}condition is updated. The
     * count is also reset
     *
     * @param numBases The total number of bases that each runner should advance as the result of a hit.
     */
    public void advanceRunnersHit(int numBases) {
        // Set hit type as game message based on the number of bases.
        switch (numBases) {
            case 1:
                message = "Single";
                break;
            case 2:
                message = "Double";
                break;
            case 3:
                message = "Triple";
                break;
            case 4:
                // If the bases were loaded, the homerun is a grand slam!
                message = (runners.size() != 3 ? "Homerun" : "Grand Slam");
                break;
        }
        battingTeam.hits++;

        // Advance each runner already on base
        for (int i = 0; i < runners.size(); i++) {
            runners.set(i, runners.get(i) + numBases);
        }
        // Add batter to the front of the list, 
        // keeping the list in order so that it is 
        // more efficient to determine which runners 
        // have scored 
        runners.addFirst(numBases);
        isWaiting = true;
        checkRunnersScored();
    }

    /**
     * Implementation of the runners being advanced by a walk,
     * with only forced runners advancing.
     */
    private void advanceRunnersWalk() {
        // Check for any runners that need to be advanced by a walk. 
        // If there is already a runner at the next base, he must also 
        // be advanced.
        int i = 0;
        while (i < runners.size() && runners.get(i) == i + 1) {
            runners.set(i, i + 2);
            i++;
        }
        // Add batter to the front of the list, 
        // keeping the list in order so that it is 
        // more efficient to determine which runners 
        // have scored 
        runners.addFirst(1);
        pitchingTeam.walks++;
        isWaiting = true;
        checkRunnersScored();
    }

    /**
     * Used to determine if any runners have crossed home plate,
     * resulting in a run for the currenty batting team.
     */
    private void checkRunnersScored() {
        int runsScored = 0;
        // Check for any runners who have crossed home plate, 
        // resulting in a run for the batting team
        while (!runners.isEmpty() && runners.getLast() >= 4) {
            runsScored++;
            battingTeam.scoreRun();
            runners.removeLast();
        }
        // Update game message indicating the number of runs that scored as a result of the play.
        if (runsScored > 0) {
            message += (", " + runsScored + (runsScored != 1 ? " runs" : " run") + " scored!");
        }
        checkGameOver();
    }

    /**
     * Implementation of an out being recorded via flyout. The out is recorded,
     * the {@code isGameOver} condition is updated, and the count is reset. If
     * the game is to continue and the current half-inning has ended, a new half-inning is
     * also began.
     */
    public void flyOut() {
        message = "Flyout";
        outMade();
    }

    /**
     * Implementation of an out being recorded via groundout. The out is recorded,
     * the {@code isGameOver} condition is updated, and the count is reset. If
     * the game is to continue and the current half-inning has ended, a new half-inning is
     * also began.
     */
    public void groundOut() {
        message = "Groundout";
        outMade();
    }

    /**
     * Implemenation of an out being made. This method records the out, checks the {@code isGameOver}
     * condition, and resets the count. If the game is to continue and the current half-inning has ended,
     * a new half-inning is also began.
     */
    void outMade() {
        outs++;
        isWaiting = true;
        checkGameOver();
    }

    /**
     * Used to determine whether or not the current inning has ended.
     * This is useful for determining the waiting state of the game.
     * @return {@code true} if the inning is over, {@code false} otherwise.
     */
    public boolean checkInningOver() {
        return outs >= 3;
    }

    /**
     * Moves the game forward if it is in a waiting state. The game pauses during significant
     * moments (such as the inning ending). This makes the game easier to follow. This method
     * must be called to continue this game.
     */
    public void continueGame() {
        if (isWaiting && !isGameOver) {
            // Check if half-inning is over
            if (checkInningOver()) {
                nextHalfInning();
                message += ", Batter up!";
            } else {
                message = "Batter up!";
            }
            count.reset();
            isWaiting = false;
        }
    }

    /**
     * Implementation of a new half-inning being started. The inning is advanced,
     * outs are reset, runners are cleard, and teams are switched.
     */
    void nextHalfInning() {
        outs = 0;
        // Top of inning
        if (isTopOfInning) {
            isTopOfInning = false;
            battingTeam = homeTeam;
            pitchingTeam = awayTeam;
            message = "Switch sides";
        }
        // Bottom of inning
        else {
            isTopOfInning = true;
            inning++;
            battingTeam = awayTeam;
            pitchingTeam = homeTeam;
            message = (inning != NUM_INNINGS + 1 ? "Next inning" : "Extra Innings");
        }
        runners.clear();
        battingTeam.newInning();
    }

    /**
     * Used to determine if the current game has ended. Sets the {@code isGameOver} flag.
     */
    void checkGameOver() {
        // Game can only end when the full number of innings have been played
        if (inning >= NUM_INNINGS) {
            // Only home team can win in the top of the inning
            if (isTopOfInning) {
                // If the home team is ahead in the top of the inning, 
                // no bottom of the inning is required
                if (outs >= 3 && homeTeam.getRuns() > awayTeam.getRuns()) {
                    isGameOver = true;
                    isWaiting = false;
                    message = "Home Team has won!";
                }
            } else {
                // If the bottom of the inning is played, and the home team wins,
                // then they have won by walkoff!
                if (homeTeam.getRuns() > awayTeam.getRuns()) {
                    isGameOver = true;
                    isWaiting = false;
                    message = "Walkoff " + message.split(",")[0] + ", Home Team has won!";
                } else {
                    // If the inning is over and the home team is losing,
                    // then the away team has won
                    if (outs >= 3 && homeTeam.getRuns() < awayTeam.getRuns()) {
                        isGameOver = true;
                        isWaiting = false;
                        message = "Away Team has won!";
                    }
                }
            }
        }
    }

    /**
     * Used to determine if it is the top or bottom of the inning.
     *
     * @return {@code boolean} indicating whether or not it is not top of the inning.
     */
    public boolean isTopInning() {
        return isTopOfInning;
    }

    /**
     * Used to determine the current inning number.
     *
     * @return Current inning number.
     */
    public int getInning() {
        return inning;
    }

    /**
     * Used to determine the number of balls in the current count.
     *
     * @return Number of balls in the current count.
     */
    public int getBalls() {
        return count.balls;
    }

    /**
     * Used to determine the number of strikes in the current count.
     *
     * @return Number of strikes in the current count.
     */
    public int getStrikes() {
        return count.strikes;
    }

    /**
     * Used to determine the number of outs in the current inning.
     *
     * @return Number of outs in the current inning.
     */
    public int getOuts() {
        return outs;
    }

    /**
     * Used to get the {@code String} name for this game's home team.
     *
     * @return String indicating the home team's name.
     */
    public String getHomeName() {
        return homeTeam.name;
    }

    /**
     * Used to determine the number of runs that the home team currently has.
     *
     * @return Number of runs for the home team.
     */
    public int getHomeRuns() {
        return homeTeam.getRuns();
    }

    /**
     * Used to determine the number of hits that the home team currently has.
     *
     * @return Number of hits for the home team.
     */
    public int getHomeHits() {
        return homeTeam.hits;
    }

    /**
     * Used to determine the number of walks that the home team has currently issued.
     *
     * @return Number of walks for the home team.
     */
    public int getHomeWalks() {
        return homeTeam.walks;
    }

    /**
     * Used to get the {@code String} name for this game's away team.
     *
     * @return String indicating the away team's name.
     */
    public String getAwayName() {
        return awayTeam.name;
    }

    /**
     * Used to determine the number of runs that the away team currently has.
     *
     * @return Number of runs for the away team.
     */
    public int getAwayRuns() {
        return awayTeam.getRuns();
    }

    /**
     * Used to determine the number of hits that the away team currently has.
     *
     * @return Number of hits for the away team.
     */
    public int getAwayHits() {
        return awayTeam.hits;
    }

    /**
     * Used to determine the number of walks that the away team has currently issued.
     *
     * @return Number of walks for the away team.
     */
    public int getAwayWalks() {
        return awayTeam.walks;
    }

    /**
     * Used to determine if a runner is currently occupying the base indicated by the given number.
     *
     * @param base Number indicating the base number to be checked.
     * @return {@code true} if there is a runner occupying the given base number, {@code false} otherwise
     */
    public boolean isRunnerOnBase(int base) {
        return runners.contains(base);
    }

    /**
     * Used to obtain this game's current message {@code String} to be displayed.
     *
     * @return String indicating this game's state and results.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Used to determine the number of innings that this game should last.
     *
     * @return Number of innings that the current game should last.
     */
    public int getNumInnings() {
        return NUM_INNINGS;
    }

    /**
     * Implementation of an action being undone, by setting this game's attributes using the given game's attributes contained
     * in the {@link HashMap}. The provided map contains the name of the action being undone, and a the {@link Game} option
     * from which the attributes should be set.
     *
     * @param state Map containing a String indicating the action that is being undone, and an instance of {@link Game}
     *              indicating the state of the game before the action occured. The current game's attributes are
     *              set using the game instance provided in the map.
     */
    public void undo(final HashMap<String, Game> state) {
        String action = state.keySet().iterator().next();
        Game game = state.get(action);
        message = "Undo: " + action;
        if (game != null)
            setState(game);
    }

    /**
     * Implementation of an action being redone, by setting this game's attributes using the given game's attributes contained
     * in the {@link HashMap}. The provided map contains the name of the action being redone, and a the {@link Game} option
     * from which the attributes should be set.
     *
     * @param state Map containing a String indicating the action that is being redone, and an instance of {@link Game}
     *              indicating the state of the game after the action occured. The current game's attributes are
     *              set using the game instance provided in the map.
     */
    public void redo(final HashMap<String, Game> state) {
        String action = state.keySet().iterator().next();
        Game game = state.get(action);
        message = "Redo: " + action;
        if (game != null)
            setState(game);
    }

    /**
     * Sets the attributes of this game based on the given game instance.
     * This method is used for the implementation of {@see undo} and {@see redo}.
     *
     * @param game {@code Game} instance from which this game's state should be set.
     */
    private void setState(Game game) {
        isGameOver = game.isGameOver;
        isWaiting = game.isWaiting;
        isTopOfInning = game.isTopOfInning;
        NUM_INNINGS = game.NUM_INNINGS;
        inning = game.inning;
        // Copies of Counts, Teams, and LinkedLists are not needed
        // because the given state will not be needed unless actions
        // are undone
        count = game.count;
        homeTeam = game.homeTeam;
        awayTeam = game.awayTeam;
        // Set batting & pitching teams based on current inning half
        // Creating copies of batting & pitching teams would result in bugs
        // because they would point to different placed in memory, rather
        // than being pointers to the current teams
        if (game.isTopOfInning) {
            battingTeam = awayTeam;
            pitchingTeam = homeTeam;
        } else {
            battingTeam = homeTeam;
            pitchingTeam = awayTeam;
        }
        outs = game.outs;
        runners = game.runners;
    }
}