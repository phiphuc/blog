package com.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.blog.domain.GoogleSearchConfig;

import com.blog.repository.GoogleSearchConfigRepository;
import com.blog.web.rest.util.HeaderUtil;
import com.sun.jndi.toolkit.url.Uri;
import io.github.jhipster.web.util.ResponseUtil;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.IAsyncResult;
import microsoft.exchange.webservices.data.notification.*;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GoogleSearchConfig.
 */
@RestController
@RequestMapping("/api")
public class GoogleSearchConfigResource {

    private final Logger log = LoggerFactory.getLogger(GoogleSearchConfigResource.class);

    private static final String ENTITY_NAME = "googleSearchConfig";

    private static ExchangeService service;

    private final GoogleSearchConfigRepository googleSearchConfigRepository;

    public GoogleSearchConfigResource(GoogleSearchConfigRepository googleSearchConfigRepository) {
        this.googleSearchConfigRepository = googleSearchConfigRepository;
    }
    @PostMapping("/google-search-configs")
    @Timed
    public ResponseEntity<GoogleSearchConfig> createGoogleSearchConfig(@RequestBody GoogleSearchConfig googleSearchConfig){
        try {
            service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
            service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
            ExchangeCredentials credentials = new WebCredentials(googleSearchConfig.getGoogleCx(), googleSearchConfig.getGoogleKey(), "outlook.office365.com");
            service.setCredentials(credentials);
            /*service.unsubscribe(subscriptionId);*/
            log.debug("START MAIL EXCHANGE");
            // Subscribe to pull notifications in the Inbox folder, and get notified when a new mail is received, when an item or folder is created, or when an item or folder is deleted.

            List folder = new ArrayList();
            folder.add(new FolderId().getFolderIdFromWellKnownFolderName(WellKnownFolderName.Inbox));

            PullSubscription subscription = service.subscribeToPullNotifications(folder, 1
                , null, EventType.NewMail);
            while (true){
                log.debug("START GET EVENTS "+subscription.getId());
                GetEventsResults events = subscription.getEvents();
                for (ItemEvent itemEvent : events.getItemEvents()) {
                    if (itemEvent.getEventType() == EventType.NewMail) {
                        EmailMessage message = EmailMessage.bind(service, itemEvent.getItemId());
                        break;
                    }
                }
                Thread.sleep(10000);
            }
        }catch (Exception e){
            log.debug("ERROR UNSUBSCRIBE");
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/google-search-configs")
    @Timed
    public ResponseEntity<GoogleSearchConfig> updateGoogleSearchConfig(@RequestBody GoogleSearchConfig googleSearchConfig) throws URISyntaxException {
        log.debug("REST request to update GoogleSearchConfig : {}", googleSearchConfig);
        if (googleSearchConfig.getId() == null) {
            return createGoogleSearchConfig(googleSearchConfig);
        }
        GoogleSearchConfig result = googleSearchConfigRepository.save(googleSearchConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, googleSearchConfig.getId().toString()))
            .body(result);
    }

    static {
       try {
           service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
           service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
           ExchangeCredentials credentials = new WebCredentials("15dh110100@st.huflit.edu.vn", "Josemourinho26011963", "outlook.office365.com");
           service.setCredentials(credentials);
       }catch (Exception e){
           System.out.println("ERROR EXCEPTION MAIL");
           e.printStackTrace();
       }
    }

    /**
     * GET  /google-search-configs : get all the googleSearchConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of googleSearchConfigs in body
     */
    @GetMapping("/google-search-configs")
    @Timed
    public void getMail(){
        /*try {
            log.debug("START MAIL EXCHANGE");
            // Subscribe to pull notifications in the Inbox folder, and get notified when a new mail is received, when an item or folder is created, or when an item or folder is deleted.

            List folder = new ArrayList();
            folder.add(new FolderId().getFolderIdFromWellKnownFolderName(WellKnownFolderName.Inbox));

            PullSubscription subscription = service.subscribeToPullNotifications(folder, 1
                , null, EventType.NewMail);
            while (true){
                log.debug("START GET EVENTS "+subscription.getId());
                GetEventsResults events = subscription.getEvents();
                for (ItemEvent itemEvent : events.getItemEvents()) {
                    if (itemEvent.getEventType() == EventType.NewMail) {
                        EmailMessage message = EmailMessage.bind(service, itemEvent.getItemId());
                        break;
                    }
                }
                Thread.sleep(10000);
            }*/

            /*WellKnownFolderName wkFolder = WellKnownFolderName.Inbox;
            FolderId folderId = new FolderId(wkFolder);
            List<FolderId> folder = new ArrayList<FolderId>();
            folder.add(folderId);

            URI callback = new URI("http://localhost:8087/rest/emailnotification/incomingevent");

            PushSubscription pushSubscription = service.subscribeToPushNotifications(
                folder,
                callback *//* The endpoint of the listener. *//*,
                1*//* Get a status event every 5 minutes if no new events are available. *//*, "AQAAAIK3OXLFGJ9Hl6nOsjL6vNxJAwwyAAAAAAA="  *//* watermark: null to start a new subscription. *//*,
                EventType.NewMail);
            System.out.println("PushSubscription = " + pushSubscription);*/
            /*service.unsubscribe();*/
            /*subscription.unsubscribe();*/
/*        } catch (Exception e) {
            log.debug("EXCEPTION MAIL EXCHANGE");
            e.printStackTrace();
        }*/

    }

    /**
     * GET  /google-search-configs/:id : get the "id" googleSearchConfig.
     *
     * @param id the id of the googleSearchConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the googleSearchConfig, or with status 404 (Not Found)
     */
    @GetMapping("/google-search-configs/{id}")
    @Timed
    public ResponseEntity<GoogleSearchConfig> getGoogleSearchConfig(@PathVariable Long id) {
        log.debug("REST request to get GoogleSearchConfig : {}", id);
        GoogleSearchConfig googleSearchConfig = googleSearchConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(googleSearchConfig));
    }

    /**
     * DELETE  /google-search-configs/:id : delete the "id" googleSearchConfig.
     *
     * @param id the id of the googleSearchConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/google-search-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteGoogleSearchConfig(@PathVariable Long id) {
        log.debug("REST request to delete GoogleSearchConfig : {}", id);
        googleSearchConfigRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
