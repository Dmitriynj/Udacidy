package by.epam.dmitriytomashevich.javatr.courses.command.user;

import by.epam.dmitriytomashevich.javatr.courses.command.Command;
import by.epam.dmitriytomashevich.javatr.courses.command.SessionRequestContent;
import by.epam.dmitriytomashevich.javatr.courses.constant.ParameterNames;
import by.epam.dmitriytomashevich.javatr.courses.domain.Request;
import by.epam.dmitriytomashevich.javatr.courses.domain.RequestForm;
import by.epam.dmitriytomashevich.javatr.courses.domain.User;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;
import by.epam.dmitriytomashevich.javatr.courses.logic.RequestFormService;
import by.epam.dmitriytomashevich.javatr.courses.logic.RequestService;
import by.epam.dmitriytomashevich.javatr.courses.logic.impl.RequestFormServiceImpl;
import by.epam.dmitriytomashevich.javatr.courses.logic.impl.RequestServiceImpl;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class RemoveRequestCommand implements Command {
    private final RequestService requestService = new RequestServiceImpl();
    private final RequestFormService requestFormService = new RequestFormServiceImpl();

    @Override
    public Optional<String> execute(SessionRequestContent content) throws LogicException {
        User user = (User) content.getSession(false).getAttribute(ParameterNames.USER);
        String conferenceIdAsString = content.getParameter("conferenceId");
        boolean isPositiveResult = false;
        if(conferenceIdAsString !=null ){
            Long conferenceId = Long.valueOf(content.getParameter("conferenceId"));

            Request request = requestService.findByUserIdAndConferenceId(user.getId(), conferenceId);
            List<RequestForm> requestFormList = requestFormService.findByRequestId(request.getId());
            for(RequestForm rf:requestFormList){
                requestFormService.delete(rf.getId());
            }
            requestService.delete(request.getId());
            isPositiveResult=true;
        }

        try {
            content.getResponse().setContentType("application/json;charset=UTF-8");
            final JsonNodeFactory factory = JsonNodeFactory.instance;
            final ObjectNode node = factory.objectNode();
            node.put(ParameterNames.IS_POSITIVE_RESULT, isPositiveResult);
            PrintWriter writer = content.getResponse().getWriter();
            writer.print(node);
        } catch (IOException e) {
            throw new LogicException(e);
        }
        return Optional.empty();
    }
}
