
package com.jiec.contact.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jiec.contact.MyApplication;

public class TreeViewAdapter extends BaseExpandableListAdapter {

    public static final float sDesity = MyApplication.getContext().getResources()
            .getDisplayMetrics().density;

    public static final int sItemHeight = (int) (45 * sDesity);

    public static final int sPaddingLeft = (int) (20 * sDesity);

    private int myPaddingLeft = 0;

    static public class TreeNode {
        public Object parent;

        public List<Object> childs = new ArrayList<Object>();
    }

    List<TreeNode> treeNodes = new ArrayList<TreeNode>();

    Context parentContext;

    public TreeViewAdapter(Context context, int myPaddingLeft) {
        parentContext = context;
        this.myPaddingLeft = (int) (myPaddingLeft * sDesity);

    }

    public List<TreeNode> getTreeNode() {
        return treeNodes;
    }

    public void updateTreeNode(List<TreeNode> nodes) {
        treeNodes = nodes;
    }

    public void removeAll() {
        treeNodes.clear();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return treeNodes.get(groupPosition).childs.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    static public TextView getTextView(Context context) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, sItemHeight);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView textView = getTextView(this.parentContext);
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setPadding(myPaddingLeft + sPaddingLeft / 3, 0, 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return textView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return treeNodes.get(groupPosition).childs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return treeNodes.get(groupPosition).parent;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return treeNodes.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView textView = getTextView(this.parentContext);
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(myPaddingLeft + (sPaddingLeft >> 1), 0, 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return textView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
