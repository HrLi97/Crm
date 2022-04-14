package com.lhr.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author lhr
 * @Date:2022/4/5 19:28
 * @Version 1.0
 */
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        servletResponse.setContentType("text/html;charset=utf-8");
        servletRequest.setCharacterEncoding("UTF-8");

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
