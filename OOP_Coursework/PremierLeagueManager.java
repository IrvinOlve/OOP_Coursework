package OOP_Coursework;

import java.util.*;
import java.io.*;


class PremierLeagueManager implements LeagueManager {

    static Scanner input = new Scanner(System.in);

    private static final ArrayList<FootballClub> footballClubsInPremierLeague = new ArrayList<>();
    private static final ArrayList<FootballClub> relegatedFootballClubsInPremierLeague = new ArrayList<>();
    private static final ArrayList<PlayedMatch> matchesPlayed = new ArrayList<>();

    public void menu() {
        String optionInput;
        readFromFile();
        GUI premierLeagueWindow = new GUI(footballClubsInPremierLeague, matchesPlayed);
        premierLeagueWindow.start();
        do {
            menuOptions();
            optionInput = input.nextLine();

            switch (optionInput.toUpperCase()) {
                case "A":
                    addFootballClub();
                    break;
                case "B":
                    relegateFootballClub();
                    break;
                case "C":
                    displayFootballClubStatistics();
                    break;
                case "D":
                    displayPremierLeagueTable(footballClubsInPremierLeague);
                    break;
                case "E":
                    addPlayedMatch();
                    break;
                case "F":
                    saveData();
                    break;
                case "N":


//                    new GUI().letmesee();

                break;
            }
        } while (!optionInput.equals("exit"));
        System.out.println("Closing Premier League Manager");
        System.exit(0);
    }

    private void menuOptions() {
        System.out.println("**** PREMIER LEAGUE MANAGER ****");
        System.out.println("a) Create a football club");
        System.out.println("b) Relegate a football club");
        System.out.println("c) Display a football club's statistic");
        System.out.println("d) Display the Premier League Table");
        System.out.println("e) Add a played match");
        System.out.println("f) Save data");
        System.out.println();
        System.out.print("Enter option: ");
    }

    public void addFootballClub() {
        System.out.print("Enter football club's name: ");
        String clubsName = input.nextLine();
        FootballClub newFootballClub = new FootballClub(clubsName);
        footballClubsInPremierLeague.add(newFootballClub);
        System.out.println(newFootballClub.getName() + " was added to the Premier League.");
        System.out.println();
    }

    public void relegateFootballClub() {
        FootballClub footballClubToRelegate;
        System.out.print("Enter football club's name: ");
        String clubsName = input.nextLine();

        footballClubToRelegate = findFootballClub(clubsName);

        if (footballClubToRelegate != null) {
            relegatedFootballClubsInPremierLeague.add(footballClubToRelegate);
            footballClubsInPremierLeague.remove(footballClubToRelegate);
            System.out.println(footballClubToRelegate.getName() + " was relegated.");
        } else {
            clubNotFound(clubsName);
        }
    }

    public void displayFootballClubStatistics() {
        FootballClub footballClubStatistics;

        System.out.print("Enter football club's name: ");
        String clubsName = input.nextLine();

        footballClubStatistics = findFootballClub(clubsName);

        if (footballClubStatistics != null) {
            footballClubStatistics.displayStatistics();
        } else {
            clubNotFound(clubsName);
        }
    }

    public void displayPremierLeagueTable(ArrayList<FootballClub> footballClubs) {

        ArrayList<FootballClub> footballClubsSortedByPoints;
        footballClubsSortedByPoints = sortFootballClubByPoints(footballClubs);
        // Left to implement > In the case
        //which two clubs have the same number of points, the club with the best goal difference
        //should appear first.
        System.out.format("%-20.30s %5s %5s %5s %5s %5s %5s %7s \n", "Club", "W", "D", "L", "S", "R", "GD", "Pts");

        for (FootballClub club : footballClubsSortedByPoints) {
            System.out.format("%-20.30s %5s %5s %5s %5s %5s %5s %6s \n", club.getName(), club.getWins(),  club.getDraws(), club.getDefeats(), club.getScoredGoals(), club.getReceivedGoals(), club.getGoalsDifference(), club.getPoints());
        }
    }

