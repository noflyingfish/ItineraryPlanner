package webservice.restful;

import entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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
import session.UsersSessionLocal;
import util.AuthFilter;
import util.Secured;

@Path("users")
public class UsersResource {

    @EJB
    private UsersSessionLocal usersSessionLocal;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<Users> list = usersSessionLocal.retrieveAllUser();
        System.out.println(list.size());
            
        GenericEntity<List<Users>> entity = new GenericEntity<List<Users>>(list) {
        };                
        
        return Response.status(200)
                .entity(entity)
                .build();
    }
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Users register(Users u) {
        System.out.println("firstName: " + u.getFirstName());
        System.out.println("lastName: " + u.getLastName());
        System.out.println("username: " + u.getUserName());
        System.out.println("password: " + u.getPassword());
        System.out.println("email: " + u.getEmail());
        System.out.println("description: " + u.getDescription());
        System.out.println("birthday: " + u.getBirthday());
        System.out.println("instaURL: " + u.getInstaURL());
        System.out.println("blogURL: " + u.getBlogURL());
        System.out.println("createdDate: " + u.getCreatedDate());
        System.out.println("userSessionLocal: " + usersSessionLocal);
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
    @Path("/{uId}/editprofile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editProfile(@PathParam("uId") Long uId,
            @Context HttpHeaders headers,
            Users u) {
//        if (!isAuthorized(headers, uId)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        } else {
        try {
            System.out.println("TESTETSTETSETSTE");
            Users newU = usersSessionLocal.updateUser(u);
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
    @Path("/{uId}/addcomment")
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
    @Path("/{uId}/updatecomment")
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
    @Secured
    @Path("/{uId}/secured")
    public Response getPosts(
            @PathParam("uId") Long uId,
            @Context HttpHeaders headers) {

        if (!isAuthorized(headers, uId)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            Users u = usersSessionLocal.getUser(uId);
//            List<Post> results = m.getPosts();
//
//            GenericEntity<List<Post>> entity = new GenericEntity<List<Post>>(results) {
//            };

            return Response.status(200)
                    .entity(u)
                    .build();
        }
    } //end getPosts

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