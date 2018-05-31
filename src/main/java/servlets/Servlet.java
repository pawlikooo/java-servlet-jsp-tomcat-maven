package servlets;

import com.mashape.unirest.http.exceptions.UnirestException;
import models.Client;
import org.apache.http.HttpException;
import sdk.stackexchange.Models.SearchAnswer;
import sdk.stackexchange.SearchApi;
import sdk.stackexchange.SimpleSearchApi;
import services.AccessService;
import services.SimpleAccessService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.*;

@WebServlet("")
public class Servlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger("Servlet");

    private AccessService accessService;
    private SearchApi searchApi;

    private static final long serialVersionUID = -2194970811734020754L;
    private static final int PAGE_SIZE = 10;
    private static final String TITLE_PARAMETER = "title";
    private static final String PAGE_PARAMETER = "page";
    private static final String JSP_FILE_NAME = "/WEB-INF/main.jsp";

    @Override
    public void init() {
        accessService = new SimpleAccessService();
        searchApi = new SimpleSearchApi();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Client client = accessService.getAccess(ip);
        logger.info(String.format("client %s    GET    Last access date %s", ip, client.getLastAccessDate()));
        if (client.isBlocked()) {
            logger.warning(String.format("client %s    GET    blocked to prevent abuse", ip));
            request.setAttribute("error", client.getBlockMessage());
        }
        request.getRequestDispatcher(JSP_FILE_NAME).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Client client = accessService.getAccess(ip);
        logger.info(String.format("client %s    POST    Last access date %s", ip, client.getLastAccessDate()));
        RequestAttributes attributes;
        if (client.isBlocked()) {
            logger.warning(String.format("client %s    POST    blocked to prevent abuse", ip));
            attributes = RequestAttributes.errorRequestAttributes(client.getBlockMessage());
        } else {
            attributes = getAttributes(request);
        }
        specifyRequestAttributes(request, attributes);
        request.getRequestDispatcher(JSP_FILE_NAME).forward(request, response);
    }

    private RequestAttributes getAttributes(HttpServletRequest request) {
        try {
            String ip = request.getRemoteAddr();
            String title = request.getParameter(TITLE_PARAMETER);
            int page = getPage(request.getParameter(PAGE_PARAMETER));
            logger.info(String.format("client %s    POST    title: %s, page: %s", ip, title, page));
            SearchAnswer answer = searchApi.getItems(title, page, PAGE_SIZE);
            if (answer == null || answer.items == null) {
                return RequestAttributes.errorRequestAttributes("Bad request");
            }
            if (answer.items.size() == 0) {
                return RequestAttributes.errorRequestAttributes("Nothing found");
            }
            logger.info(String.format("client %s    POST    search API call. Get %s items out of %s", ip, answer.items.size(), answer.total));
            return new RequestAttributes(title, page, answer.items, answer.total, null);
        } catch (UnirestException | HttpException e) {
            logger.severe(e.getMessage());
            return RequestAttributes.errorRequestAttributes(e.getMessage());
        }
    }

    private static void specifyRequestAttributes(HttpServletRequest request, RequestAttributes attributes) {
        request.setAttribute("page", attributes.getPage());
        request.setAttribute("title", attributes.getTitle());
        request.setAttribute("questions", attributes.getQuestions());
        request.setAttribute("total", attributes.getTotal());
        request.setAttribute("error", attributes.getError());
    }

    private int getPage(String pageParameterValue) {
        int page;
        if (pageParameterValue == null || pageParameterValue.isEmpty()) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(pageParameterValue);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        return page;
    }
}