package com.robynem.mit.web.filter;

import com.robynem.mit.web.util.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@WebFilter(filterName = "SessionTimeoutFilter", urlPatterns = "/private/*")
public class SessionTimeoutFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        if (req instanceof HttpServletRequest) {
            HttpServletRequest servletRequest = ((HttpServletRequest)req);
            HttpSession session = servletRequest.getSession(true);

            if (session.getAttribute(Constants.PORTAL_USER_KEY) == null) {
                RequestDispatcher requestDispatcher = servletRequest.getRequestDispatcher("/authentication/sessionTimeout");
                requestDispatcher.forward(req, resp);
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }


    }

    public void init(FilterConfig config) throws ServletException {

    }

}
