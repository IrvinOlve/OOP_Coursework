package OOP_Coursework;

public class PlayedMatch {
    private final String date;
    private final String team_A_name;
    private final String team_B_name;
    private final int team_A_gaols;
    private final int team_B_gaols;

    PlayedMatch(String date, FootballClub team_A_name, int team_A_gaols, FootballClub team_B_name, int team_B_gaols){
        this.date = date;
        this.team_A_name = team_A_name.getName();
        this.team_B_name = team_B_name.getName();
        this.team_A_gaols = team_A_gaols;
        this.team_B_gaols = team_B_gaols;
    }
    public String getDate(){
        return date;
    }
    public String getTeam_A_name(){
        return team_A_name;
    }
    public int getTeam_A_goals(){
        return team_A_gaols;
    }
    public String getTeam_B_name(){
        return team_B_name;
    }
    public int getTeam_B_goals(){
        return team_B_gaols;
    }
}
