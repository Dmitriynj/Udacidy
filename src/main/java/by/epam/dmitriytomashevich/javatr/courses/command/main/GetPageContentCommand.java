package by.epam.dmitriytomashevich.javatr.courses.command.main;

import by.epam.dmitriytomashevich.javatr.courses.command.Command;
import by.epam.dmitriytomashevich.javatr.courses.command.SessionRequestContent;
import by.epam.dmitriytomashevich.javatr.courses.constant.ParameterNames;
import by.epam.dmitriytomashevich.javatr.courses.domain.*;
import by.epam.dmitriytomashevich.javatr.courses.domain.json.JsonConference;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;
import by.epam.dmitriytomashevich.javatr.courses.logic.*;
import by.epam.dmitriytomashevich.javatr.courses.logic.impl.*;
import by.epam.dmitriytomashevich.javatr.courses.util.converter.ConferenceConverter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetPageContentCommand implements Command {
    private final ConferenceService conferenceService = new ConferenceServiceImpl();
    private final ContentService contentService = new ContentServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final SectionService sectionService = new SectionServiceImpl();
    private final RequestService requestService = new RequestServiceImpl();

    @Override
    public Optional<String> execute(SessionRequestContent content) throws LogicException {
        User client = (User) content.getSession(false).getAttribute(ParameterNames.USER);

        Long page = Long.valueOf(content.getParameter("page"));
        Long skipConferencesNumber = (page-1)* ParameterNames.CONFERENCES_PER_PAGE_AMOUNT;
        Long limit = (long) ParameterNames.CONFERENCES_PER_PAGE_AMOUNT;

        List<Conference> conferenceList = conferenceService.findFromRowIndexToLimit(skipConferencesNumber, limit);
        List<Long> requestConferenceIds = new ArrayList<>();
        JsonArray jsonConferences = new JsonArray();
        for(Conference c:conferenceList){
            c.setContent(contentService.findById(c.getContentId()));
            c.setAuthor(userService.findById(c.getAuthorId()));
            List<Section> sectionList = sectionService.findSectionsByConferenceId(c.getId());
            for(Section s:sectionList){
                s.setContent(contentService.findById(s.getContentId()));
            }
            c.setSections(sectionList);

            JsonConference jsonConference = new ConferenceConverter().convert(c);
            jsonConferences.add(new Gson().toJsonTree(jsonConference, JsonConference.class));

            Request request = requestService.findByUserIdAndConferenceId(client.getId(), c.getId());
            if(request != null){
                requestConferenceIds.add(request.getConferenceId());
            }
        }
        try {
            content.getResponse().setContentType("application/json;charset=UTF-8");
            final JsonNodeFactory factory = JsonNodeFactory.instance;
            final ObjectNode node = factory.objectNode();
            node.putPOJO("conferences", jsonConferences);
            node.putPOJO("requestConferenceIds", requestConferenceIds);
            PrintWriter writer = content.getResponse().getWriter();
            writer.print(node);
        } catch (IOException e) {
            throw new LogicException(e);
        }
        return Optional.empty();
    }
}
