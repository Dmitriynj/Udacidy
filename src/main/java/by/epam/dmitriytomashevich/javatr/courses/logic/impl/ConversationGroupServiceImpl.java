package by.epam.dmitriytomashevich.javatr.courses.logic.impl;

import by.epam.dmitriytomashevich.javatr.courses.db.dao.ConversationGroupDao;
import by.epam.dmitriytomashevich.javatr.courses.domain.Conversation;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.DAOException;
import by.epam.dmitriytomashevich.javatr.courses.domain.ConversationGroup;
import by.epam.dmitriytomashevich.javatr.courses.logic.ConversationGroupService;
import by.epam.dmitriytomashevich.javatr.courses.exceptions.LogicException;

public class ConversationGroupServiceImpl implements ConversationGroupService {
    private final ConversationGroupDao conversationGroupDao = new ConversationGroupDao();

    @Override
    public ConversationGroup findById(Long id) throws LogicException {
        try {
            return conversationGroupDao.findById(id);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public Long add(ConversationGroup conversationGroup) throws LogicException {
        try {
            return conversationGroupDao.create(conversationGroup);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public void deleteByConversationId(Long conversationId) throws LogicException {
        try {
            conversationGroupDao.deleteByConversationId(conversationId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public ConversationGroup findByUserIdAndConversationType(Long userId, Conversation.ConversationType type) throws LogicException {
        try {
            return conversationGroupDao.findByUserIdAndConversationType(userId, type);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }
}
