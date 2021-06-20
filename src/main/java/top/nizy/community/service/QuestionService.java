package top.nizy.community.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.h2.result.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.dto.PaginationDTO;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;
import top.nizy.community.mapper.QuestionExtMapper;
import top.nizy.community.mapper.QuestionMapper;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.Question;
import top.nizy.community.model.QuestionExample;
import top.nizy.community.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service 层的作用：
 * 处理业务逻辑，一个业务对应一个 Service
 */

/**
 * @Classname QuestionService
 * @Description TODO
 * @Date 2021/6/7 20:58
 * @Created by NZY271
 */
@Service
public class QuestionService {
    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    //获取数据库中的Questions列表，并根据 creator 与 User 的ID对应上
    //获得创建者的信息(例如头像)
    public PaginationDTO<QuestionDTO> list(Integer page, Integer size) {

        //获取数据库中总计的数量
        int totalCount = (int) questionMapper.countByExample(new QuestionExample());

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size);


        //计算数据库分页查询时的 offset 和 size
        int offset = size * (page - 1);
        // //会没有 TEXT 类型的数据 -- 为null
        //List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //MyBatis查询数据库中 TEXT 类型的数据返回均为空
        //withBLOBs可以解决
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将source对象的所有属性拷贝至target对象中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    //用于根据用户ID查找其相关的所有帖子，形成分页内容
    public PaginationDTO<QuestionDTO> list(Long userId, Integer page, Integer size) {

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        //获取数据库中总计的数量
        int totalCount = (int) questionMapper.countByExample(questionExample);

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size);


        //计算数据库分页查询时的 offset 和 size
        int offset = size * (page - 1);

        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        example.createCriteria()
                .andCreatorEqualTo(userId);
        // //会没有 TEXT 类型的数据 -- 为null
        //List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        User user = userMapper.selectByPrimaryKey(userId);
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            //将source对象的所有属性拷贝至target对象中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        //需要使用 QuestionMapper 在数据库中实际查询到该问题
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //创建一个 DTO 封装这个 question
        QuestionDTO questionDTO = new QuestionDTO();
        //将source对象的所有属性拷贝至target对象中
        // 属性的数据类型需要完全一致(包括基本数据类型)
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            int updated = questionMapper.updateByPrimaryKeySelective(question);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        /*
        不要使用这种方式
        当存在并发时，多个用户获取到这个 updateQuestion 对象，里面的数据已经固定
        此时各自 setViewCount 后更新的数据记录都是同样的，而不是累加的
         */
        /*Question question = questionMapper.selectByPrimaryKey(id);
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(question.getViewCount() + 1);
        updateQuestion.setId(id);
        //对象中为 null 的部分不会更新数据库中的内容
        questionMapper.updateByPrimaryKeySelective(updateQuestion);*/

        /*
        更新时使用数据库自带的 自加 则可以避免这一现象
        自己扩展的方法需要额外构建文件，否则下次 MyBatis自动生成代码时会覆盖原来的文件
         */
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);   //这是增加的值
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",，");
        String regexpTag = Arrays
                .stream(tags)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)    //过滤空格
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
