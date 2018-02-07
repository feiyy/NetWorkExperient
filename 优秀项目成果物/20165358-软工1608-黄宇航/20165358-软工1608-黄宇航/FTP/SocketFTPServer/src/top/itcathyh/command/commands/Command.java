package top.itcathyh.command.commands;

import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.ObjectOutputStream;

public interface Command {
    boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st);
}
