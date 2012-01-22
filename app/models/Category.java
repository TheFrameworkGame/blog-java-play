package models;

import java.util.List;

import javax.persistence.Entity;

import org.hibernate.mapping.Map;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Category extends Model implements Comparable<Category> {

	public static Category findOrCreateByName(final String name) {
		Category tag = Category.find("byName", name).first();
		if (tag == null) {
			tag = new Category(name);
		}
		return tag;
	}

	public static List<Map> getCloud() {
		List<Map> result = Category
				.find("select new map(c.name as category, count(p.id) as pound) from Post p join p.categories as c group by c.name order by c.name")
				.fetch();
		return result;
	}

    @Required
	public String name;

	private Category(final String name) {
		this.name = name;
	}

	@Override
	public int compareTo(final Category otherTag) {
		return name.compareTo(otherTag.name);
	}

	@Override
	public String toString() {
		return name;
	}
}