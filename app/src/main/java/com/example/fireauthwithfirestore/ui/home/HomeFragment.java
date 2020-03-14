package com.example.fireauthwithfirestore.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.fireauthwithfirestore.R;

public class HomeFragment extends Fragment implements View.OnClickListener{


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Button btAddNGo=getActivity().findViewById(R.id.btAddNGO);
        final Button btEditNGO=getActivity().findViewById(R.id.btEditNGO);
        final Button btAddEvent=getActivity().findViewById(R.id.btAddEvent);
        final Button btViewUsers=getActivity().findViewById(R.id.btViewUsers);

        return root;
    }

    @Override
    public void onClick(View v) {

    }
}
