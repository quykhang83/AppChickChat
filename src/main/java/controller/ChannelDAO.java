/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Channel;
import helper.DatabaseHelper;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author quykhang
 */
public class ChannelDAO {

    public int getIDChannel(String username, String sender) throws Exception {
        String sql = "select id from channel where (sender=? and title=?) or (sender=? and title=?)";
        int idChannel = -1;
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            psmt.setString(1, username);
            psmt.setString(2, sender);
            psmt.setString(4, username);
            psmt.setString(3, sender);

            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                idChannel = rs.getInt(1);
            }
        }

        if (idChannel == -1) {
            sql = "select count(id) from Channel";
            try ( Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
                ResultSet rsid = psmt.executeQuery();
                rsid = psmt.executeQuery();
                if (rsid.next()) {
                    idChannel = rsid.getInt(1) + 1;
                }
            }

            Channel cn = new Channel(idChannel, username,
                    java.time.LocalDateTime.now().toString().substring(0, 19), "", sender);
            insertChannel(cn);
        }
        return idChannel;
    }

    public boolean insertChannel(Channel cn) throws Exception {
        String sql = "INSERT INTO Channel VALUES(?,?,?,?,?)";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            psmt.setInt(1, cn.getId());
            psmt.setString(2, cn.getTitle());
            psmt.setString(3, cn.getLastTime());
            psmt.setString(4, "");
            psmt.setString(5, cn.getSender());

            return psmt.executeUpdate() > 0;
        }
    }

    public int displayTextMessage(int idChannel, DefaultTableModel tblModel, String username, JTable tblChat) throws Exception {
        String sql = "select * from message where idChannel = " + idChannel + " order by time";
        int typeMess = -1;
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                typeMess = rs.getInt(4);
                if (rs.getInt(4) == 1) {
                    if (rs.getString(6).equals(username)) {
                        tblModel.addRow(new Object[]{
                            rs.getString(2), ""
                        });
                    } else {
                        tblModel.addRow(new Object[]{
                            "", rs.getString(2)
                        });
                    }
                } else if (rs.getInt(4) == 2) {
//                    ImageIcon icon = new ImageIcon(rs.getString(2));
//                    Image img = ImageHelper.resize(icon.getImage(), 70, 70); //180,200
//
//                    tblModel.addRow(new Object[]{
//                        "", ""
//                    });
//
//                    if (rs.getString(6).equals(username)) {
//                        tblModel.setValueAt(new JLabel(new ImageIcon(img)), tblModel.getRowCount() - 1, 0);
//                    } else {
//                        tblModel.setValueAt(new JLabel(new ImageIcon(img)), tblModel.getRowCount() - 1, 1);
//                    }

                    if (rs.getString(6).equals(username)) {
                        tblModel.addRow(new Object[]{
                            rs.getString(2), ""
                        });
                    } else {
                        tblModel.addRow(new Object[]{
                            "", rs.getString(2)
                        });
                    }

                }

            }
        }
        return typeMess;
    }

    public void displayImgMessage(int idChannel, DefaultTableModel tblModel, String username) throws Exception {
        String sql = "select * from message where idChannel = " + idChannel + " order by time";
        try (
                 Connection con = DatabaseHelper.openConnection();  PreparedStatement psmt = con.prepareStatement(sql);) {
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                if (rs.getString(6).equals(username)) {
                    tblModel.addRow(new Object[]{
                        rs.getString(2), ""
                    });
                } else {
                    tblModel.addRow(new Object[]{
                        "", rs.getString(2)
                    });
                }

            }
        }
    }

}
