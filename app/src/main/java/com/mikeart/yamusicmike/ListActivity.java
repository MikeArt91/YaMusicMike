package com.mikeart.yamusicmike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static final String TAG = ListActivity.class.getSimpleName();

    private static final String url = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
    public List<Artist> artistList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private YaRecyclerAdapter adapter;

    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressbar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Исполнители");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));

        // проверка наличия подключения и запуск передачи из Json
        if (CheckNetwork.isInternetAvailable(this)){
            updateList(url);}

    }

    // метод отвечает за получение данных из JSON
    public void updateList(String url) {

        adapter = new YaRecyclerAdapter(ListActivity.this, artistList);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();

        // запрос объектов из JSON
        JsonArrayRequest artistReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        mProgressbar.setVisibility(View.GONE);

                        for (int i = 0; i < response.length(); i++) {
                            try {
        // добавление в базу полей из выбранного объекта с проверкой их наличия
                                JSONObject obj = response.getJSONObject(i);
                                Artist artist = new Artist();

                                if(obj.has("name")){
                                artist.setName(obj.getString("name"));}

                                if(obj.has("albums")){
                                artist.setAlbums(obj.getInt("albums"));}

                                if(obj.has("tracks")){
                                artist.setTracks(obj.getInt("tracks"));}

                                if(obj.has("id")){
                                artist.setId(obj.getInt("id"));}

                                if(obj.has("link")){
                                artist.setLink(obj.getString("link"));}

                                if(obj.has("description")){
                                artist.setDescription(obj.getString("description"));}

                                if(obj.has("cover")){
                                artist.setCoverBigUrl(obj.getJSONObject("cover").getString("big"));
                                artist.setCoverSmallUrl(obj.getJSONObject("cover").getString("small"));}

                                // добавление жанров в строку
                                JSONArray genresArray = obj.getJSONArray("genres");
                                if(obj.has("genres")){
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genresArray.length(); j++) {
                                    genre.add((String) genresArray.get(j));
                                }
                                artist.setGenres(genre);}

                                artistList.add(artist);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // сортировка листа по имени исполнителей (вне зависимости от регистра)
                        Collections.sort(artistList, new Comparator<Artist>(){
                            public int compare(Artist left, Artist right) {
                                return left.getName().compareToIgnoreCase(right.getName());
                            }
                        });

                        // указывает адаптеру изменение данных для обработки обновлённого списка
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mProgressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Что-то пошло не так(", Toast.LENGTH_LONG).show();
            }
        });

        // добавление запроса в очередь
        YaSingleton.getInstance(this).addToRequestQueue(artistReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressbar.setVisibility(View.GONE);
    }

}
