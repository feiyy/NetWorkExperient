package top.itcathyh.util;

import java.sql.*;

public class Mysql {
    private static final String URL = "jdbc:mysql://localhost:3306/socketsql?useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static Connection con = null;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            if (con.isClosed()) {
                System.exit(1);
                System.out.println("database connet error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getCon() {
        return con;
    }

    public static ResultSet getResultSet(String sql) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(sql);
            rs = statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static boolean update(String sql) {
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(sql);

            statement.executeUpdate();
            statement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean add(String sql, Object... objects) {
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(sql);

            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }

            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean delete(String sql) {
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(sql);

            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isExit(String sql, Object... objects) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(sql);

            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }

            rs = statement.executeQuery();

            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isExist(String sql) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(sql);
            rs = statement.executeQuery(sql);

            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static String[] getInformation(int ilen, String sql) {
        String stra[] = new String[ilen];

        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < ilen; i++) {
                    stra[i] = rs.getString(i + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stra;
    }

}