package OOP_Coursework;

public class FootballClub extends SportsClub {
    private boolean relegated;
    private int wins = 0;
    private int draws = 0;
    private int defeats = 0;
    private int receivedGoals = 0;
    private int scoredGoals = 0;
    private int points = 0;
    private int playedMatches = 0;

    FootballClub(String name, String location) {
        setName(name);
        setLocation(location);
    }

    public void displayStatistics() {
        System.out.format("%-20.30s %5s %5s %5s %5s %5s %7s \n", "W", "D", "L", "S", "R", "GD", "Pts");
        System.out.format("%-20.30s %5s %5s %5s %5s %5s %6s \n", wins, draws, defeats, scoredGoals, receivedGoals, getGoalsDifference(), points);
    }

    public void setPlayedMatches(int playedMatches) {
        this.playedMatches = this.playedMatches + playedMatches;
    }

    public void setPoints(int points) {
        this.points = this.points + points;
    }

    public void setReceivedGoals(int receivedGoals) {
        this.receivedGoals = this.receivedGoals + receivedGoals;
    }

    public void setWins(int wins) {
        this.wins = this.wins + wins;
        this.points = this.points + 3;
    }

    public void setDefeats(int defeats) {
        this.defeats = this.defeats + defeats;
    }

    public void setDraws(int draws) {
        this.draws = this.draws + draws;
        this.points = this.points + 1;
    }

    public void setScoredGoals(int scoredGoals) {
        this.scoredGoals = this.scoredGoals + scoredGoals;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getDefeats() {
        return defeats;
    }

    public int getReceivedGoals() {
        return receivedGoals;
    }

    public int getGoalsDifference() {
        return scoredGoals - receivedGoals;
    }

    public int getScoredGoals() {
        return scoredGoals;
    }

    public int getPlayedMatches() {
        return playedMatches;
    }

    public int getPoints() {
        return points;
    }
}