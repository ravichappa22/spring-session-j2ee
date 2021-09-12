package com.myorg.redis.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class CSRFGuradFilter implements Filter {
  @Override
  public void init (FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    System.out.println("doFilter");

    HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
    System.out.println("isnew in filter = " + httpServletRequest.getSession().isNew());
    httpServletRequest.getSession().setAttribute("setAtFilter", "setAtFilter");
    Enumeration enumeration = httpServletRequest.getSession().getAttributeNames();
    System.out.println("enumeration = " + enumeration.hasMoreElements());
    while(enumeration.hasMoreElements()) {
      System.out.println("key value = " + enumeration.nextElement());
    }
    filterChain.doFilter(httpServletRequest, (HttpServletResponse)servletResponse);

  }

  @Override
  public void destroy () {

  }
}
