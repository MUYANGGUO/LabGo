package alpha.imsl;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class SignInActivity extends AppCompatActivity {

    ImageButton mCameraButton;
    Button mSignUpButton;
    TextInputEditText mGtid;
    TextView test;

    static final int GTID_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        test = findViewById(R.id.field_show_code);

        mCameraButton = findViewById(R.id.button_camera);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBuzzCard = new Intent(SignInActivity.this, BuzzCardActivity.class);
                toBuzzCard.putExtra("caller", R.integer.FROM_CAMERA_BUTTON);
                startActivityForResult(toBuzzCard, GTID_REQUEST);
            }
        });

        mSignUpButton = findViewById(R.id.button_sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBuzzCard = new Intent(SignInActivity.this, QrCodeActivity.class);
                toBuzzCard.putExtra("caller", R.integer.FROM_SIGN_UP_BUTTON);
                startActivity(toBuzzCard);
            }
        });
    }

    /* add click event to the sign up button */
    public void scanCode (View v) {
        Intent intent = new Intent(this, QrCodeActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GTID_REQUEST) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode qrCode =  data.getParcelableExtra("qrCode");
                    test.setText(qrCode.displayValue);
                } else {
                    test.setText("00");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
