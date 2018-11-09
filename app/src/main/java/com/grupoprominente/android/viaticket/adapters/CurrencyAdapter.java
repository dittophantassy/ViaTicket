package com.grupoprominente.android.viaticket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.grupoprominente.android.viaticket.models.CurrencyType;

public class CurrencyAdapter extends ArrayAdapter<CurrencyType> {

    public CurrencyAdapter (Context context) {
        super(context, 0, CurrencyType.values());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView text= (CheckedTextView) convertView;

        if (text== null) {
            text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,  parent, false);
        }
        text.setText(getItem(position).getResource());
        return text;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CheckedTextView text = (CheckedTextView) convertView;
        if (text == null) {
            text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,  parent, false);
        }
        text.setText(getItem(position).getResource());

        return text;
    }
}
