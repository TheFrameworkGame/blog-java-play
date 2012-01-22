package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Post extends Model {

	public static List<Post> findCategory(final String name) {
		return Post
				.find("select distinct p from Post p join p.categories as c where c.name = ?",
						name).fetch();
	}

    @Required
	public String title;

    @Required
	public String slug;

    @Required
	public boolean isPublished;

    @Required
	public boolean hasComments;

    @Required
	public Date postedAt;

    @Required
	@Lob
	public String content;

    @Required
	@Lob
	public String tease;

    @Required
	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> comments;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public Set<Category> categories;

	public Post(final User author, final String title, final String content,
			final String tease) {
		this.author = author;
		this.title = title;
		this.content = content;
		this.tease = tease;
		comments = new ArrayList<Comment>();
		categories = new TreeSet<Category>();
		// TODO: improve slugification
		slug = title.replace(' ', '-');
		postedAt = new Date();
		isPublished = false;
		hasComments = true;
	}

	public Post next() {
		return Post.find("postedAt > ? order by postedAt asc", postedAt)
				.first();
	}

	public Post previous() {
		return Post.find("postedAt < ? order by postedAt desc", postedAt)
				.first();
	}

	public Post withCategory(final String name) {
		categories.add(Category.findOrCreateByName(name));
		this.save();
		return this;
	}

	public Post withComment(final String author, final String content,
			final String email) {
		if (!hasComments) {
			throw new IllegalStateException("No comments for post");
		}

		Comment newComment = new Comment(this, author, content, email).save();
		comments.add(newComment);
		this.save();
		return this;
	}

	@Override
	public String toString() {
		return title;
	}
}