    public void addPlayedMatch() {
        FootballClub FIRST_participantFootballClub;
        FootballClub SECOND_participantFootballClub;
        int FIRST_gaolsScored;
        int SECOND_gaolsScored;

        String playedMatchDate;

        System.out.print("Enter date of the played matched 'DD/MM/YYYY': ");
        playedMatchDate = input.nextLine();

        FIRST_participantFootballClub = footballClubValidation("first");
        FIRST_gaolsScored = scoredGoalsValidation();

        SECOND_participantFootballClub = footballClubValidation("second");
        SECOND_gaolsScored = scoredGoalsValidation();

        FIRST_participantFootballClub.setPlayedMatches(1);
        SECOND_participantFootballClub.setPlayedMatches(1);

        setStatistics(FIRST_participantFootballClub, FIRST_gaolsScored,
                SECOND_participantFootballClub, SECOND_gaolsScored);

        PlayedMatch playedMatch = new PlayedMatch(playedMatchDate, FIRST_participantFootballClub, FIRST_gaolsScored,
                SECOND_participantFootballClub, SECOND_gaolsScored);

        matchesPlayed.add(playedMatch);

        System.out.println("Match added!");
        System.out.println();
    }

    private void setStatistics(FootballClub Team_1, int scoredGaols_1, FootballClub Team_2, int scoredGaols_2) {

        // Set goals scored and received for each team.
        Team_1.setScoredGoals(scoredGaols_1);
        Team_1.setReceivedGoals(scoredGaols_2);
        Team_2.setScoredGoals(scoredGaols_2);
        Team_2.setReceivedGoals(scoredGaols_1);

        // If both teams score the same amount of gaols they will both get 1 point.
        if (scoredGaols_1 == scoredGaols_2) {
            Team_1.setDraws(1);
            Team_2.setDraws(1);
        }
        // If FIRST team scores more than SECOND team, they win and  get 3 points.
        else if (scoredGaols_1 > scoredGaols_2) {
            Team_1.setWins(1);
            Team_2.setDefeats(1);
        }
        // Otherwise, SECOND team wins and gets 3 points.
        else {
            Team_2.setWins(1);
            Team_1.setDefeats(1);
        }
    }

    private int scoredGoalsValidation() {
        String scoredGoalsInput;
        Integer intScoredGoals = null;

        while (intScoredGoals == null) {
            System.out.print("Enter goals scored: ");
            scoredGoalsInput = input.nextLine();

            try {
                intScoredGoals = Integer.parseInt(scoredGoalsInput);
            } catch (InputMismatchException ex) {
                System.out.println("'" + scoredGoalsInput + "' " + "Not valid. Please enter a numeric value.");
            }
        }
        return intScoredGoals;
    }

    private String[] textSplitter(String textLine) {
        // method below reads a line of text and splits the words by ":",
        // stores them in an array and returns the array.
        return textLine.split(": ");
    }

    private void readFromFile() {

        // These fields will temporally hold a match's data to later create their object.
        String playedMatchDate = null;
        FootballClub firstFootballClub = null;
        FootballClub secondFootballClub = null;
        int firstGoalsScored = 0;
        int secondGoalsScored = 0;


        // These fields will temporally hold a club's data to later create their object.
        String footballClub = null;
        String location = null;

        String playedMatchesFile = "src/data/matchesPlayed.dat";
        String footballClubsFile = "src/data/footballClubs.dat";

        Scanner playedMatches = readFile(playedMatchesFile);
        Scanner footballClubs = readFile(footballClubsFile);

        while (footballClubs.hasNextLine()) {
            String fileLine = footballClubs.nextLine();
            String[] tempString = textSplitter(fileLine);

            // Temporally stores words to then create Football clubs objects
            switch (tempString[0]) {
                case "club no.":
                    break;
                case "club name": // Temporally store club's name
                    footballClub = tempString[1];
                    break;
                case "club location": // Temporally store club's name
                    location = tempString[1];
                    FootballClub newFootballClub = new FootballClub(footballClub, location);
                    footballClubsInPremierLeague.add(newFootballClub);
                    break;
            }
        }

        while (playedMatches.hasNextLine()) {
            String[] tempString;

            String line = playedMatches.nextLine();
            tempString = textSplitter(line);

            switch (tempString[0]) {
                case "Match":
                    break;
                case "Date":
                    playedMatchDate = tempString[1];
                    break;
                case "1st team name":
                    firstFootballClub = findFootballClub(tempString[1]);
                    break;
                case "1st team goals":
                    firstGoalsScored = Integer.parseInt(tempString[1]);
                    break;
                case "2nd team name":
                    secondFootballClub = findFootballClub(tempString[1]);
                    break;
                case "2nd team goals":
                    secondGoalsScored = Integer.parseInt(tempString[1]);

                    setStatistics(firstFootballClub, firstGoalsScored, secondFootballClub, secondGoalsScored);
                    PlayedMatch playedMatch = new PlayedMatch(playedMatchDate, firstFootballClub, firstGoalsScored, secondFootballClub, secondGoalsScored);
                    matchesPlayed.add(playedMatch);
                    break;
            }
        }
    }

