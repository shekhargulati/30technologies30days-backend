package com.shekhar.challenge.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.shekhar.challenge.domain.Feedback;

@Path("/feedback")
public class FeedbackResource {

    @Inject
    private DB db;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response create(Feedback feedback) {
        BasicDBObjectBuilder basicDBObjectBuilder = BasicDBObjectBuilder.start("name", feedback.getName())
                .add("description", feedback.getDescription()).add("receivedOn", feedback.getReceivedOn()).add("lngLat", feedback.getLngLat());
        DBCollection collection = db.getCollection("feedback");
        collection.save(basicDBObjectBuilder.get());
        return Response.created(null).build();
    }
}
