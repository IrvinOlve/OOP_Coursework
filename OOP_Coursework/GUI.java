package OOP_Coursework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;

public class GUI {

    private final ArrayList<FootballClub> footballClubsList;
    private final ArrayList<PlayedMatch> matchesPlayed;

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

        frame.setSize(500, 505);
        frame.setLayout(new BorderLayout());

        String[] columns = new String[]{"CLUB", "W", "D", "L", "S", "R", "GD", "Pts"};
        MyTableModel tableModel = new MyTableModel(footballClubs, columns);
        tableModel.sortByPoints();

        JTable table = new JTable(tableModel);
        JScrollPane leagueTable = new JScrollPane(table);

        JLabel sortByTextLabel = new JLabel("Sort by: ");

        JComboBox<String> cb = new JComboBox<>();
        ItemChangeListener changeListener = new ItemChangeListener(tableModel);


        cb.addItemListener(changeListener);
        cb.addItem("Points");
        cb.addItem("Scored goals");
        cb.addItem("Wins");

        JButton randomMatchButton = new JButton("Create new match");

        JButton addMatchButton = new JButton("Add match");

        JLabel firstTeamNameLabel = new JLabel("First team");
        JLabel firstTeamGoalsLabel = new JLabel("0");
        JLabel secondTeamNameLabel = new JLabel("Second team");
        JLabel secondTeamGoalsLabel = new JLabel("0");

        randomMatchButton.addActionListener(new ButtonActionListener(firstTeamNameLabel, firstTeamGoalsLabel, secondTeamNameLabel, secondTeamGoalsLabel, tableModel));

        firstTeamNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstTeamGoalsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        secondTeamNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        secondTeamGoalsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel firstTeamPanel = new JPanel();
        JPanel secondTeamPanel = new JPanel();

        firstTeamPanel.setLayout(new BoxLayout(firstTeamPanel, BoxLayout.Y_AXIS));
        secondTeamPanel.setLayout(new BoxLayout(secondTeamPanel, BoxLayout.Y_AXIS));

        firstTeamPanel.add(firstTeamNameLabel);
        firstTeamPanel.add(firstTeamGoalsLabel);

        secondTeamPanel.add(secondTeamNameLabel);
        secondTeamPanel.add(secondTeamGoalsLabel);

        JPanel matchStats = new JPanel(new GridLayout(0, 2));

        matchStats.add(firstTeamPanel);
        matchStats.add(secondTeamPanel);

        JPanel randomMatchPanel = new JPanel(new BorderLayout());
        randomMatchPanel.add(matchStats, BorderLayout.NORTH);
        randomMatchPanel.add(randomMatchButton, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(randomMatchPanel, BorderLayout.EAST);
        // Sort by Panel


        JPanel sortByPanel = new JPanel(new GridLayout(0, 2));
        sortByPanel.add(sortByTextLabel);
        sortByPanel.add(cb);

        // Panel that will hold everything!

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(sortByPanel, BorderLayout.EAST);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(leagueTable, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    public void randomMatch() {

    }


    class MyTableModel extends AbstractTableModel {
        private final ArrayList<FootballClub> footballClubList;
        private final String[] columns;

        MyTableModel(ArrayList<FootballClub> data, String[] columns) {
            this.footballClubList = data;
            this.columns = columns;
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
            fireTableDataChanged();
        }

        public void sortByWins() {
            footballClubList.sort(Comparator.comparingInt(FootballClub::getWins).reversed());
            fireTableDataChanged();
        }

        public void sortByScoredGoals() {
            footballClubList.sort(Comparator.comparingInt(FootballClub::getScoredGoals).reversed());
            fireTableDataChanged();
        }
    }

    class ItemChangeListener implements ItemListener {

        MyTableModel tableModel;

        ItemChangeListener(MyTableModel tableModel) {
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

    class ButtonActionListener implements ActionListener {

        FootballClub firstTeam = null;
        FootballClub secondTeam = null;
        int firstTeamGoals = 0;
        int secondTeamGoals = 0;
        String datePlayed = null;

        JLabel firstTeamNameLabel;
        JLabel firstTeamGoalsLabel;
        JLabel secondTeamNameLabel;
        JLabel secondTeamGoalsLabel;
        MyTableModel tableModel;

        ButtonActionListener(JLabel firstTeamNameLabel, JLabel firstTeamGoalsLabel, JLabel secondTeamNameLabel, JLabel secondTeamGoalsLabel, MyTableModel tableModel) {
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
            FootballClub secondTeamTemp;
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

            secondTeamTemp = footballClubsList.get(randomTeam);

            while (firstTeam == secondTeamTemp) {
                randomTeam = randomNumber(clubsListSize);
                secondTeamTemp = footballClubsList.get(randomTeam);
            }

            secondTeam = secondTeamTemp;
            secondTeamGoals = randomGoals;

            int daysRange = 27;
            int monthRange = 11;

            randomDay = randomNumber(daysRange) + 1 + "";
            randomMonth = randomNumber(monthRange) + 1 + "";

            datePlayed = randomDay.concat("/").concat(randomMonth).concat("/").concat("2020");

            PlayedMatch playedMatch = new PlayedMatch(datePlayed, firstTeam, firstTeamGoals, secondTeam, secondTeamGoals);

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
}
