package session;

import entity.Event;
import entity.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

@Local
public interface EventSessionLocal {

    public void createEvent(Event e);

    public void updateEvent(Event e);

    public void removeEvent(Long eId);
    
    public List<Event> retrieveAllEvent();
    
    public List<Event> searchEventByName(String name);
    
    public List<Event> searchEventByCreatedDate(Date createdDate);
    
    public List<Event> searchEventByStartDate(Date startDate);
    
    public List<Event> searchEventByEndDate(Date endDate); 
    
    public List<Event> searchEventByDuration(String duration);  
    
    public Event getEvent(Long eId) throws NoResultException;
}
