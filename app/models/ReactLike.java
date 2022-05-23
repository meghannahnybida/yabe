package models;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

import java.util.List;

@Entity
public class ReactLike extends Model {
    @Required
    public String author;

    @ManyToOne
    @Required
    public Post post;

    @Required
    public int numLikes;

    public ReactLike(Post post, String author) {
        this.post = post;
        this.author = author;
    }

    public static List<ReactLike> findLikedBy(String profile){
        //return Comment.find("author = ?1", profile).fetch()
        return Comment.find("select p from Post p, ReactLike l where l.post = p and l.author = ?1", profile).fetch();
    }

}
