package sachin.dev.contactsredesign.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class ExpandHeightAnimation extends Animation {
    private int initialHeight,finalHeight;
    private View view;
    public ExpandHeightAnimation(View view, int finalHeight,int initialHeight){
        this.initialHeight=initialHeight;
        this.finalHeight=finalHeight;
        this.view=view;
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //int newHeight;

        //newHeight = (int) (initialHeight * interpolatedTime);

        //ril1.removeAllViews();
        //btn.setWidth(100);
        //btn.setHeight(300);
        //btn.setText("as");
        //ril1.addView(btn);
        //ril1.getLayoutParams().height = newHeight;
        //ril1.requestLayout();
        int newHeight = (int) (initialHeight + finalHeight * interpolatedTime);
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();

        //super.applyTransformation(interpolatedTime, t);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
