package com.envigil.extranet.TableOfComponents.app;

import android.content.Context;

import androidx.annotation.NonNull;

import com.envigil.extranet.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


/**
 * @Author yangsanning
 * @ClassName MyAppRefreshLayoutStyle
 * @Description 用于刷新布局的操作工具类, 如果多个地方使用，请改成单例
 * @Date 2019/12/2
 * @History 2019/12/2 author: description:
 */
public class MyAppRefreshLayoutStyle {
    /**
     * 初始化刷新的风格
     */
    public static void init() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.color_refresh_bg, R.color.color_refresh_text);
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter classicsFooter = new ClassicsFooter(context).setDrawableSize(20);
                classicsFooter.setBackgroundColor(context.getResources().getColor(R.color.color_refresh_bg));
                classicsFooter.setAccentColorId(R.color.color_refresh_text);
                return classicsFooter;
            }
        });
    }
}
