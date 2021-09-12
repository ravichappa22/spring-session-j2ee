package com.myorg.redis.servlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import javax.servlet.ServletContext;

@Configuration
public class Initializer extends AbstractHttpSessionApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {
    System.out.println("profile running = " + System.getProperty("spring.profiles.active"));
    if("sessionha".equalsIgnoreCase(System.getProperty("spring.profiles.active"))) {
      super.onStartup(servletContext);
    } else {
      System.out.println("backout plan, so no session HA");
    }

  }


}
