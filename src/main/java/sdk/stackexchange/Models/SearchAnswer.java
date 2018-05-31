package sdk.stackexchange.Models;

import java.util.List;

public class SearchAnswer<T> {
    public final List<T> items;
    public final int total;

    public SearchAnswer(List<T> items, int total) {
        this.items = items;
        this.total = total;
    }
}
