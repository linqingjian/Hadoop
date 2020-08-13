package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * 如何使用Java代码操作集群
 * @Author 北京千锋互联科技有限公司 - 大数据教学团队
 */
public class HDFSTest {

    private Configuration configuration;
    private FileSystem fs;

    @Before
    public void prepare() throws IOException, URISyntaxException, InterruptedException {
        // 1. 实例化一个对象，用来保存配置信息
        configuration = new Configuration();

        // 2. 设置要访问的HDFS集群是谁
        // configuration.set("fs.defaultFS", "hdfs://192.168.10.101:8020");
        // 3. 设置操作HDFS集群的用户是谁
        // System.setProperty("HADOOP_USER_NAME", "root");
        // fs = FileSystem.get(configuration);

        // URI: 指定了HDFS通信地址
        // Configuration: 配置文件对象
        // User: 操作集群的用户
        fs = FileSystem.get(new URI("hdfs://192.168.10.102:8020"), configuration, "root");
    }

    @After
    public void close() throws IOException {
        fs.close();
    }

    @Test
    public void fileSystemTest() throws IOException {
        // 这个对象是用来设置相关配置信息的
        // 默认的加载四大配置文件(core-site.xml、hdfs-site.xml、mapred-site.xml、yarn-site.xml)
        // 需要提前将这四个配置文件导入到项目中
        // 也可以不导入，如果不导入，就需要使用指定的方法进行设置
        Configuration configuration = new Configuration();

        // 进行属性的设置
        configuration.set("fs.defaultFS", "hdfs://192.168.10.102:8020");

        // 由于这个类是一个抽象类，不能直接实例化对象
        // 使用静态方法get，通过一个Configuration对象获取文件系统对象
        FileSystem fs = FileSystem.get(configuration);

        System.out.println(fs.getClass().getName());
    }

    // 1. 测试文件上传，将本地的一个文件，上传到HDFS中
    @Test
    public void uploadTest() throws IOException {
        // 1. 指定一个本地文件的路径
        Path srcPath = new Path("C:\\Users\\msi\\Desktop\\aa.txt");
        // 2. 指定一个HDFS上的路径
        Path dstPath = new Path("/classData");
        // 3. 将本地的文件，上传到HDFS
        fs.copyFromLocalFile(srcPath, dstPath);
        System.out.println("文件上传完成!");
    }

    // 2. 测试文件下载，将HDFS中的一个文件，下载到本地
    @Test
    public void downloadTest() throws IOException {
        // 1. 指定一个本地文件的路径
        Path dst = new Path("F:\\新建文件夹");
        // 2. 指定一个HDFS文件系统中的路径
        Path src = new Path("/classData");
        // 3. 将HDFS集群的数据下载到本地
        //fs.copyToLocalFile(src, dst);
        fs.copyToLocalFile(false, src, dst, true);
        System.out.println("下载完成!");
    }

    // 3. 创建文件夹
    @Test
    public void mkdirTest() throws IOException {
        // 1. 实例化一个Path对象，描述HDFS上需要创建的文件夹
        Path dstPath = new Path("/classTest");
        // 2. 调用方法进行创建
        fs.mkdirs(dstPath);

        System.out.println("创建成功");
    }

    // 4. 删除文件夹
    @Test
    public void rmdirTest() throws IOException {
        // 1. 实例化一个Path对象，描述HDFS上需要删除的文件夹
        Path dstPath = new Path("/classTest");
        // 2. 调用方法进行删除
        fs.delete(dstPath, true);

        System.out.println("删除成功");
    }

    // 5. 重命名文件
    @Test
    public void renameTest() throws IOException {
        // 1. 描述原来的路径
        Path old = new Path("/classData/aa.txt");
        // 2. 描述新的路径
        Path newPath = new Path("/classData/bb.txt");
        // 3. 进行更名操作
        fs.rename(old, newPath);

        System.out.println("更名成功");
    }

    // 6. 列举
    @Test
    public void listTest() throws IOException {
        // 1. 实例化一个Path对象，用来描述一个路径
        Path path = new Path("/test");
        // 2. 获取一个路径下所有的文件状态
        FileStatus[] fileStatuses = fs.listStatus(path);
        // 3. 遍历数组
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println("文件的名字: " + fileStatus.getPath().getName());
            System.out.println("文件的所有者: " + fileStatus.getOwner());
            System.out.println("文件的所属组: " + fileStatus.getGroup());
            System.out.println("文件的大小: " + fileStatus.getLen());
            System.out.println("文件的副本数量: " + fileStatus.getReplication());
            System.out.println("是否是文件夹: " + fileStatus.isDirectory());
        }
    }


    // 7. 使用IOUtils进行文件的上传
    @Test
    public void ioUtilsUploadTest() throws IOException {
        // 7.1. 上传文件
        // 7.1.1. 实例化一个InputStream对象读取本地文件
        InputStream is = new FileInputStream("C:\\Users\\msi\\Desktop\\bb.txt");
        // 7.1.2. 实例化一个FsDataOutputStream对象，将数据写入到HDFS中
        FSDataOutputStream os = fs.create(new Path("/classData"));
        // 7.1.3. 拷贝文件
        IOUtils.copyBytes(is, os, configuration);
        // 7.1.4. 关闭流
        IOUtils.closeStream(is);
        IOUtils.closeStream(os);

        System.out.println("上传完成！");
    }

    // 8. 使用IOUtils进行文件的下载
    @Test
    public void ioUtilsDownloadTest() throws IOException {
        // 1. 实例化一个FSInputStream对象，用来读取集群中的数据
        FSDataInputStream is = fs.open(new Path("/classData"));
        // 2. 实例化一个OutputStream对象，用来将数据写入到本地磁盘
        OutputStream os = new FileOutputStream("C:\\Users\\msi\\Desktop\\mm");
        // 3. 开始拷贝文件
        IOUtils.copyBytes(is,os,configuration);

        // 4. 关闭流
        IOUtils.closeStream(is);
        IOUtils.closeStream(os);

        System.out.println("文件下载完成");
    }

    // 9. 获取集群上文件中的数据
    @Test
    public void ioUtilsCatTest() throws IOException {
        // 1. 实例化一个FSInputStream对象，用来读取集群中的数据
        FSDataInputStream is = fs.open(new Path("/classData"));
        // 2. 开始拷贝文件
        IOUtils.copyBytes(is, System.out, configuration);
        // 3. 关闭流
        IOUtils.closeStream(is);
    }

    // 10. 查看文件的状态
    @Test
    public void fileStatusTest() throws IOException {
        // 获取一个文件的状态
        RemoteIterator<LocatedFileStatus> status = fs.listLocatedStatus(new Path("/classData"));

        while (status.hasNext()) {
            LocatedFileStatus fileStatus = status.next();
            System.out.println(fileStatus.getPath());
            // 获取这个文件的所有的块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                System.out.println("当前块大小: " + blockLocation.getLength());
                System.out.println("当前块的副本位置: " + Arrays.toString(blockLocation.getHosts()));
                System.out.println("当前块的副本所在IP信息: " + Arrays.toString(blockLocation.getNames()));
            }
            System.out.println("文件的大小: " + fileStatus.getLen());
        }
    }


}
