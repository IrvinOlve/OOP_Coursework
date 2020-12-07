package OOP_Coursework;

import java.util.*;
import java.io.*;

class PremierLeagueManager implements LeagueManager {

    static Scanner input = new Scanner(System.in);

    private static final ArrayList<FootballClub> footballClubsInPremierLeague = new ArrayList<>();
    private static final ArrayList<PlayedMatch> matchesPlayed = new ArrayList<>();

    public void menu() {
        String optionInput;
        readFromFile();

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
                case "G":
                    GUI premierLeagueManagerGUI = new GUI();
                    premierLeagueManagerGUI.setData(footballClubsInPremierLeague, matchesPlayed);
                    premierLeagueManagerGUI.start();
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
        System.out.println("g) Open Premier League Manager GUI");
        System.out.println("Or 'exit' close the program");
        System.out.print("\nEnter option: ");
    }

    public void addFootballClub() {

        /*
         * This method takes a name and location input, creates a football club object
         * and is then added to an arraylist.
         */

        // input football club name and location
        System.out.print("Enter football club's name: ");
        String clubsName = input.nextLine();

        System.out.print("Enter football club's location: ");
        String clubsLocation = input.nextLine();

        // create football club object and add it to footballClubsInPremierLeague
        FootballClub newFootballClub = new FootballClub(clubsName, clubsLocation);
        footballClubsInPremierLeague.add(newFootballClub);

        System.out.println(newFootballClub.getName() + " was added to the Premier League.");
        System.out.println();
    }

    public void relegateFootballClub() {

        /*
         * This method will look for a club through findFootballClub method
         * and update its relegation status.
         */

        // input football club's name
        System.out.print("Enter football club's name: ");
        String clubsName = input.nextLine();

        // call findFootballClub method with clubsName and store the returned football club object
        FootballClub footballClubToRelegate = findFootballClub(clubsName);

        // check if findFootballClub returned an object and modify it's relegation value to true
        if (footballClubToRelegate != null) {

            footballClubsInPremierLeague.remove(footballClubToRelegate);
            System.out.println(footballClubToRelegate.getName() + " was relegated.");
        } else {
            clubNotFound(clubsName);
        }
    }

    public void displayFootballClubStatistics() {

        /*
         * This method will look for a club through findFootballClub method
         * and display its statistics.
         */

        // input football club's name
        System.out.print("Enter football club's name: ");
        String clubsName = input.nextLine();

        // call findFootballClub method with clubsName and store the returned football club object
        FootballClub footballClubStatistics = findFootballClub(clubsName);

        // check if findFootballClub returned an object and display its statistics
        if (footballClubStatistics != null) {
            footballClubStatistics.displayStatistics();
        } else {
            clubNotFound(clubsName);
        }
    }

    public void displayPremierLeagueTable(ArrayList<FootballClub> footballClubs) {

        /*
         * This method goes through the passed arraylist of football clubs and display
         * its statistics if they are not relegated.
         */

        /*
         * Create a temporary arraylist that will hold the football clubs sorted by points
         * if two football clubs have the same points, display the one with more goal difference
         */

        ArrayList<FootballClub> footballClubsSortedByPoints = new ArrayList<>(footballClubs);
        footballClubsSortedByPoints.sort(Comparator.comparingInt(FootballClub::getPoints).thenComparing(FootballClub::getGoalsDifference).reversed());

        System.out.format("%-20.30s %5s %5s %5s %5s %5s %5s %5s %7s \n", "Club", "P", "W", "D", "L", "S", "R", "GD", "Pts");

        // go through footballClubsSortedByPoints and display it
        for (FootballClub club : footballClubsSortedByPoints) {
            System.out.format("%-20.30s %5s %5s %5s %5s %5s %5s %5s %6s \n", club.getName(), club.getPlayedMatches(), club.getWins(), club.getDraws(), club.getDefeats(), club.getScoredGoals(), club.getReceivedGoals(), club.getGoalsDifference(), club.getPoints());
        }
    }

