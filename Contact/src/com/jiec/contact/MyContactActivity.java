
package com.jiec.contact;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.jiec.contact.widget.SuperTreeViewAdapter;
import com.jiec.contact.widget.TreeViewAdapter;

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
        superAdapter = new SuperTreeViewAdapter(this, stvClickEvent);

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
                Toast.makeText(MyContactActivity.this, str, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    OnChildClickListener stvClickEvent = new OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                int childPosition, long id) {
            // TODO Auto-generated method stub
            String msg = "parent_id = " + groupPosition + " child_id = " + childPosition;
            Toast.makeText(MyContactActivity.this, msg, Toast.LENGTH_SHORT).show();
            return false;
        }
    };

}
