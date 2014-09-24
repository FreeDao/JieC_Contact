
package com.jiec.contact;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiec.contact.model.Contact;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.Record;
import com.jiec.contact.model.RecordModel;
import com.jiec.contact.model.RecordModel.OnDataChangeListener;
import com.jiec.contact.model.UserModel;
import com.jiec.utils.PhoneNumUtils;
import com.umeng.analytics.MobclickAgent;

public class MyRecordActivity extends ListActivity implements OnDataChangeListener {

    private RecordAdapter mAdapter;

    private Context mContext;

    public static final int SAVE_RECORD_ITEM_NUM = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new RecordAdapter();
        setListAdapter(mAdapter);

        mContext = this;

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long id) {

                final int recordId = mAdapter.getDatas().get(position).getId();
                final String recordNum = mAdapter.getDatas().get(position).getNum();

                AlertDialog.Builder builder = new AlertDialog.Builder(MyRecordActivity.this);
                builder.setTitle("备注");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                String[] itemStrings = new String[] {
                        "待办理", "回复电话"
                };

                if (mAdapter.getDatas().get(position).getName().length() < 1) {
                    itemStrings = new String[] {
                            "待办理", "回复电话", "保存联系人"
                    };
                }

                builder.setSingleChoiceItems(itemStrings, 0, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 2) {
                            // 跳转到新建联系人界面
                            Intent intent = new Intent(MyRecordActivity.this,
                                    ContactEditActivity.class);
                            intent.putExtra(MyContactActivity.NEW_REQUEST_KEY,
                                    MyContactActivity.NEW_REQUEST_KEY);
                            intent.putExtra(MyContactActivity.NEW_CONTACT_NUMBER, recordNum);
                            startActivityForResult(intent, SAVE_RECORD_ITEM_NUM);
                            dialog.dismiss();
                            return;
                        }
                        RecordModel.getInstance().updateRecord(which == 0 ? "待办理" : "回复电话",
                                recordId);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", null).show();
                return false;
            }
        });

        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                // PhoneUtils.callPhone(MyRecordActivity.this,
                // mAdapter.getDatas().get(position)
                // .getNum());

                Intent intent = new Intent(MyRecordActivity.this, ContactDetailActivity.class);
                Contact contact = ContactModel.getInstance().getContactByNameOrPhoneNumber(
                        mAdapter.getDatas().get(position).getNum());
                if (contact == null) {
                    intent = new Intent(MyRecordActivity.this, ContactEditActivity.class);
                    intent.putExtra(MyContactActivity.NEW_REQUEST_KEY,
                            MyContactActivity.NEW_REQUEST_KEY);
                    intent.putExtra(MyContactActivity.NEW_CONTACT_NUMBER,
                            mAdapter.getDatas().get(position).getNum());
                    startActivity(intent);
                } else {
                    intent.putExtra("contact", contact);
                    startActivity(intent);
                }
            }
        });

        RecordModel.getInstance().addListener(this);
    }

    @Override
    protected void onDestroy() {
        RecordModel.getInstance().removeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        if (UserModel.getInstance().isUserLogined()) {
            RecordModel.getInstance().scanSystemRecord();
            RecordModel.getInstance().requestData();
        }
        super.onStart();
    }

    @Override
    public void onDataChanged() {
        mAdapter.setDatas(RecordModel.getInstance().getRecords());

        mAdapter.notifyDataSetChanged();
    }

    private class RecordAdapter extends BaseAdapter {

        private List<Record> mRecords;

        public RecordAdapter() {
            mRecords = RecordModel.getInstance().getRecords();
        }

        public void setDatas(List<Record> records) {
            mRecords = records;
        }

        public List<Record> getDatas() {
            return mRecords;
        }

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
            if (mRecords.get(position).getType() == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_record_item,
                        null);

                name = (TextView) convertView.findViewById(R.id.tv_name);
                num = (TextView) convertView.findViewById(R.id.tv_num);
                time = (TextView) convertView.findViewById(R.id.tv_time);
                info = (TextView) convertView.findViewById(R.id.tv_info);

                name.setText(mRecords.get(position).getName());
                num.setText(PhoneNumUtils.toStarPhoneNumber(mRecords.get(position).getNum()));
                time.setText(mRecords.get(position).getDate() + " "
                        + mRecords.get(position).getTime());
                info.setText(mRecords.get(position).getInfo());

            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_sms_item, null);

                name = (TextView) convertView.findViewById(R.id.tv_name);
                num = (TextView) convertView.findViewById(R.id.tv_num);
                time = (TextView) convertView.findViewById(R.id.tv_time);
                info = (TextView) convertView.findViewById(R.id.tv_info);

                name.setText(mRecords.get(position).getName());
                num.setText(PhoneNumUtils.toStarPhoneNumber(mRecords.get(position).getNum()));
                time.setText(mRecords.get(position).getDate() + " "
                        + mRecords.get(position).getTime());
                info.setText(mRecords.get(position).getMsg());
            }

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == SAVE_RECORD_ITEM_NUM) {
            RecordModel.getInstance().refreshRecord();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
