import utils.JettyGuiceRestEasyTest;
import com.google.inject.Binder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import jaxrs.GsonMessageBodyHandler;
import jaxrs.HelloResource;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import service.HelloWorld;
import service.User;

/**
 *
 * @author szymon
 */
public class HelloResourceTest extends JettyGuiceRestEasyTest {

    @Override
    protected void configure(Binder b) {

        b.bind(GsonMessageBodyHandler.class);
        b.bind(HelloResource.class);
        b.bind(HelloWorld.class).toInstance(helloWorld);
    }

    @Override
    protected void configureRequestContext() {
        ResteasyProviderFactory.pushContext(User.class, new User("Kalle"));
    }
    private HelloWorld helloWorld = mock(HelloWorld.class);

    @Test
    public void hello() {
        when(helloWorld.say()).thenReturn("Czesc");

        Client client = ClientBuilder.newClient();
        assertEquals("Czesc Kalle", client.target("http://localhost:8080").path("/hello").request().get(String.class));
    }
}