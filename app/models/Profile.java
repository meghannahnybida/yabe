package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Profile extends Model implements Comparable<Profile> {
    @Required
    public String author;

    @ManyToOne
    @Required
    public Post post;

    public Profile(String author) {
       // this.post = post;
        this.author = author;
    }

    public String toString(){
        return author;
    }

    public int compareTo(Profile otherProfile){
        return author.compareTo(otherProfile.author);
    }

}
