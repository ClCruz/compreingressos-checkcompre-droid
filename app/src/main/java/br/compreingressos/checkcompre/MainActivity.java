package br.compreingressos.checkcompre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private String FILENAME = "user-data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        Intent i = null;

        switch (position){
            case 0:
                i = new Intent(this, LocalActivity.class);
                i.putExtra("idUser", bundle.getString("idUser"));
                break;
            case 1:
                fragment = ScanActivity.newInstance(bundle);
                break;
            case 2:
                fragment = MonitoramentoActivity.newInstance(bundle);
                break;
            case 3:
                fragment = PainelActivity.newInstance(bundle);
                break;
            case 4:
                fragment = BorderoActivity.newInstance(bundle);
                break;
            case 5:
                fragment = CotaActivity.newInstance(bundle);
                break;
            case 6:
                fragment = ConvidadoActivity.newInstance(bundle);
                break;
            case 7:
                fragment = SearchActivity.newInstance(bundle);
                break;
            case 8:
                i = new Intent(this, LoginActivity.class);
                logout();
                break;
            default:
                fragment = ScanActivity.newInstance(bundle);
        }
        if(i == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }else{
            startActivity(i);
        }
    }

    public void logout(){
        SharedPreferences settings = getSharedPreferences(FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("idUser", "0");
        editor.commit();
    }

}
