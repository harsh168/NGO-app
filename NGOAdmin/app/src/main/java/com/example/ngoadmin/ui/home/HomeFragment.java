package com.example.ngoadmin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ngoadmin.R;
import com.example.ngoadmin.ui.gallery.GalleryFragment;
import com.example.ngoadmin.ui.slideshow.SlideshowFragment;
import com.example.ngoadmin.ui.userregistered.UsersRegistered;

public class HomeFragment extends Fragment implements View.OnClickListener{


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button btedit = root.findViewById(R.id.btEdit);
        Button btadd = root.findViewById(R.id.btAdd);
        Button btuser = root.findViewById(R.id.btUser);
        btedit.setOnClickListener(this);
        btadd.setOnClickListener(this);
        btuser.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btEdit:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new GalleryFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.btAdd:
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                ft1.replace(R.id.nav_host_fragment, new SlideshowFragment());
                ft1.addToBackStack(null);
                ft1.commit();
                break;
            case R.id.btUser:
                FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                ft2.replace(R.id.nav_host_fragment, new UsersRegistered());
                ft2.addToBackStack(null);
                ft2.commit();
                break;
        }

    }
}
