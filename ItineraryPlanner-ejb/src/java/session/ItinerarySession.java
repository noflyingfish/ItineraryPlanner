package session;

import entity.Event;
import entity.Comment;
import entity.Itinerary;
import entity.Photo;
import entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ItinerarySession implements ItinerarySessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Itinerary createItinerary(Itinerary i, Long uId) {
        Users u = em.find(Users.class, uId);
        em.persist(i);
        u.getItineraryList().add(i);
        i.getUsersList().add(u);
        return i;
    }

    @Override
    public Itinerary updateItinerary(Itinerary i) {
        Itinerary oldI = em.find(Itinerary.class, i.getId());

        oldI.setLikes(i.getLikes());
        oldI.setDuration(i.getDuration());
        oldI.setStartDate(i.getStartDate());
        oldI.setEndDate(i.getEndDate());
        oldI.setTitle(i.getTitle());
        oldI.setCaption(i.getCaption());
        oldI.setPlaceList(i.getPlaceList());
        
        return oldI;
    }

//    @Override
//    public void deleteItinerary(Long iId) {
//        Itinerary i = em.find(Itinerary.class, iId);
//
//        List<Users> users = i.getUsersList();
//        i.setUsersList(null);
//
//        for (Users u : users) {
//            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :users MEMBER OF i.users");
//            q.setParameter("users", u);
//            long count = (Long) q.getSingleResult();
//            if (count == 0) {
//                em.remove(u);
//            }
//        }
//        List<Event> events = i.getEventList();
//        i.setEventList(null);
//        for (Event e : events) {
//            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :event MEMBER OF i.events");
//            q.setParameter("event", e);
//            long count = (Long) q.getSingleResult();
//            if (count == 0) {
//                em.remove(e);
//            }
//        }
//        List<Comment> comments = i.getCommentList();
//        i.setCommentList(null);
//        for (Comment c : comments) {
//            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :comment MEMBER OF i.comments");
//            q.setParameter("comment", c);
//            long count = (Long) q.getSingleResult();
//            if (count == 0) {
//                em.remove(c);
//            }
//        }
//        List<Photo> photos = i.getPhotoList();
//        i.setPhotoList(null);
//        for (Photo p : photos) {
//            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :photo MEMBER OF i.photos");
//            q.setParameter("photo", p);
//            long count = (Long) q.getSingleResult();
//            if (count == 0) {
//                em.remove(p);
//            }
//        }
//        em.remove(i);
//    }
//
//    @Override
//    public void exportItinerary(Itinerary i, Users uId) {
//
//        Users u = em.find(Users.class, uId);
//
//        em.persist(i);
//        u.getItineraryList().add(i);
//
//    }

    @Override
    public List<Users> addUser(Long uId, Long iId) {
        Itinerary i = em.find(Itinerary.class, iId);
        Users u = em.find(Users.class, uId);
        i.getUsersList().add(u);
        u.getItineraryList().add(i);
        return i.getUsersList();
    }
    
    @Override
    public List<Users> deleteUserFromItinerary(Long uId, Long iId){
        Itinerary i = em.find(Itinerary.class, iId);
        Users u = em.find(Users.class, uId);
        
        List<Users> uList = i.getUsersList();
        uList.remove(u);
        i.setUsersList(uList);
        
        List<Itinerary> iList = u.getItineraryList();
        iList.remove(i);
        u.setItineraryList(iList);
        
        return uList;
    }

