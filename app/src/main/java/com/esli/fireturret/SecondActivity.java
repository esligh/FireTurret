package com.esli.fireturret;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.esli.fireturret.aidl.ITestService;
import com.esli.fireturret.databinding.ActivitySecondBinding;
import com.esli.fireturret.service.TestService;
import com.esli.fireturretlib.FireTurret;
import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;
import com.esli.fireturretlib.utils.L;

public class SecondActivity extends AppCompatActivity {
    ActivitySecondBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_second);
        mBinding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//           startActivity(new Intent(SecondActivity.this,ThirdActivity.class));
            }
        });

        mBinding.bindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Bundle data = new Bundle();
            data.putString("name","lisc");
            TurretEvent event = new TurretEvent("123",data);
            FireTurret.getDefault().publish(event);
            }
        });

        mBinding.unbindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            IBinder ibinder = FireTurret.getDefault().getService(ITestService.class);
            if (ibinder != null) {
                ITestService testService = TestService.Stub.asInterface(ibinder);
                if (testService != null) {
                    try {
                        Toast.makeText(SecondActivity.this,testService.getName(),
                                Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            }
        });

        FireTurret.getDefault().subscribe(this,"123", new IEventListener() {
            @Override
            public void onEvent(TurretEvent data) {
                if (data != null) {
                    L.d("SecondActivity:received msg=>" + data
                            +",thread="+Thread.currentThread().getName());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FireTurret.getDefault().unregisterTurret();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
