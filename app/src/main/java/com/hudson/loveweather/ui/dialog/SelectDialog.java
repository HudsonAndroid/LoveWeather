package com.hudson.loveweather.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hudson.loveweather.R;
import com.hudson.loveweather.ui.dialog.params.SelectParams;
import com.hudson.loveweather.utils.UIUtils;

import java.util.List;

/**
 * Created by Hudson on 2017/12/11.
 */

public class SelectDialog extends AbsDialog<SelectParams> {
    public SelectDialog(Context context, SelectParams params) {
        super(context, params);
    }

    @Override
    protected View initContentView(Context context, ViewGroup parent, final SelectParams params) {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_select,parent,false);
        RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.rg_container);
        List<String> datas = params.datas;
        for (int i = 0; i < datas.size(); i++) {
            final String s = datas.get(i);
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(s);
            if(s.equals(params.preSelectedItem)){
                radioButton.setChecked(true);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int)UIUtils.getDimension(R.dimen.item_list_height));
            radioButton.setLayoutParams(layoutParams);
            final int position = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("select",s);
                    bundle.putInt("position",position);
                    params.itemSelectedRunnable.run(bundle);
                }
            });
            radioGroup.addView(radioButton);
        }
        return root;
    }
}
