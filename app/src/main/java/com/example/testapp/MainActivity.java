package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText upperBoundET;
    EditText lowerBoundET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upperBoundET = (EditText) findViewById(R.id.upperbound);
        lowerBoundET = (EditText) findViewById(R.id.lowerbound);
    }

    public void goToCommentsActivity(View view){
        String upperBound = upperBoundET.getText().toString();
        String lowerBound = lowerBoundET.getText().toString();

        if(upperBound.equals("") || lowerBound.equals("")){

            Toast.makeText(this,"Fill all fields please",Toast.LENGTH_SHORT).show();

        }else if(Integer.parseInt(upperBound) < Integer.parseInt(lowerBound)) {

            Toast.makeText(this,"Upper bound can't be less then lower, try again",Toast.LENGTH_SHORT).show();

        }else {
            Intent intent = new Intent(this, CommentsActivity.class);

            intent.putExtra("upperBound",upperBound);
            intent.putExtra("lowerBound",lowerBound);

            this.startActivity(intent);
        }
    }


}
