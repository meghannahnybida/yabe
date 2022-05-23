package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;

import java.util.*;
import java.util.stream.Collectors;

import models.*;
import play.mvc.results.RenderTemplate;

public class Application extends Controller {

    public static void index() {
        Post frontPost = Post.find("order by postedAt desc").first();
        List<Post> olderPosts = Post.find(
                "order by postedAt desc"
        ).from(1).fetch(10);
        render(frontPost, olderPosts);
    }

    @Before
    static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    public static void show(Long id) {
        Post post = Post.findById(id);
        String randomID = Codec.UUID();
        render(post, randomID);
    }

    public static void postComment(Long postId, @Required(message = "Author is required") String author,
                                   @Required(message = "A message is required") String content,
                                   @Required(message = "Please type the code") String code,
                                   String randomID) {
        Post post = Post.findById(postId);
        validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
        if (validation.hasErrors()) {
            render("Application/show.html", post, randomID);
        }
        post.addComment(author, content);
        flash.success("Thanks for posting %s!", author);
        Cache.delete(randomID);
        show(postId);
    }

    public static void reactWithLike(Long postId, @Required(message = "Author is required") String author) {
        Post post = Post.findById(postId);
        post.addLike(author);
        flash.success("Thanks for liking %s!", author);
        show(postId);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#EED4F6");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void listTagged(String tag) {
        List<Post> posts = Post.findTaggedWith(tag);
        render(tag, posts);
    }

    public static void listWrittenBy(String profile) {
        List<Post> posts = Post.findWrittenBy(profile);
        List<Comment> comments = Comment.findCommentedBy(profile);
        List<ReactLike> likes = ReactLike.findLikedBy(profile);
        render(profile, posts, comments, likes);
    }

    public static void getMetrics(){
        Map<String, Integer> jsonOutput = new HashMap<>();

        List<Post> posts = Post.findAll();
        List<Comment> comments = Comment.findAll();
        List<ReactLike> likes = ReactLike.findAll();

        jsonOutput.put("Users", 2);
        jsonOutput.put("Posts", posts.size());
        jsonOutput.put("Comments", comments.size());
        jsonOutput.put("Likes", likes.size());
        renderJSON(jsonOutput);
    }
}
