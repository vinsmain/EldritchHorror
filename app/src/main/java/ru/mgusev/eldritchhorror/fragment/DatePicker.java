package ru.mgusev.eldritchhorror.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import ru.mgusev.eldritchhorror.R;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String[] date = ((String) getArguments().get("date")).split("\\.");

        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]) - 1;
        int year = Integer.parseInt(date[2]);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
        picker.setTitle(getResources().getString(R.string.choose_date));

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.ready));
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
        TextView textView = (TextView) getActivity().findViewById(R.id.dateFieldq);
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);
        textView.setText(String.format(Locale.getDefault(), "%td.%tm.%tY", calendar, calendar, calendar));
        System.out.println(i1 + " " + i1 + " " + i2);
    }


}