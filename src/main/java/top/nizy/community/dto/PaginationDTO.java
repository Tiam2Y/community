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
public class PaginationDTO {
    private List<QuestionDTO> data;   //分页查询到的实际内容
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;   //当前页数
    private List<Integer> pages = new ArrayList<>();    //跳转页面列表中显示的页面数
    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if (totalCount % size == 0)
            this.totalPage = totalCount / size;
        else
            this.totalPage = totalCount / size + 1;

        if (page < 1) page = 1;
        if (page > this.totalPage) page = this.totalPage;

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
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
