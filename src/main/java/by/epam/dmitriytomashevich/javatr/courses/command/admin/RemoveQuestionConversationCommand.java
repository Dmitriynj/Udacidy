package by.epam.dmitriytomashevich.javatr.courses.command.admin;

import by.epam.dmitriytomashevich.javatr.courses.command.Command;
import by.epam.dmitriytomashevich.javatr.courses.command.SessionRequestContent;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;
import by.epam.dmitriytomashevich.javatr.courses.factory.ServiceFactory;
import by.epam.dmitriytomashevich.javatr.courses.logic.ConversationGroupService;
import by.epam.dmitriytomashevich.javatr.courses.logic.ConversationService;
import by.epam.dmitriytomashevich.javatr.courses.logic.MessageService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class RemoveQuestionConversationCommand implements Command {
    private final MessageService messageService;
    private final ConversationService conversationService;
    private final ConversationGroupService conversationGroupService;

    public RemoveQuestionConversationCommand(ServiceFactory serviceFactory){
        messageService = serviceFactory.createMessageService();
        conversationService = serviceFactory.createConversationService();
        conversationGroupService = serviceFactory.createConversationGroupService();
    }

    @Override
    public Optional<String> execute(SessionRequestContent content) throws LogicException {
        String conversationIdAsString = content.getParameter("conversationId");
        if(conversationIdAsString != null){
            long convId = Long.parseLong(conversationIdAsString);
            messageService.removeAllByConversationId(convId);
            conversationGroupService.deleteByConversationId(convId);
            conversationService.deleteById(convId);
        }

        return Optional.empty();
    }
}
