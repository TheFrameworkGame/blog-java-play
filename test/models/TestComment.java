package models;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class TestComment extends UnitTest {

	@Before
	public void before() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void useTheCommentsRelation() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();

		// Create a new post
		Post bobPost = new Post(bob, "My first post", "Hello world", "hello")
				.save();

		bobPost.withComment("Jeff", "Nice post", "sales@bob.com");
		bobPost.withComment("Tom", "I knew that !", "sales@bob.com");

		// Count things
		assertEquals(1, User.count());
		assertEquals(1, Post.count());
		assertEquals(2, Comment.count());

		// Retrieve Bob's post
		bobPost = Post.find("byAuthor", bob).first();
		assertNotNull(bobPost);

		// Navigate to comments
		assertEquals(2, bobPost.comments.size());
		assertEquals("Jeff", bobPost.comments.get(0).author);

		// Delete the post
		bobPost.delete();

		// Check that all comments have been deleted
		assertEquals(1, User.count());
		assertEquals(0, Post.count());
		assertEquals(0, Comment.count());
	}
}
