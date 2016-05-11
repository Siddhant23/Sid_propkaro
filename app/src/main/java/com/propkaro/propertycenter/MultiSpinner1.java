package com.propkaro.propertycenter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.propkaro.R;

public class MultiSpinner1 extends Spinner implements OnMultiChoiceClickListener, OnCancelListener {

	private List<String> items;
	private boolean[] selected;
	private String defaultText;
	private MultiSpinnerListener listener;
	private Context ctx;
	 
	public MultiSpinner1(Context context) {
		super(context);
		ctx = context;
	}

	public MultiSpinner1(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	public MultiSpinner1(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if (isChecked)
			selected[which] = true;
		else
			selected[which] = false;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		
		StringBuffer spinnerBuffer = new StringBuffer();
		
		for (int i = 0; i < items.size(); i++) {
			if (selected[i] == true) {
				if(items.size() <= 5){
					spinnerBuffer.append(items.get(i));
					spinnerBuffer.append(", ");
				} else
					Toast.makeText(ctx, "Please select maximum 5 cities !", 1).show();
			}
		}
		
		String spinnerText = "";
		spinnerText = spinnerBuffer.toString();
		if (spinnerText.length() > 2)
			spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
		else
			spinnerText = defaultText;
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				R.layout.multi_spinner_textview1,
				new String[] { spinnerText });
		setAdapter(adapter);
		if(selected.length > 0)
			listener.onItemsSelected(selected);
	}

	@Override
	public boolean performClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(defaultText);
		builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(this);
		builder.show();
		return true;
	}

	public void setItems(List<String> items, String allText, int position, MultiSpinnerListener listener) {
		this.items = items;
		this.defaultText = allText;
		this.listener = listener;

		// all selected by default
		selected = new boolean[items.size()];
		for (int i = 0; i < selected.length; i++)
			selected[i] = false;		

		// all text on the spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				R.layout.multi_spinner_textview1, new String[] { allText });
		setAdapter(adapter);
		
		if(position != -1)
		{
			selected[position] = true;
			listener.onItemsSelected(selected);
			
			onCancel(null);
		}
		
	}

	public interface MultiSpinnerListener {
		public void onItemsSelected(boolean[] selected);
	}
}