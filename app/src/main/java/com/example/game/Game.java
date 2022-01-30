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
    public boolean isWaiting = false;

    // Package private members
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
     * @param homeTeamName The desired name for the home team. Can only be a maximum of 10 characters.
     * @param awayTeamName The desired name for the away team. Can only be a maximum of 10 characters.
     */
    Game(int numInnings, String awayTeamName, String homeTeamName) {
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
        runners = new LinkedList<Integer>();

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
    Game(final Game game) {
        isGameOver = game.isGameOver;
        isWaiting = game.isWaiting;
        isTopOfInning = game.isTopOfInning;
        message = game.message;
        NUM_INNINGS = game.NUM_INNINGS;
        inning = game.inning;
        // Must create copies of Counts, Teams, and LinkedLists
        // because they are references to objects.
        count = new Count(game.count);
        battingTeam = new Team(game.battingTeam);
        pitchingTeam = new Team(game.pitchingTeam);
        if (game.homeTeam == game.battingTeam) {
            homeTeam = battingTeam;
            awayTeam = pitchingTeam;
        } else {
            homeTeam = pitchingTeam;
            awayTeam = battingTeam;
        }
        outs = game.outs;
        runners = new LinkedList<Integer>(game.runners);
    }

    /**
     * Implementation of a ball being called and the batter being walked if applicable.
     * This game's {@code Count} is increased by one ball, and if a walk is determined,
     * the runners are advanced, and the {@code isGameOver} condition is update. If the
     * game is not determined to be over, the count is reset.
     */
    void callBall() {
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
    void callStrike() {
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
    void advanceRunnersHit(int numBases) {
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
    void flyOut() {
        message = "Flyout";
        outMade();
    }

    /**
     * Implementation of an out being recorded via groundout. The out is recorded,
     * the {@code isGameOver} condition is updated, and the count is reset. If
     * the game is to continue and the current half-inning has ended, a new half-inning is
     * also began.
     */
    void groundOut() {
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

    public boolean checkInningOver() {
        return outs >= 3;
    }

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

    public boolean isTopInning() {
        return isTopOfInning;
    }

    public int getInning() {
        return inning;
    }

    public int getBalls() {
        return count.balls;
    }

    public int getStrikes() {
        return count.strikes;
    }

    public int getOuts() {
        return outs;
    }

    public int getHomeRuns() {
        return homeTeam.getRuns();
    }

    public int getHomeHits() {
        return homeTeam.hits;
    }

    public int getHomeWalks() {
        return homeTeam.walks;
    }

    public int getAwayRuns() {
        return awayTeam.getRuns();
    }

    public int getAwayHits() {
        return awayTeam.hits;
    }

    public int getAwayWalks() {
        return awayTeam.walks;
    }

    public boolean isRunnerOnBase(int base) {
        return runners.contains(base);
    }

    public String getMessage() {
        return message;
    }

    public int getNumInnings() { return NUM_INNINGS; }

    public String getAwayName() { return awayTeam.name; }

    public String getHomeName() { return homeTeam.name; }



    /**
     * Prints the final game state to the screen, formatted to look like a boxscore.
     * The runs, hits, and walk totals for each team are displayed, along with their
     * number of runs scored in each inning.
     */
    public String getFinalDisplayString() {
        String displayText = "";
        final int NAME_PADDING = Math.max(6, Math.max(homeTeam.name.length(), awayTeam.name.length()));

        // Produce formatted heading string //
        // Final state for extra innings if needed
        String headingText = (inning <= NUM_INNINGS ? "FINAL" : "FIN/" + inning);
        headingText = String.format("%-" + NAME_PADDING + "s  | ", headingText);
        // Add inning headings
        for (int i = 1; i <= awayTeam.getRunserPerInning().size(); i++) {
            headingText += (String.format("%-2d ", i));
        }
        // Add total headings
        headingText += "|R  H  W";

        // Produce spacing bar //
        String spacingString = "";
        for (int i = 0; i < headingText.length(); i++) {
            spacingString += '-';
        }

        // Produce boxscore strings for each team //
        // Add team names
        String awayString = String.format("%-" + NAME_PADDING + "s  | ", awayTeam.name);
        String homeString = String.format("%-" + NAME_PADDING + "s  | ", homeTeam.name);
        // Add runs per inning
        for (int i = 0; i < awayTeam.getRunserPerInning().size(); i++) {
            awayString += String.format("%-2d ", awayTeam.getRunserPerInning().get(i));
            // If bottom of inning was not needed, add "-" for 
            // last inning of home team indicating as such
            if (i < homeTeam.getRunserPerInning().size()) {
                homeString += String.format("%-2d ", homeTeam.getRunserPerInning().get(i));
            } else {
                homeString += "-  ";
            }
        }
        // Add team totals
        awayString += String.format("|%-2d %-2d %-2d", awayTeam.getRuns(), awayTeam.hits, awayTeam.walks);
        homeString += String.format("|%-2d %-2d %-2d", homeTeam.getRuns(), homeTeam.hits, homeTeam.walks);

        // Print scoreboard
        displayText += "\n" + headingText + "\n";
        displayText += spacingString + "\n";
        displayText += awayString + "\n";
        displayText += homeString + "\n";
        displayText += spacingString + "\n";
        displayText += "\n";

        return displayText;
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
        setState(game);
    }

    public void redo(final HashMap<String, Game> state) {
        String action = state.keySet().iterator().next();
        Game game = state.get(action);
        message = "Redo: " + action;
        setState(game);
    }

    private void setState(Game game) {
        isGameOver = game.isGameOver;
        isWaiting = game.isWaiting;
        isTopOfInning = game.isTopOfInning;
        NUM_INNINGS = game.NUM_INNINGS;
        inning = game.inning;
        count = game.count;
        battingTeam = game.battingTeam;
        pitchingTeam = game.pitchingTeam;
        if (game.homeTeam == game.battingTeam) {
            homeTeam = game.battingTeam;
            awayTeam = game.pitchingTeam;
        } else {
            homeTeam = game.pitchingTeam;
            awayTeam = game.battingTeam;
        }
        outs = game.outs;
        runners = game.runners;
    }
}