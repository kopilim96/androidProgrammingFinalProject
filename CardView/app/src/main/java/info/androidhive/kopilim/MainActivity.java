package info.androidhive.kopilim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Information> informationList;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private FirebaseAuth fire;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public boolean doubleBackLable = false;

    private TextView emailHeader, empIdHeader;

    Fragment myFragment = null;
    Class fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        informationList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, informationList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        prepareAlbums();


        //Cover Pic
        try {
            Glide.with(this).load(R.drawable.project1_cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupDrawerContent(navigationView);

        fire = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // intent call to second activity
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    emailHeader = (TextView)findViewById(R.id.textView_in_Nav);
                    emailHeader.setText(email);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        fire.addAuthStateListener(mAuthListener);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.mainPageName));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.project1_agreement,
                R.drawable.project1_contract,
                R.drawable.project1_recruitment,
                R.drawable.project1_trip,};

        Information a = new Information("Agreement", covers[0]);
        informationList.add(a);

        a = new Information("Contract", covers[1]);
        informationList.add(a);

        a = new Information("We need More People", covers[2]);
        informationList.add(a);

        a = new Information("Year-End Trip", covers[3]);
        informationList.add(a);


        adapter.notifyDataSetChanged();
    }

    /*8888888888888888888888888888888888888888888888888888888888888888888888
                        Start of Navigation Bar
     88888888888888888888888888888888888888888888888888888888888888888888*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Handle navigation view item click
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(doubleBackLable){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Exit Method
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }else {
            if (myFragment != null) {

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.remove(myFragment);
                fragmentTransaction.commit();
                myFragment = null;

            } else {
                this.doubleBackLable = true;
                Toast.makeText(getApplicationContext(), "Click BACK again to Exit"
                        , Toast.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackLable = false;
                    }

                }, 2000);
            }
        }

    }

    public void selectedItemDrawer(MenuItem menuItem){

        switch(menuItem.getItemId()){

            case R.id.checkIn:
                fragment = fragment_checkIn.class;
                break;

            case R.id.checkOut:
                fragment = fragment_checkOut.class;
                break;

            case R.id.applyOff:
                fragment = fragment_applyOff.class;
                break;

            case R.id.reportBug:
                fragment = fragment_reportBug.class;
                break;

            case R.id.manageProfile:
                fragment = fragment_manageProfile.class;
                break;

            case R.id.phoneCall:
                fragment = fragment_distress.class;
                break;

            case R.id.logout:
                fragment = fragment_signout.class;
                break;

            default:
                break;
        }


        try{
            myFragment = (Fragment)fragment.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.viewPage, myFragment).addToBackStack(null).commit();


        menuItem.setChecked(true);
        drawer.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedItemDrawer(item);
                return true;
            }
        });
    }

    /*8888888888888888888888888888888888888888888888888888888888888888888888
                        End of Navigation Bar
     88888888888888888888888888888888888888888888888888888888888888888888*/



    /*********************************************************************
     *
     * @                    Left Side Menu Bar
     * @return
     *********************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_reportBug:
                sendEmail();
                break;

            case R.id.action_logout:

                progressDialog();
                fire.signOut();
                dismissDialog();
                Toast.makeText(MainActivity.this, "Logout Successful",
                        Toast.LENGTH_LONG).show();

                //Go back LoginPage
                finish();
                break;

                //Toast.makeText(MainActivity.this,"Action Logout",Toast.LENGTH_LONG).show();

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //--------------------------------
    //      Send Email Report Bug
    //---------------------------------
    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"kopiLimCEOofCompany@kopiMail.com"};
        //String[] CC = {"kopiLim@gmail.com"};
        String[] CC = {emailHeader.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        dismissDialog();

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Sending Email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    /*********************************************************************
     *
     * @                    Left Side Menu Bar
     * @return
     *********************************************************************/


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //---------------------------------
    //      ProgressDialog
    //--------------------------------
    public void progressDialog(){
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Processing...");
        dialog.show();
    }

    public void dismissDialog(){
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.dismiss();
    }

    public String getEmailText(){
        return emailHeader.getText().toString();
    }

}
