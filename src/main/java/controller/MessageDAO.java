/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Message;
import helper.DatabaseHelper;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.rowset.serial.SerialBlob;

/**
 *
 * @author quykhang
 */
public class MessageDAO {

    public int getIDNewMessage() throws Exception {
        String sql = "select count(id) from MESSAGE";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            int idN = 0;
            ResultSet rsid = psmt.executeQuery();
            rsid = psmt.executeQuery();

            if (rsid.next()) {
                idN = rsid.getInt(1) + 1;
            }

            return idN;
        }
    }

    public boolean insertTextMessage(Message tm) throws Exception {
        String sql = "INSERT INTO MESSAGE VALUES(?,?,?,?,?,?)";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            psmt.setInt(1, tm.getId());
            psmt.setString(2, (String) tm.getContent());
            psmt.setString(3, tm.getTime());
            psmt.setInt(4, tm.getIdMessageType());
            psmt.setInt(5, tm.getIdChannel());
            psmt.setString(6, tm.getReceiver());

            return psmt.executeUpdate() > 0;
        }
    }

    public boolean insertImgMessage(Message tm) throws Exception {
        String sql = "INSERT INTO MESSAGE VALUES(?,?,?,?,?,?)";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            psmt.setInt(1, tm.getId());

            if (tm.getContent() != null) {
                Blob img = new SerialBlob((byte[]) tm.getContent());
                psmt.setBlob(2, img);
            } else {
                Blob img = null;
                psmt.setBlob(2, img);
            }

            psmt.setString(3, tm.getTime());
            psmt.setInt(4, 2);
            psmt.setInt(5, tm.getIdChannel());
            psmt.setString(6, tm.getReceiver());

            return psmt.executeUpdate() > 0;
        }
    }

    public boolean updateLastChannelTime(Message tm) throws Exception {
        String sql = "UPDATE channel set lastTime=? where id=?";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            psmt.setString(1, tm.getTime());
            psmt.setInt(2, tm.getIdChannel());

            return psmt.executeUpdate() > 0;
        }
    }
}
