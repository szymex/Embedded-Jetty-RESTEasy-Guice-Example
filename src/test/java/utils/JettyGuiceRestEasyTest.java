package utils;


import com.google.inject.Binder;
import com.google.inject.Guice;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener2;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author szymon
 */
public abstract class JettyGuiceRestEasyTest {

    protected abstract void configure(Binder b);
    private Server server;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {

        server = new Server(8080);
        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.addEventListener(Guice.createInjector(new TestModule()).getInstance((GuiceResteasyBootstrapServletContextListener2.class)));

        ServletHolder sh = new ServletHolder(HttpServletDispatcher.class);
        //servletHandler.setInitParameter("resteasy.role.based.security", "true");
        servletHandler.addServlet(DefaultServlet.class, "/*");
        servletHandler.addServlet(sh, "/*");

        server.setHandler(servletHandler);
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    private class TestModule extends RequestScopeModule {

        @Override
        protected void configure() {
            super.configure();
            JettyGuiceRestEasyTest.this.configure(this.binder());
        }
    }
}
