package com.yl.yymusic.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yl.yymusic.R;

import com.yl.yymusic.storage.table.MusicEntity;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<MusicEntity> mMusic;
        private  int resourceId;
        private TurnTime turnTime=new TurnTime();
        public ListViewAdapter(Context context, int textViewResourceId, List<MusicEntity> musics) {
            mContext=context;
         resourceId=textViewResourceId;
            mMusic = musics;
        }

        /**
         * 加载多少条数据
         * @return
         */
        @Override
        public int getCount() {
            return mMusic != null ? mMusic.size() : 0;
        }

        /**
         * 获取对应位置的数据
         * @param position
         * @return
         */
        @Override
        public MusicEntity getItem(int position) {
            return mMusic != null ? mMusic.get(position) : null;
        }

        /**
         * 获取对应位置的id
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
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
            MusicEntity musics = getItem(position);
            View view;
            //创建ViewHolder 避免重复创建视图
            ViewHolder holder = null;
            //判断视图是否为空
            if (convertView == null){
                //加载视图
                view = LayoutInflater.from(mContext).inflate(resourceId,parent, false);
                //创建ViewHolder
                holder = new ViewHolder();
                holder.id = view.findViewById(R.id.item_id);
                holder.musicName = view.findViewById(R.id.item_name);
                holder.musicImage = view.findViewById(R.id.item_image);
                holder.musicTime = view.findViewById(R.id.item_time);
                holder.singer = view.findViewById(R.id.item_singer);
                //把ViewHolder缓存起来
                view.setTag(holder);
            }else {
                //取出ViewHolder的缓存
                view=convertView;
                holder = (ViewHolder) view.getTag();
            }
            //设置数据

            if (musics != null){
                holder.id.setText(String.valueOf(position+1));
                holder.musicName.setText(musics.getMusicName());
                holder.singer.setText(musics.getSinger());
                holder.musicImage.setImageBitmap(TurnImage.byteToBitmap(musics.getMusicImage(),mContext));
                holder.musicTime.setText(turnTime.stringForTime(musics.getMusicTime()));
            }

            return view;
        }

        private class ViewHolder{
            private TextView id;
            private TextView musicName;
            private TextView singer;
            private ImageView musicImage;
            private TextView musicTime;
        }
    }

