package com.mikeart.yamusicmike;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class YaRecyclerAdapter extends RecyclerView.Adapter<YaRecyclerAdapter.ListRowViewHolder> {

    private List<Artist> artistList;
    private Context mContext;

    // albumsEnding и tracksEnding содержат возможные варианты окончаний
    // для правильной их установки после числительного в методе getEnding
    private List<String> albumsEnding = Arrays.asList("альбом", "альбома", "альбомов");
    private List<String> tracksEnding = Arrays.asList("песня", "песни", "песен");

    public YaRecyclerAdapter(Context context, List<Artist> artistList) {
        this.artistList = artistList;
        this.mContext = context;
    }

    @Override
    public ListRowViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int position) {
       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list, null);
        return new ListRowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListRowViewHolder listRowViewHolder, int position) {
        Artist listItems = artistList.get(position);
        int focusedItem = 0;
        listRowViewHolder.itemView.setSelected(focusedItem == position);
        listRowViewHolder.getLayoutPosition();

        // инициализация mImageLoader для подгрузки картинки coverSmall из кэша устройства (если она там есть)
        RequestQueue queue= YaSingleton.getInstance(mContext.getApplicationContext()).getRequestQueue();
        ImageLoader mImageLoader = new ImageLoader(queue, new LruBitmapCache(LruBitmapCache.getCacheSize(mContext)));
        listRowViewHolder.coverSmall.setImageUrl(listItems.getCoverSmallUrl(), mImageLoader);

        // если coverSmall ещё не загружено, ставится изображение по-умолчанию placeholder
        listRowViewHolder.coverSmall.setDefaultImageResId(R.drawable.placeholder);

        // имя исполнителя проходит через декодер для исправления кодировки
        // и корректного отображения русского языка
        String nameDecoded = stringDecoder(listItems.getName());
        listRowViewHolder.name.setText(nameDecoded);

        // запись последовательности жанров в переменную genresStr
        // с добавлением запятой после каждого объекта
        String genresStr = "";
        for (String str : listItems.getGenres()) {
            genresStr += str + ", ";
        }

        // убирает лишнюю запятую в конце переменной genresStr
        genresStr = genresStr.length() > 0 ? genresStr.substring(0, genresStr.length() - 2):genresStr;
        listRowViewHolder.genres.setText(genresStr);

        // в albumsStr записывается (количество альбомов) + (правильное окончание) + разделитель (",")
        // getEnding отвечает за выбор правильного окончания после числительного из albumsEnding
        String albumsStr = getEnding(listItems.getAlbums(), albumsEnding) + ", ";
        listRowViewHolder.albums.setText(albumsStr);

        // в tracksStr записывается (количество альбомов) + (правильное окончание)
        // getEnding отвечает за выбор правильного окончания после числительного из tracksEnding
        String tracksStr = getEnding(listItems.getTracks(), tracksEnding);
        listRowViewHolder.tracks.setText(tracksStr);
    }

    public class ListRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected NetworkImageView coverSmall;
        protected TextView name;
        protected TextView genres;
        protected TextView albums;
        protected TextView tracks;

        public ListRowViewHolder (View view) {
            super(view);
            view.setOnClickListener(this);
            this.coverSmall = (NetworkImageView) view.findViewById(R.id.coverSmall);
            this.name = (TextView) view.findViewById(R.id.name);
            this.genres = (TextView) view.findViewById(R.id.genres);
            this.albums = (TextView) view.findViewById(R.id.albums);
            this.tracks = (TextView) view.findViewById(R.id.tracks);
            view.setClickable(true);
        }

        // обработка нажатия на элемент списка
        @Override
        public void onClick(View v) {

            // текущая позиция в списке берётся из адаптера
            int position = getAdapterPosition();

            Artist a = artistList.get(position);

            // формирование интента для отправки в DetailActivity
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("coverBig", a.getCoverBigUrl());

            // genres, albums, tracks и name прошли предварительную обработку
            // перед показом в списке в onBindViewHolder. Их значения забираются из (TextView)
            TextView genres = (TextView) v.findViewById(R.id.genres);
            intent.putExtra("genres", genres.getText().toString());

            TextView albums = (TextView) v.findViewById(R.id.albums);
            String detAlbums = albums.getText().toString();

            // замена разделителя между альбомами и треками перед отправкой в DetailActivity
            detAlbums = detAlbums.replace(", "," • ");
            intent.putExtra("albums", detAlbums);

            TextView tracks = (TextView) v.findViewById(R.id.tracks);
            intent.putExtra("tracks", tracks.getText().toString());

            TextView name = (TextView) v.findViewById(R.id.name);
            intent.putExtra("name", name.getText().toString());

            // описание проходит через декодер для исправления кодировки и корректного отображения русского языка
            String descDecoded = stringDecoder(a.getDescription());
            String descCap = descDecoded.substring(0,1).toUpperCase() + descDecoded.substring(1);
            intent.putExtra("description", descCap);

            intent.putExtra("link",a.getLink());

            // отображение выбранной позиции и исполнителя в логе
            Log.d("Position: ", Integer.toString(position));
            Log.d("Artist: ", name.getText().toString());
            mContext.startActivity(intent);

            }
    }

    public void clearAdapter () {
        artistList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != artistList ? artistList.size() : 0);
    }

    // метод исправляет кодировку, используется при обработке названий (name) и описаний (description)
    public String stringDecoder(String parsedStr) {
        String decodedStr = "";

        try {
            decodedStr = URLDecoder.decode(URLEncoder.encode(parsedStr, "iso8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  decodedStr;
    }

    // метод исправляет окончания существительных после числительных (albums) и (tracks) ("альбом", "альбомов", и т.д.)
    // три возможных окончания должны быть инициализированы в списке, который передаётся в Ending при вызове метода
    public String getEnding(int Num, List<String> Ending) {
        String Result, sEnding;
        int iNum, i;
        iNum = Num % 100;
        if (iNum >= 11 && iNum <= 19) {sEnding = Ending.get(2);}
        else {
            i = iNum % 10;
            switch(i){
                case(1): sEnding = Ending.get(0); break;
                case(2):
                case(3):
                case(4): sEnding = Ending.get(1); break;
                default: sEnding = Ending.get(2); break;
            }
        }
        Result = String.valueOf(Num) + " " + sEnding;
        return Result;
    }

}
