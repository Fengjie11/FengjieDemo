package com.example.fengjiedemo.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fengjiedemo.R;
import com.example.fengjiedemo.bean.MyDataBean;

import java.util.List;

/**
 * Created by lenovo on 2017/3/3.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<MyDataBean.ResultBean.RowsBean> list;

    public MyAdapter(Context context, List<MyDataBean.ResultBean.RowsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_list_view,parent,false);
            holder=new ViewHolder();
            holder.loupan_name= (TextView) convertView.findViewById(R.id.loupan_name);
            TextPaint paint = holder.loupan_name.getPaint();
            paint.setFakeBoldText(true);
            holder.imageView= (ImageView) convertView.findViewById(R.id.item_image_view);
            holder.region_title= (TextView) convertView.findViewById(R.id.item_region_title);
            holder.new_price_value= (TextView) convertView.findViewById(R.id.new_price_back);
            holder.tags= (TextView) convertView.findViewById(R.id.tags);
            holder.sale_title= (TextView) convertView.findViewById(R.id.sale_title);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.loupan_name.setText(list.get(position).getInfo().getLoupan_name());
        holder.region_title.setText(list.get(position).getInfo().getRegion_title()+
                "-"+list.get(position).getInfo().getSub_region_title());
        holder.new_price_value.setText(list.get(position).getInfo().getNew_price_value()+
        list.get(position).getInfo().getNew_price_back());
        holder.tags.setText(list.get(position).getInfo().getTags());
        holder.sale_title.setText(list.get(position).getInfo().getSale_title());
        Glide.with(context).load(list.get(position).getInfo().getDefault_image())
                .into(holder.imageView);
        return convertView;
    }
    public class ViewHolder{
        ImageView imageView;
        TextView loupan_name,region_title,new_price_value,tags,sale_title;

        //TextView textView;
    }
}
