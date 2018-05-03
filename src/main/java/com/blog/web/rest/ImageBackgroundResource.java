package com.blog.web.rest;

import com.blog.domain.MailExchange;
import com.blog.service.Demo;
import com.codahale.metrics.annotation.Timed;
import com.blog.domain.ImageBackground;

import com.blog.repository.ImageBackgroundRepository;
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
import microsoft.exchange.webservices.data.credential.CredentialConstants;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.AsyncCallbackImplementation;
import microsoft.exchange.webservices.data.misc.IAsyncResult;
import microsoft.exchange.webservices.data.notification.*;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ImageBackground.
 */
@RestController
@RequestMapping("/api")
public class ImageBackgroundResource {

    private final Logger log = LoggerFactory.getLogger(ImageBackgroundResource.class);

    private static final String ENTITY_NAME = "imageBackground";

    private final ImageBackgroundRepository imageBackgroundRepository;



    private static  MailExchange mailExchange;


/*    public PushSubscription pullSubscription(){

    }*/

    @PostMapping(value = "/mail", consumes = MediaType.TEXT_XML_VALUE)
    @Timed
    public void mailExchange(HttpServletRequest request, HttpServletResponse response){
        log.debug("---------- MAIL_EXCHANGE ----------");

    }
    public ImageBackgroundResource(ImageBackgroundRepository imageBackgroundRepository) {
        this.imageBackgroundRepository = imageBackgroundRepository;
    }


    /**
     * POST  /image-backgrounds : Create a new imageBackground.
     *
     * @param imageBackground the imageBackground to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imageBackground, or with status 400 (Bad Request) if the imageBackground has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/image-backgrounds")
    @Timed
    public ResponseEntity<ImageBackground> createImageBackground(@Valid @RequestBody ImageBackground imageBackground) throws URISyntaxException {
        log.debug("REST request to save ImageBackground : {}", imageBackground);
        if (imageBackground.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new imageBackground cannot already have an ID")).body(null);
        }
        ImageBackground result = imageBackgroundRepository.save(imageBackground);
        return ResponseEntity.created(new URI("/api/image-backgrounds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /image-backgrounds : Updates an existing imageBackground.
     *
     * @param imageBackground the imageBackground to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imageBackground,
     * or with status 400 (Bad Request) if the imageBackground is not valid,
     * or with status 500 (Internal Server Error) if the imageBackground couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/image-backgrounds")
    @Timed
    public ResponseEntity<ImageBackground> updateImageBackground(@Valid @RequestBody ImageBackground imageBackground) throws URISyntaxException {
        log.debug("REST request to update ImageBackground : {}", imageBackground);
        if (imageBackground.getId() == null) {
            return createImageBackground(imageBackground);
        }
        ImageBackground result = imageBackgroundRepository.save(imageBackground);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, imageBackground.getId().toString()))
            .body(result);
    }

    /**
     * GET  /image-backgrounds : get all the imageBackgrounds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of imageBackgrounds in body
     */

    /*public List<ImageBackground> getAllImageBackgrounds() {

        log.debug("REST request to get all ImageBackgrounds");
        return imageBackgroundRepository.findAll();
    }*/
/*    @GetMapping("/image-backgrounds")
    @Timed*/
    /*public void getMail(){
        try {
            log.debug("START MAIL EXCHANGE");
            // Subscribe to pull notifications in the Inbox folder, and get notified when a new mail is received, when an item or folder is created, or when an item or folder is deleted.
            ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
            service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
            ExchangeCredentials credentials = new WebCredentials("15dh110100@st.huflit.edu.vn", "Josemourinho26011963", "outlook.office365.com");
            service.setCredentials(credentials);

            List folder = new ArrayList();
            folder.add(new FolderId().getFolderIdFromWellKnownFolderName(WellKnownFolderName.Inbox));

            PullSubscription subscription = service.subscribeToPullNotifications(folder, 1
*//* timeOut: the subscription will end if the server is not polled within 5 minutes. *//*, null *//* watermark: null to start a new subscription. *//*, EventType.NewMail, EventType.Created, EventType.Deleted);

// Wait a couple minutes, then poll the server for new events.
            GetEventsResults events = subscription.getEvents();

// Loop through all item-related events.
            for (ItemEvent itemEvent : events.getItemEvents()) {
                if (itemEvent.getEventType() == EventType.NewMail) {
                    EmailMessage message = EmailMessage.bind(service, itemEvent.getItemId());
                } else if (itemEvent.getEventType() == EventType.Created) {
                    Item item = Item.bind(service, itemEvent.getItemId());
                } else if (itemEvent.getEventType() == EventType.Deleted) {
                    break;
                }
            }

// Loop through all folder-related events.
            for (FolderEvent folderEvent : events.getFolderEvents()) {
                if (folderEvent.getEventType() == EventType.Created) {
                    Folder folder1 = Folder.bind(service, folderEvent.getFolderId());
                } else if (folderEvent.getEventType() == EventType.Deleted) {
                    System.out.println("folder  deleted"+ folderEvent.getFolderId().getUniqueId());
                }
            }
            log.debug("END MAIL EXCHANGE");
        } catch (Exception e) {
            log.debug("EXCEPTION MAIL EXCHANGE");
            e.printStackTrace();
        }
    }*/

    /**
     * GET  /image-backgrounds/:id : get the "id" imageBackground.
     *
     * @param id the id of the imageBackground to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imageBackground, or with status 404 (Not Found)
     */
    @GetMapping("/image-backgrounds/{id}")
    @Timed
    public ResponseEntity<ImageBackground> getImageBackground(@PathVariable Long id) {
        log.debug("REST request to get ImageBackground : {}", id);
        ImageBackground imageBackground = imageBackgroundRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(imageBackground));
    }

    /**
     * DELETE  /image-backgrounds/:id : delete the "id" imageBackground.
     *
     * @param id the id of the imageBackground to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/image-backgrounds/{id}")
    @Timed
    public ResponseEntity<Void> deleteImageBackground(@PathVariable Long id) {
        log.debug("REST request to delete ImageBackground : {}", id);
        imageBackgroundRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
