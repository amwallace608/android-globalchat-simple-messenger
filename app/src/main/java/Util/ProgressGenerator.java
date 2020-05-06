package Util;

import android.os.Handler;

import com.dd.processbutton.ProcessButton;

import java.util.Random;

public class ProgressGenerator {

    public interface OnCompleteListener {

        public void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;
//constructor with oncomplete listener
    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }
// recursive runnable with progress increments of 10%, random delay generated between recursive executions
    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, generateDelay());
    }

    private Random random = new Random();
//generate random delay up to 600ms
    private int generateDelay() {
        return random.nextInt(600);
    }
}
