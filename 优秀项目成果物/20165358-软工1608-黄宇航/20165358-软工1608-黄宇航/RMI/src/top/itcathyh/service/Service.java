package top.itcathyh.service;

import top.itcathyh.entity.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

public interface Service extends Remote {
    boolean register(String username, String password) throws RemoteException;

    boolean login(String username, String password) throws RemoteException;

    String addMeeting(String username, String password, String othername,
                String title, String label, Timestamp starttime, Timestamp endtime) throws RemoteException;

    String query(String username, Timestamp starttime, Timestamp endtime) throws RemoteException;

    String queryAll(String username) throws RemoteException;

    boolean delete(String username, String password,long meetingid) throws RemoteException;

    boolean clear(String username, String password) throws RemoteException;
}
