package util;

 
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
import javax.ws.rs.NameBinding;
 
@NameBinding
@Retention(RUNTIME)
public @interface Secured { }