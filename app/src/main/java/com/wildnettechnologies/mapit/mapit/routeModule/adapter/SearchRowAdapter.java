package com.wildnettechnologies.mapit.mapit.routeModule.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildnettechnologies.mapit.mapit.R;
import com.wildnettechnologies.mapit.mapit.routeModule.SearchResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchRowAdapter extends RecyclerView.Adapter<SearchRowAdapter.ViewHolder> {

    private static final String debugTag = SearchRowAdapter.class.getCanonicalName();
    private String searchResponseListDescription;
    public static ArrayList<String> selectedLocations = new ArrayList<>();
    public static ArrayList<String> selectedLocationsPlaceName = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_view_main)
        TextView searchRowMain;
        @Bind(R.id.text_view_distance)
        TextView searchRowDistance;
        @Bind(R.id.text_view_desc)
        TextView searchRowDesc;
        @Bind(R.id.location_icon)
        ImageView searchRowMarkerIcon;
        @Bind(R.id.init_icon)
        ImageView searchRowInitIcon;
        @Bind(R.id.listings_icon)
        ImageView searchRowListingsIcon;
        @Bind(R.id.favourites_icon)
        ImageView searchRowFavouritesIcon;
        @Bind(R.id.background_search_list_row)
        LinearLayout searchRowBackground;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Activity activity;

    private ArrayList<SearchResponse> searchLists;

    public SearchRowAdapter(Activity activity) {
        this.activity = activity;

    }

    public void setSearchList(ArrayList<SearchResponse> searchLists) {
        this.searchLists = searchLists;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_row, parent, false);
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
//                    holder.searchRowBackground.setBackgroundColor(Color.WHITE);
//                    if (!selectedLocations.contains(searchResponseList.getDescription()) || selectedLocations.isEmpty()) {
//                        if (selectedLocations.size() > 0)
//                            selectedLocations.remove(0);
//                        selectedLocations.add(searchResponseList.getDescription());
//                    }
//                    activity.startActivity(new Intent(activity, RouteActivity.class));
//                    activity.finish();
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
