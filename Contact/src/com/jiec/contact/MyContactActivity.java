
package com.jiec.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.jiec.contact.model.Company;
import com.jiec.contact.model.Contact;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.ContactModel.ContactChangeListener;
import com.jiec.contact.model.UserModel;
import com.jiec.contact.widget.SuperTreeViewAdapter;
import com.jiec.contact.widget.TreeViewAdapter;
import com.jiec.utils.LogUtil;
import com.jiec.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:
 * 
 * @author jiec
 * @since 2014-10-13 上午11:07:35
 */
public class MyContactActivity extends Activity implements ContactChangeListener {

    EditText mPhoneNumET;

    Button mCallBtn;

    ExpandableListView mExpandableListView;

    TreeViewAdapter mAdapter;

    SuperTreeViewAdapter mSuperAdapter;

    private List<Company> mContacts = null;

    public static final String NEW_REQUEST_KEY = "NEW_REQUEST_KEY";

    public static final String NEW_CONTACT_NUMBER = "new_contact_number";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mContacts = new ArrayList<Company>();
        ContactModel.getInstance().addListener(this);

        mPhoneNumET = (EditText) findViewById(R.id.et_phone_num);

        mCallBtn = (Button) findViewById(R.id.btn_call);
        mCallBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mPhoneNumET.getText().toString().trim().length() < 1) {
                    ToastUtil.showMsg("请输入要搜索的号码或者名字");
                    return;
                }
                Intent intent = new Intent(MyContactActivity.this, ContactDetailActivity.class);
                Contact contact = ContactModel.getInstance().getContactByNameOrPhoneNumber(
                        mPhoneNumET.getText().toString().trim());
                if (contact == null) {
                    ToastUtil.showMsg("未找到联系人");
                } else {
                    intent.putExtra("contact", contact);
                    startActivity(intent);
                }
            }
        });

        mExpandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);

        mAdapter = new TreeViewAdapter(this, 38);
        mSuperAdapter = new SuperTreeViewAdapter(this, null);

        Button btnNewContact = (Button) findViewById(R.id.btn_new_contact);
        btnNewContact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MyContactActivity.this, ContactEditActivity.class);
                intent.putExtra(NEW_REQUEST_KEY, NEW_REQUEST_KEY);
                startActivity(intent);
            }
        });

        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {

                Intent intent = new Intent(MyContactActivity.this, ContactDetailActivity.class);
                intent.putExtra("contact",
                        mContacts.get(groupPosition).getContacts().get(childPosition));
                startActivity(intent);
                return false;
            }
        });

        updateView();

        if (UserModel.getInstance().isUserLogined()) {
            ContactModel.getInstance().requestContactData();
        }
    }

    private void updateView() {
        mAdapter.removeAll();
        mAdapter.notifyDataSetChanged();
        mSuperAdapter.RemoveAll();
        mSuperAdapter.notifyDataSetChanged();

        List<TreeViewAdapter.TreeNode> treeNode = mAdapter.getTreeNode();
        for (int i = 0; i < mContacts.size(); i++) {
            TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();

            node.parent = getTitleName(mContacts.get(i));

            for (int j = 0; j < mContacts.get(i).getContacts().size(); j++) {

                node.childs.add(mContacts.get(i).getContacts().get(j).getName());
            }
            treeNode.add(node);
        }
        mAdapter.updateTreeNode(treeNode);
        LogUtil.e("updateView");
    }

    private String getTitleName(Company company) {
        String name = company.getName() + "  (";
        for (int i = 0; i < company.getContacts().size(); i++) {
            if (i > 2 || i == company.getContacts().size() - 1) {
                name += company.getContacts().get(i).getName();
                break;
            }

            name += company.getContacts().get(i).getName() + "、";
        }
        name = name + ")";
        return name;
    }

    @Override
    public void onDataChanged() {
        LogUtil.e("contactActivity onDataChanged");
        mContacts = ContactModel.getInstance().getContacts();
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                updateView();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ContactModel.getInstance().removeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); // 统计页面
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
                                                 // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
