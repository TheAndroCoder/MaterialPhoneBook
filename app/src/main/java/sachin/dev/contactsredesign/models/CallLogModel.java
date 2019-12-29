package sachin.dev.contactsredesign.models;

public class CallLogModel {
    private String name,number,type,date,duration,direction;
    private int simNumber;
    public CallLogModel(){}

    public CallLogModel(String name,String number, String type, String date, String duration, String direction, int simNumber) {
        this.number = number;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.direction = direction;
        this.name=name;
        this.simNumber = simNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(int simNumber) {
        this.simNumber = simNumber;
    }
}
