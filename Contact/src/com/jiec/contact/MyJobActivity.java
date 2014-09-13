
package com.jiec.contact;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiec.contact.model.Job;
import com.jiec.contact.model.JobModel;
import com.jiec.contact.model.JobModel.OnDataChangeListener;

public class MyJobActivity extends ListActivity implements OnDataChangeListener {

    private JobAdapter mAdapter;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JobModel.getInstance().addListener(this);
        mAdapter = new JobAdapter();
        setListAdapter(mAdapter);

        mContext = this;

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long id) {

                final int jobId = mAdapter.getDatas().get(position).getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(MyJobActivity.this);
                builder.setTitle("备注");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setSingleChoiceItems(new String[] {
                        "延迟", "完成"
                }, 0, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String date = sDateFormat.format(new java.util.Date());
                        JobModel.getInstance().updateJob(which == 0 ? "延迟" : "完成", jobId,
                                which == 1 ? date : "");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", null).show();
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        JobModel.getInstance().removeListener(this);
        super.onDestroy();
    }

    private class JobAdapter extends BaseAdapter {

        private List<Job> mJobs;

        public JobAdapter() {
            mJobs = JobModel.getInstance().getJobs();
        }

        public void setDatas(List<Job> jobs) {
            mJobs = jobs;
        }

        public List<Job> getDatas() {
            return mJobs;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mJobs.size();
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
            TextView create_time = null;
            TextView finish_time = null;
            TextView info = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_job_item, null);

            }

            name = (TextView) convertView.findViewById(R.id.tv_name);
            create_time = (TextView) convertView.findViewById(R.id.tv_create_time);
            finish_time = (TextView) convertView.findViewById(R.id.tv_finish_time);
            info = (TextView) convertView.findViewById(R.id.tv_info);

            name.setText(mJobs.get(position).getName());
            create_time.setText("创建于:" + mJobs.get(position).getCreateTime());
            finish_time.setText("完成于:" + mJobs.get(position).getFinishTime());
            info.setText(mJobs.get(position).getInfo());

            return convertView;
        }
    }

    @Override
    public void onDataChanged() {
        mAdapter.setDatas(JobModel.getInstance().getJobs());

        mAdapter.notifyDataSetChanged();
    }

}
