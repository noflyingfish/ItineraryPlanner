package session;

import entity.Comment;
import entity.Event;
import entity.Itinerary;
import entity.Photo;
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
    public Users updateUser(Users u) {
        Users oldU = em.find(Users.class, u.getId());

        //do not need to persist oldU beuause it is still managed by persistence context
        oldU.setFirstName(u.getFirstName());
        oldU.setLastName(u.getLastName());
        oldU.setUserName(u.getUserName());
        oldU.setPassword(u.getPassword());
        oldU.setProfilepic(u.getProfilepic());
        oldU.setEmail(u.getEmail());
        oldU.setDescription(u.getDescription());
        oldU.setBirthday(u.getBirthday());
        oldU.setBlogURL(u.getBlogURL());
        oldU.setInstaURL(u.getInstaURL());
        
        return oldU;
    }

    @Override
    public List<Users> searchUserByUsername(String userName) {
        Query q;
        if (!"".equals(userName)) {
            q = em.createQuery("SELECT u FROM Users u WHERE u.userName LIKE :name");
            q.setParameter("name", "%" + userName + "%");
        } else {
            //if no name, display list of Users instead
            q = em.createQuery("SELECT u FROM Users u");
        }
        return q.getResultList();
    }

    @Override
    public Users searchUserById(Long uId) {
        Users u = em.find(Users.class, uId);
        System.out.println(u.getUserName());
        return u;
    }
    
    @Override
    public List<Users> retrieveAllUser() {
        Query q = em.createQuery("SELECT u FROM Users u");
        return q.getResultList();
    }

    @Override
    public List<Itinerary> deleteItinerary(Long uId, Long iId) {
        Itinerary i = em.find(Itinerary.class, iId);
        Users u = em.find(Users.class, uId);

        u.getItineraryList().remove(i);
        // itinerary not owned by anyone, delete from db
        if (i.getUsersList().isEmpty()) {
            
            //delete comments
            List<Comment> cList = i.getCommentList();
            for(Comment c : cList){
                cList.remove(c);
                em.remove(c);
            }
            
            //delete comment
            List<Photo> pList = i.getPhotoList();
            for(Photo p : pList){
                pList.remove(p);
                em.remove(p);
            }
            
            //delete photo
            List<Event> eList= i.getEventList();
            for(Event e : eList){
                eList.remove(e);
                em.remove(e);
            }
            
            em.remove(i);
        }
        return u.getItineraryList();
    }

    @Override
    public List<Itinerary> getAllItineray(Long uId) {
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
    
    @Override
    public Comment createComment(Comment c, Long uId){
        em.persist(c);
        Users u = em.find(Users.class, uId);
        List<Comment> list = u.getCommentList();
        list.add(c);
        u.setCommentList(list);
        return c;
    }
    
    @Override
    public Comment updateComment(Comment c){
        Comment oldC = em.find(Comment.class, c.getId());
        oldC.setComment(c.getComment());
        return oldC;
    }
    
    @Override
    public List<Comment> removeComment(Long uId, Long cId){
        Comment c = em.find(Comment.class, cId);
        Users u = em.find(Users.class, uId);
        List<Comment> list = u.getCommentList();
        list.remove(c);
        em.remove(c);
        return list;
    }

    @Override
    public List<Comment> retrieveAllComment(Long uId){
        Users u = em.find(Users.class, uId);
        return u.getCommentList();
    }
}