//    @Override
//    public void deleteUser(Users User, Itinerary i) {
//
//        if (User != null) {
//            if (i != null) {
//                Query q = em.createQuery("SELECT i FROM Itinerary i WHERE :users MEMBER OF i.users");
//                q.setParameter("users", User);
//                i.getUsersList().remove(User);
//                em.remove(User);
//            }
//        }
//    }

    @Override
    public List<Itinerary> retrieveAllItinerary() {
        Query q = em.createQuery("SELECT i FROM Itinerary i");
        return q.getResultList();
    }

    @Override
    public Itinerary getItineraryById(Long iId){
        return em.find(Itinerary.class, iId);
    }
    
    @Override
    public List<Itinerary> searchItineraryByUser(String username) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.usersList.userName LIKE :username");
        q.setParameter("username", "%"+ username + "%");
        return q.getResultList();
    }

    @Override
    public List<Itinerary> searchItineraryByTitle(String title) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.title LIKE :title");
        q.setParameter("title", "%"+ title + "%");
        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByDuration(String duration) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.duration LIKE :duration");
        q.setParameter("duration","%"+ duration + "%");
        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByHashTag(String hashtag) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.caption LIKE :hashtag");
        q.setParameter("hashtag", "%"+ hashtag + "%");
        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByLocation(String location) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.countryList.location LIKE :location");
        q.setParameter("location","%"+ location + "%");
        return q.getResultList(); 
    }
    
    @Override
    public List<Event> retrieveAllEvent(Long iId){
        Itinerary i = em.find(Itinerary.class, iId);
        return i.getEventList();
    }

    @Override
    public Comment createComment(Comment c, Long iId) {
        Itinerary i = em.find(Itinerary.class, iId);
        em.persist(c);
        i.getCommentList().add(c);
        return c;
    }
    
    @Override
    public Comment updateComment(Comment c){
        Comment oldC = em.find(Comment.class, c.getId());
        oldC.setComment(c.getComment());
        return oldC;
    }
    
    @Override
    public List<Comment> removeComment(Long cId, Long iId) {
        Itinerary i = em.find(Itinerary.class, iId);
        Comment c = em.find(Comment.class, cId);
        i.getCommentList().remove(c);
        em.remove(c);
        return i.getCommentList();
    }
    
    @Override
    public List<Comment> retrieveAllComment(Long iId){
        Itinerary i = em.find(Itinerary.class, iId);
        return i.getCommentList();
    }

    @Override
    public Event addEvent(Event e, Long iId) {
        em.persist(e);
        Itinerary i = em.find(Itinerary.class, iId);
        i.getEventList().add(e);
        return e;
    }
    
    @Override
    public Event updateEvent(Event e){
        Event oldE = em.find(Event.class, e.getId());
        
        oldE.setCost(e.getCost());
        oldE.setDuration(e.getDuration());
        oldE.setEndDate(e.getEndDate());
        oldE.setLocation1(e.getLocation1());
        oldE.setLocation2(e.getLocation2());
        oldE.setName(e.getName());
        oldE.setNotes(e.getNotes());
        oldE.setStartDate(e.getStartDate());
        oldE.setType(e.getType());
        
        return oldE;
    }

    @Override
    public List<Event> removeEvent(Long eId, Long iId) {
       Event e = em.find(Event.class, eId);
       Itinerary i = em.find(Itinerary.class, iId);
     
       List<Comment> commentList = e.getCommentList();
       for(Comment c : commentList){
           em.remove(c);
       }
       commentList.clear();
       
       List<Photo> photoList = e.getPhotoList();
       for(Photo p : photoList){
           em.remove(p);
       }
       commentList.clear();
       return i.getEventList();
    }
    
    @Override
    public Photo addPhoto(Photo p, Long iId){
        em.persist(p);
        Itinerary i = em.find(Itinerary.class, iId);
        i.getPhotoList().add(p);
        return p;
    }
    
    @Override
    public List<Photo> removePhoto(Long pId, Long iId){
        Itinerary i = em.find(Itinerary.class, iId);
        Photo p = em.find(Photo.class, pId);
        i.getPhotoList().remove(p);
        em.remove(p);
        return i.getPhotoList();
    }
    
    @Override
    public List<Photo> retrieveAllPhoto(Long iId){
        Itinerary i = em.find(Itinerary.class, iId);
        return i.getPhotoList();
    }
}
