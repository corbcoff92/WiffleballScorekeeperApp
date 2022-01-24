package com.example.game;

/**
 * The Count class is used to represent a count in wiffleball. 
 * Mainly used in the main {@link Game} class.
 */
class Count
{
    /**
     * The number of balls of this count.
     */
    int balls;
    /**
     * The number of strikes of this count.
     */
    int strikes;
    
    /**
     * Initializes a blank count.
     */
    public Count()
    {
        reset();
    }

    /**
     * This constructor is a copy constructor, which provides a way to create a copy of the given existing 
     * {@link Count} instance.
     * @param count  Count instance that is to be copied.
     */
    public Count(final Count count)
    {
        this.balls = count.balls;
        this.strikes = count.strikes;
    }

    /**
     * Resets the balls and strikes of this count to zero.
     */
    public void reset()
    {
        balls = 0;
        strikes = 0;
    }

    /**
     * Produces a formatted string representing this count, that can then be printed to the screen.
     * @return The formatted display string representing this count, which can be printed to the screen.
     */
    public String getDisplayString()
    {
        return balls + "-" + strikes;
    }

    /**
     * Determines whether or not this count should result in a walk. 
     * The count does not reset itself when a walk is determined, thus 
     * the controlling class should call the {@code reset} method when
     * the count should be reset.
     * @return  Boolean indicating whether this count should result in a walk.
     */
    public boolean checkWalk()
    {
        return balls >= 4;
    }
    
    /**
     * Determines whether or not this count should result in a strikeout. 
     * The count does not reset itself when a strikeout is determined, thus 
     * the controlling class should call the {@code reset} method when
     * the count should be reset.
     * @return  Boolean indicating whether this count should result in a strikeout.
     */
    public boolean checkStrikeout()
    {
        return strikes >= 3;
    }
}