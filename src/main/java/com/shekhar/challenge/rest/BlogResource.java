package com.shekhar.challenge.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.shekhar.challenge.domain.Blog;

@Path("/blogs")
public class BlogResource {

    @Inject
    private DB db;

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<Blog> timeline() {
        DBCursor dbCursor = db.getCollection("blogs").find().sort(new BasicDBObject("publishedOn", -1)).limit(30);
        List<Blog> blogs = new ArrayList<Blog>();
        while (dbCursor.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject) dbCursor.next();
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
    public Response create(Blog blog, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        BasicDBObjectBuilder basicDBObjectBuilder = BasicDBObjectBuilder.start("title", blog.getTitle())
                .add("url", blog.getUrl()).add("publishedOn", blog.getPublishedOn());

        String authorization = request.getHeader("authorization");

        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(DatatypeConverter.parseBase64Binary(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];
            DBCollection users = db.getCollection("users");
            DBObject findOne = users.findOne(BasicDBObjectBuilder.start("username", username).add("password", password)
                    .get());
            if (findOne == null) {
                return Response.status(Status.UNAUTHORIZED).build();
            }

            DBCollection collection = db.getCollection("blogs");
            collection.save(basicDBObjectBuilder.get());
            
            try {
                sendMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Response.created(null).build();

        }

        return Response.status(Status.UNAUTHORIZED).build();

    }

    private String sendMessage() throws Exception{

        HttpPost httpPost = new HttpPost("http://aerogear-shekhargulati.rhcloud.com/rest/sender");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity("{'message':{'alert':'New blog published'}}"));
        
        DefaultHttpClient httpClient = new DefaultHttpClient();
        
        Credentials credentials = new UsernamePasswordCredentials("bf8de351-39b9-455c-8725-1cf32703975d","716a5ce3-c82f-4e41-a83a-3006332b635d");
        httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
        
        org.apache.http.HttpResponse httpResponse = httpClient.execute(httpPost);

        // Start request
        try {
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            // Log warning
            return null;
        }
    }
}
