package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//声明过滤器   拦截所有请求
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //spring 自带 路径匹配器
    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求：{}", request.getRequestURI());
        String requestURI = request.getRequestURI();

  /*      A. 获取本次请求的URI

        B. 判断本次请求, 是否需要登录, 才可以访问

        C. 如果不需要，则直接放行

        D. 判断登录状态，如果已登录，则直接放行

        E. 如果未登录, 则返回未登录结果*/
        String[] urls = {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};
        boolean check = check(requestURI, urls);
        //如果不需要，则直接放行
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        //判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            filterChain.doFilter(request, response);
            BaseContext.setThreadLocal((Long) request.getSession().getAttribute("employee"));
            return;
        }
        //如果未登录, 则通过输出流的方式向客户端页面输出数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    /**
     * 判断路径是否匹配
     *
     * @param requestUrl
     * @param urls
     * @return
     */
    public boolean check(String requestUrl, String[] urls) {
        for (String url : urls) {
            if (MATCHER.match(url, requestUrl)) {
                return true;
            }
        }
        return false;
    }
}