    public void addPlayedMatch() {

        /*
         *This method contains validation methods that return a date, football clubs
         * and scores of the match and each will be store in their corresponding
         * variables. Then an object of a played match is created, containing all the
         * information of the match. That match object is then passed on to setStatistics
         * method to update the football clubs that played the match (goals, points, etc.)
         * The match object is then added to an ArrayList.
         */
        FootballClub HomeTeam;
        FootballClub AwayTeam;
        int HomeTeamGoals;
        int AwayTeamGoals;

        String playedMatchDate;

        // input date, team's names and goals
        System.out.println("- Date of the match -");
        playedMatchDate = matchDateValidation();

        System.out.println("- Football clubs -");

        System.out.print("Enter home team: ");
        HomeTeam = footballClubValidation();

        System.out.print("Enter goals scored: ");
        HomeTeamGoals = scoredGoalsValidation();

        System.out.print("Enter away team: ");
        AwayTeam = footballClubValidation();

        System.out.print("Enter goals scored: ");
        AwayTeamGoals = scoredGoalsValidation();

        // create object with input match information
        PlayedMatch playedMatch = new PlayedMatch(playedMatchDate, HomeTeam, HomeTeamGoals, AwayTeam, AwayTeamGoals);

        // call setStatistics and pass playedMatch to update football clubs statistics
        setStatistics(playedMatch);

        // add match to matchesPlayed collection
        matchesPlayed.add(playedMatch);

        System.out.println("Match added!");
        System.out.println();
    }

    public void setStatistics(PlayedMatch matchPlayed) {

        /*
         * This method takes a played match object, access the football clubs
         * and stores them in their corresponding variables. Then each team
         * will call their setters to set the statistics.
         */

        FootballClub HomeTeam = matchPlayed.getHomeTeam();
        FootballClub AwayTeam = matchPlayed.getAwayTeam();
        int HomeTeamGoals = matchPlayed.getHomeTeamGoals();
        int AwayTeamGoals = matchPlayed.getAwayTeamGoals();

        // Set matches played for each match
        HomeTeam.setPlayedMatches(1);
        AwayTeam.setPlayedMatches(1);

        // Set goals scored and received for each team.
        HomeTeam.setScoredGoals(HomeTeamGoals);
        HomeTeam.setReceivedGoals(AwayTeamGoals);
        AwayTeam.setScoredGoals(AwayTeamGoals);
        AwayTeam.setReceivedGoals(HomeTeamGoals);

        // If both teams score the same amount of gaols they will both get 1 point.
        if (HomeTeamGoals == AwayTeamGoals) {
            HomeTeam.setDraws(1);
            AwayTeam.setDraws(1);
        }
        // If FIRST team scores more than SECOND team, they win and  get 3 points.
        else if (HomeTeamGoals > AwayTeamGoals) {
            HomeTeam.setWins(1);
            AwayTeam.setDefeats(1);
        }
        // Otherwise, SECOND team wins and gets 3 points.
        else {
            AwayTeam.setWins(1);
            HomeTeam.setDefeats(1);
        }
    }

