package controllers;

import models.Category;
import play.*;
import play.mvc.*;

@With(Secure.class)
@CRUD.For(Category.class)
public class Categories extends CRUD {
	
}