package com.irga.irga_ramadhan_databaseapiproject.hargaponline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.irga.irga_ramadhan_databaseapiproject.model.Handphone;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterHandphone extends BaseAdapter implements Filterable {
    private Context context;
    private List<Handphone> list;
    private List<Handphone> filteredList;

    public ListAdapterHandphone(Context context, List<Handphone> list) {
        this.context = context;
        this.list = list;
        this.filteredList = new ArrayList<>(list);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        Handphone hp = filteredList.get(position);
        TextView textNama = convertView.findViewById(R.id.text_nama);
        textNama.setText(hp.getNama());
        TextView textHarga = convertView.findViewById(R.id.text_harga);
        textHarga.setText(hp.getHarga());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new HandphoneFilter();
    }

    /**
     * Class filter untuk melakukan filter (pencarian)
     */
    private class HandphoneFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            List<Handphone> filteredData = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredData.addAll(list);
            } else {
                String filterString = constraint.toString().toLowerCase();
                for (Handphone hp : list) {
                    if (hp.getNama().toLowerCase().contains(filterString)) {
                        filteredData.add(hp);
                    }
                }
            }

            result.count = filteredData.size();
            result.values = filteredData;

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List<Handphone>) results.values);
            notifyDataSetChanged();
        }
    }
}
