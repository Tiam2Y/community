package top.nizy.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.dto.QuestionDTO;
import top.nizy.community.mapper.QuestionMapper;
import top.nizy.community.mapper.UserMapper;
import top.nizy.community.model.Question;
import top.nizy.community.model.User;

import java.util.ArrayList;
import java.util.List;

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
    private UserMapper userMapper;

    //获取数据库中的Questions列表，并根据 creator 与 User 的ID对应上
    //获得创建者的信息(例如头像)
    public List<QuestionDTO> list() {
        List<Question> questions = questionMapper.list();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将source对象的所有属性拷贝至target对象中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
