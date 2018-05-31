package sdk.stackexchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import sdk.stackexchange.Models.Question;
import sdk.stackexchange.Models.SearchAnswer;
import sdk.stackexchange.gson.rules.MyDateTypeAdapter;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.logging.Logger;

public class SimpleSearchApi implements SearchApi {

    private static final Logger logger = Logger.getLogger("searchAPI");
    private static final String API_URL = "https://api.stackexchange.com/2.2/search";

    @Override
    public SearchAnswer getItems(String intitle, int page, int pageSize) throws HttpException, UnirestException {
        logger.info(String.format("api.stackexchange.com call with intitle %s, page %s, pageSize %s", intitle, page, pageSize));
        HttpResponse<String> response = Unirest.get(API_URL)
                .header("accept", "application/json")
                .queryString("site", "stackoverflow")
                .queryString("order", "desc")
                .queryString("sort", "creation")
                .queryString("intitle", intitle)
                .queryString("page", page)
                .queryString("pagesize", pageSize)
                // default filter with wrapped "total"
                .queryString("filter", "!9Z(-x-Q)8")
                .asString();

        if (response.getStatus() != HttpStatus.SC_OK) {
            logger.warning(String.format("api.stackexchange.com call ends with error %s", response.getStatusText()));
        }

        SearchAnswer<Question> answer;
        try {
            String jsonString = response.getBody();
            Type answerType = new TypeToken<SearchAnswer<Question>>() {
            }.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();
            answer = gson.fromJson(jsonString, answerType);
        } catch (JsonSyntaxException e) {
            logger.warning(String.format("Error parsing JSON %s", e.getMessage()));
            throw e;
        } catch (Exception e) {
            logger.warning(String.format("Common error parsing JSON %s", e.getMessage()));
            throw e;
        }
        return answer;
    }
}