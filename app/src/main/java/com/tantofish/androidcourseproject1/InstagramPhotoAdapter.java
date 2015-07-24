package com.tantofish.androidcourseproject1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by yutu on 7/24/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(pos);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }

        TextView tvCaption  = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLikes    = (TextView) convertView.findViewById(R.id.tvLikes);

        ImageView ivUserPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
        ImageView ivPhoto     = (ImageView) convertView.findViewById(R.id.ivPhoto);

        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvLikes.setText(photo.likeCount + " Bravos");
        ivPhoto.setImageResource(0);
        ivUserPhoto.setImageResource(0);

        int length = Math.min(photo.imageHeight, photo.imageWidth);
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .resize(length, length)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(ivPhoto);

        Transformation roundTransform = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(photo.userPhotoUrl)
                .transform(roundTransform)
                .placeholder(R.drawable.ic_instagram)
                .into(ivUserPhoto);

        return convertView;
    }
}
