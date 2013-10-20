import java.io.IOException;
import java.security.Principal;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@Singleton
@WebFilter(urlPatterns = "/*")
public class HelloFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sr;

        //System.out.println("filter: " + req.getRequestURI() + " user: " + sr.getParameter("user"));
        if (sr.getParameter("user") != null) {
            fc.doFilter(new UserRoleRequestWrapper("user", sr.getParameter("user"), req), sr1);
        } else {
            fc.doFilter(req, sr1);
        }

    }

    @Override
    public void destroy() {
    }

    private class UserRoleRequestWrapper extends HttpServletRequestWrapper {

        private String role;
        private String user;

        public UserRoleRequestWrapper(String role, String user, HttpServletRequest request) {
            super(request);
            this.role = role;
            this.user = user;
        }

        @Override
        public boolean isUserInRole(String role) {
            if (this.role == null) {
                return super.isUserInRole(role);
            }
            return this.role.equals(role);
        }

        @Override
        public Principal getUserPrincipal() {
            if (this.user == null) {
                return super.getUserPrincipal();
            }

            return new Principal() {
                @Override
                public String getName() {
                    return user;
                }
            };
        }
    }
}
