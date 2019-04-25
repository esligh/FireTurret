package com.esli.fireturret;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.esli.fireturret.aidl.ITestService;
import com.esli.fireturret.databinding.ActivityMainBinding;
import com.esli.fireturret.service.TestService;
import com.esli.fireturretlib.FireTurret;
import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;
import com.esli.fireturretlib.utils.L;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);

        mBinding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });

        mBinding.testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("name","lisc");
                TurretEvent event = new TurretEvent("123",data);
                FireTurret.getDefault().publish(event);
            }
        });

        FireTurret.getDefault().subscribe(this,"123", new IEventListener() {
            @Override
            public void onEvent(TurretEvent event) {
                if(event != null) {
                    L.d("MainActivity:received msg=>" + event
                            +",thread="+Thread.currentThread().getName());
                    FireTurret.getDefault().unRegisterService(ITestService.class);
                }
            }
        });

        FireTurret.getDefault().registerService(ITestService.class,new TestService());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
