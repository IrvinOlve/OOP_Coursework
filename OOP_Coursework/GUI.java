package OOP_Coursework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;

public class GUI {

    private ArrayList<FootballClub> footballClubsList;
    private ArrayList<PlayedMatch> matchesPlayed;

    public void start() {

        // premierLeagueTable will display the data passed to it in a JTable.
        showGUI(footballClubsList);

    }

    public void setData(ArrayList<FootballClub> footballClubs, ArrayList<PlayedMatch> matchesPlayed) {
        this.footballClubsList = footballClubs;
        this.matchesPlayed = matchesPlayed;
    }

    private void showGUI(ArrayList<FootballClub> footballClubs) {

        JFrame frame = new JFrame("League Manager");

        frame.setSize(750, 500);
        frame.setLayout(new BorderLayout());

        // Creation of the Premier League, played matches tables and table models.
        PremierLeagueTableModel premierLeagueTM = new PremierLeagueTableModel(footballClubs);
        PlayedMatchesTableModel playedMatchesTM = new PlayedMatchesTableModel(matchesPlayed);

        JTable premierLeagueTable = new JTable(premierLeagueTM);
        JTable playedMatchesTable = new JTable(playedMatchesTM);
        premierLeagueTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
        playedMatchesTable.setPreferredScrollableViewportSize(new Dimension(320, 200));

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
        JPanel sortByPanel = new JPanel();
        JPanel topPanel = new JPanel(new BorderLayout());

        sortByPanel.add(sortByTextLabel);
        sortByPanel.add(sortTableOptionsCB);

        topPanel.add(sortByPanel, BorderLayout.EAST);

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

        // Container creation for match generator and table of standings.
        JPanel randomMatchPanel = new JPanel(new GridLayout(0, 1));
        JPanel teamContainer = new JPanel();

        // Set dimensions and positioning of the container.
        Dimension randomMatchContainerDimension = new Dimension(280, 60);

        randomMatchPanel.setPreferredSize(randomMatchContainerDimension);
        randomMatchPanel.setMaximumSize(randomMatchContainerDimension);

        teamContainer.setLayout(new BoxLayout(teamContainer, BoxLayout.X_AXIS));
        teamContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add match generator panel and button to randomMatchPanel container
        randomMatchPanel.add(matchStats);
        randomMatchPanel.add(randomMatchButton);

        randomMatchPanel.setBorder(new EmptyBorder(0, 15, 0, 15));

        teamContainer.add(randomMatchPanel);
        teamContainer.add(premierLeagueSP);


        // Container creation for the search of matches and played matches table
        JPanel matchContainer = new JPanel();
        JPanel matchToolsContainer = new JPanel(new GridLayout(0, 2));

        // Set dimensions and positioning of the containers.
        Dimension matchToolsContainerDimension = new Dimension(280, 90);

        matchToolsContainer.setPreferredSize(matchToolsContainerDimension);
        matchToolsContainer.setMaximumSize(matchToolsContainerDimension);

        matchContainer.setLayout(new BoxLayout(matchContainer, BoxLayout.X_AXIS));
        matchContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Create labels, text fields and buttons to search or show all matches.
        JLabel searchMatchLabel = new JLabel("  Date (dd/mm/yyyy)");
        JLabel messageStatusLabel = new JLabel("");

        JTextField textField = new JTextField(7);

        JButton searchMatchButton = new JButton("Search");
        JButton showAllMatchesButton = new JButton("Show all matches");

        // Add listeners to the buttons that will look for a specific match or show all matches.
        searchMatchButton.addActionListener(new SearchMatchButtonListener(messageStatusLabel, textField, playedMatchesTable));
        showAllMatchesButton.addActionListener(new ShowAllMatchesButtonListener(textField, messageStatusLabel, playedMatchesTable, matchesPlayed));

        // Add all created components to matchToolsContainer
        matchToolsContainer.add(searchMatchLabel);
        matchToolsContainer.add(textField);
        matchToolsContainer.add(showAllMatchesButton);
        matchToolsContainer.add(searchMatchButton);
        matchToolsContainer.add(messageStatusLabel);

        // Add matchToolsContainer and playedMatchesSP to one container
        matchContainer.add(matchToolsContainer);
        matchContainer.add(playedMatchesSP);

        // panel that will hold all the
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(teamContainer);
        mainPanel.add(matchContainer);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void setColumnWidth(JTable clubs) {
        // This method will set the width of the table of standings for a better visualisation
        clubs.getColumnModel().getColumn(0).setPreferredWidth(100);
        for (int i = 1; i <= 8; i++) {
            clubs.getColumnModel().getColumn(i).setPreferredWidth(5);
        }
    }

    private class PremierLeagueTableModel extends AbstractTableModel {
        /*
         * This class creates a table model containing football clubs created or already saved
         * in the files. It will show all the statistics available for each object. By default
         * all values should be 0, but as soon as a random match is generated the table will
         * update accordingly. This class contains custom methods that will sort the data passed
         * on to it according to the assignment specifications (by: points, goals scored or wins)
         */

        private ArrayList<FootballClub> footballClubs;
        private String[] columns;

        PremierLeagueTableModel(ArrayList<FootballClub> data) {
            this.footballClubs = data;
            this.columns = new String[]{"CLUB", "P", "W", "D", "L", "S", "R", "GD", "Pts"};
            sortByPoints();
        }

        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public int getRowCount() {
            return footballClubs.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int row, int column) {

                switch (column) {
                    case 0:   // col 1
                        return footballClubs.get(row).getName();
                    case 1:   // col 2
                        return footballClubs.get(row).getPlayedMatches();
                    case 2:   // col 2
                        return footballClubs.get(row).getWins();
                    case 3:   // col 3
                        return footballClubs.get(row).getDraws();
                    case 4:   // col 4
                        return footballClubs.get(row).getDefeats();
                    case 5:   // col 4
                        return footballClubs.get(row).getScoredGoals();
                    case 6:   // col 4
                        return footballClubs.get(row).getReceivedGoals();
                    case 7:   // col 4
                        return footballClubs.get(row).getGoalsDifference();
                    case 8:   // col 4
                        return footballClubs.get(row).getPoints();
                    default:
                        return "";
                }
        }

        public void sortByPoints() {
            footballClubs.sort(Comparator.comparingInt(FootballClub::getPoints).reversed());
            this.fireTableDataChanged();
        }

        public void sortByWins() {
            footballClubs.sort(Comparator.comparingInt(FootballClub::getWins).reversed());
            this.fireTableDataChanged();
        }

        public void sortByScoredGoals() {
            footballClubs.sort(Comparator.comparingInt(FootballClub::getScoredGoals).reversed());
            this.fireTableDataChanged();
        }
    }

    private class PlayedMatchesTableModel extends AbstractTableModel {

        /* This class creates a table model containing manually added, randomly generated
         * or already saved matches. This method has a custom method that will sort out the
         * matches by date played in ascending order.
         */
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
                    return matchesPlayed.get(row).getHomeTeamName();
                case 2:   // col 3
                    return matchesPlayed.get(row).getHomeTeamGoals() + " - " + matchesPlayed.get(row).getAwayTeamGoals();
                case 3:   // col 4
                    return matchesPlayed.get(row).getAwayTeamName();
                default:
                    return "";
            }
        }

