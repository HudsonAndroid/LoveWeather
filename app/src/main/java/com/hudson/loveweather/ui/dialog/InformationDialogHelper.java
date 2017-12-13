package com.hudson.loveweather.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.ui.dialog.params.InformationParams;

/**
 * Created by Hudson on 2017/12/10.
 */

public class InformationDialogHelper extends AbsDialogHelper<InformationParams> {

    /**
     *
     * @param context
     * @param params 包含的数据 1.string  2.runnable
     */
    public InformationDialogHelper(Context context, InformationParams params) {
        super(context, params);
    }

    @Override
    protected View initContentView(Context context, ViewGroup parent, final InformationParams params) {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_information, parent, false);
        TextView information = (TextView) root.findViewById(R.id.tv_information);
        information.setText(params.msg);
        root.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                params.sureRunnable.run(null);
            }
        });
        root.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                params.cancelRunnable.run(null);
            }
        });
        return root;
    }
}
