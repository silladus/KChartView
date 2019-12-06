package com.silladus.stock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;
import com.github.tifezh.kchartlib.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silladus.stock.kchart.chart.KChartAdapter;
import com.silladus.stock.kchart.chart.KChartView;
import com.silladus.stock.kchart.chart.KLineEntity;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by silladus on 2017/3/6.
 */

public class FragmentK extends Fragment {
    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(0);
        mKChartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice() + " chg:" + String.format(Locale.getDefault(), "%.2f", data.getRate() / data.getLastClosePrice()));
            }
        });
        mKChartView.setOverScrollRange(ViewUtil.dp2Px(requireActivity(), 0));
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String fileName = "ibm.json"; //k线图的数据
                String fileName = "ibm-short.json"; //k线图的数据
                String res = "";
                try {
                    InputStream in = getResources().getAssets().open(fileName);
                    int length = in.available();
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    res = EncodingUtils.getString(buffer, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final List<KLineEntity> data = new Gson().fromJson(res, new TypeToken<List<KLineEntity>>() {
                }.getType());
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addFooterData(data);
                        mKChartView.startAnimation();
                    }
                });
            }
        }).start();
    }
}
