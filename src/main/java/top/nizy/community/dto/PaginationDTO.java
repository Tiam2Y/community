package top.nizy.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PaginationDTO
 * @Description TODO
 * @Date 2021/6/8 21:08
 * @Created by NZY271
 */
@Data
public class PaginationDTO<T> {
    private List<T> data;   //分页查询到的实际内容
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;   //当前页数
    private List<Integer> pages = new ArrayList<>();    //跳转页面列表中显示的页面数
    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        //计算分页时的总页数
        if (totalCount % size == 0) {
            this.totalPage = totalCount / size;
        } else {
            this.totalPage = totalCount / size + 1;
        }
        //页数越界则进行修改
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        this.page = page;

        pages.add(page);
        //显示当前页面的前3页+后3页(如果存在的话)
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        showPrevious = page != 1;

        // 是否展示下一页
        showNext = !page.equals(totalPage);

        // 是否展示第一页
        showFirstPage = !pages.contains(1);

        // 是否展示最后一页
        showEndPage = !pages.contains(totalPage);
    }
}
