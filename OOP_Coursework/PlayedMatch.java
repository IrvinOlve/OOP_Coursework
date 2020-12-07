package OOP_Coursework;

public class PlayedMatch {
    private final String date;
    private final FootballClub team_A;
    private final FootballClub team_B;
    private final int team_A_gaols;
    private final int team_B_gaols;

    PlayedMatch(String date, FootballClub team_A, int team_A_gaols, FootballClub team_B, int team_B_gaols) {
        this.date = date;
        this.team_A = team_A;
        this.team_B = team_B;
        this.team_A_gaols = team_A_gaols;
        this.team_B_gaols = team_B_gaols;
    }

    public String getDate() {
        return date;
    }

    public String getHomeTeamName() {
        return team_A.getName();
    }

    public int getHomeTeamGoals() {
        return team_A_gaols;
    }

    public String getAwayTeamName() {
        return team_B.getName();
    }

    public int getAwayTeamGoals() {
        return team_B_gaols;
    }

    public FootballClub getHomeTeam() {
        return team_A;
    }

    public FootballClub getAwayTeam() {
        return team_B;
    }
}