    private String matchDateValidation() {

        /*
         *This method contains a series of loops that will take
         * an input for the day, month and year and validate them.
         * Then it returns a string with the already validated date
         * in the form of dd/mm/yyyy
         */

        Integer dateDay;
        Integer dateMonth;
        Integer dateYear;
        String date;

        System.out.print("Enter the day: ");
        while (true) {
            String dateDayInput = input.nextLine();
            try {
                dateDay = Integer.parseInt(dateDayInput);

                if (dateDay.toString().length() == 1) {
                    date = "0" + dateDay.toString().concat("/");
                } else {
                    date = dateDay.toString().concat("/");
                }


                break;
            } catch (NumberFormatException ex) {
                System.out.print("Day '" + dateDayInput + "' is not valid. \nTry again: ");
            }
        }

        System.out.print("Enter the month: ");
        while (true) {
            String dateMonthInput = input.nextLine();
            try {
                dateMonth = Integer.parseInt(dateMonthInput);

                if (dateMonth.toString().length() == 1) {
                    date = date + "0" + dateMonth.toString().concat("/");
                } else {
                    date = date + dateMonth.toString().concat("/");
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.print("Month '" + dateMonthInput + "' is not valid. \nTry again: ");
            }
        }

        System.out.print("Enter the year: ");
        while (true) {
            String dateYearInput = input.nextLine();
            try {
                dateYear = Integer.parseInt(dateYearInput);
                int dateYearLength = dateYear.toString().length();
                if (dateYearLength != 4) {
                    System.out.print("Year '" + dateYearInput + "' is not valid. \nTry again: ");
                } else if (dateYearLength == 4) {
                    date = date + dateYear;
                    break;
                }

            } catch (NumberFormatException ex) {
                System.out.print("Year '" + dateYearInput + "' is not valid. \nTry again: ");
            }
        }

        return date;
    }

    private int scoredGoalsValidation() {
        /*
         * This method contains a while loop that asks for number of goals scored
         * and returns it. It makes sure the input is a numeric value.
         */
        Integer intScoredGoals;
        while (true) {
            String scoredGoalsInput = input.nextLine();
            try {
                intScoredGoals = Integer.parseInt(scoredGoalsInput);
                break;
            } catch (NumberFormatException ex) {
                System.out.print("'" + scoredGoalsInput + "' " + "is not valid. \nPlease enter a numeric value: ");
            }
        }
        return intScoredGoals;
    }

    private FootballClub footballClubValidation() {

        /*
         * This method contains a while loop that asks for a football club name,
         * which is then passed on to findFootballClub method, which returns a
         * football club object or null if it didn't find any object. If it's null
         * it will keep asking to input for a valid football club. The football club
         * is then store and returned.
         */
        FootballClub participantFootballClub;
        while (true) {
            String footballClubInput = input.nextLine();
            participantFootballClub = findFootballClub(footballClubInput);

            if (participantFootballClub == null) {
                clubNotFound(footballClubInput);
                System.out.print("Please try again: ");
            } else {
                break;
            }
        }
        return participantFootballClub;
    }

    private void readFromFile() {
        /*
         * This method creates temporary variables to hold data to then create
         * their corresponding objects. The data is created by reading files that
         * contain football clubs and played matches. Once the data is created,
         * object are created, which are then added to arraylists.
         */


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
            String[] tempString = fileLine.split(": ");

            // Temporally stores words to then create Football clubs objects
            switch (tempString[0]) {
                case "club no.":
                    break;
                case "club name": // Temporally store club's name
                    footballClub = tempString[1];
                    break;
                case "club location": // Temporally store club's location
                    location = tempString[1];
                    FootballClub newFootballClub = new FootballClub(footballClub, location);
                    footballClubsInPremierLeague.add(newFootballClub);
                    break;
            }
        }

        while (playedMatches.hasNextLine()) {
            String[] tempString;

            String line = playedMatches.nextLine();
            tempString = line.split(": ");

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

                    PlayedMatch playedMatch = new PlayedMatch(playedMatchDate, firstFootballClub, firstGoalsScored, secondFootballClub, secondGoalsScored);
                    setStatistics(playedMatch);

                    break;
            }
        }
    }

    private Scanner readFile(String fileName) {
        /*
         * This methods checks if a file exists and returns it
         */
        File inputFile = new File(fileName);
        Scanner inFromFile = null;

        try {
            inFromFile = new Scanner(inputFile);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }
        return inFromFile;
    }

    private void clubNotFound(String clubsName) {
        /*
         * This method displays a message saying the passed club name
         * was not found.
         */
        System.out.println("Football club " + "'" + clubsName + "' " + "not found.");
    }

    private FootballClub findFootballClub(String footballClub) {
        /*
         * This method will look for the football club name passed inside the
         * arraylist of football clubs. If something is found it's stored and returned.
         */
        FootballClub foundFootballClub = null;

        for (FootballClub club : footballClubsInPremierLeague) {
            if (club.getName().equalsIgnoreCase(footballClub)) {
                foundFootballClub = club;
            }
        }
        return foundFootballClub;
    }

    private void saveData() {
        /*
         * This method will save the data stored inside the football clubs and played matches
         * stored inside their array lists into their corresponding files.
         */
        String footballClubsFile = "src/data/footballClubs.dat";
        String matchesPlayedFile = "src/data/matchesPlayed.dat";
        PrintWriter footballClubsPrint = null;
        PrintWriter matchesPlayedPrint = null;

        // Custom index will be used to number each football club and played match.
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
            matchesPlayedPrint.println("1st team name: " + match.getHomeTeamName());
            matchesPlayedPrint.println("1st team goals: " + match.getHomeTeamGoals());
            matchesPlayedPrint.println("2nd team name: " + match.getAwayTeamName());
            matchesPlayedPrint.println("2nd team goals: " + match.getAwayTeamGoals());
            index++;
        }
        matchesPlayedPrint.close();
        footballClubsPrint.close();

        System.out.println("Data saved successfully!");

    }

}

