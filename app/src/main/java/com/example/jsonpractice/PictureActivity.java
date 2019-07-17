package com.example.jsonpractice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PictureActivity extends AppCompatActivity {

    String address;
    WebView imageView;
    Uri webpage;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        intent = getIntent();
        // webpage = Uri.parse(intent.getStringExtra("url_address"));
        imageView = new WebView(this);
        imageView.loadUrl(intent.getStringExtra("url_address"));
        setContentView(imageView);
    }
}
