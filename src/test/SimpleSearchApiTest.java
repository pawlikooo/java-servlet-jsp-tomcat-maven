import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sdk.stackexchange.Models.Owner;
import sdk.stackexchange.Models.Question;
import sdk.stackexchange.Models.SearchAnswer;
import sdk.stackexchange.SimpleSearchApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class SimpleSearchApiTest {

    @Mock
    SimpleSearchApi api;
    Random random;
    final int total = 1241;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        random = new Random();
    }

    @Test
    public void getItems() throws HttpException, UnirestException {
        final int pageSize = 10;
        String inputText = "search";

        SearchAnswer<Question> page1 = generate(pageSize);
        SearchAnswer<Question> page2 = generate(random.nextInt(pageSize));
        when(api.getItems(inputText, 1, pageSize)).thenReturn(page1);
        when(api.getItems(inputText, 2, pageSize)).thenReturn(page2);
        assertTrue(page2.items.size() <= page1.items.size());
        assertTrue(page2.items.size() <= pageSize);

        SearchAnswer answer1 = api.getItems(inputText, 1, pageSize);
        SearchAnswer answer2 = api.getItems(inputText, 2, pageSize);

        assertArrayEquals(answer1.items.toArray(), page1.items.toArray());
        assertArrayEquals(answer2.items.toArray(), page2.items.toArray());
        assertEquals(answer1.total, page1.total);
        assertEquals(answer2.total, page2.total);
    }

    private SearchAnswer<Question> generate(int count) {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            boolean isAnswered = random.nextFloat() > 0.5;
            Owner owner = new Owner("name", "link", "image");
            Question question = new Question("title" + i, owner, new Date(), isAnswered, "link" + i, random.nextInt(50));
            questions.add(question);
        }
        SearchAnswer<Question> result = new SearchAnswer<>(questions, total);
        return result;
    }
}