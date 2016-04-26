package com.mikeart.yamusicmike;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class DetailActivity extends AppCompatActivity {

    String link;
    Intent intent;

    TextView genres;
    TextView albums;
    TextView tracks;
    TextView description;

    NetworkImageView coverBig;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = getApplicationContext();
        // проверка подключения к интернету
        CheckNetwork.isInternetAvailable(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // получение данных из интента и вывод на экран
        String stCoverBig = getIntent().getStringExtra("coverBig");
        String stGenres = getIntent().getStringExtra("genres");
        String stAlbums = getIntent().getStringExtra("albums");
        String stTracks = getIntent().getStringExtra("tracks");
        String stDescription = getIntent().getStringExtra("description");
        String stName = getIntent().getStringExtra("name");
        link = getIntent().getStringExtra("link");

        coverBig = (NetworkImageView) findViewById(R.id.coverBig);
        genres = (TextView)findViewById(R.id.detail_genres);
        albums = (TextView)findViewById(R.id.detail_albums);
        tracks = (TextView)findViewById(R.id.detail_tracks);
        description = (TextView)findViewById(R.id.detail_description);

        // инициализация mImageLoader для подгрузки картинки coverBig из кэша устройства (если она там есть)
        RequestQueue queue= YaSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        ImageLoader mImageLoader = new ImageLoader(queue, new LruBitmapCache(LruBitmapCache.getCacheSize(this)));
        coverBig.setImageUrl(stCoverBig, mImageLoader);

        genres.setText(stGenres);
        albums.setText(stAlbums);
        tracks.setText(stTracks);
        description.setText(stDescription);

        intent = new Intent(this, WebViewActivity.class);

        setTitle(stName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // проверка наличия ссылки на сайт перед отправкой интента
                    if( link != null && !link.isEmpty())
                    {
                        intent.putExtra("link",link);
                        startActivity(intent);
                        }
                    else{
                        Toast.makeText(context, "У исполнителя нет сайта(", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        // показывает кнопку Up в ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
