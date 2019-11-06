package session;

import entity.Comment;
import entity.Event;
import entity.Photo;
import entity.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface EventSessionLocal {

    public void updateEvent(Event e);

    public List<Event> searchEventByName(String name);

    public List<Event> searchEventByCreatedDate(Date createdDate);

    public List<Event> searchEventByStartDate(Date startDate);

    public List<Event> searchEventByEndDate(Date endDate);

    public List<Event> searchEventByDuration(String duration);

    public Event getEvent(Long eId);
    
    // comments
    public Comment createComment(Comment c, Long eId);

    public Comment updateComment(Comment c);

    public List<Comment> removeComment(Long cId, Long eId);

    public List<Comment> retrieveAllComment(Long eId);
    
    // photo
    public Photo addPhoto(Photo p, Long eId);
    
    public List<Photo> removePhoto(Long pId, Long eId);
    
    public List<Photo> retrieveAllPhoto(Long eId);
}
