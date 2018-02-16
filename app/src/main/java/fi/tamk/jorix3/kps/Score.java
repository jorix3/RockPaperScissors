package fi.tamk.jorix3.kps;

import android.support.annotation.NonNull;

/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.15
 * @since 1.8
 */
public class Score implements Comparable<Score>{
    private String name;
    private int wins;
    private int draws;
    private int losses;
    
    public Score() {
    
    }
    
    public Score(String name, int wins, int draws, int losses) {
        this.name = name;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getWins() {
        return wins;
    }
    
    public void setWins(int wins) {
        this.wins = wins;
    }
    
    public int getDraws() {
        return draws;
    }
    
    public void setDraws(int draws) {
        this.draws = draws;
    }
    
    public int getLosses() {
        return losses;
    }
    
    public void setLosses(int losses) {
        this.losses = losses;
    }
    
    @Override
    public int compareTo(@NonNull Score other) {
        // 1 -> 10
        int result = Integer.compare(other.getWins(), this.getWins());
        
        if (result == 0) {
            // 1 -> 10
            result = Integer.compare(other.getDraws(), this.getDraws());
        }
        
        if (result == 0) {
            // 10 -> 1
            result = Integer.compare(this.getLosses(), other.getLosses());
        }
        
        if (result == 0) {
            // A -> Z ignore case
            result = this.getName().compareToIgnoreCase(other.getName());
        }
    
        return result;
    }
    
    @Override
    public String toString() {
        return "Name: " + name
                + "\n"
                + "Wins: " + wins
                + " Draws: " + draws
                + " Losses: " + losses;
    }
}
