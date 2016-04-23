package com.lol.contacts.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lol.contacts.R;
import com.lol.contacts.fragement.FragmentPhoneList;
import com.lol.contacts.fragement.FragmentViewPage;
import com.lol.contacts.service.IntimacyDegreeService;

public class MainActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity","MainActivity");
        FragmentPhoneList fragmentPhoneList = new FragmentPhoneList();
        FragmentViewPage fragmentViewPage = new FragmentViewPage();

        //移除mainActivity以前存在的fragment将新的fragment加入到当前布局中，将以后的逻辑放在fragment中
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_activitymain_layout,fragmentViewPage);

        //提交事务后才生效
        fragmentTransaction.commit();
        startService(new Intent(this,IntimacyDegreeService.class));

    }
}
