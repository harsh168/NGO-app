package com.example.fireauthwithfirestore.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.fireauthwithfirestore.R;

public class AddNGOFrafment extends Fragment  {
String NGOType="";


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final EditText etngoname=getActivity().findViewById(R.id.etNGOname);
        final EditText etNgoemail=getActivity().findViewById(R.id.etNgoemail);
        final EditText etMobile=getActivity().findViewById(R.id.etMobile);
        final EditText etAdd=getActivity().findViewById(R.id.etAdd);
        final EditText etCity=getActivity().findViewById(R.id.etCity);
        final EditText etState=getActivity().findViewById(R.id.etState);
        final EditText etPin=getActivity().findViewById(R.id.etPin);
        final EditText etType=getActivity().findViewById(R.id.etType);
        final EditText etDescription=getActivity().findViewById(R.id.etDescription);

final Spinner spinner=getActivity().findViewById(R.id.etType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Ngo_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        NGOType=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
        return root;
    }


}
