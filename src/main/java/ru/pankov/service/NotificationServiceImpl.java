package ru.pankov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pankov.logic.BusinessLogic;
import ru.pankov.dao.NotificationRepository;
import ru.pankov.entity.Comment;
import ru.pankov.entity.Notification;
import ru.pankov.service.inter.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Page<Notification> getNotifications(Integer page) {
        return notificationRepository.findAll(PageRequest.of(page - 1, 10));
    }

    @Override
    @Transactional
    public Notification addNotification(Comment comment) {
        Notification notification = notificationRepository.save(new Notification(comment));

        try {
            BusinessLogic.doSomeWorkOnNotification();
        } catch (RuntimeException e) {
            notification.setDelivered(false);
        }

        log.info(String.format("Notification #%d: " + (notification.getDelivered() ? "success" : "failure"), notification.getId()));
        return notification;
    }
}