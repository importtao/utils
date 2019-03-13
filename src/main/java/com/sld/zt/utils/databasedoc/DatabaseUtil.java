package com.sld.zt.utils.databasedoc;

import com.alibaba.fastjson.JSON;
import com.sld.zt.utils.model.Colume;
import com.sld.zt.utils.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class DatabaseUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);

    /*private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.100.56:22767/swingcard?useUnicode=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1a2s3d$f";
    private static final String DATABASE = "swingcard";*/
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://47.110.244.88:3307/smart_planet?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=TRUE&allowMultiQueries=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "'planet++520'";
    private static final String DATABASE = "smart_planet";

    private static final String SQL = "SELECT * FROM ";// 数据库操作
    private static final String getTableInfoSql = "SELECT isc.COLUMN_NAME,isc.ORDINAL_POSITION,isc.COLUMN_DEFAULT,isc.IS_NULLABLE,isc.DATA_TYPE,isc.COLUMN_TYPE,isc.COLUMN_KEY,isc.COLUMN_COMMENT from information_schema.COLUMNS isc where table_schema = '"+DatabaseUtil.DATABASE+"' and table_name = ?";
    private static final String getTableCommentSql = "show table STATUS where Name =?";

   static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("加载驱动失败", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public  Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public  void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }


    public  List<String> getTableNames() {
        List<String> tableNames = new ArrayList<String>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            LOGGER.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }


    //获取连接
    private static Connection getConnections(String driver,String url,String user,String pwd) throws Exception {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.put("remarksReporting", "true");
            props.put("user", user);
            props.put("password", pwd);
            Class.forName(driver);
            conn = DriverManager.getConnection(url, props);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }

    //其他数据库不需要这个方法 oracle和db2需要
    private static String getSchema(Connection conn) throws Exception {
        String schema;
        schema = conn.getMetaData().getUserName();
        if ((schema == null) || (schema.length() == 0)) {
            throw new Exception("ORACLE数据库模式不允许为空");
        }
        return schema.toUpperCase().toString();
    }

    //获取表字段
    public String getTableComment(String tableName){
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        try{
            pStemt = conn.prepareStatement(DatabaseUtil.getTableCommentSql);
            pStemt.setString(1,tableName);
            ResultSet resultSet = pStemt.executeQuery();

            while (resultSet.next()){
            return resultSet.getString("Comment");

            }
        }catch (SQLException e){
            LOGGER.error("预编译失败！");
            e.printStackTrace();
        }
        return null;
    }

    //获取表字段
    public List<Colume> getTableField(String tableName){
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        List<Colume> columes = new ArrayList<>();
        try{
            pStemt = conn.prepareStatement(DatabaseUtil.getTableInfoSql);
            pStemt.setString(1,tableName);
            ResultSet resultSet = pStemt.executeQuery();

            while (resultSet.next()){
                Colume colume = new Colume();
                colume.setName(resultSet.getString("COLUMN_NAME"));
                colume.setPosition(resultSet.getString("ORDINAL_POSITION"));
                colume.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
                colume.setNullAble(resultSet.getString("IS_NULLABLE"));
                colume.setDataType(resultSet.getString("DATA_TYPE"));
                colume.setColumnType(resultSet.getString("COLUMN_TYPE"));
                colume.setKey(resultSet.getString("COLUMN_KEY"));
                colume.setComment(resultSet.getString("COLUMN_COMMENT"));
                columes.add(colume);
            }
        }catch (SQLException e){
            LOGGER.error("预编译失败！");
            e.printStackTrace();
        }
        return columes;
    }

    public Long getPeriodicResidualTime(String periodic){
        // 取当前日期：
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime hourEnd = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),today.getHour(),59,59);
        LocalDateTime dayEnd = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),23,59,59);

        Date now = new Date();
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(now);
        LocalDateTime weekEnd = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),23,59,59);

        LocalDateTime lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime monthEnd = LocalDateTime.of(lastDayOfThisMonth.getYear(), lastDayOfThisMonth.getMonth(), lastDayOfThisMonth.getDayOfMonth(),23,59,59);
        switch(periodic){
            case "month":
                return ChronoUnit.MILLIS.between(today,monthEnd);
            case "week":
                return (ChronoUnit.MILLIS.between(today,dayEnd)+(1000*3600*24*(7+1-gc.get(Calendar.DAY_OF_WEEK))));
            case "day":
                return ChronoUnit.MILLIS.between(today,dayEnd);
            case "hour":
                return ChronoUnit.MILLIS.between(today,hourEnd);
            default:
                return ChronoUnit.MILLIS.between(today,monthEnd);
        }
    }

    public static void main(String[] args) {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        List<String> tableNames = databaseUtil.getTableNames();
        List<Table> tables = new ArrayList<Table>();
        tableNames.stream().forEach(tableName->{
            Table table = new Table();
            table.setTableName(tableName);
            table.setColume(databaseUtil.getTableField(tableName));
            table.setTableCommon(databaseUtil.getTableComment(tableName));
            tables.add(table);
        });
        System.out.println(JSON.toJSON(tables));
        try{MarkDownUtils.generatorMarkdown(tables);}catch (IOException e){
            e.printStackTrace();
        }
        //WordDocUtils.generatorDataBaseDoc(tables);
        /*Date d = new Date();
        GregorianCalendar gc=new GregorianCalendar();
        SimpleDateFormat sf  =new SimpleDateFormat("ddHH");
        gc.setTime(d);
        gc.add(Calendar.HOUR,-12);
        List time = new ArrayList();
        for(int i=0;i<12;i++){
            time.add(sf.format(gc.getTime()));
            gc.add(Calendar.HOUR,1);
        }
        System.out.println(JSON.toJSON(time));*/
        /*DatabaseUtil databaseUtil = new DatabaseUtil();System.out.println(databaseUtil.getPeriodicResidualTime("month")/3600000.0+"小时");
        System.out.println(databaseUtil.getPeriodicResidualTime("week")/3600000.0+"小时");
        System.out.println(databaseUtil.getPeriodicResidualTime("day")/3600000.0+"小时");
        System.out.println(databaseUtil.getPeriodicResidualTime("hour")/60000.0+"分钟");

        System.out.println(databaseUtil.getPeriodicResidualTime("month"));
        System.out.println(databaseUtil.getPeriodicResidualTime("week"));
        System.out.println(databaseUtil.getPeriodicResidualTime("day"));
        System.out.println(databaseUtil.getPeriodicResidualTime("hour"));*/

        // 取当前日期：
        /*LocalDateTime today = LocalDateTime.now();
        LocalDateTime hourEnd = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),today.getHour(),59,59);
        LocalDateTime dayEnd = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),23,59,59);

        Date now = new Date();
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(now);
        LocalDateTime weekEnd = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),23,59,59);

        LocalDateTime lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime monthEnd = LocalDateTime.of(lastDayOfThisMonth.getYear(), lastDayOfThisMonth.getMonth(), lastDayOfThisMonth.getDayOfMonth(),23,59,59);
        System.out.println(dayEnd);
        System.out.println("----------");
        System.out.println(gc.getTime());
        System.out.println(hourEnd);
        System.out.println(dayEnd);
        System.out.println(monthEnd);
        System.out.println("----------");
        System.out.println("hour:"+ChronoUnit.MILLIS.between(today,hourEnd));
        System.out.println("days:"+ChronoUnit.MILLIS.between(today,dayEnd));
        System.out.println("week:"+(ChronoUnit.MILLIS.between(today,dayEnd)+(1000*3600*24*(7+1-gc.get(Calendar.DAY_OF_WEEK)))));
        System.out.println("month:"+ChronoUnit.MILLIS.between(today,monthEnd));*/


    }

}
