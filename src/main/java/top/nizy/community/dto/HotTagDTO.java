package top.nizy.community.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * @Classname HotTagDTO
 * @Description 传输热门标签的优先级
 * @Date 2021/6/22 14:32
 * @Created by NZY271
 */
@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;
    private Long talkNums;
    private Long commentNums;

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}
