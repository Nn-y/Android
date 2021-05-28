package cn.swu.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
* 新增条目，添加至数据库，显示在页面上recyclerview
* 启动app时读出数据库内容显示
* 删除、更新操作
*
*
* recyclerview:
 * manager
 * adapter: holder  3个方法
*
* */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mAddBtn;
    EditText mTitleInput,mContentInput;
    RecyclerView mRecycler;
    public static LayoutInflater mLayoutInflater;
    private ItemAdapter mAdapter;
    private List<Item> mRows = new ArrayList<Item>();
    private static DB mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        mDb = Room.databaseBuilder(getApplicationContext(),
                DB.class, "ItemDB").addMigrations()
                .allowMainThreadQueries().build();

        mRows = mDb.itemDao().getAll();

        mAddBtn = findViewById(R.id.addBtn);//取布局元素
        mRecycler = findViewById(R.id.recycler_view);

        mAddBtn.setOnClickListener(this);//添加点击事件

        mAdapter = new ItemAdapter(this,mRows);
        mLayoutInflater =  getLayoutInflater();
        mRecycler.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);


        mAdapter.buttonSetOnclick(new ItemAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                switch (view.getId()){
                    case R.id.update:
                        updateItem(position);
                        break;
                    case R.id.delete:
                        deleteItem(position);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void updateItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog = View.inflate(MainActivity.this,R.layout.dialog,null);

        EditText titleInput = dialog.findViewById(R.id.title_input);
        EditText contentInput = dialog.findViewById(R.id.content_input);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Item item = mRows.get(position);
                item.setTitle(titleInput.getText().toString());
                item.setContent(contentInput.getText().toString());
                item.setDate(getTime());
                mRows.set(position,item);
                mAdapter.notifyDataSetChanged();
                mDb.itemDao().update(item);
                dialog.dismiss();
            }});
        builder.setTitle("Item").setView(dialog).create().show();
    }

    private void deleteItem(int position){
        mDb.itemDao().delete(mRows.get(position));
        mRows.remove(position);
        mAdapter.notifyDataSetChanged();//刷新全部条目
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addBtn:
                addItem();
                break;
            default:
                break;
        }
    }


    private String getTime(){
        long timecurrentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String time = sdf.format(timecurrentTimeMillis);

        return time;
    }

    private void addItem() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog = View.inflate(MainActivity.this,R.layout.dialog,null);

        mTitleInput = dialog.findViewById(R.id.title_input);
        mContentInput = dialog.findViewById(R.id.content_input);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Item item = new Item();
                item.setTitle(mTitleInput.getText().toString());
                item.setContent(mContentInput.getText().toString());
                item.setDate(getTime());
                mRows.add(item);
                mAdapter.notifyItemInserted(mAdapter.getItemCount());
                mDb.itemDao().insert(item);
                dialog.dismiss();
            }});
        builder.setTitle("Item").setView(dialog).create().show();

    }


}