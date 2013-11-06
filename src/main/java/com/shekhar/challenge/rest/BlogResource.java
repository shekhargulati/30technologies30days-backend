package com.shekhar.challenge.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.shekhar.challenge.domain.Blog;

@Path("/blogs")
public class BlogResource {

    @Inject
    private DB db;

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<Blog> timeline() {
        DBCursor dbCursor = db.getCollection("blogs").find();
        List<Blog> blogs = new ArrayList<Blog>();
        while (dbCursor.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject)dbCursor.next();
            String title = dbObject.getString("title");
            String url = dbObject.getString("url");
            Date publishedOn = dbObject.getDate("publishedOn");
            String id = dbObject.getObjectId("_id").toString();
            blogs.add(new Blog(id, title, url, publishedOn));
        }
        return blogs;
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response create(Blog blog) {
        BasicDBObjectBuilder basicDBObjectBuilder = BasicDBObjectBuilder.start("title", blog.getTitle()).add("url",
                blog.getUrl()).add("publishedOn", blog.getPublishedOn());
        DBCollection collection = db.getCollection("blogs");
        collection.save(basicDBObjectBuilder.get());
        return Response.created(null).build();
    }
}