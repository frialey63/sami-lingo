package org.pjp.lingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PWA(name = "Sami Lingo", shortName = "Sami Lingo")
@Theme("my-theme")
public class LingoApp implements AppShellConfigurator {

    private static final long serialVersionUID = -1501968338403573964L;

    public static void main(String[] args) {
        SpringApplication.run(LingoApp.class, args);
    }
}
