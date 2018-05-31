package servlets;

import sdk.stackexchange.Models.Question;

import java.util.List;

public class RequestAttributes {
    private final String title;
    private final int page;
    private final List<Question> questions;
    private final int total;
    private final String error;

    static RequestAttributes errorRequestAttributes(String error) {
        return new RequestAttributes(error);
    }

    public String getTitle() {
        return title;
    }

    public int getPage() {
        return page;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getTotal() {
        return total;
    }

    public String getError() {
        return error;
    }

    public RequestAttributes(String title, int page, List<Question> questions, int total, String error) {
        this.title = title;
        this.page = page;
        this.questions = questions;
        this.total = total;
        this.error = error;
    }

    private RequestAttributes(String error) {
        this.title = null;
        this.page = 0;
        this.questions = null;
        this.total = 0;
        this.error = error;
    }


}
