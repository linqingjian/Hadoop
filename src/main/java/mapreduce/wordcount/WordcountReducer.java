package mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * reduce阶段处理的数据，已经经过了Shuffle阶段的处理，并按照Key进行分组，把所有的value整合成Iterable集合
 *
 * reduce的输入类型，就是K2和V2
 *
 * reduce的输出类型: 由于我们要将每一个单词出现的次数做统计
 * K3: 单词，即Text类型
 * V3: 次数，即IntWritable类型
 *
 * @Author 北京千锋互联科技有限公司 - 大数据教学团队
 */
public class WordcountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    // 每一组数据，都会调用一次reduce方法
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // 基础逻辑: 统计指定的单词出现的次数
        int sum = 0;
        // 1. 遍历所有的values
        for (IntWritable value : values) {
            // 2. 统计次数(通过get方法，将IntWritable转为int类型)
            sum += value.get();
        }
        // 3. 将处理结果输出
        context.write(key, new IntWritable(sum));
    }
}
