package com.intent.list.session503;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
public class MainActivity extends Activity {

    ListView courseListView;


    String[] Courses = {
            "Android Development",
            "Front End Web Development",
            "Full Stack Web Development",
            "Big Data & Hadoop Development",
            "Node JS",
            "Java",
            "Robotics",
            "Cloud Computing",
            "Digital Marketing",
            "Machine Learning With R",
            "Business Analytics With R"};

    String[] Mentor = {
            "Indraneel",
            "Mentor Of Front End",
            "Mentor Of Stack Web",
            "Mentor Of Big Data",
            "Mentor Of Node JS",
            "Mentor Of Java",
            "Mentor Of Robotics",
            "Mentor Of Cloud Computing",
            "Mentor Of Digital Marketing",
            "Mentor Of Machine Learning With R",
            "Mentor Of Business Analytics With R"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseListView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Courses);
        courseListView.setAdapter(Adapter);

        courseListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> MainActivity, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, Mentor.class);
                        i.putExtra("key", Mentor[position]);
                        startActivity(i);
                    }
                });


    }
}


