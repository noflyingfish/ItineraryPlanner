package session;

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

    public void updateUser(Users u);

    public void deleteUser(Long uId);

    public List<Users> searchUser(String userName);

    public Users getUser(Long uId);

    public void addItinerary(Long uId, Itinerary i);

    public void deleteItinerary(Long uId, Itinerary i);

    public List<Users> retrieveAllUser();
            
    public List<Itinerary> getAllItineray(Long uId, Itinerary i);

    public Users userLogin(String userName, String password);

}
