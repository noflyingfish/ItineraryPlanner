package session;

import entity.Event;
import entity.Comment;
import entity.Itinerary;
import entity.Photo;
import entity.Users;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ItinerarySessionLocal {

    public Itinerary createItinerary(Itinerary i, Long uId);

    public Itinerary updateItinerary(Itinerary i);

    //public void exportItinerary(Itinerary i, Users u);
    public List<Users> addUser(Long uId, Long iId);

    public List<Itinerary> retrieveAllItinerary();

    public List<Itinerary> searchItineraryByUser(String username);

    public List<Itinerary> searchItineraryByTitle(String title);

    public List<Itinerary> searchItineraryByDuration(String duration);

    public List<Itinerary> searchItineraryByHashTag(String hashtag);

    public List<Itinerary> searchItineraryByLocation(String location);

    //Event
    public Event addEvent(Event e, Long iId);
    
    public Event updateEvent(Event e);

    public List<Event> removeEvent(Long eId, Long iId);
    
    public List<Event> retrieveAllEvent(Long iId);
    
    // Photo
    public Photo addPhoto(Photo p, Long iId);
    
    public List<Photo> removePhoto(Long pId, Long iId);
    
    public List<Photo> retrieveAllPhoto(Long iId);

    // Comments 
    public Comment createComment(Comment c, Long iId);

    public Comment updateComment(Comment c);

    public List<Comment> removeComment(Long cId, Long iId);

    public List<Comment> retrieveAllComment(Long iId);
}