        public void sortByDate() {
            // custom comparator to sort matches by date in ascending order.
            matchesPlayed.sort(Comparator.comparing(o ->
                    {
                        try {
                            // as date is saved as a string, it needs to be parsed to date in the format as shown "dd/MM/yyyy"
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

    private class GenerateMatchButtonListener extends PremierLeagueManager implements ActionListener {

        /*
         * This class is a custom listener for the button that generates random matches. It takes some
         * components that will be updated based on the random match generated. This class also contains
         * temporary variables that will hold data for the random match generated. It contains a
         * methods that handles the generation of a TOTAL random match between two teams and a date.
         */

        FootballClub HomeTeam = null;
        FootballClub AwayTeam = null;
        int HomeTeamGoals = 0;
        int AwayTeamGoals = 0;
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
            firstTeamNameLabel.setText(HomeTeam.getName());
            firstTeamGoalsLabel.setText(String.valueOf(HomeTeamGoals));
            secondTeamNameLabel.setText(AwayTeam.getName());
            secondTeamGoalsLabel.setText(String.valueOf(AwayTeamGoals));
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

            // clubsListSize will hold the number of football clubs currently existing.
            // This is to know at what range a random number should be generated.
            int clubsListSize = footballClubsList.size();

            // number of goals a football club could possibly score. Let's keep it realistic.
            int goalsRange = 6;

            // random football club index and random number of goals scored for Home Team
            randomTeam = randomNumber(clubsListSize);
            randomGoals = randomNumber(goalsRange);

            // Get football club by index with random generated number and store goals scored.
            HomeTeam = footballClubsList.get(randomTeam);
            HomeTeamGoals = randomGoals;

            // random football club index and random number of goals scored for Away Team
            randomGoals = randomNumber(goalsRange);
            randomTeam = randomNumber(clubsListSize);

            // Get football club by index with random generated number and store goals scored.
            AwayTeam = footballClubsList.get(randomTeam);
            AwayTeamGoals = randomGoals;

            // Common problem. Computers do not know what random is really, so this loop makes sure a team
            // does not play with themselves.
            while (HomeTeam == AwayTeam) {
                randomTeam = randomNumber(clubsListSize);
                AwayTeam = footballClubsList.get(randomTeam);
            }

            // Range for the date of the match.
            int daysRange = 27;
            int monthRange = 11;

            // A random number can also be 0, but is not acceptable when it comes to date, so we add 1 to it.
            randomDay = randomNumber(daysRange) + 1 + "";
            randomMonth = randomNumber(monthRange) + 1 + "";

            // This is more for better visualisation of the table of standings. If the random date is a digit long
            // simply add a 0 in front.
            if (randomDay.length() == 1) {
                randomDay = "0" + randomDay;
            }
            if (randomMonth.length() == 1) {
                randomMonth = "0" + randomMonth;
            }

            // We add everything together and save it.
            datePlayed = randomDay.concat("/").concat(randomMonth).concat("/").concat("2020");
            playedMatch = new PlayedMatch(datePlayed, HomeTeam, HomeTeamGoals, AwayTeam, AwayTeamGoals);

            // set statistics of the match and update scored of the clubs involved.
            setStatistics(playedMatch);

            // add match to arraylist and update table.
            matchesPlayed.add(playedMatch);
            tableModel.fireTableDataChanged();
        }
    }

    private class SearchMatchButtonListener implements ActionListener {

        /*
         * This class is a custom listener for the search match button. It takes a few components that
         * wil be updated accordingly based on the matches found.
         */
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
                messageStatusLabel.setText("  Not matches found!");
            } else {
                messageStatusLabel.setText("  " + foundPlayedMatches.size() + " matches found.");
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

        /*
         * This class is a custom listener for the show all matches button. It takes a few
         * components that will be updated accordingly based on the matches found.
         */
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

            if (matchesPlayed.isEmpty()) {
                messageStatusLabel.setText("  Not matches found!");
            } else {
                messageStatusLabel.setText("  " + matchesPlayed.size() + " matches found.");
            }
            textField.setText("");
        }
    }

    private class OptionComboBoxListener implements ItemListener {

        /*
         * This is a custom listener for the combo box. It will listen for the option
         * selected and update the table of standings based accordingly.
         */
        PremierLeagueTableModel tableModel;

        OptionComboBoxListener(PremierLeagueTableModel tableModel) {
            this.tableModel = tableModel;
        }

        @Override
        public void itemStateChanged(ItemEvent event) {

            String selectedOption = event.getItem().toString();

            // This switch will listen for the word selected and call the methods required.
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