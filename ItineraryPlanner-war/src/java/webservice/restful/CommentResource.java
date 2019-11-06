package webservice.restful;

import entity.Comment;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.CommentSessionLocal;

@Path("comments")
public class CommentResource {

    @EJB
    private CommentSessionLocal commentSessionLocal;

    //Tested API (Comment with userID will cause internal error 
    //Get all Comment
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAllComment() {
        return commentSessionLocal.retrieveAllComment();
    } //end retrieveAllComments

    //Tested API
    //Search Event
    @GET
    @Path("/searchComment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchComment(
            @QueryParam("comment") String comment,
            @QueryParam("createdDate") Date createdDate)
            /* @QueryParam("writer") Users writer */
            /* @QueryParam("itinerary") Itinerary itinerary */ {
        
        if (comment != null) {
            List<Comment> results = commentSessionLocal.searchCommentByComment(comment);
            GenericEntity<List<Comment>> entity = new 
            GenericEntity<List<Comment>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        else if (createdDate != null) {
            List<Comment> results = commentSessionLocal.searchCommentByCreatedDate(createdDate);
            GenericEntity<List<Comment>> entity = new 
            GenericEntity<List<Comment>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        /*else if (writer != null) {
            List<Comment> results = commentSessionLocal.searchCommentByWriter(writer.getId());
            GenericEntity<List<Comment>> entity = new 
            GenericEntity<List<Comment>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        } */

        else {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "No query condition")
                    .build();

            return Response.status(400).entity(exception)
                    .build();
        }
    }
    
    //Tested API
    //Get Comment with ID
    @GET
    @Path("/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getComment(
	@PathParam("id") Long cId) {
	
	try {
            Comment c = commentSessionLocal.getComment(cId);
            return Response.status(200)
                    .entity(c)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
	} catch (NoResultException n) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();

            return Response.status(404)
                    .entity(exception)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
	}
    } //end getComment
    
    //Tested API
    //Create Comment
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Comment createComment(Comment c) {
        //Need add currentUser ID
	c.setCreatedDate (new Date());
	commentSessionLocal.createComment(c);
	return c;
    } //end createEvent
    
    //Tested API
    //Edit Comment
    @PUT
    @Path("/{id}")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateComment(
	@PathParam("id") Long cId, Comment c) {
	c.setId(cId);

	try {
            commentSessionLocal.updateComment(c);
            return Response.status(204).build();
	} catch (NoResultException n) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
	
        return Response.status(404)
                .entity(exception)
                .type(MediaType.APPLICATION_JSON)
                .build();
        }
    } //end updateEvent
    
    //Tested API
    //Delete Comment
    @DELETE
    @Path("/{id}")
    @Produces()
    public Response deleteComment(
	@PathParam("id") Long cId) {
	try {
            commentSessionLocal.removeComment(cId);
            return Response.status(204)
                    .build();
	} catch (NoResultException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Comment ID not found")
                    .build();

        return Response.status(404)
                .entity(exception)
                .build();
        }
    } //end deleteEvent    
}