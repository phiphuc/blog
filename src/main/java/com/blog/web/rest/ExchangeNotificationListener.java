package com.blog.web.rest;


import org.apache.juli.logging.LogFactory;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/emailnotification")
public class ExchangeNotificationListener {

    private static final String OK = "OK";
    private static final String UNSUBSCRIBE = "Unsubscribe";

    /**
     * This method receives a SOAP message from Microsoft Exchange Web Services, parses it into a ExchangeNotification object,
     * do some business logic and sends an ACK response to keep the subscription alive.
     *
     * @param request
     *            EWS Push Notification request
     * @param response
     * @throws Exception
     */
    @Path("/incomingevent")
    @POST()
    @Consumes(MediaType.TEXT_XML)
    public void onNotificationReceived(@Context HttpServletRequest request, @Context HttpServletResponse response)
        throws Exception {
        System.out.println("START SENT MAIL");

    }
}
