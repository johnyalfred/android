package com.custom.list.session402;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String[] Names = new String[] { "Name1",
            "Name2","Name3","Name4","Name5","Name6","Name7" };

    public static final String[] Num = new String[] {
            "PhoneNumber1","PhoneNumber2","PhoneNumber3","PhoneNumber4",
            "PhoneNumber5","PhoneNumber6","PhoneNumber7"};
    ListView listView;
    List<row> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowItems = new ArrayList<row>();
        for (int i = 0; i < Names.length; i++) {
            row item = new row(Names[i], Num[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.listView);
        CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems);
        listView.setAdapter(adapter);
    }
    public class row {
         private String Names;
        private String Num;

        public row(String Names, String Num) {

            this.Names = Names;
            this.Num = Num;
        }

        public String getNum () {
            return Num;
        }
        public void setNum(String Num) {
            this.Num = Num;
        }
        public String getNames() {
            return Names;
        }
        public void setNames(String Names) {
            this.Names = Names;
        }
        @Override
        public String toString() {
            return Names + "\n" + Num;
        }
    }
}
