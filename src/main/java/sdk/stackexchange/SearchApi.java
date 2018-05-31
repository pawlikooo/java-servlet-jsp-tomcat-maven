package sdk.stackexchange;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpException;
import sdk.stackexchange.Models.SearchAnswer;

public interface SearchApi {
    SearchAnswer getItems(String title, int page, int pageSize) throws HttpException, UnirestException;
}
