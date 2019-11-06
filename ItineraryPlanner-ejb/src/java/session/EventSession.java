package session;

import entity.Event;
import entity.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class EventSession implements EventSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createEvent(Event e) {
        em.persist(e);
    } //end createEvent

    @Override
    public void updateEvent(Event e) {
        em.merge(e);
        em.flush();
    }

    @Override
    public void removeEvent(Long eId) throws NoResultException {
        Event e = em.find(Event.class, eId);
        
        if (e == null){
            throw new NoResultException("Not found");
        }
        else {
            em.remove(e);
            }
        }
    
    @Override
    public List<Event> retrieveAllEvent() {
        Query q = em.createQuery("SELECT e FROM Event e");
        return q.getResultList();
    }
    
    @Override
    public List<Event> searchEventByName(String name) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.name = :name");
        q.setParameter("name", name);
        List<Event> results = q.getResultList();
        
        return q.getResultList();
    }
    
    @Override
    public List<Event> searchEventByCreatedDate(Date createdDate) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.createdDate = :createdDate");
        q.setParameter("createdDate", createdDate);
        
        return q.getResultList();
    }
    
    @Override
    public List<Event> searchEventByStartDate(Date startDate) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.startDate = :startDate");
        q.setParameter("startDate", startDate);
        
        return q.getResultList();
    }    
    
    @Override
    public List<Event> searchEventByEndDate(Date endDate) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.endDate = :endDate");
        q.setParameter("endDate", endDate);
        
        return q.getResultList();
    }        
    
    @Override
    public List<Event> searchEventByDuration(String duration) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.duration = :duration");
        q.setParameter("duration", duration);
        
        return q.getResultList();
    }        
    
    @Override
    public Event getEvent(Long eId) throws NoResultException {
        Event e = em.find(Event.class, eId);

        if (e != null) {
            return e;
        } else {
            throw new NoResultException("Not found");
        }
    } //end getEvent       
}