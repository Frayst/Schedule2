package rido.schedule2;

import  android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import rido.schedule2.Interface.ItemClickListener;
import rido.schedule2.Model.Category;
import rido.schedule2.ViewHolder.MenuViewHolder;


public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

      private DrawerLayout mDrawer;

    private Toolbar toolbar;

    private NavigationView nvDrawer;

    public Boolean isAdmin;

    TextView txtFullName, txtEmail;
    ImageView imgAvatar;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference category;

    RecyclerView recyler_menu;
    RecyclerView.LayoutManager layoutManager;
    StorageReference mStorage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

                //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        mStorage = FirebaseStorage.getInstance().getReference();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        imgAvatar = (ImageView)headerView.findViewById(R.id.imgProfile);
        //Set User name
        auth = FirebaseAuth.getInstance();

        MobileAds.initialize(this, "ca-app-pub-7265545828484759~2384016923");

        if(Objects.equals(auth.getCurrentUser().getUid(), "dbBC8NzHGuhNFm0U80q86xkfTff1")){
            isAdmin = Boolean.TRUE;
            Toast.makeText(this, "You are Administrator!", Toast.LENGTH_LONG).show();
        } else if (Objects.equals(auth.getCurrentUser().getUid(), "")){
            isAdmin = Boolean.TRUE;
        }

        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        if(auth.getCurrentUser() != null)
        {
            if(!Objects.equals(auth.getCurrentUser().getDisplayName(), "")) {
                txtFullName.setText(auth.getCurrentUser().getDisplayName());
            } else {
                txtFullName.setText(auth.getCurrentUser().getEmail());
            }

        } else {
            startActivity(new Intent(HomePage.this,Main.class));
        }
        txtFullName.setOnClickListener(this);

        txtEmail = (TextView)headerView.findViewById(R.id.txtEmail);
        if(auth.getCurrentUser() != null)
        {
            txtEmail.setText(auth.getCurrentUser().getEmail());
        }

        //Load menu
        recyler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recyler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyler_menu.setLayoutManager(layoutManager);

        loadMenu();

        //Set User Image TODO

        StorageReference load = mStorage.child("users").child(auth.getCurrentUser().getUid()).child("Avatar").child("avatar.png");

        load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(HomePage.this).load(uri.toString()).into(imgAvatar);
            }
        });

    }




    private void loadMenu() {

        FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomePage.this, ""+clickItem, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyler_menu.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent homeIntent = new Intent (HomePage.this,HomePage.class);
            startActivity(homeIntent);

        } else if (id == R.id.nav_schedule) {
            Intent Schedule = new Intent (HomePage.this,Schedule.class);
            startActivity(Schedule);

        } else if (id == R.id.nav_homeworks) {

        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_maps) {
           Intent Maps = new Intent (HomePage.this,Maps.class);
            startActivity(Maps);

        } else if (id == R.id.nav_cribs) {

        } else if (id == R.id.nav_walks) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_signout) {
            auth.signOut();
            startActivity(new Intent(HomePage.this,Main.class));
           finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txtFullName){
            startActivity(new Intent(HomePage.this,Profile.class));
        }
    }

}
