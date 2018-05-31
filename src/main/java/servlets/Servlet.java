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

@WebServlet("")
public class Servlet extends HttpServlet {
    private AccessService accessService;
    private SearchApi searchApi;

    private static final long serialVersionUID = -2194970811734020754L;
    private final int PAGE_SIZE = 10;
    private final String TITLE_PARAMETER = "title";
    private final String PAGE_PARAMETER = "page";
    private final String JSP_FILE_NAME = "/WEB-INF/main.jsp";

    @Override
    public void init() {
        accessService = new SimpleAccessService();
        searchApi = new SimpleSearchApi();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Client client = accessService.getAccess(request.getRemoteAddr());
        if (client.isBlocked()) {
            request.setAttribute("error", client.getBlockMessage());
        }
        request.getRequestDispatcher(JSP_FILE_NAME).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Client client = accessService.getAccess(request.getRemoteAddr());
        if (client.isBlocked()) {
            request.setAttribute("error", client.getBlockMessage());
        } else {
            try {
                String title = request.getParameter(TITLE_PARAMETER);
                int page = getPage(request.getParameter(PAGE_PARAMETER));
                SearchAnswer answer = searchApi.getItems(title, page, PAGE_SIZE);
                request.setAttribute("page", page);
                request.setAttribute("title", title);
                request.setAttribute("questions", answer.items);
                request.setAttribute("total", answer.total);
            } catch (UnirestException | HttpException e) {
                request.setAttribute("error", e.getMessage());
            }
        }
        request.getRequestDispatcher(JSP_FILE_NAME).forward(request, response);
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