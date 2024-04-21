package com.health.devicesevent.interceptor;

import com.health.devicesevent.configuration.TenantContext;
import com.health.devicesevent.dao.DataSourceConfigRepository;
import com.health.devicesevent.entity.Tenant;
import com.health.devicesevent.exception.TenantNotFoundException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


public class TenantNameInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(TenantNameInterceptor.class);

//    @Autowired
//    private ApplicationContext context;

    @Autowired
    DataSourceConfigRepository configRepo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        if(requestURI.contains("/error"))
            return false;
        String tenantId = request.getParameter("tenantId");
        log.info("Request URI: {}", requestURI);
        log.info("Tenant Id: {}", tenantId);

        try {
            Optional<Tenant> tenant = configRepo.findById(tenantId);

            if (tenant.isEmpty()) {
//            throw new TenantNotFoundException("Tenant Id "+tenantId+" not found ");
                response.getWriter().write("Tenant Id is not present in the request");
                response.setStatus(400);
                return false;
            }
        }catch (Exception e){
            //case where exception is thrown and different schema-db is connected.
            response.getWriter().write("Device id or Tenant invalid");
            response.setStatus(404);
            return false;
        }
        TenantContext.setCurrentTenant(tenantId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        log.info("Clear tenant context");
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        if (ex != null) {
            ex.printStackTrace();
            log.error("An exception occurred with message: {}", ex.getMessage());
        }

    }
}