package com.rockstargames.gtasa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rockstargames.gtasa.GTASA;
import com.rockstargames.gtasa.R;
import com.rockstargames.gtasa.data.SettingPref;
import com.rockstargames.gtasa.data.enums.GtaVersionEnum;
import com.rockstargames.gtasa.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GTASA.class));
            }
        });

        SettingPref pref = SettingPref.getInstance(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GtaVersionEnum.getVersionList());
        binding.dropDownMenuSelectVersion.setAdapter(adapter);
        binding.dropDownMenuSelectVersion.setText(GtaVersionEnum.getVersionList().get(GtaVersionEnum.getIndexByName(pref.getGTASAVersion())), false);
        binding.dropDownMenuSelectVersion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pref.setGTASAVersion(GtaVersionEnum.values()[position].name());
            }
        });


    }
}
