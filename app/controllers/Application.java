package controllers;

import java.util.List;

import models.Category;
import models.Post;
import models.User;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
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

	public static void category(@Required String name) {
		categoryPage(name, 1);
	}

	public static void categoryPage(@Required String name, @Required Integer n) {
		Category.findByName(name);
		int start = (n - 1) * 10;
		renderPage(n, Post.findCategory(name, start));
	}

	public static void page(@Required Integer n) {
		int start = (n - 1) * 10;
		renderPage(n, Post.find("order by postedAt desc").from(start)
				.<Post> fetch(10));
	}

	private static void renderPage(Integer n, List<Post> posts) {
		boolean hasMore = Post.count() > (n * 10);
		List<Category> categories = Category.findAll();
		render("Application/page.html", posts, n, hasMore, categories);
	}

}