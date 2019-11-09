package session;

import entity.Comment;
import entity.Event;
import entity.Photo;
import entity.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class EventSession implements EventSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Event> searchEventByName(String name) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.name LIKE :name");
        q.setParameter("name", "%" + name + "%");
        List<Event> results = q.getResultList();

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventByCreatedDate(Date createdDate) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.createdDate LIKE :createdDate");
        q.setParameter("createdDate", "%" + createdDate + "%");

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventByStartDate(Date startDate) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.startDate LIKE :startDate");
        q.setParameter("startDate", "%" + startDate + "%");

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventByEndDate(Date endDate) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.endDate LIKE :endDate");
        q.setParameter("endDate", "%" + endDate + "%");

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventByDuration(String duration) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.duration LIKE :duration");
        q.setParameter("duration", "%" + duration + "%");

        return q.getResultList();
    }

    @Override
    public Event getEvent(Long eId) {
        Event e = em.find(Event.class, eId);

        return e;
    } //end getEvent       

    @Override
    public Comment createComment(Comment c, Long eId) {
        em.persist(c);
        Event e = em.find(Event.class, eId);
        e.getCommentList().add(c);
        return c;
    }

    @Override
    public Comment updateComment(Comment c) {
        Comment oldC = em.find(Comment.class, c.getId());
        oldC.setComment(c.getComment());
        return oldC;
    }

    @Override
    public List<Comment> removeComment(Long cId, Long eId) {
        Comment c = em.find(Comment.class, cId);
        Event e = em.find(Event.class, eId);
        e.getCommentList().remove(c);
        em.remove(c);
        return e.getCommentList();
    }

    @Override
    public List<Comment> retrieveAllComment(Long eId) {
        Event e = em.find(Event.class, eId);
        return e.getCommentList();
    }

    @Override
    public Photo addPhoto(Photo p, Long eId) {
        em.persist(p);
        Event e = em.find(Event.class, eId);
        e.getPhotoList().add(p);
        return p;
    }

    public List<Photo> removePhoto(Long pId, Long eId){
        Event e = em.find(Event.class, eId);
        Photo p = em.find(Photo.class, pId);
        e.getPhotoList().remove(p);
        em.remove(p);
        return e.getPhotoList();
    }

    public List<Photo> retrieveAllPhoto(Long eId){
        Event e = em.find(Event.class, eId);
        return e.getPhotoList();
    }
}
