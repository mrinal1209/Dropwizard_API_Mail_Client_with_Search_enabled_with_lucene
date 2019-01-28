package resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.http.HttpStatus;

import com.codahale.metrics.annotation.Timed;
import model.Message;
import  model.MessageBox;
import representation.ComposeRepresentation;
import representation.Representation;
import service.MessageBoxService;
import service.MessageService;
import service.UserService;

@Path("/compose")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class ComposeResource {
    private final MessageService msgService;
    private final MessageBoxService msgBoxService;
    private final UserService userService;

    public ComposeResource(MessageService msgService , MessageBoxService msgBoxService ,UserService userService) {
        this.msgService = msgService;
        this.msgBoxService = msgBoxService;
        this.userService = userService;
    }


    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Representation<String> createMessage(ComposeRepresentation cr) {
        Message msg = new Message((int)userService.getIdFromEmail(cr.getCreatorId()),cr.getSubject(),cr.getBody(),null);
        msgService.createMessage(msg);
        int msgId = msgService.lastInsertId();
        //System.out.println("TESTING FOR ID" + msgId);
        for(String email : cr.getRecevierId()) {
            msgBoxService.createMessageBox(new MessageBox((int) userService.getIdFromEmail(email), msgId));
            //System.out.println("TESTING FOR MESSAGE BOX");
        }

        return new Representation<>(HttpStatus.OK_200, "Email successfully sent");
    }
    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reply")
    public Representation<String> createMessageWithParentId(ComposeRepresentation cr) {
        Message oldMsg = msgService.getMessage(cr.getMessageId());
        Message msg = new Message((int)userService.getIdFromEmail(cr.getCreatorId()),oldMsg.getSubject(),cr.getBody(),cr.getMessageId());
        msgService.createMessageWithParentId(msg);
        int msgId = msgService.lastInsertId();
        msgBoxService.createMessageBox(new MessageBox(oldMsg.getCreatorId(), msgId));
        return new Representation<>(HttpStatus.OK_200, "Reply to an Email successfully sent");
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/forward")
    public Representation<String> forwardMessage(ComposeRepresentation cr) {
        Message oldMsg = msgService.getMessage(cr.getMessageId());
        Message newMsg = new Message((int)userService.getIdFromEmail(cr.getCreatorId()),oldMsg.getSubject(),oldMsg.getBody(),null);
        msgService.createMessage(newMsg);
        int newMsgId = msgService.lastInsertId();
        for(String email : cr.getRecevierId()) {
            msgBoxService.createMessageBox(new MessageBox((int) userService.getIdFromEmail(email), newMsgId));
        }

        return new Representation<>(HttpStatus.OK_200, "Email successfully forwarded");
    }



}