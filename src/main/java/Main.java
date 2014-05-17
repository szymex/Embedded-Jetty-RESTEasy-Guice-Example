
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import javax.inject.Singleton;
import jaxrs.ClientErrorExceptionMapper;
import jaxrs.GsonMessageBodyHandler;
import jaxrs.HelloResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.guice.RequestScoped;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import service.HelloWorld;
import service.HelloWorldFI;
import service.HelloWorldPL;
import service.User;

public class Main {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new HelloModule(args));

        injector.getAllBindings();

        injector.createChildInjector().getAllBindings();

        Server server = new Server(8080);
        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.addEventListener(injector.getInstance(GuiceResteasyBootstrapServletContextListener.class));

        ServletHolder sh = new ServletHolder(HttpServletDispatcher.class);
        servletHandler.setInitParameter("resteasy.role.based.security", "true");
        servletHandler.addFilter(new FilterHolder(injector.getInstance(HelloFilter.class)), "/*", null);
        //servletHandler.addServlet(DefaultServlet.class, "/*");
        servletHandler.addServlet(sh, "/*");

        server.setHandler(servletHandler);
        server.start();
        server.join();
    }

    private static class HelloModule extends RequestScopeModule {

        private String[] args;

        public HelloModule(String[] args) {
            this.args = args;
        }

        @Provides
        @Singleton
        public HelloWorld provideHelloWorld() {
            if (args.length > 0 && args[0].equals("fi")) {
                return new HelloWorldFI();
            } else {
                return new HelloWorldPL();
            }
        }

        @Override
        protected void configure() {
            super.configure();
            bind(GsonMessageBodyHandler.class);
            bind(HelloResource.class);
            bind(ClientErrorExceptionMapper.class);
        }

        @Provides
        @RequestScoped
        public User provideUser() {
            return ResteasyProviderFactory.getContextData(User.class);
        }
    }
}
