package com.sharity.sharityUser.Utils;

/*import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.sharity.sharityUser.R;

public class AdapterCategory extends ParseQueryAdapter<ParseObject> {

	public AdapterCategory(Context context) {// Use the QueryFactory to construct a PQA that will only show
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Category");
				return query;
			}
		});
	}


	// Customize the layout by overriding getItemView
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.sample_gridlayout, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		ParseImageView todoImage = (ParseImageView) v.findViewById(android.R.id.icon);
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			todoImage.setParseFile(imageFile);
			todoImage.loadInBackground();
		}

		// Add the title view
		final TextView titleTextView = (TextView) v.findViewById(R.id.os_texts);
		titleTextView.setText(object.getString("name"));
		return v;
	}


}*/