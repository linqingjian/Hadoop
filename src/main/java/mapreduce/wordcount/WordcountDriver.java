package mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @Author 北京千锋互联科技有限公司 - 大数据教学团队
 */
public class WordcountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1. 获取集群配置信息
        //    在main下面新建一个resources文件夹，将四个核心配置文件粘贴到里面
        //    通过无参构造方法实例化的configuration会自动的读取这几个文件
        Configuration configuration = new Configuration();
        System.setProperty("HADOOP_USER_NAME", "root");
        // 2. 获取Job对象
        Job job = Job.getInstance(configuration);
        // 3. 设置驱动类型
        job.setJarByClass(WordcountDriver.class);

        // 4. 设置Map和Reduce类型
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        // 5. 设置Map的输出类型K2和V2，如果K2V2和reduce的K2V2相同，这里可以省略不写
        // job.setMapOutputKeyClass(Text.class);
        // job.setMapOutputValueClass(IntWritable.class);

        // 6. 设置Reduce的输出类型K3,V3
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 7. 设置ReduceTask的个数
        // job.setNumReduceTasks(1);

        // 8. 设置需要处理的数据输入路径
        FileInputFormat.setInputPaths(job, new Path("/input/a.txt"));
        // 9. 设置要处理的数据的输出路径
        //    判断输出路径上有没有数据，如果有，则删除
        Path path = new Path("/output");
        FileSystem fs = FileSystem.get(configuration);
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
        FileOutputFormat.setOutputPath(job, path);

        // 10. 提交任务
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
