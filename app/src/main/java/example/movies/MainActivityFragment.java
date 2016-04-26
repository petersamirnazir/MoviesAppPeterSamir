package example.movies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    boolean isConnected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isFavorite;
    public static boolean ispopular;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by_popularity_settings){
            isFavorite=false;
            ispopular=true;
            Log.v("Sort:","popularity called");
            updateMovies("popularity");
            return true;
        }
        else if (id == R.id.sort_by_rating_settings){
            isFavorite=false;
            ispopular=false;
            Log.v("Sort:","rating called");
            updateMovies("vote_average");
            return true;
        }
        else if (id == R.id.show_favorite){
            isFavorite = true;
            ispopular=false;
            updateMovies("favorites");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String[] postersUrls;
    String[] overviews;
    String[] release_dates;
    String[] titles;
    String[] popularities;
    String[] vote_averages;
    String[] ids;
    moviesAdapter mveAdapter;
    GridView gvPosters;

    public MainActivityFragment() {
    }

    public void updateMovies(String sort) {
        if (sort=="favorites"){
            dbHelper dbh = new dbHelper(getActivity());
            ArrayList<String> postersDB = dbh.getAllOf(dbh.COL_IMAGE);
            if (postersDB!=null){
                postersUrls = postersDB.toArray(new String[postersDB.size()]);
                for (int i=0;i<postersUrls.length;i++){
                    Log.v("Posters from DB",postersUrls[i]);
                }
                mveAdapter.newClear(postersUrls);
            }
        }
        else {
            if (isConnected) {
                Toast.makeText(getActivity(), "Loading Movies...", Toast.LENGTH_LONG).show();
                fetchMovieData fmd = new fetchMovieData();
                fmd.execute(sort);
            } else {
                Toast.makeText(getActivity(), "Sorry no internet connection!", Toast.LENGTH_LONG).show();
                ;
            }
        }
    }

    boolean mTwoPane;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        gvPosters = (GridView) rootview.findViewById(R.id.gridView_movies);
        ispopular=true;
        postersUrls = new String[5];
        overviews = new String[5];
        release_dates = new String[5];
        titles = new String[5];
        popularities = new String[5];
        vote_averages = new String[5];
        ids = new String[5];
        mveAdapter = new moviesAdapter(getActivity());
        gvPosters.setAdapter(mveAdapter);
        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        else{
            mTwoPane=false;
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        updateMovies("popularity");
        gvPosters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isFavorite) {
                    if (isConnected) {
                        Log.v("overView Click", overviews[position]);
                        Log.v("release_date Click", release_dates[position]);
                        Log.v("title Click", titles[position]);
                        Log.v("popularity Click", popularities[position]);
                        Log.v("vote_average Click", vote_averages[position]);
                        Log.v("id Click", ids[position]);
                        Log.v("Denisty", String.valueOf(getActivity().getResources().getDisplayMetrics().widthPixels));
                        if (mTwoPane == false) {
                            Log.v("TEST_IF_MAIN_FRAG", String.valueOf(mTwoPane));
                            Intent iDetail = new Intent(getActivity().getBaseContext(), DetailAcivity.class);
                            iDetail.putExtra("image", postersUrls[position]);
                            iDetail.putExtra("overview", overviews[position]);
                            iDetail.putExtra("release_date", release_dates[position]);
                            iDetail.putExtra("title", titles[position]);
                            iDetail.putExtra("popularity", popularities[position]);
                            iDetail.putExtra("vote_average", vote_averages[position]);
                            iDetail.putExtra("id", ids[position]);
                            startActivity(iDetail);
                        } else {
                            Log.v("TEST_IF_MAIN_FRAG", String.valueOf(mTwoPane));
                            Intent iDetail = new Intent(getActivity().getBaseContext(), DetailAcivity.class);
                            iDetail.putExtra("image", postersUrls[position]);
                            iDetail.putExtra("overview", overviews[position]);
                            iDetail.putExtra("release_date", release_dates[position]);
                            iDetail.putExtra("title", titles[position]);
                            iDetail.putExtra("popularity", popularities[position]);
                            iDetail.putExtra("vote_average", vote_averages[position]);
                            iDetail.putExtra("id", ids[position]);
                            iOnMovieSelectionChanged iomsc = (iOnMovieSelectionChanged) getActivity();
                            iomsc.onMovieSelectionChanged(iDetail);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Sorry no internet connection!", Toast.LENGTH_LONG).show();
                        ;
                    }


                }
                else {

                    if (mTwoPane == false) {
                        Log.v("TEST_IF_MAIN_FRAG", String.valueOf(mTwoPane));
                        dbHelper dbh = new dbHelper(getActivity());
                        Cursor c = dbh.getData(position+1);
                        c.moveToFirst();
                        String titleDB = c.getString(c.getColumnIndex(dbh.COL_TITLE));
                        String overviewDB = c.getString(c.getColumnIndex(dbh.COL_OVERVIEW));
                        String release_dateDB = c.getString(c.getColumnIndex(dbh.COL_RELEASE_DATE));
                        String popularityDB = c.getString(c.getColumnIndex(dbh.COL_POPULARITY));
                        String vote_averageDB = c.getString(c.getColumnIndex(dbh.COL_VOTE_AVERAGE));
                        String imageDB = c.getString(c.getColumnIndex(dbh.COL_IMAGE));
                        Intent iDetail = new Intent(getActivity().getBaseContext(), DetailAcivity.class);
                        iDetail.putExtra("image", imageDB);
                        iDetail.putExtra("overview", overviewDB);
                        iDetail.putExtra("release_date", release_dateDB);
                        iDetail.putExtra("title", titleDB);
                        iDetail.putExtra("popularity", popularityDB);
                        iDetail.putExtra("vote_average", vote_averageDB);
                        iDetail.putExtra("id", ids[position]);
//                    Log.v("overView DB", overviewDB);
                        Log.v("release_date DB", release_dateDB);
                        Log.v("title DB", titleDB);
                        Log.v("popularity DB", popularityDB);
                        Log.v("vote_average DB", vote_averageDB);
                        Log.v("image DB", imageDB);
                        startActivity(iDetail);
                    } else {
                        Log.v("TEST_IF_MAIN_FRAG", String.valueOf(mTwoPane));
                        dbHelper dbh = new dbHelper(getActivity());
                        Cursor c = dbh.getData(position+1);
                        c.moveToFirst();
                        String titleDB = c.getString(c.getColumnIndex(dbh.COL_TITLE));
                        String overviewDB = c.getString(c.getColumnIndex(dbh.COL_OVERVIEW));
                        String release_dateDB = c.getString(c.getColumnIndex(dbh.COL_RELEASE_DATE));
                        String popularityDB = c.getString(c.getColumnIndex(dbh.COL_POPULARITY));
                        String vote_averageDB = c.getString(c.getColumnIndex(dbh.COL_VOTE_AVERAGE));
                        String imageDB = c.getString(c.getColumnIndex(dbh.COL_IMAGE));
                        Intent iDetail = new Intent(getActivity().getBaseContext(), DetailAcivity.class);
                        iDetail.putExtra("image", imageDB);
                        iDetail.putExtra("overview", overviewDB);
                        iDetail.putExtra("release_date", release_dateDB);
                        iDetail.putExtra("title", titleDB);
                        iDetail.putExtra("popularity", popularityDB);
                        iDetail.putExtra("vote_average", vote_averageDB);
                        iDetail.putExtra("id", ids[position]);
//                    Log.v("overView DB", overviewDB);
                        Log.v("release_date DB", release_dateDB);
                        Log.v("title DB", titleDB);
                        Log.v("popularity DB", popularityDB);
                        Log.v("vote_average DB", vote_averageDB);
                        Log.v("image DB", imageDB);
                        iOnMovieSelectionChanged iomsc = (iOnMovieSelectionChanged) getActivity();
                        iomsc.onMovieSelectionChanged(iDetail);
                    }

                }
            }
        });
        return rootview;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public class fetchMovieData extends AsyncTask<String, String, String[]> {

        private final String logTag = fetchMovieData.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            //http://api.themoviedb.org/3/movie/popular?api_key
            String baseUrl = "http://api.themoviedb.org/3/discover/movie?";
            String mode = params[0];
            String apiKey = "127d4b489c9593916a095e1dc1a3e684";

            try {
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter("sort_by", mode + ".desc")
                        .appendQueryParameter("api_key", apiKey).build();
                URL url = new URL(builtUri.toString());
                Log.v(logTag, url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream movieInputSt = urlConnection.getInputStream();
                StringBuffer movieStrBuffer = new StringBuffer();
                if (movieInputSt == null) {
                    Log.v(logTag, "input stream equals null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(movieInputSt));
                String buffedLine;
                while ((buffedLine = reader.readLine()) != null) {
                    movieStrBuffer.append(buffedLine + "\n");
                }
                if (movieStrBuffer.length() == 0) {
                    Log.v(logTag, "buffer length is zero");
                    return null;
                }
                movieJsonStr = movieStrBuffer.toString();
                Log.v(logTag, "movies json string is: " + movieJsonStr);
            } catch (MalformedURLException e) {
                Log.v(logTag, "MalformedURLException man!");
                e.printStackTrace();
            } catch (IOException e) {
                Log.v(logTag, "IOException man!");
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.v(logTag, "error with closing the reader");
                        e.printStackTrace();
                    }
                }
            }
            //this is where the calling of the json method
            try {
                return getMoviesFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.v(logTag, "json method not called");
                e.printStackTrace();
                return null;
            }
        }

        public String[] getMoviesFromJson(String moviesJsonStr) throws JSONException {
            JSONObject moviesJsonObjct = new JSONObject(moviesJsonStr);
            JSONArray movieArray = moviesJsonObjct.getJSONArray("results");
            postersUrls = new String[movieArray.length()];
            overviews = new String[movieArray.length()];
            release_dates = new String[movieArray.length()];
            titles = new String[movieArray.length()];
            popularities = new String[movieArray.length()];
            vote_averages = new String[movieArray.length()];
            ids = new String[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieJsonObjct = movieArray.getJSONObject(i);
                postersUrls[i] = "http://image.tmdb.org/t/p/w185" + movieJsonObjct.getString("poster_path");
                overviews[i] =  movieJsonObjct.getString("overview");
                release_dates[i] = movieJsonObjct.getString("release_date");
                titles[i] =  movieJsonObjct.getString("title");
                popularities[i] =  movieJsonObjct.getString("popularity");
                vote_averages[i] = movieJsonObjct.getString("vote_average");
                ids[i] = movieJsonObjct.getString("id");

            }
            Log.v(logTag, "urls for the posters man :" + postersUrls);
            return postersUrls;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            if (strings != null) {
                mveAdapter.newClear(strings);
            }
        }
    }

    public class moviesAdapter extends BaseAdapter {
        String logTag = moviesAdapter.class.getSimpleName();
        Context context;
        List<String> urls = new ArrayList<String>();

        public moviesAdapter(Context context) {
            this.context = context;
            Collections.addAll(urls, postersUrls);
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public String getItem(int position) {
            return urls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView newIv = new ImageView(context);
            Log.v(logTag, "pos Url =" + urls.get(position));
            Picasso.with(context).load(urls.get(position)).placeholder(R.drawable.loading).fit().into(newIv);
            return newIv;
        }

        public void newClear(String[] Strng) {
            urls.clear();
            for (String moviepstr : Strng) {

                urls.add(moviepstr);
                Log.e("URLS", moviepstr);
            }
            this.notifyDataSetChanged();
        }
    }
}
