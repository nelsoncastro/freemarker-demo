package com.util.notification.interceptors;


import com.model.enums.NotificationType;
import com.model.service.MailService;
import com.util.mail.Mail;
import com.util.notification.Notification;
import com.util.notification.Processors.NotificationProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Aspect
public class NotificationInterceptor {

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationProcessor processor;

    @AfterReturning(value = "@annotation(notification)", returning = "data")
    public void sendMail(JoinPoint jp, Notification notification, Object data) {
        System.out.println("sending " + notification.type() + " notification after return from " + jp.getSignature().getName() + " method...");

        if (notification.type() != NotificationType.CHARGEBACK || data == null)
            return;

        Map<String, Object> model = new HashMap<>();
        model.put("mensagem", "O seu pedido de Chargeback foi registrado. O acompanhamento pode ser realizado pela plataforma Cabal NET.");
        model.put("dado", data);
        String mailContent = processor.process("chargeback/content.ftl", model);

        String[] mailFrom = {"nelson.castro@cabal.com.br"};
        String[] mailTo = {"nelson.castro@cabal.com.br"};
        String mailSubject = "BANDEIRA - SOLICITAÇÃO DE CHARGEBACK";

        Mail mail = new Mail.Builder(mailFrom[0], mailTo[0], mailSubject, mailContent).build();

        //mailService.sendMail(mail);
    }
}
