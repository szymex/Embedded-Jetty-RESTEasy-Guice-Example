package jaxrs;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.HelloWorld;
import service.User;

@Path("/")
public class HelloResource {

    @Inject HelloWorld helloWord;
    @Inject User user;

    @GET
    @Path("/hello.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Data getJsonMessage() {
        return new Data(helloWord.say());
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public String getTxtMessage() {
        return helloWord.say() + " " + user;
    }

    @GET
    @Path("/async-hello")
    @Produces(MediaType.TEXT_PLAIN)
    public void getAsyncData(@Suspended final AsyncResponse response ,
            @QueryParam("d") @DefaultValue("1") final int delaySec) throws IOException {
        
        response.setTimeout(6, TimeUnit.SECONDS);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delaySec * 1000);
                    
                    if (!response.isSuspended()){
                        System.out.println("Async response is not suspended");
                        return;
                    }
                    
                    if (!response.resume(Response.ok(helloWord.say()).build() )){
                        System.out.println("Async response not resumed");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public static class Data {

        private String text;

        public Data(String data) {
            this.text = data;
        }

        public String getData() {
            return text;
        }
    }
}