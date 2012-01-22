package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Comment extends Model {

    @Required
	public String author;
    
    @Required
	public String email;
    
    @Required
	public Date postedAt;

    @Required
	@Lob
	public String content;

    @Required
	@ManyToOne
	public Post post;

	public Comment(final Post post, final String author, final String content,
			final String email) {
		this.post = post;
		this.author = author;
		this.content = content;
		// TODO: validate email
		this.email = email;
		postedAt = new Date();
	}
	
	@Override
	public String toString() {
		return author + " @ " +postedAt;
	}

}