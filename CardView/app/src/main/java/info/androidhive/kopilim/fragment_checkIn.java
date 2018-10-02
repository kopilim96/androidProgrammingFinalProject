package info.androidhive.kopilim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class fragment_checkIn extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView dateAndTime;
    private Looper looper;

    SurfaceView cameraView;
    TextView textResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_checkIn() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static fragment_checkIn newInstance(String param1, String param2) {
        fragment_checkIn fragment = new fragment_checkIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_check_in, container, false);

        final Thread t = new Thread(){
            @Override
            public void run(){
                //Looper.prepare();
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                long date = System.currentTimeMillis();
                                SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateString  = adf.format(date);
                                dateAndTime = (TextView)v.findViewById(R.id.checkInTime);
                                dateAndTime.setText(dateString);
                            }
                        });
                    }//End of While

                }catch(InterruptedException e){

                }
            }
        };
        t.start();

        cameraView = (SurfaceView)v.findViewById(R.id.cameraView);
        textResult = (TextView)v.findViewById(R.id.textView);

        barcodeDetector = new BarcodeDetector.Builder(v.getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(v.getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 640).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {

                    textResult.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create vibrate
                            Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textResult.setText(qrcodes.valueAt(0).displayValue);
                            showResultDialogue(qrcodes.valueAt(0).displayValue);
                            t.interrupt();


                        }
                    });

                }//End if Statement
            }
        });

        return v;
    }

    /***************************************************************************************

                        Display Message from Scanner


     ***********************************************************************************8***/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //show dialogue with result
                showResultDialogue(result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //method to construct dialogue with scan results
    public void showResultDialogue(final String result) {

        final AlertDialog.Builder builder;
        final AlertDialog.Builder builder1;
        final AlertDialog.Builder builder2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            builder1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            builder2 = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);

        } else {

            builder = new AlertDialog.Builder(getContext());
            builder1 = new AlertDialog.Builder(getContext());
            builder2 = new AlertDialog.Builder(getContext());
        }

        //If the text of QR Code is success then Check-in Successful.
        if(result.equalsIgnoreCase("success")){

            //Stop the scanner
            barcodeDetector.release();

            builder.setTitle("Check In")
                    .setMessage("Check In : "+result)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*
                                        After OK message click. display Map
                             */
                            builder1.setTitle("Working Area")
                                    .setMessage("Loading Your Working Area....")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Random random = new Random();
                                            int r = random.nextInt(6 - 1 ) +1;
                                            Intent intent = null;
                                            switch(r){
                                                case 1:
                                                    //UUM Library
                                                    intent = new Intent(getActivity(), MapsActivity.class);
                                                    intent.putExtra("latt",6.46284);
                                                    intent.putExtra("longt", 100.5054398);
                                                    startActivity(intent);
                                                    break;
                                                case 2:
                                                    //UUM COB
                                                    intent = new Intent(getActivity(), MapsActivity.class);
                                                    intent.putExtra("latt",6.4635654);
                                                    intent.putExtra("longt", 100.5069532);
                                                    startActivity(intent);
                                                    break;
                                                case 3:
                                                    //UUM CAS
                                                    intent = new Intent(getActivity(), MapsActivity.class);
                                                    intent.putExtra("latt",6.4678949);
                                                    intent.putExtra("longt", 100.5075618);
                                                    startActivity(intent);
                                                    break;
                                                case 4:
                                                    //UUM COLGIS
                                                    intent = new Intent(getActivity(), MapsActivity.class);
                                                    intent.putExtra("latt",6.4578851);
                                                    intent.putExtra("longt", 100.5072477);
                                                    startActivity(intent);
                                                    break;
                                                case 5:
                                                    //UUM U-assist
                                                    intent = new Intent(getActivity(), MapsActivity.class);
                                                    intent.putExtra("latt",6.461182);
                                                    intent.putExtra("longt", 100.5051364);
                                                    startActivity(intent);
                                                    break;

                                            }//End of Switch
                                        }//End 2nd OnClick
                                    }).show();

                        }//End 1st OnClick
                    }).show();
        }else{
            barcodeDetector.release();
            builder2.setTitle("Check In Fail")
                    .setMessage("Please Scan Again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    //------------------------------------------------------------------------------
    //
    //                  Below are System Auto Generated Method
    //
    //------------------------------------------------------------------------------


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}// End of Class
