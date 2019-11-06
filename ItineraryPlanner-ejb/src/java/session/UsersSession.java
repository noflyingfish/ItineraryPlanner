package session;

import entity.Itinerary;
import entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Cheyenne Koh
 */
@Stateless
public class UsersSession implements UsersSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createUser(Users u) {
        em.persist(u);
    } //end createUser

    @Override
    public void updateUser(Users u) {
        Users oldU = em.find(Users.class, u.getId());

        //do not need to persist oldU beuause it is still managed by persistence context
        oldU.setFirstName(u.getFirstName());
        oldU.setLastName(u.getLastName());
        oldU.setUserName(u.getUserName());
        oldU.setPassword(u.getPassword());
        oldU.setEmail(u.getEmail());
    }

    @Override
    public void deleteUser(Long uId) {
        Users u = em.find(Users.class, uId);

        em.remove(u);

    }

    @Override
    public List<Users> searchUser(String userName) {
        Query q;
        if (userName != null) {
            q = em.createQuery("SELECT u FROM Users u WHERE " + "LOWER(u.name) LIKE :name");
            q.setParameter("name", "%" + userName.toLowerCase() + "%");
        } else {
            //if no name, display list of Users instead
            q = em.createQuery("SELECT u FROM User u");
        }

        return q.getResultList();
    }

    @Override
    public Users getUser(Long uId) {
        Users u = em.find(Users.class, uId);

        return u;

    }

    @Override
    public void addItinerary(Long uId, Itinerary i) {
        Users user = em.find(Users.class, uId);
        user.getItineraryList().add(i);
    }

    @Override
    public void deleteItinerary(Long uId, Itinerary i) {
        Itinerary selectedI = em.find(Itinerary.class, i.getId());
        Users selectedU = em.find(Users.class, uId);

        selectedU.getItineraryList().remove(selectedI);
        // itinerary not owned by anyone, delete from db
        if (selectedI.getUsersList().isEmpty()) {
            em.remove(selectedI);
        }
    }

    @Override
    public List<Users> retrieveAllUser() {
        Query q = em.createQuery("SELECT u FROM Users u");
        return q.getResultList();
    }
    
    @Override
    public List<Itinerary> getAllItineray(Long uId, Itinerary i) {
        Users user = em.find(Users.class, uId);
        return user.getItineraryList();
    }

    @Override
    public Users userLogin(String userName, String password) {

        Query q;
        q = em.createQuery("SELECT u FROM Users u WHERE u.userName LIKE :name AND u.password LIKE :password");
        q.setParameter("name", userName);
        q.setParameter("password", password);

        Users u = (Users) q.getSingleResult();

        return u;
    }

}
