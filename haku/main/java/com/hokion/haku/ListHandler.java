package com.hokion.haku;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by NGo on 2016/04/27.
 * To handle adding rows
 */
public class ListHandler extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private PackageManager pm;
    List<ApplicationInfo> packages;

    public ListHandler (Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.item_list, null);

        Spinner spinner = (Spinner) rowView.findViewById(R.id.row_spinner);
        RowItem adapter = new RowItem(context,android.R.layout.simple_spinner_dropdown_item);
        for (ApplicationInfo packageInfo : packages) {
            adapter.add(packageInfo.loadLabel(pm).toString());
        }
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO save app details to SharedPref
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO enable/disable depending on checkbox toggle
                    //TODO save position + on/off to SharedPref
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rowView;
    }

    private class RowItem extends ArrayAdapter {

        public RowItem(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, parent);
        }

        public View getCustomView(int position, ViewGroup parent) {
            View spinner = inflater.inflate(R.layout.spinner_item, parent, false);

            ImageView icon = (ImageView) spinner.findViewById(R.id.app_icon);
            icon.setImageBitmap(ImageProcessing.scaleImage(packages.get(position).loadIcon(pm), 144));

            TextView name = (TextView) spinner.findViewById(R.id.app_name);
            String nameTemp = packages.get(position).loadLabel(pm).toString();
            name.setText(nameTemp);

            return spinner;
        }
    }
}
