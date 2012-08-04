package com.zdm.picabus.tempmoran;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.zdm.picabus.R;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

public class AddressFetcher extends Activity {

	double LATITUDE = 37.42233;
	double LONGITUDE = -122.083;

	/** Called when the activity is first created. */
	//@Override
/*	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_fetcher);
		TextView myLatitude = (TextView) findViewById(R.id.mylatitude);
		TextView myLongitude = (TextView) findViewById(R.id.mylongitude);
		TextView myAddress = (TextView) findViewById(R.id.myaddress);

		myLatitude.setText("Latitude: " + String.valueOf(LATITUDE));
		myLongitude.setText("Longitude: " + String.valueOf(LONGITUDE));

		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,
					LONGITUDE, 1);

			if (addresses != null && addresses.size() > 0) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder(
						"Address:\n");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									"\n");
				}
				myAddress.setText(strReturnedAddress.toString());
			} else {
				myAddress.setText("No Address returned!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			myAddress.setText("Canont get Address!");
		}

	}*/
}
