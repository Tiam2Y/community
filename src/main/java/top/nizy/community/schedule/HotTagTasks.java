package top.nizy.community.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.nizy.community.cache.HotTagCache;
import top.nizy.community.mapper.QuestionMapper;
import top.nizy.community.model.Question;
import top.nizy.community.model.QuestionExample;

import java.util.*;

/**
 * @Classname HotTagTasks
 * @Description 关于热门标签的 Scheduling 任务 —— 每隔一段时间进行更新
 * @Date 2021/6/21 22:00
 * @Created by NZY271
 */
@Component
@Slf4j
public class HotTagTasks {
    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    /**
     * fixedRate 设定间隔时间 —— 以 ms 为单位
     */
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    @Scheduled(fixedRate = 1000 * 10)
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 10;
        log.info("hotTagSchedule start {}", new Date());
        List<Question> list = new ArrayList<>();

        Map<String, Integer> priorities = new HashMap<>();
        while (offset == 0 || list.size() == limit) {
            //分页查出一部分问题(当查出的那一部分正好等于size说明可能还有下一页，直至查到不足页也就是最后一页)
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
            for (Question question : list) {
                //使用英文逗号或者中文逗号实现分隔
                //先都统一至小写
                String tagLower = StringUtils.lowerCase(question.getTag());
                String[] tags = StringUtils.split(tagLower, ",，");
                for (String tag : tags)
                    priorities.put(tag, priorities.getOrDefault(tag, 0) + 5 + question.getCommentCount());
            }
//            offset += limit;
            offset++;
        }
        hotTagCache.updateTags(priorities);
        //hotTagCache.getHots().forEach(System.out::println);
        log.info("hotTagSchedule stop {}", new Date());
    }
}
