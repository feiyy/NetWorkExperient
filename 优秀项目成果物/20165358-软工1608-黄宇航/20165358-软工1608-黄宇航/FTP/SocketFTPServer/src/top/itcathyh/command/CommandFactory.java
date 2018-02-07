package top.itcathyh.command;

import top.itcathyh.command.commands.*;

public final class CommandFactory {
    public static Command getCommand(String str) {
        switch (str) {
            case "USER":
                return UserCommand.getInstance();
            case "PASS":
                return PassCommand.getInstance();
            case "QUIT":
                return QuitCommand.getInstance();
            case "LIST":
                return ListCommand.getInstance();
            case "STOR":
                return StorCommand.getInstance();
            case "PASV":
                return PasvCommand.getInstance();
            case "RETR":
                return RetrCommand.getInstance();
            case "SIZE":
                return SizeCommand.getInstance();
            case "CWD":
                return CwdCommand.getInstance();
            case "REST":
                return RestCommand.getInstance();
            case "LISTP" :
                return ListpCommand.getInstance();
            case "RETRP" :
                return RetrpCommand.getInstance();
        }

        return null;
    }
}
