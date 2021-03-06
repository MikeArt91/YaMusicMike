# YaMusicMike
#### Test task for Yandex Summer School 2016

APK is availiable for download via that link:
https://www.dropbox.com/s/7v5ug4968zdaptz/YaMusicMike.apk?dl=0

Task Description: Make Android application using Java. Parse JSON data from provided source. Base view should contain 2 screens:
1) List of Artists. 2) Detail information. Any additional libraries are allowed.

##### UPD: This app was approved by Yandex

* * *

#### Application contains 3 acivities:

- ListActivity contains list of artists with some further details (based on RecyclerView)
- DetailActivity provides description of the Artist and a FloatingButton which leads to WebViewActivity
- WebViewActivity will show artist's personal page on the Internet (if there is one)

<p align="center">
  <img src="/screenshots/ListActivity.png" alt="ListActivity" width="250"/>
  <img src="/screenshots/DetailActivity.png" alt="DetailActivity" width="250"/>
  <img src="/screenshots/WebViewActivity.png" alt="WebViewActivity" width="250"/>
</p>

#####Features:
* Parsing JSON using Volley library 

Volley library is used to parse JSON. A singleton pattern implemented to set up a Request Queue. I choose Volley instead of other options (retrofit + picaso etc.) cause it supports nice caching, network images out of the box.
And ammount of data to catch from JSON is really not that big. Volley handles it quite well.
* Sorting. 

List of artists derived from JSON is mixed. Sorting was done using Collection.sort ignoring the case of the letters.
* WebView

Original task desription never mentioned creating a WebView, but JSON contains links to the personal pages of the most of artists (not all of them). So I decided to make simple WebView with a ProgressBar.
* Text data modifications

Fixed encoding of a text data. The endings of nouns after numerals are corrected. First letter of description was capitalized (originally it wasn't ¯\_(ツ)_/¯)
* Errors handling

Several checks: network connection , availability of the JSON,  presence of artist's personal site.
Toast messages are shown if something is wrong.

* Design

Divider in RecyclerView provided by DividerItemDecoration.class. Collapsing toolbar layout in DetailActivity. It just looks nice :-) 

#####Additional JAVA classes:

* YaRecyclerAdapter.class custom adapter and ViewHolder with onClick action handling.
* YaSingleton.class provides RequestQueue and ImageLoader functionality.
* LruBitmapCache.class extends the LruCache class and implements the ImageLoader.ImageCache interface.
* CheckNetwork.class to check network state (obviously).
* DividerItemDecoration.class adds a divider between items in RecyclerView.

***




