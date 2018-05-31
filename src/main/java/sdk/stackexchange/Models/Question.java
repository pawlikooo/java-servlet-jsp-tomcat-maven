package sdk.stackexchange.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Question {
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final String title;
    private final Owner owner;
    private String date;
    private final Boolean is_answered;
    private final String link;
    private final int answer_count;

    private Date creation_date;

    public Question(String title, Owner owner, Date date, Boolean is_answered, String link, int answer_count) {
        this.title = title;
        this.owner = owner;
        this.creation_date = date;
        this.is_answered = is_answered;
        this.link = link;
        this.answer_count = answer_count;
    }

    public String getDate() {
        return dateFormat.format(creation_date);
    }

    public String getTitle() {
        return title;
    }

    public Owner getOwner() {
        return owner;
    }

    public Boolean getIs_answered() {
        return is_answered;
    }

    public String getLink() {
        return link;
    }

    public int getAnswer_count() {
        return answer_count;
    }
}