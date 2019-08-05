package com.yl.yymusic.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yl.yymusic.R;

import com.yl.yymusic.storage.table.ListEntity;

import java.util.List;

public class NewMusicListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ListEntity> mMusicList;
    private  int resourceId;
    public NewMusicListAdapter(Context context, int textViewResourceId, List<ListEntity> lists) {
        mContext=context;
        resourceId=textViewResourceId;
        mMusicList = lists;
    }

    /**
     * 加载多少条数据
     * @return
     */
    @Override
    public int getCount() {
        return mMusicList != null ? mMusicList.size() : 0;
    }

    /**
     * 获取对应位置的数据
     * @param position
     * @return
     */
    @Override
    public ListEntity getItem(int position) {
        return mMusicList != null ? mMusicList.get(position) : null;
    }

    /**
     * 获取对应位置的id
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).getList_id();
    }

    /**
     * 创建视图
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListEntity listEntity = getItem(position);
        View view;
        //创建ViewHolder 避免重复创建视图
        ViewHolder holder = null;
        //判断视图是否为空
        if (convertView == null){
            //加载视图
            view = LayoutInflater.from(mContext).inflate(resourceId,parent, false);
            //创建ViewHolder
            holder = new ViewHolder();
            holder.id = view.findViewById(R.id.id_musicList);
            holder.musicListName = view.findViewById(R.id.text_name_musicList);
            holder.musicCount = view.findViewById(R.id.text_count_music);
            //把ViewHolder缓存起来
            view.setTag(holder);
        }else {
            //取出ViewHolder的缓存
            view=convertView;
            holder = (ViewHolder) view.getTag();
        }
        //设置数据

        if (listEntity != null){
            holder.id.setText(String.valueOf(position+1));
            holder.musicListName.setText(listEntity.getListName());
            holder.musicCount.setText(String.valueOf(listEntity.getMusicCount()));
        }

        return view;
    }

    private class ViewHolder{
        private TextView id;
        private TextView musicListName;
        private TextView musicCount;
    }
}

