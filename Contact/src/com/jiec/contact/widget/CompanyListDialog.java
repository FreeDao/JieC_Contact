
package com.jiec.contact.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jiec.contact.R;
import com.jiec.contact.model.Company;
import com.jiec.contact.model.CompanyModel;
import com.jiec.contact.model.CompanyModel.OnInsertDataResult;
import com.jiec.utils.AppUtil;
import com.jiec.utils.ToastUtil;

public class CompanyListDialog {

    public static interface OnCompanyItemClickListener {
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

    private OnCompanyItemClickListener mListener;

    public CompanyListDialog(Context context, OnCompanyItemClickListener listener) {
        mContext = context;
        mListener = listener;

        mCompanyDataList = new ArrayList<HashMap<String, String>>();
        List<Company> companies = CompanyModel.getInstance().getCompanies();
        for (int i = 0; i < companies.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", companies.get(i).getName());
            map.put("id", companies.get(i).getId());
            mCompanyDataList.add(map);
        }
    }

    public void createDialog() {
        mDialog = new Dialog(mContext);
        mDialog.setTitle("公司列表");
        mDialog.show();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View viewDialog = inflater.inflate(R.layout.dialog_company_list, null);

        // 设置对话框的宽高
        int w = AppUtil.getScreenSize(mContext)[0] * 80 / 100;
        int h = (int) (w * 1.2);
        LayoutParams layoutParams = new LayoutParams(w, h);
        mDialog.setContentView(viewDialog, layoutParams);

        mNewCompany = (EditText) viewDialog.findViewById(R.id.et_new_company_name);

        mNewCompanyBtn = (Button) viewDialog.findViewById(R.id.btn_add_company);
        mNewCompanyBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mNewCompany.getText().toString().trim().length() < 1) {
                    ToastUtil.showMsg("请输入新增加公司名字");
                    return;
                }
                CompanyModel.getInstance().insertCompany(mNewCompany.getText().toString().trim(),
                        new OnInsertDataResult() {

                            @Override
                            public void onSuccess(String id, String name) {
                                mListener.onClick(name, id);
                                mDialog.dismiss();
                            }

                            @Override
                            public void onFailed(String reason) {
                                // TODO Auto-generated method stub
                                ToastUtil.showMsg(reason);
                            }
                        });
            }
        });

        mCompanyList = (ListViewForScrollView) viewDialog.findViewById(R.id.lv_companys);

        mAdapter = new SimpleAdapter(mContext, mCompanyDataList, R.layout.dialog_item,
                new String[] {
                        "name", "id"
                }, new int[] {
                        R.id.tv_company_name, R.id.tv_company_id
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
