package com.example.anant.iitbhuvaranasi.Adapters;



import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.example.anant.iitbhuvaranasi.Activities.Feedfragment_notifcation_Activity;
import com.example.anant.iitbhuvaranasi.R;
import com.example.anant.iitbhuvaranasi.Models.SingleVerticalData;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import android.util.

public class VerticalAdapter_Feedfragment extends RecyclerView.Adapter<VerticalAdapter_Feedfragment.MyViewHolder1> {
    ArrayList<SingleVerticalData> data = new ArrayList<>();
    private Context mcontext;
    private static RequestQueue mRequestQueue;


    public VerticalAdapter_Feedfragment(Context context, ArrayList<SingleVerticalData> data) {
        this.data = data;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_layout_feedfragment, parent, false);
        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder1 holder, final int position) {

        final LayoutInflater inflater = LayoutInflater.from(mcontext);

           holder.title.setText(data.get(position).getTitle_event());
        //  holder.club.setText(data.get(position).getClub());
        String originalString = data.get(position).getDate_event();
        String original = originalString.replace("T", " ");
        String original1 = original.replace("Z", "");

        Date date2 = null;
        try {
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(original1);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String newString = new SimpleDateFormat("E, dd MMM  hh:mm a").format(date2);
        holder.date.setText(newString);



        // holder.viewcount.setText(data.get(position).getViewcount());
        // holder.interestedcount.setText(data.get(position).getInterested());
        Glide.with(mcontext)
                .load(data.get(position).getImage_event())
                .error(R.drawable.background)
                .thumbnail(.1f)
                .fitCenter() // scale to fit entire image within ImageView
                .into(holder.image);

        final Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(holder.image, "fullscreen");
        pairs[1] = new Pair<View, String>(holder.title, "feedtitle");
        pairs[2] = new Pair<View, String>(holder.date, "feed_date");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Feedfragment_notifcation_Activity.class);

                // ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mcontext, pair1);
                Gson gson = new Gson();
                String json = gson.toJson(data.get(position));
                intent.putExtra("all", json);
                intent.putExtra("time", newString);
                intent.putExtra("title", data.get(position).getTitle_event());
                intent.putExtra("date", data.get(position).getDate_event());
                intent.putExtra("image", data.get(position).getImage_event());
                intent.putExtra("map_location", data.get(position).getMap_location());

                mcontext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, date /*club, viewcount,goingCount, interestedcount*/;
        // CardView cardView;
        LinearLayout layout;
        // ImageButton shareEvent, setReminder, mapLocation;
        // Button goButton, interestedButton;

        public MyViewHolder1(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.event_image1);
            title = (TextView) itemView.findViewById(R.id.event_title1);
            layout = itemView.findViewById(R.id.layoutfeed);
            // club = (TextView) itemView.findViewById(R.id.event_title_club);
            // interestedcount = (TextView) itemView.findViewById(R.id.event_interested);
            date = (TextView) itemView.findViewById(R.id.event_dates1);
        }
    }


}
