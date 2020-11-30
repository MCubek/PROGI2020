package hr.fer.pi.geoFighter.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.*;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String verificationURL) {
        Context context = new Context();
        context.setVariable("verificationURL", verificationURL);
        return templateEngine.process("mailTemplate", context);
    }
}
