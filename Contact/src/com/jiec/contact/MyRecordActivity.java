
package com.jiec.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiec.contact.model.Record;
import com.jiec.contact.model.RecordModel;
import com.jiec.contact.model.RecordModel.OnDataChangeListener;

public class MyRecordActivity extends ListActivity implements OnDataChangeListener {

    private RecordAdapter mAdapter;

    private List<Record> mRecords;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecords = new ArrayList<Record>();

        RecordModel.getInstance().addListener(this);
        RecordModel.getInstance().requestData();

        mAdapter = new RecordAdapter();
        setListAdapter(mAdapter);

        mContext = this;
    }

    @Override
    protected void onDestroy() {
        RecordModel.getInstance().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onDataChanged() {
        mRecords.clear();

        mRecords = RecordModel.getInstance().getRecords();

        mAdapter.notifyDataSetChanged();
    }

    private class RecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mRecords.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView name = null;
            TextView num = null;
            TextView time = null;
            TextView info = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_record_item,
                        null);
                name = (TextView) convertView.findViewById(R.id.tv_name);
                num = (TextView) convertView.findViewById(R.id.tv_num);
                time = (TextView) convertView.findViewById(R.id.tv_time);
                info = (TextView) convertView.findViewById(R.id.tv_info);
            }

            name.setText(mRecords.get(position).getName());
            num.setText(mRecords.get(position).getNum());
            time.setText(mRecords.get(position).getTime());
            info.setText(mRecords.get(position).getInfo());
            return convertView;
        }
    }

}
