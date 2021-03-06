package com.myorg.redis.servlet;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

@Component
public class WebAppInitializer implements ApplicationListener<SessionCreatedEvent> {

  @Override
  @EventListener
  public void onApplicationEvent(SessionCreatedEvent event) {
    System.out.println("Received spring custom event - " + event.getSessionId());
  }
}
