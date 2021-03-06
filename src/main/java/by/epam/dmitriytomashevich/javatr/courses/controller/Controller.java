package by.epam.dmitriytomashevich.javatr.courses.controller;

import by.epam.dmitriytomashevich.javatr.courses.command.ActionFactory;
import by.epam.dmitriytomashevich.javatr.courses.command.Command;
import by.epam.dmitriytomashevich.javatr.courses.command.CommandFactory;
import by.epam.dmitriytomashevich.javatr.courses.command.SessionRequestContent;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionRequestContent content = new SessionRequestContent(request, response);
        Optional<String> action = Optional.empty();

        CommandFactory commandFactory = new CommandFactory();
        Command command = new ActionFactory().defineCommand(request, commandFactory);
        try {
            action = command.execute(content);
            content.insertAttributes();
        } catch (LogicException e) {
            LOGGER.error("Error with controller", e);
        }

        if (action.isPresent()) {
            if (content.getActionType().equals(SessionRequestContent.ActionType.FORWARD)) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(action.get());
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + action.get());
            }
        }
    }
}
