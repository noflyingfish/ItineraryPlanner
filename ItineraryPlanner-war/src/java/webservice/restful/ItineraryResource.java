package webservice.restful;

import entity.Event;
import entity.Itinerary;
import entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.ItinerarySessionLocal;
import session.UsersSessionLocal;
import util.AuthFilter;
import util.Secured;

@Path("itinerary")
public class ItineraryResource {

    @EJB
    ItinerarySessionLocal itinerarySessionLocal;
    @EJB
    UsersSessionLocal usersSessionLocal;

    //create itinerary
    @POST
    //@Secured
    @Path("/{uId}/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItinerary(
            @PathParam("uId") Long uId,
            @Context HttpHeaders headers,
            Itinerary i) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            itinerarySessionLocal.createItinerary(i, uId);
            return Response.status(200)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //}
    }

    //add user to itinerary
    @PUT
    //@Secured
    @Path("/{uId}/iti_add_user/{new_uId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToIti(
            @PathParam("uId") Long uId,
            @PathParam("new_uId") Long new_uId,
            @Context HttpHeaders headers,
            Long iId) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Users> uList = itinerarySessionLocal.addUser(new_uId, iId);
            GenericEntity<List<Users>> entity = new GenericEntity<List<Users>>(uList) {
            };
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //}
    }

    //get all itinerary
    @GET
    @Path("/getallitinerary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItinerary() {

        try {
            List<Itinerary> iList = itinerarySessionLocal.retrieveAllItinerary();
            // for all the itinerary
            for (Itinerary i : iList) {
                //break the itinerary ---> user -/-> itinerary
                for (Users u : i.getUsersList()) {
                    u.setItineraryList(null);
                }

                //break the itinerary ---> event -/-> itinerary    
                for (Event e : i.getEventList()) {
                    e.setItinerary(null);
                }
            }
            GenericEntity<List<Itinerary>> entity = new GenericEntity<List<Itinerary>>(iList) {
            };
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
    }

    //get itinerary by user
    @GET
    //@Secured
    @Path("/{uId}/iti_by_user/{uId_get}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItineraryByUser(
            @PathParam("uId") Long uId,
            @PathParam("uId_get") Long uId_get) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Users u = usersSessionLocal.getUser(uId_get);
            List<Itinerary> iList = itinerarySessionLocal.searchItineraryByUser(u.getUserName());
            // for all the itinerary
            for (Itinerary i : iList) {
                //break the itinerary ---> user -/-> itinerary
                for (Users u1 : i.getUsersList()) {
                    u1.setItineraryList(null);
                }
                //break the itinerary ---> event -/-> itinerary    
                for (Event e : i.getEventList()) {
                    e.setItinerary(null);
                }
            }
            GenericEntity<List<Itinerary>> entity = new GenericEntity<List<Itinerary>>(iList) {
            };
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //}
    }

    //get itinerary by location
    @GET
    @Path("iti_by_loc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItineraryByLocation(
            String place) {
        try {
            List<Itinerary> iList = itinerarySessionLocal.searchItineraryByLocation(place);
            // for all the itinerary
            for (Itinerary i : iList) {
                //break the itinerary ---> user -/-> itinerary
                for (Users u : i.getUsersList()) {
                    u.setItineraryList(null);
                }
                //break the itinerary ---> event -/-> itinerary    
                for (Event e : i.getEventList()) {
                    e.setItinerary(null);
                }
            }
            GenericEntity<List<Itinerary>> entity = new GenericEntity<List<Itinerary>>(iList) {
            };
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to create new itinerary")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
    }

    //if no user, delete everythig, event in it also.
    //update itinerary
    //method that returns whether the logged in user
    //have access to user with mId
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
