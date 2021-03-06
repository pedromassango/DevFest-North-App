package com.defvest.devfestnorth.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.defvest.devfestnorth.R;
import com.defvest.devfestnorth.activities.OrganizersDetail;
import com.defvest.devfestnorth.models.OrganizersModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
    public class Organizers extends Fragment {
    View rootView;
    Context context;

    private FirebaseRecyclerAdapter<OrganizersModel, OrganisersView> firebaserecyclerAdapter;

    public static Organizers newInstance(){
        return new Organizers();
    }
    public Organizers() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_organizers, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycle);
        TextView mEmptyListView = rootView.findViewById(R.id.list_organizers_error);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Organizers");
        myref.keepSynced(true);

        if (isNetworkConnected() || isWifiConnected()) {
            /*Toast.makeText(this, "Network is Available", Toast.LENGTH_SHORT).show();*/
        } else {
           mEmptyListView.setVisibility(View.VISIBLE);
        }

        FirebaseRecyclerOptions<OrganizersModel> options = new FirebaseRecyclerOptions.Builder<OrganizersModel>().setQuery(myref,OrganizersModel.class).build();

        firebaserecyclerAdapter = new FirebaseRecyclerAdapter<OrganizersModel, OrganisersView>(options) {



            @SuppressLint("CheckResult")
            @Override
            protected void onBindViewHolder(OrganisersView viewholder, final int position, final OrganizersModel model) {
                viewholder.setName(model.getName());
                viewholder.setGDG(model.getGDG());
                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.warning);
                viewholder.setPhoto(model.getPhoto());
                viewholder.setAbout(model.getAbout());

                viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Bundle extras = new Bundle();
                        Intent nextactivity = new Intent(v.getContext(), OrganizersDetail.class);
                        nextactivity.putExtra("name",model.getName());
                        nextactivity.putExtra("gdg",model.getGDG());
                        nextactivity.putExtra("photo",model.getPhoto());
                        nextactivity.putExtra("about",model.getAbout());
                        nextactivity.putExtras(extras);
                        v.getContext().startActivity(nextactivity);

                    }
                });
            }

            @Override
            public OrganisersView onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.organizers_custom_list,parent,false);
                return new OrganisersView(v);
            }
        };
        recyclerView.setAdapter(firebaserecyclerAdapter);
        return rootView;
    }
    public static class OrganisersView extends RecyclerView.ViewHolder {
        View mView;
        TextView name;
        TextView gdg;

        ImageView photo;
        TextView about;

        OrganisersView(View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView) itemView.findViewById(R.id.vname);
            gdg = (TextView) itemView.findViewById(R.id.vgdg);
            photo=  itemView.findViewById(R.id.profile_photo);
            about= (TextView) itemView.findViewById(R.id.vabout);
        }

        public void setName(String names) {
            name.setText(names);
        }
        void setGDG(String gdgs) {
            gdg.setText(gdgs);
        }
        @SuppressLint("CheckResult")
        public void setPhoto(String photos) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.warning);
            Glide.with(mView.getContext()).load(photos).into(photo);
        }
            public void setAbout(String abouts ) {
            about.setText(abouts);
        }
        }

    @Override
    public void onStart() {
        super.onStart();
        firebaserecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaserecyclerAdapter.stopListening();
    }

    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
              getActivity().getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
               getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }


}
