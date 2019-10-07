package com.jf.my.view.goods;

/**
 * @author fengrs
 * @date 2019/7/11
 */
public class LoadImgTabCustomTabEntity implements LoadImgCustomTabEntity {
    public String title;
    public String selectedIcon;
    public String unSelectedIcon;


    public LoadImgTabCustomTabEntity (String title, String selectedIcon, String unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public String getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public String getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
