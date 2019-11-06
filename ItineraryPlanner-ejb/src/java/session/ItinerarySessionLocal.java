package session;

import entity.Event;
import entity.Comment;
import entity.Itinerary;
import entity.Users;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ItinerarySessionLocal {
    
    public void createItinerary(Itinerary i, Long uId);

    public void updateItinerary(Itinerary i);

    public void deleteItinerary(Long iId);

    public void exportItinerary(Itinerary i, Users u);

    public void addUser(Users User, Long iId);

    public void deleteUser(Users User, Itinerary i);

    public List<Itinerary> retrieveAllItinerary();

    public List<Itinerary> searchItineraryByUser(String username);

    public List<Itinerary> searchItineraryByTitle(String title);

    public List<Itinerary> searchItineraryByDuration(String duration);

    public List<Itinerary> searchItineraryByHashTag(String hashtag);

    public List<Itinerary> searchItineraryByLocation(String location);

    public void addComment(Comment c, Itinerary i);

    public void deleteComment(Comment c, Itinerary i);
    
    public void addActivity(Event a, Itinerary i);
    
    public void deleteActivity(Event a, Itinerary i);
}
