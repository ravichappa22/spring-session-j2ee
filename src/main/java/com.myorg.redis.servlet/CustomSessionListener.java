package com.myorg.redis.servlet;

import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
public class CustomSessionListener implements HttpSessionListener {

  @Override
  public void sessionCreated (HttpSessionEvent httpSessionEvent) {
    System.out.println("session created " + httpSessionEvent.getSession());
    httpSessionEvent.getSession().setAttribute("csrfgurad", "csrfguad");
    System.out.println("csrfGuardValue in sessionlistener = " + httpSessionEvent.getSession().getAttribute("csrfgurad"));
    System.out.println("isnew in listener = " + httpSessionEvent.getSession().isNew());
  }

  @Override
  public void sessionDestroyed (HttpSessionEvent httpSessionEvent) {
    System.out.println("session destroyed");
  }

}
