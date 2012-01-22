import models.Post;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() {
		// Check if the database is empty
		if (User.count() == 0) {
			Fixtures.deleteDatabase();
			Fixtures.loadModels("initial-data.yml");

			User user = User.connect("bob@gmail.com", "secret");
			for (int i = 0; i < 20; i++) {
				new Post(user, "title"+i, "content"+i,"tease"+i).save();
			}
		}
	}

}