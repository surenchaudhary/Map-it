package com.wildnettechnologies.mapit.mapit.routeModule.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wildnettechnologies.mapit.mapit.R;
import com.wildnettechnologies.mapit.mapit.routeModule.RouteActivity;
import com.wildnettechnologies.mapit.mapit.routeModule.SearchResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class InitSearchRowAdapter extends RecyclerView.Adapter<InitSearchRowAdapter.ViewHolder> {

    private static final String debugTag = InitSearchRowAdapter.class.getCanonicalName();
    private String searchResponseListDescription;
    public static ArrayList<String> selectedLocations = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_view_init_search)
        TextView searchRowMain;
        @Bind(R.id.text_view_desc)
        TextView searchRowDesc;
        @Bind(R.id.clock_icon)
        ImageView searchClockIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Activity activity;

    private ArrayList<SearchResponse> searchLists;

    public InitSearchRowAdapter(Activity activity) {
        this.activity = activity;

    }

    public void setSearchList(ArrayList<SearchResponse> searchLists) {
        this.searchLists = searchLists;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.init_search_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SearchResponse searchResponseList = searchLists.get(position);
        final String searchRowMain = searchResponseList.getDescription().split(",")[0];
        final String searchRowDesc = getDescriptionFromAddress(searchResponseList);

        holder.searchRowMain.setText(searchRowMain);
        if (searchRowDesc != null)
            holder.searchRowDesc.setText(searchRowDesc.replace(",", ""));
        else
            holder.searchRowDesc.setText(searchRowMain);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selectedLocations.contains(searchResponseList.getDescription()) || selectedLocations.isEmpty()) {
                    if (selectedLocations.size() > 0)
                        selectedLocations.remove(0);
                    selectedLocations.add(searchResponseList.getDescription());
                }
                activity.startActivity(new Intent(activity, RouteActivity.class));
                activity.finish();
            }
        });

    }


    @NonNull
    private String getDescriptionFromAddress(SearchResponse searchResponseList) {
        final String searchRowMain = searchResponseList.getDescription().split(",")[0];

        String searchText = searchResponseList.getDescription().replace(searchRowMain, "");

        return searchText;
    }

    @Override
    public int getItemCount() {
        return searchLists == null ? 0 : searchLists.size();
    }

}
