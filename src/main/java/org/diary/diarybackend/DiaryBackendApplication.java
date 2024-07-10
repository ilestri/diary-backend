package org.diary.diarybackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;

@SpringBootApplication
public class DiaryBackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(DiaryBackendApplication.class);

    public static void main(String[] args) {
        // SpringApplication.run(DiaryBackendApplication.class, args);

        // More complex application startup with SpringApplicationBuilder
        new SpringApplicationBuilder(DiaryBackendApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .logStartupInfo(true)
                .listeners(applicationListeners())
                .run(args);

        System.out.println("##### diary-server start #####");
    }

    private static ApplicationListener<?>[] applicationListeners() {
        return new ApplicationListener[]{
                (ApplicationListener<ContextRefreshedEvent>) event -> logger.info("Application started..."),
                (ApplicationListener<ContextStoppedEvent>) event -> logger.info("Application stopped...")
        };
    }
}
