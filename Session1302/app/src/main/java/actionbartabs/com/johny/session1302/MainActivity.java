package actionbartabs.com.johny.session1302;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.DrawableRes;


public class MainActivity extends Activity {

    ActionBar.Tab tab1, tab2;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tab1 = actionBar.newTab();
        tab1.setText("Audio");
        tab1 .setIcon(R.drawable.ic_launcher);
        tab2 = actionBar.newTab();
        tab2.setText("Video");
        tab2 .setIcon(R.drawable.ic_launcher);
        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));


        actionBar.addTab(tab1);
        actionBar.addTab(tab2);

    }
}