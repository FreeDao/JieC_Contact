
package com.jiec.contact.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jiec.contact.R;
import com.jiec.contact.model.Company;
import com.jiec.contact.model.ContactModel;
import com.jiec.utils.AppUtil;

public class NameListDialog {

    public static interface OnNameItemClickListener {
        public void onClick(String name, String id);
    }

    private Dialog mDialog = null;

    // 列表
    private ListView mCompanyList = null;

    private EditText mNewCompany = null;

    // 添加item
    private Button mNewCompanyBtn = null;

    private SimpleAdapter mAdapter = null;

    // 记录所有listview的item的数据
    private List<HashMap<String, String>> mCompanyDataList = null;

    private Context mContext;

    private OnNameItemClickListener mListener;

    public NameListDialog(Context context, OnNameItemClickListener listener) {
        mContext = context;
        mListener = listener;

        mCompanyDataList = new ArrayList<HashMap<String, String>>();
        List<Company> companies = ContactModel.getInstance().getContacts();
        for (int i = 0; i < companies.size(); i++) {
            for (int j = 0; j < companies.get(i).getContacts().size(); j++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", companies.get(i).getContacts().get(j).getName());
                map.put("id", String.valueOf(companies.get(i).getContacts().get(j).getId()));
                mCompanyDataList.add(map);
            }

        }
    }

    public void createDialog() {
        mDialog = new Dialog(mContext);
        mDialog.setTitle("联系人列表");
        mDialog.show();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View viewDialog = inflater.inflate(R.layout.dialog_company_list, null);

        // 设置对话框的宽高
        int w = (int) (AppUtil.getScreenSize(mContext)[0]);
        int h = (int) (AppUtil.getScreenSize(mContext)[1]);
        LayoutParams layoutParams = new LayoutParams(w, h);
        mDialog.setContentView(viewDialog, layoutParams);

        mNewCompany = (EditText) viewDialog.findViewById(R.id.et_new_company_name);
        mNewCompany.setVisibility(View.GONE);
        mNewCompanyBtn = (Button) viewDialog.findViewById(R.id.btn_add_company);
        mNewCompanyBtn.setVisibility(View.GONE);

        mCompanyList = (ListViewForScrollView) viewDialog.findViewById(R.id.lv_companys);

        mAdapter = new SimpleAdapter(mContext, mCompanyDataList, R.layout.dialog_item,
                new String[] {
                    "name"
                }, new int[] {
                    R.id.tv_company_name
                });
        mCompanyList.setAdapter(mAdapter);

        mCompanyList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onClick(mCompanyDataList.get(position).get("name"), mCompanyDataList
                            .get(position).get("id"));
                    mDialog.dismiss();
                }
            }

        });
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
