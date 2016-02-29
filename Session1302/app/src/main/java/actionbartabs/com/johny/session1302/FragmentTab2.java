package actionbartabs.com.johny.session1302;

/**
 * Created by Johny on 20-01-2016.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;

public class FragmentTab2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video, container, false);
        TextView textview = (TextView) view.findViewById(R.id.videotxtview);
        textview.setText("This is Video Fragment Sample");
        return view;
    }
}