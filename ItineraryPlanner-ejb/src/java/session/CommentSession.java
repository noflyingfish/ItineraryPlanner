/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Comment;
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

/**
 *
 * @author jinwei
 */
@Stateless
@LocalBean
public class CommentSession implements CommentSessionLocal {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    // Need add currentUser ID
    public void createComment(Comment c) {
        em.persist(c);
    } //end createEvent
    
    @Override
    public void updateComment(Comment c) {
        em.merge(c);
        em.flush();
    }
    
   @Override
    public void removeComment(Long cId) throws NoResultException {
        Comment c = em.find(Comment.class, cId);
        
        if (c == null){
            throw new NoResultException("Not found");
        }
        else {
            em.remove(c);
            }
        }
    
    @Override
    public List<Comment> retrieveAllComment() {
        Query q = em.createQuery("SELECT c FROM Comment c");
        return q.getResultList();
    }    
    
    @Override
    public List<Comment> searchCommentByComment(String comment) {
        Query q = em.createQuery("SELECT c FROM Comment c WHERE c.comment = :comment");
        q.setParameter("comment", comment);
        
        return q.getResultList();
    }
    
    @Override
    public List<Comment> searchCommentByCreatedDate(Date createdDate) {
        Query q = em.createQuery("SELECT c FROM Comment c WHERE c.createdDate = :createdDate");
        q.setParameter("createdDate", createdDate);
        
        return q.getResultList();
    }
    
    @Override
    public List<Comment> searchCommentByWriter(Long uId) {
        Query q = em.createQuery("SELECT c FROM Comment c WHERE c.user = :uId");
        q.setParameter("uId", uId);
        
        return q.getResultList();
    }
    
    @Override
    public Comment getComment(Long cId) throws NoResultException {
        Comment c = em.find(Comment.class, cId);

        if (c != null) {
            return c;
        } else {
            throw new NoResultException("Not found");
        }
    } //end getComment       
}