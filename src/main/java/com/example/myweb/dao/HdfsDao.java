package com.example.myweb.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HdfsDao {
    @Value("${hdfs.host}")
    private String hdfsHost;
    @Value("${hdfs.port}")
    private int hdfsPort;
    @Value("${hdfs.dir}")
    private String hdfsDir;

    private String hdfsPath = "hdfs://" + hdfsHost + ":" + hdfsPort + "/" + hdfsDir + "/";

    public String gethdfsinfo() {
        return this.hdfsPath;
    }


    public void copyFile(String local, String desturl) throws IOException {
        Configuration conf = new Configuration();
        if (desturl == null) {
            desturl = hdfsPath;
        }
        if (desturl.length() < 1) {
            desturl = hdfsPath;
        }

//        conf.set("dfs.blocksize", "4096"); // Michael
//        FileSystem fs = FileSystem.get(URI.create(desturl), conf,"root");
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(desturl), conf, "root");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fs.copyFromLocalFile(new Path(local), new Path(desturl));
        System.out.println("copy from: " + local + " to " + hdfsPath);
        fs.close();
    }

    public void deleteFromHdfs(String deletePath) {
        Configuration conf = new Configuration();
        try {
            FileSystem fs = FileSystem.get(URI.create(deletePath), conf, "root");
            fs.deleteOnExit(new Path(deletePath));
            fs.close();
        } catch (Exception ex) {

        }
    }

    //创建新目录
    public void createdir(String dirpath) {
        try {
            String dirname = hdfsPath + dirpath;
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(dirname), conf, "root");
            Path f = new Path(dirname);
            if (!fs.exists(new Path(dirname))) {
                fs.mkdirs(f);
            }

            System.out.println("ok");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 遍历HDFS上的文件和目录
     */
    public FileStatus[] getDirectoryFromHdfs() {
        String dst = hdfsPath;
        Configuration conf = new Configuration();
        // conf.set("fs.default.name", fsdefaultname);
        FileStatus[] list = null;
        try {
            FileSystem fs = FileSystem.get(URI.create(dst), conf, "root");
            list = fs.listStatus(new Path(dst));
            if (list != null) {
                for (FileStatus f : list) {
                    System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath().getName(), f.isDir(), f.getLen());
                }
            }
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 遍历HDFS上的文件和目录
     */
    public FileStatus[] getDirectoryFromHdfs(String path) {
        FileStatus[] list = null;
        try {
            Configuration conf = new Configuration();
            // conf.set("fs.default.name", fsdefaultname);
            String dst = hdfsPath;
            if (path.length() > 0) {
                dst = path;
            }
            FileSystem fs = FileSystem.get(URI.create(dst), conf, "root");
            list = fs.listStatus(new Path(dst));
            if (list != null)
                for (FileStatus f : list) {
                    System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath().getName(), f.isDir(), f.getLen());
                }
            fs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    //获取文件输入流
    public InputStream getFileInputStreamForPath(String strpath) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", strpath);
//        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf,"root");
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(hdfsPath), conf, "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fs.open(new Path(strpath));
    }

    //下载hdfs文件到本地
    public void download(String remote, String local) throws IOException {
        Configuration conf = new Configuration();
//    	Path path = new Path(remote);
//        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf,"root");
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(hdfsPath), conf, "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        fs.copyToLocalFile(path, new Path(local));
        fs.copyToLocalFile(false, new Path(remote), new Path(local), true);
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }

    public void testWrite2() {
        Configuration configuration = new Configuration();
        try {
            FileSystem fs = FileSystem.get(URI.create(hdfsPath), configuration, "root");

//            Path path = new Path("hdfs://192.168.181.201/user/sanglp/hello.txt");
            //public FSDataOutputStream create(Path f, boolean overwrite, int bufferSize, short replication, long blockSize)
            FSDataOutputStream fsDataOutputStream = fs.create(new Path("/user/sanglp/hadoop/a.txt"), true, 1024, (short) 2, 5);
            fsDataOutputStream.write("how are you".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

