package webservice.restful;

import entity.Comment;
import entity.Event;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.EventSessionLocal;
import session.ItinerarySessionLocal;
import util.AuthFilter;

@Path("events")
public class EventResource {

    @EJB
    private EventSessionLocal eventSessionLocal;
    @EJB
    private ItinerarySessionLocal itinerarySessionLocal;
    
    //Add Event
    @POST
    //@Secured
    @Path("/{uId}/{iId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvent(
            @PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers,
            Event e) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Event e1 = itinerarySessionLocal.addEvent(e, iId);
            return Response.status(200)
                    .entity(e1)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new event in itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //}
    }
    
    //Edit Event
    @PUT
    //@Secured
    @Path("/{uId}/{iId}")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateEvent(
	@PathParam("uId") Long uId, 
        @PathParam("iId") Long iId, 
            Event e) {

//      if (!isAuthorized(headers, uId)) {
//          return Response.status(Response.Status.UNAUTHORIZED).build();
//      } else {
            try {
                Event e1 = itinerarySessionLocal.updateEvent(e);
                return Response.status(200)
                        .entity(e1)
                        .build();
            } catch (Exception ex) {
                JsonObject exception = Json.createObjectBuilder()
                        .add("error", "Not found")
                        .build();
            return Response.status(404)
                    .entity(exception)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }
        //}
    } //end updateEvent
    
    //Remove event
    @DELETE
    //@Secured
    @Path("/{uId}/{iId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEvent(
            @PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers,
            Event e) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Event> list = itinerarySessionLocal.removeEvent(e.getId(), iId);
            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(list) {
            };
            
            return Response.status(200)
                    .entity(list)
                    .build();
        } catch (Exception ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new event in itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //}
    }
    
    //retrieve all event
    @GET
    //@Secured
    @Path("/{uId}/all/{iId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItineraryEvent(
            @PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Event> list = itinerarySessionLocal.retrieveAllEvent(iId);
            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(list) {
            };
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new event in itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //}
    }
    
    //Tested API (Dates format is wrong)
    //Search Event
    @GET
    @Path("/searchEvent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEvent(
            @QueryParam("name") String name,
            @QueryParam("createdDate") Date createdDate,
            @QueryParam("startDate") Date startDate,
            @QueryParam("endDate") Date endDate,
            @QueryParam("duration") String duration) {
        
        if (name != null) {
            List<Event> results = eventSessionLocal.searchEventByName(name);
            GenericEntity<List<Event>> entity = new 
            GenericEntity<List<Event>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        else if (createdDate != null) {
            List<Event> results = eventSessionLocal.searchEventByCreatedDate(createdDate);
            GenericEntity<List<Event>> entity = new 
            GenericEntity<List<Event>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        else if (startDate != null) {
            List<Event> results = eventSessionLocal.searchEventByStartDate(startDate);
            GenericEntity<List<Event>> entity = new 
            GenericEntity<List<Event>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        else if (endDate != null) {
            List<Event> results = eventSessionLocal.searchEventByEndDate(endDate);
            GenericEntity<List<Event>> entity = new 
            GenericEntity<List<Event>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        else if (duration != null) {
            List<Event> results = eventSessionLocal.searchEventByDuration(duration);
            GenericEntity<List<Event>> entity = new 
            GenericEntity<List<Event>>(results){};

            return Response.status(200)
                    .entity(entity)
                    .build();
        }

        else {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "No query condition")
                    .build();

            return Response.status(400)
                    .entity(exception)
                    .build();
        }
    }
    
    //Get Event with ID
    @GET
    @Path("/{uId}/{eId}")
    //@Secured
    @Produces (MediaType.APPLICATION_JSON)
    public Response getEvent(
	@PathParam("uId") Long uId,
        @PathParam("eId") Long eId,
        @Context HttpHeaders headers) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
	try {
            Event e = eventSessionLocal.getEvent(eId);
            return Response.status(200)
                    .entity(e)
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
   //}
}//end getEvent
    
    //Add comment
    @POST
    @Path("/{uId}/{eId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("uId") Long uId,
            @PathParam("eId") Long eId,
            @Context HttpHeaders headers,
            Comment c) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Comment newC = eventSessionLocal.createComment(c, eId);

            return Response.status(200)
                    .entity(newC)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to add new comment")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //} 
    }
    
    //Update comment 
    @PUT
    @Path("/{uId}/{eId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComment(@PathParam("uId") Long uId,
            @PathParam("eId") Long eId,
            @Context HttpHeaders headers,
            Comment c) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Comment newC = eventSessionLocal.updateComment(c);

            return Response.status(200)
                    .entity(newC)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to update comment")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //} 
    }
    
    //Remove comment
    @DELETE
    @Path("/{uId}/{eId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeComment(@PathParam("uId") Long uId,
            @PathParam("eId") Long eId,
            @Context HttpHeaders headers,
            Comment c) {
        System.out.println("vfdbggfsbgf");
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            System.out.println("asdsadsadsa");
            List<Comment> list = itinerarySessionLocal.removeComment(uId, c.getId());
            GenericEntity<List<Comment>> entity = new GenericEntity<List<Comment>>(list) {};
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to retreive comments")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //} 
    }
    
    //Retrieve all comment
    @GET
    @Path("/{uId}/{eId}/comment")
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventComment(@PathParam("uId") Long uId,
            @PathParam("eId") Long eId,
            @Context HttpHeaders headers) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Comment> list = eventSessionLocal.retrieveAllComment(eId);
            GenericEntity<List<Comment>> entity = new GenericEntity<List<Comment>>(list) {};
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to retreive comments")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //} 
    }
    
    //Add Photo
    
    //Remove Photo
    
    //Retrieve all photo
    
     private boolean isAuthorized(HttpHeaders headers, Long uId) {
        List<String> headerString = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("#headerString: " + headerString);
        System.out.println("#uId: " + uId);

        //since AuthenticationFilter allowed it to reach here, the size of the header should be at least 1
        Jws<Claims> accessToken = AuthFilter.getAccessTokenFromHeaderString(headerString.get(0));

        boolean authorized = false;

        String loggedInUserId = accessToken
                .getBody()
                .getSubject();
        if (loggedInUserId.equals("" + uId)) {
            authorized = true;
        }
        return authorized;
    } //end isAuthorized
}