package mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * map阶段:
 * 输入数据，是一个键值对，key是行偏移量，值是行记录
 * 行偏移量，固定使用LongWritable
 *
 * 输出数据，是一个键值对，记录了单词与1
 * K2 : Text
 * V2 : IntWritable
 *
 * @Author 北京千锋互联科技有限公司 - 大数据教学团队
 */
public class WordcountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    // 读取到的源数据中的每一行记录都会调用一次map方法
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 这里的key，是每行数据的行偏移量，在这个程序中，这个数据是用不到的，只需要处理这一行的数据即可
        // 1. 将每行的数据，由Text类型，转成String类型
        String line = value.toString();
        // 2. 切割出每一行中的所有单词
        String[] words = line.split(" +");
        // 3. 遍历数组
        for (String word : words) {
            // 4. 实例化K2对象
            Text k2 = new Text(word);
            // 5. 实例化V2对象
            IntWritable v2 = new IntWritable(1);
            // 6. 将K2, V2写出(到Shuffle缓冲区)
            context.write(k2, v2);
        }
    }
}
