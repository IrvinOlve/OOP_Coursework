package OOP_Coursework;

abstract class SportsClub {
    private String name;
    private String location;

    void setName(String name) {
        this.name = name;
    }

    void setLocation(String location) {
        this.location = location;
    }

    String getName() {
        return name;
    }

    String getLocation() {
        return location;
    }
}
