package com.wgm.scaneqinfo.util;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import com.wgm.scaneqinfo.R;

public class LoadingDialog extends Dialog {

	private View loading_layout;
	private ImageView iv_loading;
	private Context context;

	public LoadingDialog(Context context) {
		this(context, R.style.LoadingDialog);
		this.context = context;
	}

	public LoadingDialog(Context context, int themeResId) {
		super(context, themeResId);
		init();
	}

	private void init() {
		loading_layout = View.inflate(getContext(), R.layout.loading_layout, null);
		iv_loading = (ImageView) loading_layout.findViewById(R.id.iv_loading);
		this.setCancelable(false);
		this.setContentView(loading_layout);
	}

	@Override
	public void show() {
		if (iv_loading != null) {
			iv_loading.setBackgroundResource(R.drawable.load_list);
			AnimationDrawable add = (AnimationDrawable) iv_loading.getBackground();
			if (add != null && !add.isRunning()) {
				add.start();
			}
		}
		super.show();
	}

	/**
	 * 加载数据完成播放的动画
	 */
	public void success(final CallBack cb) {
		if (iv_loading != null) {
			iv_loading.setBackgroundResource(R.drawable.success_list);
			AnimationDrawable ad = (AnimationDrawable) iv_loading.getBackground();
			if (ad != null && !ad.isRunning()) {
				ad.start();
				iv_loading.postDelayed(new Runnable() {

					@Override
					public void run() {
						if(cb != null)
							cb.callback();
						dismiss();
					}
				}, 20 * 100);// 这里的时间是由animation-list计算出来的
			}
		}
	}

	public interface CallBack{
		public void callback();
	}

}
