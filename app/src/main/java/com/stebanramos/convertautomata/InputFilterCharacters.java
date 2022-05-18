package com.stebanramos.convertautomata;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterCharacters implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        //*Caracter o caracteres no permitidos.
        String charactersForbiden = "[`~!@#$%^&*()+=|{}':;',[].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        if (source != null && charactersForbiden.contains((source))) {
            return "";
        }
        return null;
    }
}
