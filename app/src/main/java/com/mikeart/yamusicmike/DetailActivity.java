package com.mikeart.yamusicmike;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class DetailActivity extends AppCompatActivity {

    TextView genres;
    TextView albums;
    TextView tracks;
    TextView description;

    NetworkImageView coverBig;
    ImageLoader mImageLoader;

    String link;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


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

        mImageLoader = YaSingleton.getInstance(this).getImageLoader();
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
                    intent.putExtra("link",link);
                    startActivity(intent);
                }
            });
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
           // NavUtils.navigateUpTo(this, new Intent(this, ListActivity.class));
           // NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
