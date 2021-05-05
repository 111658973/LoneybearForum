package com.lbf.pack.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.ResponseBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.beans.VisitHistoryBean;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.service.QueryDataService;
import com.lbf.pack.service.UserDataService;
import com.lbf.pack.service.VisitHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomUrlIntercepter implements HandlerInterceptor {
    @Autowired
    private QueryDataService queryDataService;
    @Autowired
    private VisitHistoryService visitHistoryService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        //        String path = request.getServletPath();
        String url = request.getRequestURI();
        String method = request.getMethod();
//        return path.matches("/zone/*") || path.matches("post/**");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal!="anonymousUser"){
            UserLoginBean user = (UserLoginBean) principal;
            VisitHistoryBean histroy = new VisitHistoryBean();
            if(url.contains("like")){
                histroy.setEnable(true);
            }
            if(url.contains("dislike")){
                histroy.setEnable(false);
            }


            if(queryDataService == null){
                WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                queryDataService = wac.getBean(QueryDataService.class);
            }
            long uid =queryDataService.getUidByUsername(user.getUsername());
            histroy.setMethod(method);
            histroy.setUrl(url);
            histroy.setLastVisitTime(new TimeUtil().getCuerrent_time());


            if(visitHistoryService == null){
                WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                visitHistoryService = wac.getBean(VisitHistoryService.class);
            }
            if (uid!=-1){
                histroy.setUid(uid);
                ResponseBean insert = visitHistoryService.insert(histroy);
            }

        }
    }
}
