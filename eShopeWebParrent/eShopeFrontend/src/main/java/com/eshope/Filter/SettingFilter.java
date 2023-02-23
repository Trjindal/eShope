package com.eshope.Filter;

import com.eShope.common.entity.Setting.Setting;
import com.eshope.Service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {

    @Autowired
    private SettingService settingService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String url=((HttpServletRequest) servletRequest).getRequestURL().toString();
        if(url.endsWith(".css")||url.endsWith(".js")||url.endsWith(".png")||url.endsWith("jpg")||url.endsWith(".jpeg")||url.endsWith(".woff2")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        List<Setting> generalSettings=settingService.getGeneralSettings();
        generalSettings.forEach(setting -> {
            servletRequest.setAttribute(setting.getKey(),setting.getValue());
        });

        filterChain.doFilter(servletRequest,servletResponse);


    }
}
