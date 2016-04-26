package example.movies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailAcivityFragment extends Fragment {

    ImageView ivDetailPoster;
    String ids[];
    String reviews[];
    ArrayAdapter<String> trailersAdapter;
    ArrayAdapter<String> reviewsAdapter;
    boolean mTwoPane;
    ListView lvReviews;
    ListView lvTrailers;
    TextView tvDetailTitle, tvDetailOverview, tvDetailRelease_date, tvDetailVote_average;
    ImageButton ibStar;
    final static String KEY_POSITION = "position";

    public DetailAcivityFragment() {
    }

    Bundle args;
    String imageUrlGot, overviewGot, release_dateGot, titleGot, popularityGot, vote_averageGot,idGot;

//    @Override
//    public void onStart() {
//        super.onStart();
//        args = getArguments();
//        if (args!=null){
//            putDetailData(args);
//        }
//        else {
//            Log.d("Fragment_test","no args received!");
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imageUrl saved", imageUrlGot);
        outState.putString("overview saved", overviewGot);
        outState.putString("releaseDate saved", release_dateGot);
        outState.putString("title saved", titleGot);
        outState.putString("popularity saved", popularityGot);
        outState.putString("vote_average saved", vote_averageGot);
        outState.putString("id saved", idGot);
    }

    View rootView;

    boolean isConnected;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        rootView = inflater.inflate(R.layout.fragment_detail_acivity, container, false);

        ids = new String[5];
        reviews = new String[5];
        trailersAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        reviewsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        lvTrailers = (ListView) rootView.findViewById(R.id.lvTrailers);
        lvTrailers.setAdapter(trailersAdapter);
        lvReviews = (ListView) rootView.findViewById(R.id.lvReviews);
        lvReviews.setAdapter(reviewsAdapter);


        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            mTwoPane=false;
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (isConnected || MainActivityFragment.isFavorite) {


            if (mTwoPane == false ) {
                Log.v("TEST_IF", "mTwoPane in detail is false");
                if (MainActivityFragment.isFavorite){
                    if (getActivity().getIntent().getExtras() != null) {
                        imageUrlGot = getActivity().getIntent().getStringExtra("image");
                        overviewGot = getActivity().getIntent().getStringExtra("overview");
                        release_dateGot = getActivity().getIntent().getStringExtra("release_date");
                        titleGot = getActivity().getIntent().getStringExtra("title");
                        popularityGot = getActivity().getIntent().getStringExtra("popularity");
                        vote_averageGot = getActivity().getIntent().getStringExtra("vote_average");
                        idGot = getActivity().getIntent().getStringExtra("id");
                        Log.v("overView OnePane", imageUrlGot);
//                        Log.v("release_date OnePane", overviewGot);
                        Log.v("title OnePane", release_dateGot);
                        Log.v("popularity OnePane", titleGot);
                        Log.v("vote_average OnePane", popularityGot);
                        Log.v("vote_average OnePane", vote_averageGot);
                        ivDetailPoster = (ImageView) rootView.findViewById(R.id.ivDetailPoster);
                        tvDetailTitle = (TextView) rootView.findViewById(R.id.tvDetailName);
                        tvDetailOverview = (TextView) rootView.findViewById(R.id.tvDetailOverview);
                        tvDetailRelease_date = (TextView) rootView.findViewById(R.id.tvDetailReleaseDate);
                        tvDetailVote_average = (TextView) rootView.findViewById(R.id.tvDetailVote_average);
                        Picasso.with(getActivity()).load(imageUrlGot).placeholder(R.drawable.loading).into(ivDetailPoster);
                        tvDetailTitle.setText(titleGot);
                        tvDetailRelease_date.setText(release_dateGot);
                        tvDetailVote_average.setText(vote_averageGot + "/10");
                        tvDetailOverview.setText(overviewGot);
                    } else if (getActivity().getIntent().getExtras() == null) {
                        Log.v("TEST_IF", "intent extras are null");
                    }
                }
                else if (getActivity().getIntent().getExtras() != null) {
                    imageUrlGot = getActivity().getIntent().getStringExtra("image");
                    overviewGot = getActivity().getIntent().getStringExtra("overview");
                    release_dateGot = getActivity().getIntent().getStringExtra("release_date");
                    titleGot = getActivity().getIntent().getStringExtra("title");
                    popularityGot = getActivity().getIntent().getStringExtra("popularity");
                    vote_averageGot = getActivity().getIntent().getStringExtra("vote_average");
                    idGot = getActivity().getIntent().getStringExtra("id");
                    Log.v("overView OnePane", imageUrlGot);
                    Log.v("release_date OnePane", overviewGot);
                    Log.v("title OnePane", release_dateGot);
                    Log.v("popularity OnePane", titleGot);
                    Log.v("vote_average OnePane", popularityGot);
                    Log.v("vote_average OnePane", vote_averageGot);
                    ivDetailPoster = (ImageView) rootView.findViewById(R.id.ivDetailPoster);
                    tvDetailTitle = (TextView) rootView.findViewById(R.id.tvDetailName);
                    tvDetailOverview = (TextView) rootView.findViewById(R.id.tvDetailOverview);
                    tvDetailRelease_date = (TextView) rootView.findViewById(R.id.tvDetailReleaseDate);
                    tvDetailVote_average = (TextView) rootView.findViewById(R.id.tvDetailVote_average);
                    Picasso.with(getActivity()).load(imageUrlGot).placeholder(R.drawable.loading).into(ivDetailPoster);
                    tvDetailTitle.setText(titleGot);
                    tvDetailRelease_date.setText(release_dateGot);
                    tvDetailVote_average.setText(vote_averageGot + "/10");
                    tvDetailOverview.setText(overviewGot);
                } else if (getActivity().getIntent().getExtras() == null) {
                    Log.v("TEST_IF", "intent extras are null");
                }
            } else {
                Log.v("TEST_IF", "mTwoPane in detail is true");
                if (MainActivityFragment.isFavorite){
                        imageUrlGot = getArguments().getString("image");
                        overviewGot = getArguments().getString("overview");
                        release_dateGot = getArguments().getString("release_date");
                        titleGot = getArguments().getString("title");
                        popularityGot = getArguments().getString("popularity");
                        vote_averageGot = getArguments().getString("vote_average");
                    idGot = getArguments().getString("id");
                    Log.v("overView OnePane", imageUrlGot);
                        Log.v("release_date OnePane", overviewGot);
                        Log.v("title OnePane", release_dateGot);
                        Log.v("popularity OnePane", titleGot);
                        Log.v("vote_average OnePane", popularityGot);
                        Log.v("vote_average OnePane", vote_averageGot);
                        ivDetailPoster = (ImageView) rootView.findViewById(R.id.ivDetailPoster);
                        tvDetailTitle = (TextView) rootView.findViewById(R.id.tvDetailName);
                        tvDetailOverview = (TextView) rootView.findViewById(R.id.tvDetailOverview);
                        tvDetailRelease_date = (TextView) rootView.findViewById(R.id.tvDetailReleaseDate);
                        tvDetailVote_average = (TextView) rootView.findViewById(R.id.tvDetailVote_average);
                        Picasso.with(getActivity()).load(imageUrlGot).placeholder(R.drawable.loading).into(ivDetailPoster);
                        tvDetailTitle.setText(titleGot);
                        tvDetailRelease_date.setText(release_dateGot);
                        tvDetailVote_average.setText(vote_averageGot + "/10");
                        tvDetailOverview.setText(overviewGot);
                }
                else {
                    ivDetailPoster = (ImageView) rootView.findViewById(R.id.ivDetailPoster);
                    tvDetailTitle = (TextView) rootView.findViewById(R.id.tvDetailName);
                    tvDetailOverview = (TextView) rootView.findViewById(R.id.tvDetailOverview);
                    tvDetailRelease_date = (TextView) rootView.findViewById(R.id.tvDetailReleaseDate);
                    tvDetailVote_average = (TextView) rootView.findViewById(R.id.tvDetailVote_average);
                    if (getArguments() == null) {
                        tvDetailTitle.setText("TESTING fragment");
                        tvDetailRelease_date.setText("TESTING fragment");
                        tvDetailVote_average.setText("TESTING fragment");
                        tvDetailOverview.setText("TESTING fragment");
                        Log.v("TEST_IF", "getArguments is null");
                    } else {
                        Log.v("TEST_IF", "getArguments isn't null");
                        imageUrlGot = getArguments().getString("image");
                        overviewGot = getArguments().getString("overview");
                        release_dateGot = getArguments().getString("release_date");
                        titleGot = getArguments().getString("title");
                        popularityGot = getArguments().getString("popularity");
                        vote_averageGot = getArguments().getString("vote_average");
                        idGot = getArguments().getString("id");
                        Log.v("imageUrl TwoPane", imageUrlGot);
                        Log.v("overview TwoPane", overviewGot);
                        Log.v("releaseDate TwoPane", release_dateGot);
                        Log.v("title TwoPane", titleGot);
                        Log.v("popularity TwoPane", popularityGot);
                        Log.v("vote_average TwoPane", vote_averageGot);
                        Picasso.with(getActivity()).load(imageUrlGot).into(ivDetailPoster);
                        tvDetailTitle.setText(titleGot);
                        tvDetailRelease_date.setText(release_dateGot);
                        tvDetailVote_average.setText(vote_averageGot + "/10");
                        tvDetailOverview.setText(overviewGot);
                    }
                }

            }
            if(isConnected) {
                if (MainActivityFragment.ispopular||MainActivityFragment.isFavorite) {
                    fetchMovieTrailers fmt = new fetchMovieTrailers();
                    fmt.execute(idGot);
                    fetchMovieReviews fmr = new fetchMovieReviews();
                    fmr.execute(idGot);
                }
            }
            lvTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String youtubeUrl = "https://www.youtube.com/watch?v="+ids[position];
                    Log.v("youtube",youtubeUrl);
                    Intent iYoutube = new Intent(Intent.ACTION_VIEW,Uri.parse(youtubeUrl));
                    startActivity(iYoutube);
                }
            });
            lvReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.v("testList","review list clicked");
                }
            });
            if (savedInstanceState != null) {
                Log.v("TEST_IF", "SavedInstance isn't null");
                imageUrlGot = savedInstanceState.getString("imageUrl saved");
                overviewGot = savedInstanceState.getString("overview saved");
                release_dateGot = savedInstanceState.getString("releaseDate saved");
                titleGot = savedInstanceState.getString("title saved");
                popularityGot = savedInstanceState.getString("popularity saved");
                vote_averageGot = savedInstanceState.getString("vote_average saved");
                idGot = savedInstanceState.getString("id saved");
                Picasso.with(getActivity()).load(imageUrlGot).into(ivDetailPoster);
                tvDetailTitle.setText(titleGot);
                tvDetailRelease_date.setText(release_dateGot);
                tvDetailVote_average.setText(vote_averageGot + "/10");
                tvDetailOverview.setText(overviewGot);
            }
            ibStar = (ImageButton) rootView.findViewById(R.id.ibStar);
            if (MainActivityFragment.isFavorite){
                ibStar.setBackgroundResource(R.drawable.star_on);
            }
            ibStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!MainActivityFragment.isFavorite) {
                        dbHelper dbh = new dbHelper(getActivity());
                        dbh.insertMovie(titleGot, imageUrlGot, release_dateGot, popularityGot, vote_averageGot, overviewGot);
                        Log.v("DATABASE", "ADDED " + titleGot);
                        ibStar.setBackgroundResource(R.drawable.star_on);
                    }
                    else {
                        Toast.makeText(getActivity(),"this movie is already a favorite!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(getActivity(),"Sorry no internet connection!",Toast.LENGTH_LONG).show();
        }
        return rootView;

    }

    public class fetchMovieTrailers extends AsyncTask<String, String, String[]> {

        private final String logTag = getActivity().getClass().getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            //http://api.themoviedb.org/3/movie/popular?api_key
            String key = params[0];
            String baseUrl = "http://api.themoviedb.org/3/movie/"+key+"/videos?";
            String apiKey = "127d4b489c9593916a095e1dc1a3e684";

            try {
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
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
            ids = new String[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieJsonObjct = movieArray.getJSONObject(i);
                ids[i] = movieJsonObjct.getString("key");

            }
            Log.v(logTag, "urls for the trailers ids man :" + ids);
            return ids;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            if (strings != null) {
                trailersAdapter.addAll(strings);
                Log.v("testAdapter",trailersAdapter.getItem(0));
            }
        }
    }

    public class fetchMovieReviews extends AsyncTask<String, String, String[]> {

        private final String logTag = getActivity().getClass().getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            //http://api.themoviedb.org/3/movie/popular?api_key
            String key = params[0];
            String baseUrl = "http://api.themoviedb.org/3/movie/"+key+"/reviews?";
            String apiKey = "127d4b489c9593916a095e1dc1a3e684";

            try {
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
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
            reviews = new String[movieArray.length()*2];
            int j=-1;
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieJsonObjct = movieArray.getJSONObject(i);
                reviews[++j] = movieJsonObjct.getString("author");
                reviews[++j] = movieJsonObjct.getString("content");
            }

            Log.v(logTag, "urls for the trailers ids man :" + ids);
            return reviews;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            if (strings != null) {
                reviewsAdapter.addAll(strings);
            }
            reviewsAdapter.notifyDataSetChanged();
            Log.v("reviewAdapterSize", String.valueOf(reviewsAdapter.getCount()));
        }
    }

