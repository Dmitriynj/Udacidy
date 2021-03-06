package by.epam.dmitriytomashevich.javatr.courses.command.admin;

import by.epam.dmitriytomashevich.javatr.courses.command.Command;
import by.epam.dmitriytomashevich.javatr.courses.command.SessionRequestContent;
import by.epam.dmitriytomashevich.javatr.courses.constant.ParameterNames;
import by.epam.dmitriytomashevich.javatr.courses.domain.Conference;
import by.epam.dmitriytomashevich.javatr.courses.domain.Content;
import by.epam.dmitriytomashevich.javatr.courses.domain.Section;
import by.epam.dmitriytomashevich.javatr.courses.domain.User;
import by.epam.dmitriytomashevich.javatr.courses.logic.ConferenceService;
import by.epam.dmitriytomashevich.javatr.courses.logic.ContentService;
import by.epam.dmitriytomashevich.javatr.courses.logic.SectionService;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;
import by.epam.dmitriytomashevich.javatr.courses.logic.impl.ConferenceServiceImpl;
import by.epam.dmitriytomashevich.javatr.courses.logic.impl.ContentServiceImpl;
import by.epam.dmitriytomashevich.javatr.courses.logic.impl.SectionServiceImpl;
import by.epam.dmitriytomashevich.javatr.courses.util.validation.FieldValidator;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CreateConferenceCommand implements Command {
    private final ConferenceService conferenceService = new ConferenceServiceImpl();
    private final ContentService contentService = new ContentServiceImpl();
    private final SectionService sectionService = new SectionServiceImpl();

    @Override
    public Optional<String> execute(SessionRequestContent content) throws LogicException {
        User admin = (User) content.getSession(false).getAttribute(ParameterNames.USER);
        String htmlContent = content.getParameter(ParameterNames.CONTENT);
        List<String> jsonSections = new Gson().fromJson(content.getParameter(ParameterNames.SECTIONS), List.class);

        if (FieldValidator.isNotFilled(htmlContent) ||
                jsonSections.stream().anyMatch(FieldValidator::isNotFilled)) {
            return Optional.empty();
        }

        Content conferenceContent = new Content();
        conferenceContent.setContent(htmlContent);
        Long conferenceContentId = contentService.create(conferenceContent);

        Conference conference = new Conference();
        conference.setContentId(conferenceContentId);
        conference.setAuthorId(admin.getId());
        Long conferenceId = conferenceService.create(conference);


        for (String stringSection : jsonSections) {
            Content sectionContent = new Content();
            sectionContent.setContent(stringSection);
            Long sectionContentId = contentService.create(sectionContent);

            Section section = new Section();
            section.setConferenceId(conferenceId);
            section.setContentId(sectionContentId);

            sectionService.create(section);
        }

        try {
            content.getResponse().setContentType("text/plain");
            PrintWriter writer = content.getResponse().getWriter();
            writer.print("You successfully created new conference! Congratulations!");
        } catch (IOException e) {
            throw new LogicException(e);
        }
        return Optional.empty();
    }
}
