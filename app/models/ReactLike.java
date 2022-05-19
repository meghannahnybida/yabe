package models;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

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

}
