package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsActivity extends AppCompatActivity {
    final String TAG = "MyTAG";
    int preLast = -1;
    boolean loadMore = false;
    int requestLimit = 10;

    int lowerBound;
    int upperBound;
    List<Comment> comments;
    CustomAdapter ca;
    Retrofit retrofit;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        lowerBound = Integer.parseInt(intent.getExtras().getString("lowerBound"));
        upperBound = Integer.parseInt(intent.getExtras().getString("upperBound"));

        ca = new CustomAdapter();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getComments();

        ListView listView = (ListView)findViewById(R.id.listView);

        listView.setAdapter(ca);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean scrolled = false;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                scrolled = true;
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (scrolled) {
                    int lastItem = visibleItemCount + firstVisibleItem;

                    if ((lastItem) >= (totalItemCount)) {
                        if (preLast != lastItem) {
                            getComments();

                            scrolled = false;
                            preLast = lastItem;
                        }
                    }
                }
            }
        });

    }

    class CustomAdapter extends BaseAdapter {
        private int count;

        public CustomAdapter(){
            count = 0;
        }

        @Override
        public int getCount() {
            if(comments != null){
                count = comments.size();
            }

            return count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.list_item, null);

            if(count == 0){
                return view;
            }

            TextView postId = (TextView) view.findViewById(R.id.postIdTextView);
            TextView Id = (TextView) view.findViewById(R.id.IdTextView);
            TextView name = (TextView) view.findViewById(R.id.nameTextView);
            TextView email = (TextView) view.findViewById(R.id.emailTextView);
            TextView body = (TextView) view.findViewById(R.id.bodyTextView);

            postId.setText("PostId: " + comments.get(i).getPostId());
            Id.setText("ID: " + comments.get(i).getId());
            name.setText("name: " + comments.get(i).getName());
            email.setText("email:  " + comments.get(i).getEmail());
            body.setText("body: " + comments.get(i).getBody());

            return view;
        }

        public void refresh(){
            getCount();
            notifyDataSetChanged();
        }
    }

    public void getComments(){
        if(upperBound - lowerBound < requestLimit){
            requestLimit = upperBound - lowerBound;
        }else{
            requestLimit = 10;
        }

        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("" + lowerBound, "" + requestLimit);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: Code: " + response.code());
                    return;
                }

                if(comments != null){
                    List<Comment> tmpCommentsList = response.body();

                    for(Comment comment : tmpCommentsList) {
                        comments.add(comment);
                        Log.i(TAG, "onResponse: ID " + comment.getId() + ", Body :" + comment.getBody());
                    }
                }else{
                    comments = response.body();

                    for(Comment comment : comments) {
                        Log.i(TAG, "onResponse: ID " + comment.getId() + ", Body :" + comment.getBody());
                    }
                }

                ca.refresh();

                /*int temp = upperBound + lowerBound;
                lowerBound = upperBound;
                upperBound = temp;*/
                lowerBound += requestLimit;
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
