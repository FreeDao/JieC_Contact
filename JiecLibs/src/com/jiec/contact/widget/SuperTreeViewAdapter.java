
package com.jiec.contact.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.example.contactlib.R;

public class SuperTreeViewAdapter extends BaseExpandableListAdapter {

    private float mItemHeight = 40;

    static public class SuperTreeNode {
        Object parent;

        // 二级树形菜单的结构体
        List<TreeViewAdapter.TreeNode> childs = new ArrayList<TreeViewAdapter.TreeNode>();
    }

    private List<SuperTreeNode> superTreeNodes = new ArrayList<SuperTreeNode>();

    private Context parentContext;

    private OnChildClickListener stvClickEvent;// 外部回调函数

    public SuperTreeViewAdapter(Context view, OnChildClickListener stvClickEvent) {
        parentContext = view;
        this.stvClickEvent = stvClickEvent;

        this.mItemHeight = view.getResources().getDimension(R.dimen.common_tree_height);
    }

    public List<SuperTreeNode> GetTreeNode() {
        return superTreeNodes;
    }

    public void UpdateTreeNode(List<SuperTreeNode> node) {
        superTreeNodes = node;
    }

    public void RemoveAll() {
        superTreeNodes.clear();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return superTreeNodes.get(groupPosition).childs.get(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        return superTreeNodes.get(groupPosition).childs.size();
    }

    public ExpandableListView getExpandableListView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) mItemHeight);
        ExpandableListView superTreeView = new ExpandableListView(parentContext);
        superTreeView.setLayoutParams(lp);
        return superTreeView;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public Object getGroup(int groupPosition) {
        return superTreeNodes.get(groupPosition).parent;
    }

    public int getGroupCount() {
        return superTreeNodes.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }
}
