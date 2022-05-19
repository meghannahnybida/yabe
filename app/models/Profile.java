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

    public static Profile findOrCreateByName(String author) {
        Profile profile = Tag.find("byName", author).first();
        if(profile == null) {
            profile = new Profile(author);
        }
        return profile;
    }

    public static List<Map> getCloud(){
        List<Map> result = Profile.find(
                "select new map(pf.author as profile, count(p.id) as pound) from Post p join p.profiles as pf group by pf.author order by pf.author").fetch();
        return result;
    }

}
