package com.myorg.redis.servlet;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import javax.servlet.ServletContext;

public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {
    System.out.println("onstatup called");
    if("sessionha".equalsIgnoreCase(System.getProperty("spring.profiles.active"))) {
      System.out.println("adding spring sesson filter");
      super.onStartup(servletContext);
    } else {
      System.out.println("backout plan, so no session HA");
    }
  }

}
