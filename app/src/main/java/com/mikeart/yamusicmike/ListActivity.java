package com.mikeart.yamusicmike;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Log TAG
    public static final String TAG = "YaRecyclerList";

    private static final String url = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
    public List<Artist> artistList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private YaRecyclerAdapter adapter;


    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        pDialog = new ProgressDialog(this);
        // Показывает диалоговое окно при загрузке
        pDialog.setMessage("Загрузка...");
        pDialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Исполнители");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));

        updateList(url);

    }

    // метод отвечает за получение данных из JSON
    public void updateList(String url) {

        adapter = new YaRecyclerAdapter(ListActivity.this, artistList);
        mRecyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);

        adapter.clearAdapter();

        // запрос объектов из JSON при помощи библиотеки volley
        JsonArrayRequest artistReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Artist artist = new Artist();

                                artist.setName(obj.getString("name"));
                                artist.setAlbums(obj.getInt("albums"));
                                artist.setTracks(obj.getInt("tracks"));

                                artist.setId(obj.getInt("id"));
                                artist.setLink(obj.getString("link"));
                                artist.setDescription(obj.getString("description"));
                                artist.setCoverBigUrl(obj.getJSONObject("cover").getString("big"));

                                // обработка жанров как массива
                                JSONArray genreArry = obj.getJSONArray("genres");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                artist.setGenres(genre);

                                artist.setCoverSmallUrl(obj.getJSONObject("cover").getString("small"));

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
                hidePDialog();
            }
        });

        // добавление запроса в очередь
        queue.add(artistReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
