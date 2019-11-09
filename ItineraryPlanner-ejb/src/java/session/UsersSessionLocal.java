package session;

import entity.Comment;
import entity.Itinerary;
import entity.Users;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Cheyenne Koh
 */
@Local
public interface UsersSessionLocal {

    public void createUser(Users u);

    public Users updateUser(Users u);

    public List<Users> searchUserByUsername(String userName);
    
    public Users searchUserById (Long uId);

    public List<Users> retrieveAllUser();

    public List<Itinerary> deleteItinerary(Long uId, Long iId);

    public List<Itinerary> getAllItineray(Long uId);

    public Users userLogin(String userName, String password);

    // comments 

    public Comment createComment(Comment c, Long uId);

    public Comment updateComment(Comment c);

    public List<Comment> removeComment(Long uId, Long cId);

    public List<Comment> retrieveAllComment(Long uId);

}
