import utils.JettyGuiceRestEasyTest;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import jaxrs.ClientErrorExceptionMapper;
import jaxrs.GsonMessageBodyHandler;
import jaxrs.HelloResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener2;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import service.HelloWorld;
import service.HelloWorldFI;
import service.User;

/**
 *
 * @author szymon
 */
public class HelloResourceTest extends JettyGuiceRestEasyTest{

    @Override
    protected void configure(Binder b) {
            
        b.bind(GsonMessageBodyHandler.class);
        b.bind(HelloResource.class);
        b.bind(HelloWorld.class).toInstance(helloWorld);
        b.bind(User.class).toInstance(new User("Kalle"));
    }
    
    private HelloWorld helloWorld = mock(HelloWorld.class);
    
    @Test
    public void hello() {
        when(helloWorld.say()).thenReturn("Czesc");
        
        Client client = ClientBuilder.newClient();        
        assertEquals("Czesc Kalle", client.target("http://localhost:8080").path("/hello").request().get(String.class));
    }
}