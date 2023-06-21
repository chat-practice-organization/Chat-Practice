package com.practice.chat.config;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class FilterConfig {
//    @Bean
//    public FilterRegistrationBean logFilter() {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new Filter() {
//            private static int count = 0;
//            @Override
//            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//                synchronized (this){
//                    count++;
//                    log.info(String.valueOf(count));
//                }
//                chain.doFilter(request, response);
//            }
//        });
//
//        return filterRegistrationBean;
//    }
}
