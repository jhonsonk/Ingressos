package com.digitalingressos.ingressos.data.database.converts;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListConverter {
    @TypeConverter
    public static String fromStringList(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            sb.append(str).append(",");
        }
        return sb.toString();
    }

    @TypeConverter
    public static List<String> toStringList(String data) {
        String[] strings = data.split(",");
        return new ArrayList<>(Arrays.asList(strings));
    }
}
