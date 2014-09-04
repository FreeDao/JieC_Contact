
package com.jiec.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.jiec.contact.model.Company;
import com.jiec.contact.model.ContactModel;
import com.jiec.contact.model.ContactModel.ContactChangeListener;
import com.jiec.contact.widget.SuperTreeViewAdapter;
import com.jiec.contact.widget.TreeViewAdapter;
import com.jiec.utils.LogUtil;

public class MyContactActivity extends Activity implements ContactChangeListener {

    ExpandableListView mExpandableListView;

    TreeViewAdapter mAdapter;

    SuperTreeViewAdapter mSuperAdapter;

    private List<Company> mContacts = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mContacts = new ArrayList<Company>();
        ContactModel.getInstance().setChangeListener(this);

        mExpandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);

        mAdapter = new TreeViewAdapter(this, 38);
        mSuperAdapter = new SuperTreeViewAdapter(this, null);

        updateView();

        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {

                Intent intent = new Intent(MyContactActivity.this, ContactDetailActivity.class);
                startActivity(intent);
                return false;
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                ContactModel.getInstance().getContacts();
            }
        }).start();

    }

    private void updateView() {
        mAdapter.removeAll();
        mAdapter.notifyDataSetChanged();
        mSuperAdapter.RemoveAll();
        mSuperAdapter.notifyDataSetChanged();

        List<TreeViewAdapter.TreeNode> treeNode = mAdapter.getTreeNode();
        for (int i = 0; i < mContacts.size(); i++) {
            TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();

            node.parent = mContacts.get(i).getName();

            for (int j = 0; j < mContacts.get(i).getContacts().size(); j++) {

                node.childs.add(mContacts.get(i).getContacts().get(j).getName());
            }
            treeNode.add(node);
        }
        mAdapter.updateTreeNode(treeNode);
        LogUtil.e("updateView");
    }

    @Override
    public void onDataChanged() {
        LogUtil.e("contactActivity onDataChanged");
        mContacts = ContactModel.getInstance().getContacts();
        updateView();
    }

}
