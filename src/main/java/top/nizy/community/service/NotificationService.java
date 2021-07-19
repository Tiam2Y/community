package top.nizy.community.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.dto.NotificationDTO;
import top.nizy.community.dto.PaginationDTO;
import top.nizy.community.dto.UserDTO;
import top.nizy.community.enums.NotificationStatusEnum;
import top.nizy.community.enums.NotificationTypeEnum;
import top.nizy.community.exception.CustomizeErrorCode;
import top.nizy.community.exception.CustomizeException;
import top.nizy.community.mapper.NotificationMapper;
import top.nizy.community.model.Notification;
import top.nizy.community.model.NotificationExample;
import top.nizy.community.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Classname NotificationService
 * @Description TODO
 * @Date 2021/6/20 16:28
 * @Created by NZY271
 */
@Service
public class NotificationService {

    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    /**
     * 获取该用户相关的通知
     */
    public PaginationDTO<NotificationDTO> list(Long userId, Integer page, Integer size) {

        //根据用户ID查询到所有的通知
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size);

        //size*(page-1)
        int offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");    //根据创建时间降序排列

        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, UserDTO user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
