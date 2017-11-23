package co.ortizol.mediacloudapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import co.ortizol.mediacloudapp.fragments.AnimacionesFragment;
import co.ortizol.mediacloudapp.fragments.AudioFragment;
import co.ortizol.mediacloudapp.fragments.GraficosFragment;
import co.ortizol.mediacloudapp.fragments.ImagenFragment;
import co.ortizol.mediacloudapp.fragments.VideoFragment;

public class MediaCloudActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar tbrPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_cloud);
        tbrPrincipal = (Toolbar) findViewById(R.id.tbrPrincipal);
        setSupportActionBar(tbrPrincipal);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tbrPrincipal, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tbrPrincipal.setSubtitle(getString(R.string.animaciones));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layContenedorPrincipal, new AnimacionesFragment())
                .commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mitAnimaciones) {
            tbrPrincipal.setSubtitle(getString(R.string.animaciones));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layContenedorPrincipal, new AnimacionesFragment())
                    .commit();
        } else if (id == R.id.mitGraficos) {
            tbrPrincipal.setSubtitle(getString(R.string.graficos));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layContenedorPrincipal, new GraficosFragment())
                    .commit();
        } else if (id == R.id.mitImagen) {
            tbrPrincipal.setSubtitle(getString(R.string.imagen));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layContenedorPrincipal, new ImagenFragment())
                    .commit();
        } else if (id == R.id.mitAudio) {
            tbrPrincipal.setSubtitle(getString(R.string.audio));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layContenedorPrincipal, new AudioFragment())
                    .commit();
        } else if (id == R.id.mitVideo) {
            tbrPrincipal.setSubtitle(getString(R.string.video));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layContenedorPrincipal, new VideoFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
