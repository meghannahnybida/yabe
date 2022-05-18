package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Like extends Model {
    @Required
    public String author;

    @ManyToOne
    @Required
    public Post post;

    public Like(Post post, String author) {
        this.post = post;
        this.author = author;
    }

}