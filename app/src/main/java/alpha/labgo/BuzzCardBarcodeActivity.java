package alpha.labgo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BuzzCardBarcodeActivity extends BaseActivity {

    private static final int PERMISSION_REQUESTS = 2;
    private static final String TAG = "BuzzCardBarcodeActivity";

    private SurfaceView mBuzzCardPreview;
    private int caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz_card_barcode);

        mBuzzCardPreview = findViewById(R.id.buzz_card_preview);

        // check the caller
        caller = getIntent().getIntExtra("caller", 0);

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
            createCameraSource();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check the caller
        caller = getIntent().getIntExtra("caller", 0);
        createCameraSource();
    }

    private void createCameraSource() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(BuzzCardBarcodeActivity.this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 900)
                .build();

        mBuzzCardPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(BuzzCardBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cameraSource.start(mBuzzCardPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    Intent intent;

                    // if the caller is alpha.labgo.camera button, just finish current activity and the request
                    // will handle it back to signin activity.
                    // if the caller is signup button, go back to signup activity.
                    switch (caller) {
                        case R.integer.FROM_CAMERA_BUTTON:
                            intent = new Intent(BuzzCardBarcodeActivity.this, SignInActivity.class);
                            setResult(CommonStatusCodes.SUCCESS, intent);
                            intent.putExtra("qrCode", barcodes.valueAt(0));
                            finish();
                            break;
                        default:
                            intent = new Intent(BuzzCardBarcodeActivity.this, SignUpActivity.class);
                            intent.putExtra("qrCode", barcodes.valueAt(0)); // get latest qr code from the array
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
