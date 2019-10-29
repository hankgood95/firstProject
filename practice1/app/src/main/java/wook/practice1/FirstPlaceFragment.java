package wook.practice1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;

public class FirstPlaceFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap Gmap;
    private SupportMapFragment supportMapFragment;
    double wedo;
    double gyeoungdo;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_firstplace, container, false);
        FragmentManager fm = getChildFragmentManager();

        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.googlemap);
        supportMapFragment.getMapAsync(this);


        listView =(ListView) view.findViewById(R.id.firstplace);
        ArrayAdapter<String> firstplace = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sell)
        );
        listView.setAdapter(firstplace);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(listView.getItemAtPosition(position).toString().equals("복권나라")){
                    wedo = 37.3488680207;gyeoungdo = 127.9526655529;
                }
                if(listView.getItemAtPosition(position).toString().equals("복권뱅크")){
                    wedo = 37.3411596325;gyeoungdo = 127.9539403838;
                }
                if(listView.getItemAtPosition(position).toString().equals("황금복권")){
                    wedo = 37.351182142;gyeoungdo = 127.9295174661;
                }
                if(listView.getItemAtPosition(position).toString().equals("복권나라명륜점")){
                    wedo = 37.338505844;gyeoungdo = 127.9514107321;
                }
                if(listView.getItemAtPosition(position).toString().equals("복권세상도영점")){
                    wedo = 37.334280455;gyeoungdo = 127.9439508408;
                }
                if(listView.getItemAtPosition(position).toString().equals("드림로또")){
                    wedo = 37.3492799355;gyeoungdo = 127.9421838952;
                }
                LatLng place = new LatLng(wedo,gyeoungdo);
                Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,17));
            }
        });
        return view;
    }
        public  void onMapReady(GoogleMap googleMap){
            Gmap = googleMap;


            LatLng wonju = new LatLng(37.3420475881,127.9200971232);
            LatLng place1 = new LatLng(37.3488680207,127.9526655529);
            LatLng place2 = new LatLng(37.3411596325,127.9539403838);
            LatLng place3 = new LatLng(37.351182142,127.9295174661);
            LatLng place4 = new LatLng(37.338505844,127.9514107321);
            LatLng place5 = new LatLng(37.334280455,127.9439508408);
            LatLng place6 = new LatLng(37.3492799355,127.9421838952);

            Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(wonju,12));

            Gmap.addMarker(new MarkerOptions().position(place1));
            Gmap.addMarker(new MarkerOptions().position(place2));
            Gmap.addMarker(new MarkerOptions().position(place3));
            Gmap.addMarker(new MarkerOptions().position(place4));
            Gmap.addMarker(new MarkerOptions().position(place5));
            Gmap.addMarker(new MarkerOptions().position(place6));

        }
}

