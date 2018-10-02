package info.androidhive.kopilim;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_reportBug extends Fragment {


    public fragment_reportBug() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report_bug, container, false);
        MainActivity activity = (MainActivity)getActivity();
        String email = activity.getEmailText();

        sendEmail(email);
        return v;
    }

    protected void sendEmail(String email) {
        //String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.i("Send email", "");

        String[] TO = {"ITSupport@kopi.mail.com"};
        String[] CC = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Bug");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        //dismissDialog();

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Sending Email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void progressDialog(){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Processing...");
        dialog.show();
    }

    public void dismissDialog(){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.dismiss();
    }

}
