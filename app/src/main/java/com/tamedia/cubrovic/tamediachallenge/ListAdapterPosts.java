package com.tamedia.cubrovic.tamediachallenge;

public class ListAdapterPosts extends android.widget.BaseAdapter {
    static class ViewHolder {
        android.widget.LinearLayout layoutPostItem;

        android.widget.TextView textViewTitle;

        android.widget.TextView textViewBody;

    }

    private java.util.List<com.tamedia.cubrovic.tamediachallenge.domain.Post> listPost;

    private static final String VPPOST_ITEM = "VPPostItem";

    public java.util.List<com.tamedia.cubrovic.tamediachallenge.domain.Post> getListPosts() {
        return listPost;
    }

    private final android.app.Activity activity;

    public ListAdapterPosts(final android.app.Activity activity, final java.util.List<com.tamedia.cubrovic.tamediachallenge.domain.Post> listPost) {
        super();
        this.activity = activity;
        this.listPost = listPost;
    }

    @Override
    public int getCount() {
        return listPost.size();
    }

    @Override
    public Object getItem(final int position) {
        return listPost.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public android.view.View getView(final int position, final android.view.View convertView, final android.view.ViewGroup parent) {
        ViewHolder viewHolder;
        android.view.View view;
        if (convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.l_post_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.layoutPostItem = (android.widget.LinearLayout) view;

            viewHolder.textViewTitle = view.findViewById(R.id.TextViewIdPostItemTitle);
            viewHolder.textViewBody = view.findViewById(R.id.TextViewIdPostItemBody);

            view.setTag(R.id.TAG_KEY_VH, viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag(R.id.TAG_KEY_VH);
        }
        final android.widget.LinearLayout layoutPostItem = viewHolder.layoutPostItem;

        final android.widget.TextView textViewTitle = viewHolder.textViewTitle;
        final android.widget.TextView textViewBody = viewHolder.textViewBody;

        final com.tamedia.cubrovic.tamediachallenge.domain.Post post = listPost.get(position);
        if (textViewTitle != null) {
            textViewTitle.setText(post.getTitle());
        }
        if (textViewBody != null) {
            textViewBody.setText(post.getBody());
        }


        view.setTag(VPPOST_ITEM + position);
        return view;
    }

}
