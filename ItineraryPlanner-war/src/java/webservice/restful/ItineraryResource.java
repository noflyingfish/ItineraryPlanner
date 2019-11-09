package webservice.restful;

import entity.Comment;
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
import javax.ws.rs.DELETE;
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
    @Path("/{uId}")
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
            return Response.status(204)
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
    @Path("/{uId}/users/{uId_get}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItineraryByUser(
            @PathParam("uId") Long uId,
            @PathParam("uId_get") Long uId_get,
            @Context HttpHeaders headers) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Users u = usersSessionLocal.searchUserById(uId_get);
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
    
    //get all users in itinerary
    @GET
    //@Secured
    @Path("/{uId}/users/{iId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersInItinerary(
            @PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers) {
    //        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Itinerary i = itinerarySessionLocal.getItineraryById(iId);
            List<Users> uList = i.getUsersList();
            for(Users u : uList){
                u.setItineraryList(null);
            }
            
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
    
    //delete user in itinerary
    @DELETE
    //@Secured
    @Path("/{uId}/{iId}/{uId_del}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserInItinerary(
            @PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @PathParam("uId_del") Long uId_del,
            @Context HttpHeaders headers){
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Users> uList = itinerarySessionLocal.deleteUserFromItinerary(uId_del, iId);

            for(Users u : uList){
                u.setItineraryList(null);
            }
            
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

//    //get itinerary by location
//    @GET
//    @Path("iti_by_loc")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getItineraryByLocation() {
//        try {
//            List<Itinerary> iList = itinerarySessionLocal.searchItineraryByLocation();
//            // for all the itinerary
//            for (Itinerary i : iList) {
//                //break the itinerary ---> user -/-> itinerary
//                for (Users u : i.getUsersList()) {
//                    u.setItineraryList(null);
//                }
//                //break the itinerary ---> event -/-> itinerary    
//                for (Event e : i.getEventList()) {
//                    e.setItinerary(null);
//                }
//            }
//            GenericEntity<List<Itinerary>> entity = new GenericEntity<List<Itinerary>>(iList) {
//            };
//            return Response.status(200)
//                    .entity(entity)
//                    .build();
//        } catch (Exception e) {
//            JsonObject exception = Json.createObjectBuilder()
//                    .add("error", "Unable to create new itinerary")
//                    .build();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity(exception)
//                    .build();
//        }
//    }
    
    
    //add comments
    @POST
    @Path("/{uId}/{iId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers,
            Comment c) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Comment newC = itinerarySessionLocal.createComment(c, iId);
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

    @PUT
    @Path("/{uId}/{iId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComment(@PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers,
            Comment c) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Comment newC = itinerarySessionLocal.updateComment(c);
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

    @GET
    @Path("/{uId}/{iId}/comment")
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItineraryComment(@PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @Context HttpHeaders headers) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Comment> list = itinerarySessionLocal.retrieveAllComment(iId);
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
    
    @DELETE
    @Path("/{uId}/{iId}/comment/{cId>")
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeComment(@PathParam("uId") Long uId,
            @PathParam("iId") Long iId,
            @PathParam("cId") Long cId,
            @Context HttpHeaders headers) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Comment> list = itinerarySessionLocal.removeComment(uId, cId);
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
