package cn.swu.newsapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ItemViewHolder extends RecyclerView.ViewHolder{

    TextView mContent;
    TextView mTitle;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title);
        mContent = itemView.findViewById(R.id.content);
    }
}
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Item> data ;
    private Context context;
    private OnItemClickListener listener;

    public ItemAdapter(Context context,List<Item> data){
        this.data = data;
        this.context = context;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  MainActivity.mLayoutInflater.inflate(R.layout.item_recycler,parent,false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = data.get(position);
        holder.mTitle.setText(item.getTitle());
        holder.mContent.setText(item.getSource());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition(); // 1
                listener.onItemClick(holder.itemView,position);
            }

        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
