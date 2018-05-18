// Generated code from Butter Knife. Do not modify!
package com.example.melike.uberapp;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PassengerMapsActivity_ViewBinding implements Unbinder {
  private PassengerMapsActivity target;

  private View view2131230887;

  private View view2131230835;

  @UiThread
  public PassengerMapsActivity_ViewBinding(PassengerMapsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PassengerMapsActivity_ViewBinding(final PassengerMapsActivity target, View source) {
    this.target = target;

    View view;
    target.rootFrame = Utils.findRequiredViewAsType(source, R.id.rootFrame, "field 'rootFrame'", FrameLayout.class);
    target.rootll = Utils.findRequiredViewAsType(source, R.id.rootll, "field 'rootll'", LinearLayout.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", ViewPager.class);
    view = Utils.findRequiredView(source, R.id.rlwhere, "field 'rlWhere' and method 'openPlacesView'");
    target.rlWhere = Utils.castView(view, R.id.rlwhere, "field 'rlWhere'", RelativeLayout.class);
    view2131230887 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.openPlacesView();
      }
    });
    view = Utils.findRequiredView(source, R.id.ivHome, "field 'ivHome' and method 'showViewPagerWithTransition'");
    target.ivHome = Utils.castView(view, R.id.ivHome, "field 'ivHome'", ImageView.class);
    view2131230835 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showViewPagerWithTransition();
      }
    });
    target.tvWhereto = Utils.findRequiredViewAsType(source, R.id.tvWhereTo, "field 'tvWhereto'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PassengerMapsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rootFrame = null;
    target.rootll = null;
    target.viewPager = null;
    target.rlWhere = null;
    target.ivHome = null;
    target.tvWhereto = null;

    view2131230887.setOnClickListener(null);
    view2131230887 = null;
    view2131230835.setOnClickListener(null);
    view2131230835 = null;
  }
}
