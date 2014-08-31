
package com.jiec.contact;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.jiec.contact.widget.SuperTreeViewAdapter;
import com.jiec.contact.widget.TreeViewAdapter;
import com.jiec.utils.ToastUtil;

public class MyContactActivity extends Activity {

    ExpandableListView expandableListView;

    TreeViewAdapter adapter;

    SuperTreeViewAdapter superAdapter;

    public String[] groups = {
            "A公司", "B公司", "C公司", "D公司", "E公司", "F公司"
    };

    public String[][] childs = {
            {
                    "A", "AA", "AAA", "AAAA", "AAAAA"
            }, {
                    "B", "BB", "BBB", "BBBB", "BBBBB"
            }, {
                    "B", "BB", "BBB", "BBBB", "BBBBB"
            }, {
                    "B", "BB", "BBB", "BBBB", "BBBBB"
            }, {
                    "B", "BB", "BBB", "BBBB", "BBBBB"
            }, {
                    "B", "BB", "BBB", "BBBB", "BBBBB"
            }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        expandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);
        adapter = new TreeViewAdapter(this, 38);
        superAdapter = new SuperTreeViewAdapter(this, null);

        adapter.removeAll();
        adapter.notifyDataSetChanged();
        superAdapter.RemoveAll();
        superAdapter.notifyDataSetChanged();

        List<TreeViewAdapter.TreeNode> treeNode = adapter.getTreeNode();
        for (int i = 0; i < groups.length; i++) {
            TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
            node.parent = groups[i];
            for (int j = 0; j < childs[i].length; j++) {
                node.childs.add(childs[i][j]);
            }
            treeNode.add(node);
        }
        adapter.updateTreeNode(treeNode);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                // TODO Auto-generated method stub
                String str = "parent_id = " + groupPosition + " child_id = " + childPosition;
                
                ToastUtil.showMsg(str);
                Intent intent = new Intent(
                		MyContactActivity.this, ContactDetailActivity.class);//Intent.ACTION_CALL, Uri.parse("tel:" + 10086));  
                startActivity(intent);
                return false;
            }
        });

    }

}
