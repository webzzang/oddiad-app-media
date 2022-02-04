
package com.exflyer.oddiad.databinding;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.text.NumberFormat;

public class DataBindingAdapter {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView)
                .load(imageUrl)
                .into(imageView);
    }

    @BindingAdapter({"imageUrl", "imagePlaceHolder"})
    public static void loadImage(ImageView imageView, String imageUrl, Drawable placeHolder) {
        Glide.with(imageView)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(placeHolder))
                .into(imageView);
    }


    @BindingAdapter({"imageUrl", "imageRequestListener"})
    public static void loadImage(ImageView imageView, String imageUrl, RequestListener<Drawable> requestListener) {
        Glide.with(imageView)
                .load(imageUrl)
                .listener(requestListener)
                .into(imageView);
    }

    @BindingAdapter({"text_line_break_html"})
    public static void text(TextView textView, String value) {
        String convertedString = "";

        try {
            convertedString = value.replace(System.getProperty("line.separator"), "<br/>");

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                convertedString = Html.fromHtml(convertedString, Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                convertedString = Html.fromHtml(convertedString).toString();
            }
        } catch (Exception e) {
        }

        textView.setText(convertedString);
    }

    @BindingAdapter({"text_number_format"})
    public static void text(TextView textView, Integer value) {
        String convertedString = "";

        try {
            convertedString = NumberFormat.getInstance().format(value);
        } catch (Exception e) {

        }

        textView.setText(convertedString);
    }

    @BindingAdapter({"textWithWordWrapping"})
    public static void textWithWordWrapping(TextView textView, String text) {
        String convertedString = "";

        try {
            convertedString = text.replace("\\n", "\n");
        } catch (Exception e) {

        }

        textView.setText(convertedString);
    }

}
