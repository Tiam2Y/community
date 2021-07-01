package top.nizy.community.cache;

import top.nizy.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname TagCache
 * @Description TODO
 * @Date 2021/6/18 10:04
 * @Created by NZY271
 */
public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("形式");
        program.setTags(Arrays.asList("电影", "电视剧", "动漫", "纪录片", "短片"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("类型");
        framework.setTags(Arrays.asList("喜剧", "动作", "爱情", "科幻", "悬疑", "犯罪", "历史"));
        tagDTOS.add(framework);


        TagDTO server = new TagDTO();
        server.setCategoryName("地区");
        server.setTags(Arrays.asList("中国", "欧美", "日韩", "英国", "法国", "德国"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("年代");
        db.setTags(Arrays.asList("2021", "2020", "2010年代", "2000年代", "90年代", "更早"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("特色");
        tool.setTags(Arrays.asList("经典", "青春", "文艺", "搞笑", "励志", "魔幻"));
        tagDTOS.add(tool);
        return tagDTOS;
    }
}
