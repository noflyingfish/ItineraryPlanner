package webservice.restful;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.EventSessionLocal;
import util.AuthFilter;

@Path("events")
public class EventResource {

    @EJB
    private EventSessionLocal eventSessionLocal;

    //Tested API
    //Get all Events
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Event> getAllEvents() {
//        return eventSessionLocal.retrieveAllEvent();
//    } //end retrieveAllEvents

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
    
    //Tested API
    //Get Event with ID
    @GET
    @Path("/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getEvent(
	@PathParam("id") Long eId) {
	
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
} //end getEvent
    
//    //Create Event
//    @POST
//    @Consumes (MediaType.APPLICATION_JSON)
//    @Produces (MediaType.APPLICATION_JSON)
//    public Event createEvent(Event e) {
//	e.setCreatedDate (new Date());
//	eventSessionLocal.createEvent(e);
//	return e;
//    } //end createEvent
    
    //Tested API
    //Edit Event
    @PUT
    @Path("/{id}")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateEvent(
	@PathParam("id") Long eId, Event e) {
	e.setId(eId);

	try {
            eventSessionLocal.updateEvent(e);
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
    //Delete Event
//    @DELETE
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteEvent(
//	@PathParam("id") Long eId) {
//	try {
//            eventSessionLocal.removeEvent(eId);
//            return Response.status(204)
//                    .build();
//	} catch (NoResultException e) {
//            JsonObject exception = Json.createObjectBuilder()
//                    .add("error", "Event ID not found")
//                    .build();
//
//        return Response.status(404)
//                .entity(exception)
//                .build();
//        }
//    } //end deleteEvent    
    
    
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