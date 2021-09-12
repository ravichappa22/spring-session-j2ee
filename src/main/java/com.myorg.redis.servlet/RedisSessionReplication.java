package com.myorg.redis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

public class RedisSessionReplication extends HttpServlet {
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  }

  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Boolean display = true;
    HttpSession session = request.getSession();
    if (session == null) {
      session = request.getSession(true);

      request.setAttribute("isNewTest", "Session is created first time." + session);
      System.out.println("New Session = " + session.getId());
    }
    else {
      request.setAttribute("isNewTest", "Session already created");
      System.out.println("Session exists = " + session.getId() + " original Session" + session);
      session.setMaxInactiveInterval(600);
    }

    if (request.getParameter("action") != null) {
      if (request.getParameter("action").equals("Set Attribute") && request.getParameter("key") != null
            && !request.getParameter("value").equals("null")) {
        System.out.println("set action");
        session.setAttribute(request.getParameter("key"), request.getParameter("value"));
      }

      if (request.getParameter("action").equals("Get Attribute") && request.getParameter("key") != null) {
        System.out.println("get action");
        request.setAttribute("getKey", session.getAttribute(request.getParameter("key")));
      }

      if (request.getParameter("action").equals("Delete Attribute") && request.getParameter("key") != null) {
        session.removeAttribute(request.getParameter("key"));
      }

      if (request.getParameter("action").equals("complete")) {
        System.out.println("complete action " + session.getId());
        session.invalidate();
        //this has to delete only session data, but not session key
        /*HazelcastInstance instance = HazelClientIntfImpl.getInstance();
        instance.getMap("wc-sessions").delete(session.getId());*/

      }

      if (request.getParameter("action").equals("logout")) {
        System.out.println("logout action");
        display = false;
        String hazelId = session.getId();
        // String jessionId = ((HazelcastHttpSession)session).getOriginalSessionId();
        session.invalidate();
        request.getRequestDispatcher("/logout.jsp").forward(request, response);
        //response.getWriter().println("hz session invalidated" + hazelId);
        //response.getWriter().println("session invalidated" + jessionId);
      }
    } else {
       request.getSession().invalidate();
       System.out.println("invalidated going to create new");
       session = request.getSession(true);
      System.out.println("isnew in servlet = " + session.isNew());
    }

    if (display) {
      Enumeration names = session.getAttributeNames();
      StringBuilder sb = new StringBuilder();
      sb.append("<table border=\"1\"><th>Key</th><th>Value</th>");
      while (names.hasMoreElements()) {
        String name = (String) names.nextElement();
        sb.append("<tr><td>").append(name).append("</td><td>").append(session.getAttribute(name)).append("</td></tr>");
      }
      sb.append("</table>");
      String res = sb.toString();

      request.setAttribute("res", res);
      request.getRequestDispatcher("/redis.jsp").forward(request, response);
    }
  }
}