//            if(rootView.findViewById(R.id.fragmentDetail)!=null) {
//            }
//            else {
//                    String imageUrlGot = getActivity().getIntent().getStringExtra("image");
//                    String overviewGot = getActivity().getIntent().getStringExtra("overview");
//                    String release_dateGot = getActivity().getIntent().getStringExtra("release_date");
//                    String titleGot = getActivity().getIntent().getStringExtra("title");
//                    String popularityGot = getActivity().getIntent().getStringExtra("popularity");
//                    String vote_averageGot = getActivity().getIntent().getStringExtra("vote_average");
//                    Log.v("overView log", imageUrlGot);
//                    Log.v("release_date log", overviewGot);
//                    Log.v("title log", release_dateGot);
//                    Log.v("popularity log", titleGot);
//                    Log.v("vote_average log", popularityGot);
//                    Log.v("vote_average log", vote_averageGot);
//                    ivDetailPoster = (ImageView) rootView.findViewById(R.id.ivDetailPoster);
//                    tvDetailTitle = (TextView) rootView.findViewById(R.id.tvDetailName);
//                    tvDetailOverview = (TextView) rootView.findViewById(R.id.tvDetailOverview);
//                    tvDetailRelease_date = (TextView) rootView.findViewById(R.id.tvDetailReleaseDate);
//                    tvDetailVote_average = (TextView) rootView.findViewById(R.id.tvDetailVote_average);
//                    Picasso.with(getActivity()).load(imageUrlGot).placeholder(R.drawable.loading).into(ivDetailPoster);
//                    tvDetailTitle.setText(titleGot);
//                    tvDetailRelease_date.setText(release_dateGot);
//                    tvDetailVote_average.setText(vote_averageGot + "/10");
//                    tvDetailOverview.setText(overviewGot);
//                    return rootView;

//            }

}
