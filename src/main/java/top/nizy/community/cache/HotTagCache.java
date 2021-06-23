package top.nizy.community.cache;

import lombok.Data;
import org.springframework.stereotype.Component;
import top.nizy.community.dto.HotTagDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @Classname HotTagCache
 * @Description 需要被隔一段时间就更新的热门标签的缓存
 * @Date 2021/6/22 14:29
 * @Created by NZY271
 */
@Component
@Data
public class HotTagCache {
    //热门标签的缓存
    private List<HotTagDTO> hots = new ArrayList<>();

    /**
     * 使用大小堆(PriorityQueue) 实现 TopN 算法
     *
     * @param tags
     */
    public void updateTags(Map<String, Integer> tags, Map<String, Long> talks, Map<String, Long> comments) {
        int max = 10;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            hotTagDTO.setTalkNums(talks.get(name));
            hotTagDTO.setCommentNums(comments.get(name));
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else {
                //小顶堆
                HotTagDTO minHot = priorityQueue.peek();
                if (hotTagDTO.compareTo(minHot) > 0) {
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        List<HotTagDTO> sortedTags = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            HotTagDTO poll = priorityQueue.poll();
            sortedTags.add(0, poll);
        }
        hots = sortedTags;
    }
}
