package OOP_Coursework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;

public class GUI {

    private ArrayList<FootballClub> footballClubsList;
    private ArrayList<PlayedMatch> matchesPlayedTableData = null;
    private ArrayList<PlayedMatch> matchesPlayed;


    GUI(ArrayList<FootballClub> footballClubs, ArrayList<PlayedMatch> matchesPlayed) {
        this.footballClubsList = footballClubs;
        this.matchesPlayed = matchesPlayed;
    }

    public void start() {

        // Array below will contain the data pulled from football clubs to populate the Premier League table
        String[][] populatedArray;
        ArrayList<FootballClub> footballClubsSortedByPoints;

        // sortByPoints method will take an Arraylist and return it sorted by points.
//        footballClubsSortedByPoints = sortByPoints(footballClubs);

        // populationHandler method will take an Arraylist and return a populated 2d array.
//        data = populationHandler(footballClubs);

        // premierLeagueTable will display the data passed to it in a JTable.
        premierLeagueTable(footballClubsList);


    }

    private void premierLeagueTable(ArrayList<FootballClub> footballClubs) {

        JFrame frame = new JFrame("League Manager");

        frame.setSize(650, 500);
        frame.setLayout(new BorderLayout());

        // Creation of the Premier League, played matches tables and table models.


        PremierLeagueTableModel premierLeagueTM = new PremierLeagueTableModel(footballClubs);
        PlayedMatchesTableModel playedMatchesTM = new PlayedMatchesTableModel(matchesPlayed);

        JTable premierLeagueTable = new JTable(premierLeagueTM);
        JTable playedMatchesTable = new JTable(playedMatchesTM);

        // Adjusts table's columns sizes for better visualisation.
        setColumnWidth(premierLeagueTable);

        // Add tables in scrollable panes.
        JScrollPane premierLeagueSP = new JScrollPane(premierLeagueTable);
        JScrollPane playedMatchesSP = new JScrollPane(playedMatchesTable);

        // Add premierLeagueTable and playedMatchesTable to a panel for better visualisation.
        JPanel tablePanel = new JPanel(new GridLayout(2, 1));
        tablePanel.add(premierLeagueSP);
        tablePanel.add(playedMatchesSP);

        // Creation of Premier League table sorter, based on ComboBox.
        JLabel sortByTextLabel = new JLabel("Sort by: ");
        JComboBox<String> sortTableOptionsCB = new JComboBox<>();

        // Creates and adds listener to ComboBox that will modify the table accordingly based on selected option.
        OptionComboBoxListener changeListener = new OptionComboBoxListener(premierLeagueTM);
        sortTableOptionsCB.addItemListener(changeListener);

        // Add options ComboBox
        sortTableOptionsCB.addItem("Points");
        sortTableOptionsCB.addItem("Scored goals");
        sortTableOptionsCB.addItem("Wins");

        // Sort by Panel
        JPanel sortByPanel = new JPanel(new GridLayout(0, 2));
        sortByPanel.add(sortByTextLabel);
        sortByPanel.add(sortTableOptionsCB);

        // Creation of random matches generator.
        JLabel firstTeamNameLabel = new JLabel("First team");
        JLabel firstTeamGoalsLabel = new JLabel("0");
        JLabel secondTeamNameLabel = new JLabel("Second team");
        JLabel secondTeamGoalsLabel = new JLabel("0");

        firstTeamNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstTeamGoalsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        secondTeamNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        secondTeamGoalsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panels that will hold each team and score generated
        JPanel firstTeamPanel = new JPanel();
        JPanel secondTeamPanel = new JPanel();
        firstTeamPanel.setLayout(new BoxLayout(firstTeamPanel, BoxLayout.Y_AXIS));
        secondTeamPanel.setLayout(new BoxLayout(secondTeamPanel, BoxLayout.Y_AXIS));

        // Add team names and scores labels to their panel.
        firstTeamPanel.add(firstTeamNameLabel);
        firstTeamPanel.add(firstTeamGoalsLabel);
        secondTeamPanel.add(secondTeamNameLabel);
        secondTeamPanel.add(secondTeamGoalsLabel);

        // Create a new panel that will hold both previously created panels (the ones that each hold team's name and scores)
        JPanel matchStats = new JPanel(new GridLayout(0, 2));
        matchStats.add(firstTeamPanel);
        matchStats.add(secondTeamPanel);

        // Create button and add listener that will modify the each JLabel accordingly to the random match generated.
        JButton randomMatchButton = new JButton("Generate match");
        randomMatchButton.addActionListener(new GenerateMatchButtonListener(firstTeamNameLabel, firstTeamGoalsLabel, secondTeamNameLabel, secondTeamGoalsLabel, premierLeagueTM));


        JLabel searchMatchLabel = new JLabel("    Enter date (dd/mm/yyyy): ");
        JTextField textField = new JTextField("");

        JButton searchMatchButton = new JButton("Search");

        JButton showAllMatchesButton = new JButton("Show all matches");
        JLabel messageStatusLabel = new JLabel("");

        showAllMatchesButton.addActionListener(new ShowAllMatchesButtonListener(textField, messageStatusLabel, playedMatchesTable, matchesPlayed));

        searchMatchButton.addActionListener(new SearchMatchButtonListener(messageStatusLabel, textField, playedMatchesTable));

        JPanel randomMatchPanel = new JPanel(new GridLayout(0, 1));

        JPanel searchMatchContainer = new JPanel(new GridLayout(0, 2));

        searchMatchContainer.add(textField);
        searchMatchContainer.add(searchMatchButton);

        randomMatchPanel.add(matchStats);
        randomMatchPanel.add(randomMatchButton);
        randomMatchPanel.add(searchMatchLabel);
        randomMatchPanel.add(searchMatchContainer);
        randomMatchPanel.add(showAllMatchesButton);
        randomMatchPanel.add(messageStatusLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(randomMatchPanel);

        // Panel that will hold everything!

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(sortByPanel, BorderLayout.EAST);
        topPanel.add(bottomPanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.WEST);


        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void setColumnWidth(JTable clubs) {
        clubs.getColumnModel().getColumn(0).setPreferredWidth(50);
        for (int i = 1; i <= 7; i++) {
            clubs.getColumnModel().getColumn(i).setPreferredWidth(7);
        }
    }

    private class PremierLeagueTableModel extends AbstractTableModel {
        private ArrayList<FootballClub> footballClubList;
        private String[] columns;

        PremierLeagueTableModel(ArrayList<FootballClub> data) {
            this.footballClubList = data;
            this.columns = new String[]{"CLUB", "W", "D", "L", "S", "R", "GD", "Pts"};
            sortByPoints();
        }

        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public int getRowCount() {
            return footballClubList.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int row, int column) {

            switch (column) {
                case 0:   // col 1
                    return footballClubList.get(row).getName();
                case 1:   // col 2
                    return footballClubList.get(row).getWins();
                case 2:   // col 3
                    return footballClubList.get(row).getDraws();
                case 3:   // col 4
                    return footballClubList.get(row).getDefeats();
                case 4:   // col 4
                    return footballClubList.get(row).getScoredGoals();
                case 5:   // col 4
                    return footballClubList.get(row).getReceivedGoals();
                case 6:   // col 4
                    return footballClubList.get(row).getGoalsDifference();
                case 7:   // col 4
                    return footballClubList.get(row).getPoints();
                default:
                    return "";
            }
        }

        public void sortByPoints() {
            footballClubList.sort(Comparator.comparingInt(FootballClub::getPoints).reversed());
            this.fireTableDataChanged();
        }

        public void sortByWins() {
            footballClubList.sort(Comparator.comparingInt(FootballClub::getWins).reversed());
            this.fireTableDataChanged();
        }

        public void sortByScoredGoals() {
            footballClubList.sort(Comparator.comparingInt(FootballClub::getScoredGoals).reversed());
            this.fireTableDataChanged();
        }
    }

    private class PlayedMatchesTableModel extends AbstractTableModel {
        private ArrayList<PlayedMatch> matchesPlayed;
        private String[] columns;

        PlayedMatchesTableModel(ArrayList<PlayedMatch> data) {
            this.matchesPlayed = data;
            this.columns = new String[]{"Date", "Home team", "Score", "Away Team"};
        }

        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public int getRowCount() {
            return matchesPlayed.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:   // col 1
                    return matchesPlayed.get(row).getDate();
                case 1:   // col 2
                    return matchesPlayed.get(row).getTeam_A_name();
                case 2:   // col 3
                    return matchesPlayed.get(row).getTeam_A_goals() + " - " + matchesPlayed.get(row).getTeam_B_goals();
                case 3:   // col 4
                    return matchesPlayed.get(row).getTeam_B_name();
                default:
                    return "";
            }
        }

        public void sortByDate() {
            matchesPlayed.sort(Comparator.comparing(o ->
                    {
                        try {
                            return new SimpleDateFormat("dd/MM/yyyy").parse(o.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
            ));
            this.fireTableDataChanged();
        }
    }

    private class SearchMatchButtonListener implements ActionListener {
        JLabel messageStatusLabel;
        JTextField textField;
        JTable table;

        SearchMatchButtonListener(JLabel messageStatusLabel, JTextField textField, JTable table) {
            this.messageStatusLabel = messageStatusLabel;
            this.textField = textField;
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            // get date from text field
            String matchDate = textField.getText();

            // find matches from given date and store them in a list
            ArrayList<PlayedMatch> foundPlayedMatches = findFootballMatch(matchDate);

            // create new table model with only the found matches
            PlayedMatchesTableModel playedMatchesTM = new PlayedMatchesTableModel(foundPlayedMatches);

            // update table's model if any match was found
            if (foundPlayedMatches.isEmpty()) {
                messageStatusLabel.setText("Not matches found!");
            } else {
                table.setModel(playedMatchesTM);
            }
        }

        private ArrayList<PlayedMatch> findFootballMatch(String matchDate) {
            ArrayList<PlayedMatch> foundMatches = new ArrayList<>();
            for (PlayedMatch match : matchesPlayed) {
                if (match.getDate().equalsIgnoreCase(matchDate)) {
                    foundMatches.add(match);
                }
            }
            return foundMatches;
        }
    }

    private class ShowAllMatchesButtonListener implements ActionListener {
        JLabel messageStatusLabel;
        JTextField textField;
        JTable table;
        ArrayList<PlayedMatch> matchesPlayed;

        ShowAllMatchesButtonListener(JTextField textField, JLabel messageStatusLabel, JTable table, ArrayList<PlayedMatch> matchesPlayed) {
            this.messageStatusLabel = messageStatusLabel;
            this.textField = textField;
            this.table = table;
            this.matchesPlayed = matchesPlayed;

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PlayedMatchesTableModel tableModel = new PlayedMatchesTableModel(matchesPlayed);
            tableModel.sortByDate();
            table.setModel(tableModel);
            tableModel.fireTableDataChanged();
            textField.setText("");
            messageStatusLabel.setText("");
        }
    }

    private class GenerateMatchButtonListener implements ActionListener {

        FootballClub firstTeam = null;
        FootballClub secondTeam = null;
        int firstTeamGoals = 0;
        int secondTeamGoals = 0;
        String datePlayed = null;

        JLabel firstTeamNameLabel;
        JLabel firstTeamGoalsLabel;
        JLabel secondTeamNameLabel;
        JLabel secondTeamGoalsLabel;
        PremierLeagueTableModel tableModel;

        GenerateMatchButtonListener(JLabel firstTeamNameLabel, JLabel firstTeamGoalsLabel, JLabel secondTeamNameLabel, JLabel secondTeamGoalsLabel, PremierLeagueTableModel tableModel) {
            this.tableModel = tableModel;
            this.firstTeamNameLabel = firstTeamNameLabel;
            this.firstTeamGoalsLabel = firstTeamGoalsLabel;
            this.secondTeamNameLabel = secondTeamNameLabel;
            this.secondTeamGoalsLabel = secondTeamGoalsLabel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            generateMatch();
            firstTeamNameLabel.setText(firstTeam.getName());
            firstTeamGoalsLabel.setText(String.valueOf(firstTeamGoals));
            secondTeamNameLabel.setText(secondTeam.getName());
            secondTeamGoalsLabel.setText(String.valueOf(secondTeamGoals));
        }

        private int randomNumber(int range) {
            Random random = new Random();
            return random.nextInt(range);
        }

        private void generateMatch() {
            PlayedMatch playedMatch;
            String randomDay;
            String randomMonth;
            int randomGoals;
            int randomTeam;

            int clubsListSize = footballClubsList.size();
            int goalsRange = 6;

            randomTeam = randomNumber(clubsListSize);
            randomGoals = randomNumber(goalsRange);

            firstTeam = footballClubsList.get(randomTeam);
            firstTeamGoals = randomGoals;

            randomGoals = randomNumber(goalsRange);
            randomTeam = randomNumber(clubsListSize);

            secondTeam = footballClubsList.get(randomTeam);
            secondTeamGoals = randomGoals;

            while (firstTeam == secondTeam) {
                randomTeam = randomNumber(clubsListSize);
                secondTeam = footballClubsList.get(randomTeam);
            }

            int daysRange = 27;
            int monthRange = 11;

            randomDay = randomNumber(daysRange) + 1 + "";
            randomMonth = randomNumber(monthRange) + 1 + "";

            if (randomDay.length() == 1) {
                randomDay = "0" + randomDay;
            }
            if (randomMonth.length() == 1) {
                randomMonth = "0" + randomMonth;
            }

            datePlayed = randomDay.concat("/").concat(randomMonth).concat("/").concat("2020");

            playedMatch = new PlayedMatch(datePlayed, firstTeam, firstTeamGoals, secondTeam, secondTeamGoals);

            matchesPlayed.add(playedMatch);
            setStatistics(firstTeam, firstTeamGoals, secondTeam, secondTeamGoals);
            tableModel.fireTableDataChanged();
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
    }

    private class OptionComboBoxListener implements ItemListener {
        PremierLeagueTableModel tableModel;

        OptionComboBoxListener(PremierLeagueTableModel tableModel) {
            this.tableModel = tableModel;
        }

        @Override
        public void itemStateChanged(ItemEvent event) {

            String selectedOption = event.getItem().toString();

            if (event.getStateChange() == ItemEvent.SELECTED) {
                switch (selectedOption.toLowerCase()) {
                    case ("wins"):
                        tableModel.sortByWins();
                        break;
                    case ("scored goals"):
                        tableModel.sortByScoredGoals();
                        break;
                    case ("points"):
                        tableModel.sortByPoints();
                        break;
                }
            }
        }
    }



}