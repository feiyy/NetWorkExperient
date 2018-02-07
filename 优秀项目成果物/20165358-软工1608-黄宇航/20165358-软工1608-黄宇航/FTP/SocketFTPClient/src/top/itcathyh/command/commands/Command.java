package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Command {
    boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ObjectInputStream ios, ClientThread st);
}
