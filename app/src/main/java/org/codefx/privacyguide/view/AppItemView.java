package org.codefx.privacyguide.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.codefx.privacyguide.R;
import org.codefx.privacyguide.localized.LocalizedApp;
import org.codefx.privacyguide.localized.LocalizedAppState;
import org.codefx.privacyguide.view.state.AppItemStateViews;

public class AppItemView extends FrameLayout {

	private static final int paddingInDip = 6;

	private final ViewGroup appItemView;
	private final Drawable background;

	private final ImageView iconView;
	private final TextView nameView;
	private final TextView summaryView;
	private final TextView descriptionView;
	private final ViewGroup stateContainer;
	private View stateView;

	private LocalizedApp app;
	private boolean expanded;

	/*
	 * CONSTRUCTION
	 */

	/**
	 * Provides an app item view.
	 *
	 * @return the {@code view} if it is not null and an instance of this class, or a new instance of this class
	 */
	public static AppItemView provideAppItemView(Context context, View view) {
		if (view instanceof AppItemView)
			return (AppItemView) view;
		else
			return new AppItemView(context);
	}

	private AppItemView(Context context) {
		super(context);
		appItemView = createView();
		background = addBackground(appItemView);

		iconView = (ImageView) findViewById(R.id.appItem_appIconView);
		nameView = (TextView) findViewById(R.id.appItem_appNameView);
		summaryView = (TextView) findViewById(R.id.appItem_appSummaryView);
		descriptionView = (TextView) findViewById(R.id.appItem_appDescriptionView);
		stateContainer = (ViewGroup) findViewById(R.id.appItem_stateContainer);
	}

	private ViewGroup createView() {
		return (ViewGroup) LayoutInflater
				.from(getContext())
				.inflate(R.layout.app_item_layout, this, true);
	}

	private Drawable addBackground(ViewGroup view) {
		Resources resources = getContext().getResources();
		Drawable background = resources.getDrawable(R.drawable.app_item_background);

		int paddingVertical = (int) resources.getDimension(R.dimen.appItem_paddingVertical);
		int paddingHorizontal = (int) resources.getDimension(R.dimen.appItem_paddingHorizontal);
		Drawable insetBackground =
				new InsetDrawable(background, paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
		view.setBackground(insetBackground);

		return background;
	}

	/*
	 * DRAW STATE
	 */

	private void updateView() {
		setBackgroundColor();
		setStateView();
	}

	private void setBackgroundColor() {
		int color = getColorForState(app.getState());
		background.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
	}

	private int getColorForState(LocalizedAppState state) {
		switch (state) {
			case UNKNOWN:
				return getResources().getColor(R.color.app_state_unknown);
			case UNINSTALLED:
				return getResources().getColor(R.color.app_state_uninstalled);
			case IGNORED:
				return getResources().getColor(R.color.app_state_ignored);
			case INSTALLED:
				return getResources().getColor(R.color.app_state_installed);
			default:
				throw new IllegalArgumentException(
						"Unknown " + LocalizedAppState.class.getSimpleName() + ": " + state + ".");
		}
	}

	private void setStateView() {
		if (stateView != null)
			stateContainer.removeAllViews();
		stateView = AppItemStateViews.getViewForState(getContext(), app);
		stateContainer.addView(stateView);
	}

	/*
	 * MUTATORS
	 */

	public void showApp(LocalizedApp app) {
		this.app = app;

		nameView.setText(app.getName());
		summaryView.setText(app.getSummary());
		descriptionView.setText(app.getDescription());

		updateView();
	}

	public void expand() {
		if (expanded)
			return;

		expanded = true;
		updateView();
	}

	public void contract() {
		if (!expanded)
			return;

		expanded = false;
		updateView();
	}

}
