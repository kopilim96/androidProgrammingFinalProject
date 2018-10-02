package info.androidhive.kopilim;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_signout extends Fragment {
    private FirebaseAuth fire;

    public fragment_signout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signout, container, false);
        fire = FirebaseAuth.getInstance();
        Toast.makeText(getContext(),"Sign Out Successful", Toast.LENGTH_SHORT).show();
        fire.signOut();
        getActivity().finish();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        return v;
    }

}
