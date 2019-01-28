package resource;

import java.io.*;
import java.util.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import lucene.InMemoryLuceneIndex;
import model.Message;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;
import org.eclipse.jetty.http.HttpStatus;


import com.codahale.metrics.annotation.Timed;
import model.User;
import org.hibernate.validator.constraints.NotEmpty;
import representation.MailRepresentation;
import representation.Representation;
import representation.SearchRepresentation;
import service.MessageBoxService;
import service.MessageService;
import service.UserService;



@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UserResource {

    private final UserService usersService;
    private final MessageBoxService msgBoxService;
    private final MessageService msgService;


    public UserResource(UserService usersService, MessageBoxService msgBoxService, MessageService msgService) {
        this.usersService = usersService;
        this.msgBoxService = msgBoxService;
        this.msgService = msgService;
    }

    @GET
    @Timed
    public Representation<List<User>> getUser() {
        return new Representation<List<User>>(HttpStatus.OK_200, usersService.getUsers());
    }

    @GET
    @Timed
    @Path("{id}")
    public Representation<User> getUser(@PathParam("id") final int id) {
        return new Representation<User>(HttpStatus.OK_200, usersService.getUser(id));
    }

    @POST
    @Timed
    public Representation<User> createUser(@NotNull @Valid final User User) {
        return new Representation<User>(HttpStatus.OK_200, usersService.createUser(User));
    }


    @DELETE
    @Timed
    @Path("{id}")
    public Representation<String> deleteUser(@PathParam("id") final int id) {
        return new Representation<String>(HttpStatus.OK_200, usersService.deleteUser(id));
    }

    @GET
    @Timed
    @Path("{id}/mails")
    public Representation<List<MailRepresentation>> fetchMail(@PathParam("id") final int id) throws IOException {
        User user = usersService.getUser(id);
        List<MailRepresentation> result = new ArrayList<>();
        List<Integer> messageIds = msgBoxService.getMessageIdFromRecevierId(id);
        for(int msgId : messageIds){
            Message msg = msgService.getMessage(msgId);
            result.add(new MailRepresentation(user.getUserName(),usersService.getEmailFromId(msg.getCreatorId()),msg.getSubject(),msg.getBody()));
        }
        return new Representation<>(HttpStatus.OK_200, result);

        /*String path = "/home/mrinal/Documents/Dev/drop-wizard/dw/userdata/"+user.getId()+".txt";
        File fp = new File(path);
        if(!fp.exists())
            fp.createNewFile();
        Scanner file = new Scanner(fp);
        HashSet<String> mails = new HashSet<>();
        while (file.hasNextLine()) {
            mails.add(file.next());
        }
        for(MailRepresentation mail : result)
            mails.add(mail.toString());
        BufferedWriter out = new BufferedWriter(new FileWriter(path,false));
        for(String mail : mails) {
            out.write(mail);
            out.newLine();
        }
        out.close();
        */
    }


    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/search")
    public Representation<List<String>> searchFromMail(@PathParam("id") final int id , SearchRepresentation data) throws IOException {
        User user = usersService.getUser(id);
        List<MailRepresentation> result = new ArrayList<>();
        List<Integer> messageIds = msgBoxService.getMessageIdFromRecevierId(id);
        for (int msgId : messageIds) {
            Message msg = msgService.getMessage(msgId);
            result.add(new MailRepresentation(user.getUserName(), usersService.getEmailFromId(msg.getCreatorId()), msg.getSubject(), msg.getBody()));
        }

        InMemoryLuceneIndex inMemoryLuceneIndex = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());

        for (MailRepresentation mail : result)
            inMemoryLuceneIndex.indexDocument(mail.getTo(), mail.getFrom(), mail.getSubject(), mail.getBody());

        List<Document> searchResult = inMemoryLuceneIndex.searchIndex(data.getInField(),data.getQuery());
        List<String> jsonResult = new ArrayList<>();
        for(Document d : searchResult){
            jsonResult.add(d.toString());
        }
        return new Representation<>(HttpStatus.OK_200, jsonResult);

    }

}