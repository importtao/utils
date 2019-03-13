package com.sld.zt.utils.databasedoc;

import com.sld.zt.utils.model.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MarkDownUtils {
    static final String mTableTitle = "\n<center><h3>tableName-tableComment</h3></center>\n";
    static final String mTableHead = "\n|列名|序号|默认值|是否为空|数据类型|字段类型|主键|注释|\n" +
            "|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|\n";
    static final String mTableColumn = "|1|2|3|4|5|6|7|8|\n";
    static String blank = "";

    static String nullToBlank(String input){
        if(input == null){
            return blank;
        }
        return input;
    }

    static boolean  generatorMarkdown(List<Table> tables) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("数据库说明文档.md"));
        StringBuffer stringBuffer = new StringBuffer("<center><h1>数据库说明文档</h1></center>\n");
        tables.stream().forEach(table -> {
            stringBuffer.append(mTableTitle.replaceAll("tableName",table.getTableName()).replaceAll("tableComment",table.getTableCommon()));
            stringBuffer.append(mTableHead);
            table.getColume().stream().forEach(colume -> {
                stringBuffer.append(mTableColumn.replaceAll("1",colume.getName())
                        .replaceAll("2",nullToBlank(colume.getPosition()))
                        .replaceAll("3",nullToBlank(colume.getDefaultValue()))
                        .replaceAll("4",nullToBlank(colume.getNullAble()))
                        .replaceAll("5",nullToBlank(colume.getDataType()))
                        .replaceAll("6",nullToBlank(colume.getColumnType()))
                        .replaceAll("7",nullToBlank(colume.getKey()))
                        .replaceAll("8",nullToBlank(colume.getComment())));

            });

        });
        System.out.println(stringBuffer.toString());
        byte [] data = stringBuffer.toString().getBytes();
        fileOutputStream.write(data);
        fileOutputStream.close();
        return  true;
    }
}
