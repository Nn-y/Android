package cn.swu.todolist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class ItemViewHolder extends RecyclerView.ViewHolder{

    TextView content;
    TextView title;
    TextView date;
    Button updateBtn;
    Button deleteBtn;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        content = itemView.findViewById(R.id.content);
        date = itemView.findViewById(R.id.date);
        updateBtn = itemView.findViewById(R.id.update);
        deleteBtn = itemView.findViewById(R.id.delete);
    }
}




public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Item> data ;
    private Context context;
    private ButtonInterface buttonInterface;

    public ItemAdapter(Context context,List<Item> data){
        this.data = data;
        this.context = context;
    }

    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    public interface ButtonInterface {
        void onclick(View view, int position);
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
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.date.setText(item.getDate());
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
                    buttonInterface.onclick(v,position);
                }
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
                    buttonInterface.onclick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
