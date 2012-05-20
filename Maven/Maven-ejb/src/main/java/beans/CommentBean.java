/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.project.entities.Comment;
import com.project.entities.Event;
import com.project.entities.User;
import com.project.fascades.CommentFacade;
import com.project.fascades.EventFacade;
import com.project.fascades.UserFacade;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
/**
 *
 * @author przemic
 */
@Stateless
@Remote(CommentRemote.class)
public class CommentBean implements CommentRemote {
    
    @EJB
    private CommentFacade commentFacade;
    @EJB
    private UserFacade userFacade;
    @EJB
    private EventFacade eventFacade;
//    @EJB
//    private StatsBean statsBean;
//    @EJB
//    private CategoryFacade categoryFacade;
//    @Resource(mappedName = "jms/VoteQueue")
//    private Queue voteQueue;
//    @Resource(mappedName = "jms/VoteQueueFactory")
//    private ConnectionFactory voteQueueFactory;
//    @EJB
//    private CommentFacade commentFacade;
//    @EJB
//    private UserFacade userFacade;
//    @EJB
//    private CityFacade cityFacade;
//    @EJB
//    private ClubFacade clubFacade;
//    @EJB
//    private VoteFacade voteFacade;
//    
//    @Override
//    public Club getClub(int id) throws NotExistingData {
//        Club c = clubFacade.find(Integer.valueOf(id));
//        
//        if( c == null ) {
//            throw new NotExistingData("There is no such club.");
//        }
//        
//        return c;
//    }
//
//    @Override
//    public void addClub(String name, String desc, String street, int cityId, String homeNumber) throws NotExistingData, AlreadyExistingAddressException {
//        City city = cityFacade.find(Integer.valueOf(cityId));
//        
//        if( city == null ) {
//            throw new NotExistingData("There is no such city.");
//        }
//        
//        Club club = clubFacade.getClubByAddress(street, homeNumber);
//        
//        if( club == null ) {
//            club = new Club();
//            club.setName(name);
//            club.setDesc(desc);
//            club.setStreet(street);
//            club.setCity(city);
//            club.setHomeNumber(homeNumber);
//            club.setAddedAt(new Date());
//
//            int clubId = clubFacade.createAndGetUser(club);
//            statsBean.update(clubId);
//        } else {
//            throw new AlreadyExistingAddressException("There is already a club on this address.");
//        }
//    }
    @Override
    public void addComment(String description_text, Event Event_id, User User_id) {
        Comment comment = new Comment();
        comment.setDescriptionText(description_text);
        comment.setEventid(Event_id);
        comment.setUserid(User_id);
        
        commentFacade.create(comment);
    }
    
    @Override
    public Comment getComment(Integer id) {
        Comment comment = commentFacade.find(id);
        
        return comment;
    }
    
    @Override
    public Collection<Comment> getAllByEvent(Integer eventId) {
        Event event = eventFacade.find(eventId);
        
        return commentFacade.getAllByEvent(event);
    }
//
//    @Override
//    public Collection<Club> getClubsFromCity(int cityId) {
//        City city = cityFacade.find(cityId);
//        
//        return clubFacade.getClubsFromCity(city);
//    }
//
//    @Override
//    public List<ClubStats> getClubsFromCityByCategory(int cityId, String categoryName, int userId) {
//        Category category = categoryFacade.getCategoryByName(categoryName);
//        City city = cityFacade.find(cityId);
//        
//        if( category != null && city != null ) {
//            List<ClubStats> clubs = statsBean.getClubsStatsFromCityByCategory(city, category);
//            return clubs;
//        }
//        
//        //todo zapisywanie preferencji uzytkownika/wyszukiwan
//        
//        return null;
//    }
//
//    @Override
//    public String voteOnClub(int clubId, String properties, int userId) throws JMSException, NotEnoughTimePassedException {
//        Club club = clubFacade.find(Integer.valueOf(clubId));
//        User user = userFacade.find(Integer.valueOf(userId));
//            
//        if( club != null && user != null ) {
//            Vote vote = voteFacade.getVoteByUserAndClub(club, user);                                   
//            if( vote != null ) {
//                throw new NotEnoughTimePassedException("Can't vote on this club");
//            }
//        }
//        
//        Connection connection = null;
//        Session session = null;
//        try {
//            connection = voteQueueFactory.createConnection();
//            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            MessageProducer messageProducer = session.createProducer(voteQueue);
//            
//            MapMessage mapMessage = session.createMapMessage();
//            
//            mapMessage.setInt("club", clubId);
//            mapMessage.setString("properties", properties);
//            mapMessage.setInt("user", userId);
//            
//            messageProducer.send(mapMessage);
//        } finally {
//            if (session != null) {
//                try {
//                    session.close();
//                } catch (JMSException e) {
//                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
//                }
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//        
//        return "OK";
//    }
//
//    @Override
//    public String commentClub(long clubId, String desc, long userId) throws NotExistingData, NotEnoughTimePassedException {
//        User user = userFacade.find(userId);
//        Club club = clubFacade.find(clubId);
//        
//        if( user != null && club != null ) {
//            Date now = new Date();
//            long diff = now.getTime() - user.getLastCommented().getTime();
//
//            if( diff > 100*60*2 ) { 
//                Comment comment = new Comment();
//                comment.setText(desc);
//                commentFacade.create(comment);
//                
//                user.setLastCommented(now);
//                userFacade.edit(user);
//            } 
//            
//            throw new NotEnoughTimePassedException("You have to wait 2min before posting a new comment.");
//        }
//        
//        throw new NotExistingData("There is no such user or club.");
//    }
//
//    @Override
//    public ClubStats getStats(int id) throws NotExistingData {
//        return statsBean.getStats(getClub(id));
//    }
    
}
