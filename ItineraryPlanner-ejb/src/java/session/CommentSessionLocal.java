/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Comment;
import entity.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

@Local
public interface CommentSessionLocal {
    public void createComment(Comment c);
    
    public void updateComment(Comment c);
    
    public void removeComment(Long cId);
    
    public List<Comment> retrieveAllComment();
            
    public List<Comment> searchCommentByComment(String comment);
            
    public List<Comment> searchCommentByCreatedDate(Date createdDate);
    
    public List<Comment> searchCommentByWriter(Long uId);
    
    public Comment getComment(Long cId) throws NoResultException;
}
