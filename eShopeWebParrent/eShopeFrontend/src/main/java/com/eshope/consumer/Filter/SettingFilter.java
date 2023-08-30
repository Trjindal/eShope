package com.eshope.consumer.Filter;

import com.eShope.common.entity.Menu;
import com.eShope.common.entity.Setting.Setting;
import com.eshope.consumer.Service.MenuService;
import com.eshope.consumer.Service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {

    @Autowired
    private SettingService articleService;

    @Autowired
    private MenuService menuService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String url=((HttpServletRequest) servletRequest).getRequestURL().toString();
        if(url.endsWith(".css")||url.endsWith(".js")||url.endsWith(".png")||url.endsWith("jpg")||url.endsWith(".jpeg")||url.endsWith(".woff2")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        loadGeneralSettings(request);
        loadMenuSettings(request);


        filterChain.doFilter(servletRequest,servletResponse);


    }

    private void loadMenuSettings(ServletRequest request) {
        List<Menu> headerMenuItems = menuService.getHeaderMenuItems();
        request.setAttribute("headerMenuItems", headerMenuItems);

        List<Menu> footerMenuItems = menuService.getFooterMenuItems();
        request.setAttribute("footerMenuItems", footerMenuItems);
    }

    private void loadGeneralSettings(ServletRequest request) {
        List<Setting> generalSettings = articleService.getGeneralSettings();

        generalSettings.forEach(setting -> {
            request.setAttribute(setting.getKey(), setting.getValue());
            System.out.println(setting.getKey() + " == > " + setting.getValue());
        });

//        request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
    }

}
