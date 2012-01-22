package controllers;

import java.util.List;

import models.Post;
import models.User;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullname);
        }
    }
	
	public static void addCommentToPost(@Required final Long postId,
			@Required final String author, @Required final String content,
			@Required final String email) {
		Post post = Post.findById(postId);
		if (!validation.hasErrors()) {
			post.withComment(author, content, email);
			flash.success("Thanks for posting %s", author);
		}
		render("Application/post.html", post);
	}

	public static void index() {
		page(1);
	}

	public static void post(@Required String slug) {
		Post post = Post.findBySlug(slug);
		render(post);
	}
	
	public static void page(@Required Integer n) {
		int start = (n - 1) * 10;
		List<Post> posts = Post.find("order by postedAt desc").from(start).fetch(10);
		boolean hasMore = Post.count() > (n * 10);
		render(posts,n,hasMore);
	}

}