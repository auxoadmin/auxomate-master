package com.auxomate.mynewself.mynewself.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.auxomate.mynewself.mynewself.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.auxomate.mynewself.mynewself.R.drawable.ic_cloud_done_black_24dp;

public class AspireViewPageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageUrls;
    private ArrayList<String> desc;
    private LayoutInflater layoutInflater;

    public AspireViewPageAdapter(Context context, ArrayList<String> imageUrls,ArrayList<String> desc){
        this.context = context;
        this.imageUrls = imageUrls;
        this.desc=desc;
    }

    @Override
    public int getCount() {
        return imageUrls.size()+1;


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.aspire_gallery, container, false);
        if(position< imageUrls.size()) {


            Log.d("Size", " " + imageUrls.size());
//            Log.d("imageUrl", imageUrls.get(position));
            ImageView imageView = view.findViewById(R.id.aspire_gallery_image);
            TextView tv = view.findViewById(R.id.aspire_gallery_des);
            Picasso.with(context)
                    .load(imageUrls.get(position)).resize(800, 1500)
                    .centerCrop().networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(imageUrls.get(position)).resize(800, 1500)
                            .centerCrop().into(imageView);

                }
            });
            tv.setText(desc.get(position));
            container.addView(view);

        }else
            {
                ImageView imageView = view.findViewById(R.id.aspire_gallery_image);
                TextView tv = view.findViewById(R.id.aspire_gallery_des);
                imageView.setImageResource(R.drawable.ic_cloud_done_black_24dp);
                tv.setText("You are done with your Visulization");
                container.addView(view);
        }

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
