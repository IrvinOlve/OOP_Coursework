package OOP_Coursework;

import java.util.ArrayList;

interface LeagueManager{
    void addFootballClub();
    void relegateFootballClub();
    void displayFootballClubStatistics();
    void displayPremierLeagueTable(ArrayList<FootballClub> footballClubs);
    void addPlayedMatch();
}
