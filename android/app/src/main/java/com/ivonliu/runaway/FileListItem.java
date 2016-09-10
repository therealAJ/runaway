package com.ivonliu.runaway;

/**
 * Created by ivon on 9/10/16.
 */
public class FileListItem {
    public final boolean isDirectory;
    public final String name;

    public FileListItem(boolean isDirectory, String name) {
        this.isDirectory = isDirectory;
        this.name = name;
    }

    @Override
    public String toString() {
        return "(" + isDirectory + ", " + name + ")";
    }
}