    private Scanner readFile(String fileName) {
        File inputFile = new File(fileName);
        Scanner inFromFile = null;

        try {
            inFromFile = new Scanner(inputFile);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }
        return inFromFile;
    }

    private FootballClub footballClubValidation(String club) {
        String footballClubInput;
        FootballClub participantFootballClub = null;

        while (participantFootballClub == null) {
            System.out.print("Enter the " + club + " football club that played in the match: ");

            footballClubInput = input.nextLine();
            participantFootballClub = findFootballClub(footballClubInput);

            if (participantFootballClub == null) {
                clubNotFound(footballClubInput);
            }
        }
        return participantFootballClub;
    }

    private void clubNotFound(String clubsName) {
        System.out.println("'" + clubsName + "' " + "not found in the Premier League.");
    }

    private FootballClub findFootballClub(String footballClub) {
        FootballClub foundFootballClub = null;

        for (FootballClub club : footballClubsInPremierLeague) {
            if (club.getName().equalsIgnoreCase(footballClub)) {
                foundFootballClub = club;
            }
        }
        return foundFootballClub;
    }

    private void saveData() {
        String footballClubsFile = "src/data/footballClubs.dat";
        String matchesPlayedFile = "src/data/matchesPlayed.dat";
        PrintWriter footballClubsPrint = null;
        PrintWriter matchesPlayedPrint = null;
        int index = 1;

        try {
            footballClubsPrint = new PrintWriter(footballClubsFile);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }

        try {
            matchesPlayedPrint = new PrintWriter(matchesPlayedFile);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }

        System.out.println();
        for (FootballClub club : footballClubsInPremierLeague) {
            footballClubsPrint.println("club no.: " + index);
            footballClubsPrint.println("club name: " + club.getName());
            footballClubsPrint.println("club location: " + club.getLocation());
            index++;
        }

        index = 1;

        for (PlayedMatch match : matchesPlayed) {
            matchesPlayedPrint.println("Match no.:" + index);
            matchesPlayedPrint.println("Date: " + match.getDate());
            matchesPlayedPrint.println("1st team name: " + match.getTeam_A_name());
            matchesPlayedPrint.println("1st team goals: " + match.getTeam_A_goals());
            matchesPlayedPrint.println("2nd team name: " + match.getTeam_B_name());
            matchesPlayedPrint.println("2nd team goals: " + match.getTeam_B_goals());
            index++;
        }

        System.out.println("Data saved successfully!");
        matchesPlayedPrint.close();
        footballClubsPrint.close();
    }

    private ArrayList<FootballClub> sortFootballClubByPoints(ArrayList<FootballClub> footballClubs) {
        ArrayList<FootballClub> footballClubsSortedByPoints = new ArrayList<>(footballClubs);
        footballClubsSortedByPoints.sort(Comparator.comparingInt(FootballClub::getGoalsDifference).thenComparing(FootballClub::getPoints).reversed());
        return footballClubsSortedByPoints;
    }
}

