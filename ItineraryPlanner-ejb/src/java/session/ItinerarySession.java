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
    public void createItinerary(Itinerary i, Long uId) {
        Users u = em.find(Users.class, uId);
        em.persist(i);
        u.getItineraryList().add(i);
        i.getUsersList().add(u);
    }

    @Override
    public void updateItinerary(Itinerary i) {
        Itinerary oldI = em.find(Itinerary.class, i.getId());

        oldI.setLikes(i.getLikes());
        oldI.setDuration(i.getDuration());
        oldI.setCreatedDate(i.getCreatedDate());
        oldI.setStartDate(i.getStartDate());
        oldI.setEndDate(i.getEndDate());
        oldI.setTitle(i.getTitle());
        oldI.setCaption(i.getCaption());
    }

    @Override
    public void deleteItinerary(Long iId) {
        Itinerary i = em.find(Itinerary.class, iId);

        List<Users> users = i.getUsersList();
        i.setUsersList(null);

        for (Users u : users) {
            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :users MEMBER OF i.users");
            q.setParameter("users", u);

            long count = (Long) q.getSingleResult();

            if (count == 0) {
                em.remove(u);
            }
        }
        List<Event> events = i.getEventList();
        i.setEventList(null);

        for (Event e : events) {
            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :event MEMBER OF i.events");
            q.setParameter("event", e);

            long count = (Long) q.getSingleResult();

            if (count == 0) {
                em.remove(e);
            }
        }

        List<Comment> comments = i.getCommentList();
        i.setCommentList(null);

        for (Comment c : comments) {
            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :comment MEMBER OF i.comments");
            q.setParameter("comment", c);

            long count = (Long) q.getSingleResult();

            if (count == 0) {
                em.remove(c);
            }
        }

        List<Photo> photos = i.getPhotoList();
        i.setPhotoList(null);

        for (Photo p : photos) {
            Query q = em.createQuery("SELECT count(i) FROM Itinerary i WHERE :photo MEMBER OF i.photos");
            q.setParameter("photo", p);

            long count = (Long) q.getSingleResult();

            if (count == 0) {
                em.remove(p);
            }
        }

        em.remove(i);
    }

    @Override
    public void exportItinerary(Itinerary i, Users uId) {

        Users u = em.find(Users.class, uId);

        em.persist(i);
        u.getItineraryList().add(i);

    }

    @Override
    public void addUser(Users User, Long iId) {
        Itinerary i = em.find(Itinerary.class, iId);

        i.getUsersList().add(User);
        User.getItineraryList().add(i);

    }

    @Override
    public void deleteUser(Users User, Itinerary i) {

        if (User != null) {
            if (i != null) {
                Query q = em.createQuery("SELECT i FROM Itinerary i WHERE :users MEMBER OF i.users");
                q.setParameter("users", User);
                i.getUsersList().remove(User);
                em.remove(User);
            }

        }
    }

    @Override
    public List<Itinerary> retrieveAllItinerary() {
        Query q = em.createQuery("SELECT i FROM Itinerary i");
        return q.getResultList();
    }

    @Override
    public List<Itinerary> searchItineraryByUser(String username) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.usersList.userName = :username");
        q.setParameter("username", username);

        List<Itinerary> itineraries = q.getResultList();

        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByTitle(String title) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.title = :title");
        q.setParameter("title", title);

        List<Itinerary> itineraries = q.getResultList();

        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByDuration(String duration) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.duration = :duration");
        q.setParameter("duration", duration);

        List<Itinerary> itineraries = q.getResultList();

        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByHashTag(String hashtag) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.hashtagList.tag = :hashtag");
        q.setParameter("hashtag", hashtag);

        List<Itinerary> itineraries = q.getResultList();

        return q.getResultList();

    }

    @Override
    public List<Itinerary> searchItineraryByLocation(String location) {
        Query q = em.createQuery("SELECT i FROM Itinerary i WHERE i.countryList.location = :location");
        q.setParameter("location", location);

        List<Itinerary> itineraries = q.getResultList();

        return q.getResultList();
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public void addComment(Comment c, Itinerary i) {
        Itinerary itinerary = em.find(Itinerary.class, i.getId());

        em.persist(c);
        itinerary.getCommentList().add(c);
    }

    @Override
    public void deleteComment(Comment c, Itinerary i) {
        Itinerary itinerary = em.find(Itinerary.class, i.getId());
        Comment comment = em.find(Comment.class, c.getId());

        itinerary.getCommentList().remove(c);
        em.remove(c);
    }

    @Override
    public void addActivity(Event a, Itinerary i) {
        Query q;
        Long itId = i.getId();
        em.persist(a);
        q = em.createQuery("SELECT i FROM Itinerary WHERE i.id =: itId");
        q.setParameter("itId", itId);
        if (q != null) {
            Itinerary itinerary = (Itinerary) q.getSingleResult();
            itinerary.getEventList().add(a);
        }
    }

    @Override
    public void deleteActivity(Event a, Itinerary i) {
        Query q1;
        Query q2;
        Long itId = i.getId();
        Long aId = a.getId();

        q1 = em.createQuery("SELECT i FROM Itinerary WHERE i.id =: itId");
        q1.setParameter("itId", itId);
        q2 = em.createQuery("SELECT a FROM Activity WHERE a.id =: aId");
        q2.setParameter("aId", aId);
        if (q1 != null) {
            Itinerary itinerary = (Itinerary) q1.getSingleResult();
            if (q2 != null) {
                Event activity = (Event) q2.getSingleResult();
                itinerary.getEventList().remove(activity);
                em.remove(activity);
            }
        }
    }
}
