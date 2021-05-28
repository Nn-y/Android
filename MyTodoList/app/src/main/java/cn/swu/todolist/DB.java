package cn.swu.todolist;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1, exportSchema = false)

public abstract class DB extends RoomDatabase {

    public abstract ItemDao itemDao();


}
