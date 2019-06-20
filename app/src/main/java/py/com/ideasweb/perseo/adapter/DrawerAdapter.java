package py.com.ideasweb.perseo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.ui.elements.DrawerItem;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private ArrayList<DrawerItem> drawerItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DrawerAdapter(ArrayList<DrawerItem> drawerItems) {
        this.drawerItems = drawerItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView itemTitle = (TextView) holder.itemView.findViewById(R.id.textViewDrawerItemTitle);
        ImageView itemIcon = (ImageView) holder.itemView.findViewById(R.id.imageViewDrawerItemIcon);
        TextView url = (TextView) holder.itemView.findViewById(R.id.drawerActivity);
        TextView type = (TextView) holder.itemView.findViewById(R.id.drawerType);

        itemTitle.setText(drawerItems.get(position).getItemTitle());
        itemIcon.setImageDrawable(drawerItems.get(position).getItemIcon());
        url.setText(drawerItems.get(position).getUrl());
        type.setText(drawerItems.get(position).getType());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return drawerItems.size();
    }
}