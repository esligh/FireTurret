//package com.esli.fireturret;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//
//import com.esli.fireturretlib.FireTurret;
//import com.esli.fireturretlib.event.IEventListener;
//import com.esli.fireturretlib.event.TurretEvent;
//import com.esli.fireturretlib.utils.L;
//
//public class ThirdActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_third);
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            Bundle data = new Bundle();
//            data.putString("name","lisc");
//            TurretEvent event = new TurretEvent("123",data);
//            FireTurret.getDefault().publish("123",data);
//            }
//        });
//
//        FireTurret.getDefault().registerTurret();
//
//        FireTurret.getDefault().subscribe(this,"123", new IEventListener() {
//            @Override
//            public void onEvent(Bundle data) {
//                if (data != null) {
//                    L.d("ThirdActivity:received msg=>" + data);
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//}
