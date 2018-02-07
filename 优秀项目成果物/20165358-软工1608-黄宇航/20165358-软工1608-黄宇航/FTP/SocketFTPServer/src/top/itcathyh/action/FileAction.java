package top.itcathyh.action;

import top.itcathyh.entity.CommandInfo;

import java.io.File;

public final class FileAction {
    public static void search(File file, String key, String dir, CommandInfo commandinfo, String rootpath) {
        if (file.getName().equals(key)){
            commandinfo.setStr(file.getPath().replace(rootpath,""));
            return;
        }

        if (file.isDirectory()){
            File list[] = file.listFiles();

            for (File tmp : list) {
                if (tmp.getName().equals(key)){
                    commandinfo.setStr(tmp.getPath().replace(rootpath,""));
                    return;
                }

                if (tmp.isDirectory()) {
                    search(tmp, key,dir + File.separator + tmp.getName(), commandinfo, rootpath);
                }
            }
        }
    }

    public static void main(String args[]){
        CommandInfo commandInfo = new CommandInfo();

        FileAction.search(new File("D:\\Code\\ftptest\\"),"artifacts.xml",
                "D:\\Code\\ftptest\\",commandInfo,"D:\\Code\\ftptest\\");

        System.out.println(commandInfo.getStr());
    }
}
