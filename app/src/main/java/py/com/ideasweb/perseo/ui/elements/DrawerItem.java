package py.com.ideasweb.perseo.ui.elements;

import android.graphics.drawable.Drawable;

public class DrawerItem {

    Drawable itemIcon;
    String itemTitle;
    String url;
    String type;

    public DrawerItem(Drawable itemIcon, String itemTitle, String url, String type) {

        this.itemIcon = itemIcon;
        this.itemTitle = itemTitle;
        this.url = url;
        this.type = type;

    }

    public Drawable getItemIcon() {
        return itemIcon;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}