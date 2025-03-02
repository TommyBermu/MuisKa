package com.muiska.clases;

import android.util.Log;
import java.sql.*;

public class SQLConnection {
    private final String TAG = "SQL CONNECTION";
    private static Connection conexion;

    /* Clever Cloud
    private static String bd="bgpfat5wzdvekaov7kov";
    private static String user="uqdbpv2oajapfvuc";
    private static String password="FcP2hbBhfDKwI2m4H88t";
    private static String host="bgpfat5wzdvekaov7kov-mysql.services.clever-cloud.com";
    private static String port="3306";
    */

    // ngrok
    private static String bd="MuisKa";
    private static String user="root";
    private static String password="";
    private static String host="4.tcp.ngrok.io"; // cambia segun la sesion
    private static String port="12540"; // cambia segun la sesion

    private static String server="jdbc:mysql://"+host+":"+port+"/"+bd+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; // TODO no se si al ser solo UTC y no UTC + 5 las fechas seran diferentes

    public Connection conectar() {
        //conectar
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(server, user, password);
            Log.d(TAG, "Conexion a base de datos " + server + " ... OK");
            return conexion;
        } catch (ClassNotFoundException ex) {
            Log.e(TAG, "Error cargando el Driver MySQL JDBC ... FAIL");
        } catch (SQLException ex) {
            Log.e(TAG, "Imposible realizar conexion con " + server + " ... FAIL");
        }
        return conexion;
    }

    public void desconectar(){
        try {
            conexion.close();
            Log.d(TAG,"Cerrar conexion con "+server+" ... OK");
        } catch (SQLException ex) {
            Log.e(TAG,"Imposible cerrar conexion ... FAIL");
        }
    }
}
