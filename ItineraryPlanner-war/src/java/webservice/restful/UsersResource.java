package webservice.restful;

import entity.Comment;
import entity.Event;
import entity.Itinerary;
import entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
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

@Path("users")
public class UsersResource {

    @EJB
    private UsersSessionLocal usersSessionLocal;
    @EJB
    private ItinerarySessionLocal itinerarySessionLocal;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Users register(Users u) {
        System.out.println("username: " + u.getUserName());
        System.out.println("password: " + u.getPassword());
        System.out.println(u.getFirstName());
        System.out.println("userSessionLocal: " + usersSessionLocal);
        u.setCreatedDate(new Date());
        usersSessionLocal.createUser(u);

        //remove password value before returning
        u.setPassword(null);
        return u;
    } //end register

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Users u) {
        String username = u.getUserName();
        String password = u.getPassword();
        Users curr = usersSessionLocal.userLogin(username, password);

        if (curr != null) {
            try {
                //use the subject to store the member id
                String accessToken = Jwts.builder().setSubject("" + curr.getId()).signWith(AuthFilter.SECRET_KEY).compact();
                JsonObject obj = Json.createObjectBuilder()
                        .add("uId", curr.getId())
                        .add("accessToken", accessToken)
                        .build();
                return Response.status(200)
                        .entity(obj)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                //Invalid Signing configuration / Couldn't convert Claims.
                JsonObject exception = Json.createObjectBuilder()
                        .add("error", "Unable to obtain access token")
                        .build();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                        exception
                ).build();
            }
        }
        JsonObject exception = Json.createObjectBuilder()
                .add("error", "username or password is wrong")
                .build();

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(exception)
                .build();
    } //end login

    @PUT
    @Path("/{uId}")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editProfile(@PathParam("uId") Long uId,
            @Context HttpHeaders headers,
            Users u) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Users newU = usersSessionLocal.updateUser(u);

            for (Itinerary i : newU.getItineraryList()) {
                i.setUsersList(null);
                for (Event e : i.getEventList()) {
                    e.setItinerary(null);
                }
            }
            return Response.status(200)
                    .entity(newU)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to edit profile")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
        //} 
    }

    @POST
    @Path("/{uId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("uId") Long uId,
            @Context HttpHeaders headers,
            Comment c) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Comment newC = usersSessionLocal.createComment(c, uId);

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
    @Path("/{uId}/comment")
    //@Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComment(@PathParam("uId") Long uId,
            @Context HttpHeaders headers,
            Comment c) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Comment newC = usersSessionLocal.updateComment(c);

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
    @Path("/{uId}/comment")
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserComment(@PathParam("uId") Long uId,
            @Context HttpHeaders headers) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Comment> list = usersSessionLocal.retrieveAllComment(uId);
            GenericEntity<List<Comment>> entity = new GenericEntity<List<Comment>>(list) {
            };
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
    @Path("/{uId}/comment/{cId}")
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeComment(@PathParam("uId") Long uId,
            @PathParam("cId") Long cId,
            @Context HttpHeaders headers) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Comment> list = usersSessionLocal.removeComment(uId, cId);
            GenericEntity<List<Comment>> entity = new GenericEntity<List<Comment>>(list) {
            };
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

    //add user to itinerary
    @POST
    //@Secured
    @Path("/{uId}/itinerary/{new_uId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToItinerary(
            @PathParam("uId") Long uId,
            @PathParam("new_uId") Long new_uId,
            @Context HttpHeaders headers,
            Itinerary i) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Users> uList = itinerarySessionLocal.addUser(new_uId, i.getId());
            for (Users u : uList) {
                for (Itinerary i1 : u.getItineraryList()) {
                    i.setUsersList(null);
                    for (Event e : i1.getEventList()) {
                        e.setItinerary(null);
                    }
                }
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

    @GET
    @Path("/{uId}/itinerary")
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserItinerary(@PathParam("uId") Long uId,
            @Context HttpHeaders headers) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Itinerary> list = usersSessionLocal.getAllItineray(uId);

            for (Itinerary i1 : list) {
                i1.setUsersList(null);
                for (Event e : i1.getEventList()) {
                    e.setItinerary(null);
                }
            }
            GenericEntity<List<Itinerary>> entity = new GenericEntity<List<Itinerary>>(list) {
            };
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
    //@Secured
    @Path("/{uId}/itinerary/{iId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItinerary(
            @PathParam("uId") Long uId,
            @Context HttpHeaders headers,
            Itinerary i) {

//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Itinerary> iList = usersSessionLocal.deleteItinerary(uId, i.getId());
            // for all the itinerary
            for (Itinerary i1 : iList) {
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
        //}
    }

    //search user by username
    @GET
    //@Secured
    @Path("/{search}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(
            @PathParam("search") String search,
            @Context HttpHeaders headers) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            List<Users> list = usersSessionLocal.searchUserByUsername(search);
            System.out.println(search);
            for (Users u : list) {
                for (Itinerary i1 : u.getItineraryList()) {
                    i1.setUsersList(null);
                    i1.setEventList(null);
                }
            }
            GenericEntity<List<Users>> entity = new GenericEntity<List<Users>>(list) {
            };
            return Response.status(200)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to search for user")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
//    }    
    }

    //get user by id
    @GET
    //@Secured
    @Path("/{uid}/{uId_get}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(
            @PathParam("uId") Long uId,
            @PathParam("uId_get") Long uId_get,
            @Context HttpHeaders headers) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            Users u = usersSessionLocal.searchUserById(uId);

            for (Itinerary i : u.getItineraryList()) {
                i.setUsersList(null);
                for (Event e : i.getEventList()) {
                    e.setItinerary(null);
                }
            }

            return Response.status(200)
                    .entity(u)
                    .build();
        } catch (Exception e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unable to search for user")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception)
                    .build();
        }
//    }    
    }
    
    @GET
    @Path("/getallusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<Users> list = usersSessionLocal.retrieveAllUser();
        for (Users u : list) {
            for (Itinerary i : u.getItineraryList()) {
                i.setUsersList(null);
                for (Event e : i.getEventList()) {
                    e.setItinerary(null);
                }
            }
        }
        GenericEntity<List<Users>> entity = new GenericEntity<List<Users>>(list) {
        };
        return Response.status(200)
                .entity(entity)
                .build();
    }

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
