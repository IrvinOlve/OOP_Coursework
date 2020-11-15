package OOP_Coursework;

public class PlayedMatch {
    private String date;
    private String team_A_name;
    private String team_B_name;
    private int team_A_gaols;
    private int team_B_gaols;

    PlayedMatch(String date, FootballClub team_A_name, int team_A_gaols, FootballClub team_B_name, int team_B_gaols){
        this.date = date;
        this.team_A_name = team_A_name.getName();
        this.team_B_name = team_B_name.getName();
        this.team_A_gaols = team_A_gaols;
        this.team_B_gaols = team_B_gaols;
    }
    public String getDate(){
        return this.date;
    }
    public String getTeam_A_name(){
        return this.team_A_name;
    }
    public int getTeam_A_goals(){
        return this.team_A_gaols;
    }
    public String getTeam_B_name(){
        return this.team_B_name;
    }
    public int getTeam_B_goals(){
        return this.team_B_gaols;
    }
}
