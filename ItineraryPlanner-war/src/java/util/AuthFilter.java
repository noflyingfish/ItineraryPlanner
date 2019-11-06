package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author lekhsian
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    public static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //method that takes in a authorization header string and return a JWT object
    //return null if not valid
    public static Jws<Claims> getAccessTokenFromHeaderString(String authorizationHeader) {
        String accessToken = authorizationHeader.substring("Bearer".length()).trim();
        System.out.println("###accesstoken : " + accessToken);

        Jws<Claims> jws;

        try {
            jws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken);

            return jws;
        } catch (JwtException ex) {
            return null;
        }
    } //end getAccessTokenFromHeaderString

    @Override
    public void filter(ContainerRequestContext requestContext) {

        // Extract Authorization header details
        String authorizationHeader
                = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            abort(requestContext);
            return;
        }

        // Extract the token
        Jws<Claims> jws = getAccessTokenFromHeaderString(authorizationHeader);

        if (jws == null) {
            abort(requestContext);
            return;
        }
    }

    private void abort(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}

